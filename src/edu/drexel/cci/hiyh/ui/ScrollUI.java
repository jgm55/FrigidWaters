package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ScrollUI implements InputUI {

    private final BooleanInputSource inputsrc;
    private final JFrame frame = new JFrame("HIYH");
    private final JPanel idlePanel = new JPanel();

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

        public synchronized Optional<T> get() {
            while (!isSet)
                // XXX Should we pass this up instead of swallowing it?
                try {
                    wait();
                } catch (InterruptedException exc) {}
            return value;
        }
    }

    @Override
    public void await(String message) {
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
    }

    @Override
    public <T extends Displayable> Optional<T> select(List<T> items) {
        final AwaitableValue<T> val = new AwaitableValue<T>();
        switchTo(new ScrollSelector<T>(inputsrc, items, val::set, val::cancel));

        Optional<T> rv = val.get();
        switchTo(idlePanel);
        return rv;
    }

    @Override
    public <T> Optional<T> get(Class<T> c) {
        throw new IllegalArgumentException("Not supported: " + c);
    }

    @Override
    public Component getGlassPane() {
        return frame.getGlassPane();
    }
}
