package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScrollSelector<T extends Displayable> extends JPanel implements BooleanInputSource.Listener {
	private static final int ITERATIONS_BEFORE_CANCEL = 2;

	private final List<T> items;
	private final Consumer<T> success;
	private final Runnable cancel;

	private final JLabel headerLabel = new JLabel();
	private JLabel imageLabel = new JLabel();

	private int index = 0, counter = 0;
	private boolean active = true;

	private Timer timer = new Timer(true);

	private final BooleanInputSource inputsrc;

	public ScrollSelector(BooleanInputSource inputsrc, List<T> items, Consumer<T> success, Runnable cancel) {
		this.items = items;
		this.success = success;
		this.cancel = cancel;
		this.inputsrc = inputsrc;
		SwingUtilities.invokeLater(this::buildUI);
		inputsrc.addListener(this);
		startTimer();
		if (items.size() == 1) {
			select();
		}
	}

	public void onBooleanInput() {
		new Thread(this::select).start();
	}


	private void buildUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		add(headerLabel);
		add(imageLabel);
		updateDisplay();
	}

	private synchronized void cleanup() {
		stopTimer();
		inputsrc.removeListener(this);
		active = false;
	}

	private void updateDisplay() {
		headerLabel.setText(items.get(index).getDisplayText());
		addMessageImage();
	}

	private void addMessageImage(){
		Image icon = items.get(index).getDisplayImage();
		if(icon != null){
			imageLabel.setIcon(new ImageIcon(icon));
		}

		//		Uncomment to test while getDisplayImage still returns null
		//    	
		//        BufferedImage myPicture;
		//        String imagePath = "Path/to/image.jpg";    	
		//    	try {
			//			myPicture = ImageIO.read(new File(imagePath));			
		//			imageLabel.setIcon(new ImageIcon(myPicture));
		//		} catch (IOException e1) {
		//			// TODO Auto-generated catch block
		//			e1.printStackTrace();
		//		}

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
		if (counter >= ITERATIONS_BEFORE_CANCEL * items.size()) {
			cleanup();
			cancel.run();
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
