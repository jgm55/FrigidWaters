package edu.drexel.cci.hiyh.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScrollInteger extends JPanel implements
		BooleanInputSource.Listener {
	private static final int ITERATIONS_BEFORE_CANCEL = 2;
	private static int MAX_NUMBER_OF_DIGITS = 3;

	private final int lowerBound;
	private final int upperBound;

	private final Consumer<Integer> success;
	private final Runnable cancel;

	// replace with list of "slot" labels
	private List<JLabel> slotList = new ArrayList<JLabel>();

	// list of integers corresponding to the labels (GOLD VALS)
	private List<Integer> finalDigitValues = new ArrayList<Integer>();

	// index for currently active slot label
	private int activeSlotIndex = 0;

	private final JLabel headerLabel = new JLabel();
	private int index = 0, counter = 0;
	private boolean active = true;

	private Timer timer = new Timer(true);

	private final BooleanInputSource inputsrc;

	public ScrollInteger(BooleanInputSource inputsrc, int lower, int upper,
			Consumer<Integer> success, Runnable cancel) {
		this.success = success;
		this.cancel = cancel;
		this.inputsrc = inputsrc;
		lowerBound = lower;
		upperBound = upper;
		index = lowerBound;

		int range = upper - lower;
		MAX_NUMBER_OF_DIGITS = (int)Math.log10(lower + range) + 1;
		
		SwingUtilities.invokeLater(this::buildUI);
		inputsrc.addListener(this);
		startTimer();
	}

	private void buildUI() {
		add(headerLabel);
		// TODO add labels for slots, depending on bounds
		// place labels for dashes
		JLabel digitSlot;
		for (int i = 0; i < MAX_NUMBER_OF_DIGITS; i++) {
			digitSlot = new JLabel();
			slotList.add(digitSlot);

			// Set as dashes not index
			// digitSlot.setText(Integer.toString(i));

			finalDigitValues.add(-1);
			add(digitSlot);
		}

		updateCurrentDigitValue();
		updateDisplay();
	}

	private synchronized void cleanup() {
		stopTimer();
		inputsrc.removeListener(this);
		active = false;
	}

	private void updateCurrentDigitValue() {
		finalDigitValues.set(activeSlotIndex, index);
	}

	private void updateDisplay() {
		// headerLabel.setText(Integer.toString(index));
		// Set labels to GOLD VALS
		// slotList.get(activeSlotIndex).setText(finalDigitValues.get(activeSlotIndex).toString());

		for (int i = 0; i < slotList.size(); i++) {
			if (finalDigitValues.get(i) < 0) {
				slotList.get(i).setText("-");
			} else {
				slotList.get(i).setText(finalDigitValues.get(i).toString());
			}
		}
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
		if (counter >= ITERATIONS_BEFORE_CANCEL * (upperBound - lowerBound)) {
			cleanup();
			cancel.run();
		} else {
			index = counter % (10); // Mod 10 because this is number if digits
									// we cycle through
			updateCurrentDigitValue();
			// updateDisplay();
			SwingUtilities.invokeLater(this::updateDisplay);
		}
	}

	// TODO: Combine digits to one integer number
	public int getFinalInteger() {
		return finalDigitValues.stream().reduce(0, (a, b) -> a * 10 + b);
	}

	public void onBooleanInput() {
		new Thread(this::select).start();
	}

	private synchronized void select() {
		// Move cycle to next digit
		activeSlotIndex++;
		if (!(activeSlotIndex >= MAX_NUMBER_OF_DIGITS)) {
			counter = 0;
			index = 0;
			updateCurrentDigitValue();
			updateDisplay();
		} else {
			// Only call when DONE or entire number is entered
			if (!active)
				return;
			cleanup();
			success.accept((getFinalInteger()));
		}
	}
}
