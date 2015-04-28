package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import edu.drexel.cci.hiyh.has.device.ParamType;

public class ScrollUI implements InputUI {

    private final BooleanInputSource inputsrc;
    private final JFrame frame = new JFrame("HIYH");
    private final JPanel idlePanel = new JPanel();

    // Used to interrupt a client upon window close.
    private class InterruptLock {
        private Thread t = null;
        private boolean closing = false;

        public synchronized void registerThread() throws InterruptedException {
            if (closing)
                throw new InterruptedException();
            t = Thread.currentThread();
        }

        public synchronized void deregisterThread() {
            t = null;
        }

        public synchronized void interrupt() {
            if (t != null)
                t.interrupt();
            closing = true;
        }
    }
    private final InterruptLock interruptLock = new InterruptLock();

    // TODO find a better place for this?
    static {
        Font prevFont = (Font)UIManager.get("Label.font");
        UIManager.put("Label.font", new Font(prevFont.getName(), Font.PLAIN, 30));
    }

    public ScrollUI(BooleanInputSource inputsrc) {
        this.inputsrc = inputsrc;

        try {
            SwingUtilities.invokeAndWait(this::buildUI);
            inputsrc.initAndStart(this);
        } catch (InterruptedException | InvocationTargetException e) {
            // FIXME
            e.printStackTrace();
        }
    }

    private void buildUI() {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                interruptLock.interrupt();
            }
        });
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 200));

        idlePanel.add(new JLabel("Loading..."));
        switchTo(idlePanel);

        frame.setVisible(true);
    }

    private void switchTo(Container pane) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setContentPane(pane);
                frame.pack();
            }
        });
    }

    @Override
    public void showMessage(String message) {
        JPanel jp = new JPanel();
        jp.add(new JLabel(message));
        switchTo(jp);
    }

    // TODO Check for something like this in standard libs, or move to utility
    private class AwaitableValue<T> {
        Optional<T> value;
        boolean isSet = false;

        public synchronized void set(T t) {
            value = Optional.of(t);
            isSet = true;
            notifyAll();
        }

        public synchronized void cancel() {
            value = Optional.empty();
            isSet = true;
            notifyAll();
        }

        public synchronized Optional<T> get() throws InterruptedException {
            while (!isSet)
                wait();
            return value;
        }
    }

    @Override
    public synchronized void await(String message) throws InterruptedException {
        interruptLock.registerThread();

        showMessage(message);

        final AwaitableValue<Void> val = new AwaitableValue<Void>();
        inputsrc.addListener(new BooleanInputSource.Listener() {
            public void onBooleanInput() {
                inputsrc.removeListener(this);
                val.cancel();
            }
        });
        val.get();

        switchTo(idlePanel);

        interruptLock.deregisterThread();
    }

    @Override
    public synchronized <T extends Displayable> Optional<T> select(List<T> items) throws InterruptedException {
        interruptLock.registerThread();

        final AwaitableValue<T> val = new AwaitableValue<T>();
        switchTo(new ScrollSelector<T>(inputsrc, items, val::set, val::cancel));

        Optional<T> rv = val.get();
        switchTo(idlePanel);

        interruptLock.deregisterThread();
        return rv;
    }

    @Override
    public synchronized <T> Optional<T> get(ParamType<T> p) throws InterruptedException {
        throw new IllegalArgumentException("Not supported: " + p);
    }

    @Override
    public Component getGlassPane() {
        return frame.getGlassPane();
    }
}
