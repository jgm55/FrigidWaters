package edu.drexel.cci.hiyh.ui;

import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScrollUI implements InputUI {

    private BooleanInputSource inputsrc;
    private final JFrame frame = new JFrame("HIYH");
    private final JPanel idlePanel = new JPanel();

    public ScrollUI(BooleanInputSource inputsrc) {
        this.inputsrc = inputsrc;
        try {
            SwingUtilities.invokeAndWait(this::buildUI);
        } catch (InterruptedException | InvocationTargetException e) {
            // FIXME
            e.printStackTrace();
        }
    }

    private void buildUI() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        idlePanel.add(new JLabel("(idle)"));
        idlePanel.setVisible(true);

        switchToIdle();

        frame.setVisible(true);
    }


    private void switchTo(Container pane) {
        frame.setContentPane(pane);
        frame.pack();
    }

    private void switchToIdle() {
        switchTo(idlePanel);
    }

    // Consumer-thing so we can chain it in select and get
    private <T> void switchToIdle(T unused) {
        switchToIdle();
    }

    @Override
    public <T extends Displayable> void select(List<T> items, Consumer<T> success, Consumer<Void> cancel) {
        switchTo(new ScrollSelector<T>(
                    inputsrc,
                    items,
                    ((Consumer<T>)this::switchToIdle).andThen(success),
                    ((Consumer<Void>)this::switchToIdle).andThen(cancel)));
    }

    @Override
    public <T> void get(Class<T> c, Consumer<T> success, Consumer<Void> cancel) {
        throw new IllegalArgumentException("Not supported: " + c);
    }
}
