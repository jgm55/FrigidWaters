package edu.drexel.cci.hiyh.ui;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScrollSelector<T extends Displayable> extends JPanel {
    private static final int ITERATIONS_BEFORE_CANCEL = 2;

    private final List<T> items;
    private final Consumer<T> success;
    private final Consumer<Void> cancel;

    private final JLabel headerLabel = new JLabel();
    private int index = 0, counter = 0;
    private boolean active = true;

    private Timer timer = new Timer();

    public ScrollSelector(List<T> items, Consumer<T> success, Consumer<Void> cancel) {
        this.items = items;
        this.success = success;
        this.cancel = cancel;
        try {
            SwingUtilities.invokeAndWait(this::buildUI);
        } catch (InterruptedException | InvocationTargetException e) {
            // FIXME
            e.printStackTrace();
        }
        startTimer();
    }

    private void buildUI() {
		headerLabel.setHorizontalAlignment(JLabel.CENTER);
		headerLabel.setVerticalAlignment(JLabel.CENTER);
		
		setLayout(new FlowLayout());

		setSize(400,200);
		setVisible(true);
		
		add(headerLabel);

		addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        new Thread(ScrollSelector.this::select).start();
		    }
		});

		updateDisplay();
		// TODO ???
		//this.repaint();
		//this.validate();
    }

    private synchronized void cleanup() {
        stopTimer();
        active = false;
    }

    private void updateDisplay() {
        headerLabel.setText(items.get(index).getDisplayText());
        // TODO ???
        // this.validate();
    }

    private void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                next();
            }
        }, 5000, 5000);
    }

    private void stopTimer() {
        timer.cancel();
    }

    private synchronized void next() {
        if (!active)
            return;
        counter++;
        if (counter > ITERATIONS_BEFORE_CANCEL * items.size()) {
            cleanup();
            cancel.accept(null);
        } else {
            index = counter % items.size();
            SwingUtilities.invokeLater(this::updateDisplay);
        }
    }

    private synchronized void select() {
        if (!active)
            return;
        cleanup();
        success.accept(items.get(index));
    }
}
