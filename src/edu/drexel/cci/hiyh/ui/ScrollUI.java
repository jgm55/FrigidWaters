package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
        switchToIdle();

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

    private void switchToIdle() {
        switchTo(idlePanel);
    }

    // Consumer-thing so we can chain it in select and get
    private <T> void switchToIdle(T unused) {
        switchToIdle();
    }

    // TODO Move to utility class?
    private Runnable chain(Runnable... rs) {
        return new Runnable() {
            public void run() {
                for (Runnable r : rs)
                    r.run();
            }
        };
    }

    @Override
    public void showMessage(String message) {
        JPanel jp = new JPanel();
        jp.add(new JLabel(message));
        switchTo(jp);
    }

    @Override
    public void await(Runnable target, String message) {
        showMessage(message);
        inputsrc.addListener(new BooleanInputSource.Listener() {
            public void onBooleanInput() {
                inputsrc.removeListener(this);
                switchToIdle();
                target.run();
            }
        });
    }

    @Override
    public <T extends Displayable> void select(List<T> items, Consumer<T> success, Runnable cancel) {
        switchTo(new ScrollSelector<T>(
                    inputsrc,
                    items,
                    ((Consumer<T>)this::switchToIdle).andThen(success),
                    chain(this::switchToIdle, cancel)));
    }

    @Override
    public <T> void get(Class<T> c, Consumer<T> success, Runnable cancel) {
        throw new IllegalArgumentException("Not supported: " + c);
    }

    @Override
    public Component getGlassPane() {
        return frame.getGlassPane();
    }
}
