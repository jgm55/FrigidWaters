package edu.drexel.cci.hiyh.bci;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.drexel.cci.hiyh.ui.AbstractBooleanInputSource;
import edu.drexel.cci.hiyh.ui.InputUI;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Main interface into the BCI. Reports a signal whenever it detects an
 * "active" thought/state from the user.
 */
public class SignalDetector extends AbstractBooleanInputSource {
    // "Training" is the calibration phase, where we train our classifier on
    // the user's BCI input.
    // "Listening" is when we use the classifier to detect active thoughts
    // from the user.
    //
    // A "window" is the set of samples that we pass to the feature extractor.
    // The "offset" is the number of samples we advance past between windows.
    // If offset < window size, then we have overlapping windows.
	private static final int TRAINING_WINDOW = 128;
	private static final int TRAINING_OFFSET = 64;
	// Number of windows we use for training each state (neutral and active).
	private static final int TRAINING_WINDOWS_PER_STATE = 10;

	private static final int LISTENING_WINDOW = 128;
	private static final int LISTENING_OFFSET = 128;
	// The minimum number of consecutive windows that must classify positive
	// in order to generate a signal.
	private static final int LISTENING_MIN_POSITIVE = 4;
	
	private Instances trainingSet;
	
    private static final FastVector attributes = new FastVector(46);
	static {
	    // FIXME Do we really need a nominal field for the class value?
	    FastVector classVals = new FastVector(2);
		classVals.addElement("negative");
		classVals.addElement("positive");
		// FIXME magic number of features
	    for (int i = 0; i < 45; i++)
	        attributes.addElement(new Attribute("" + i));
	    attributes.addElement(new Attribute("theClass", classVals));
	}

	private final Classifier classifier = new NaiveBayes();
	private final BCICollector collector = new BCICollector();
	    // TODO Close this at some point

    @Override
    public void initAndStart(InputUI ui) {
        calibrate(ui);

        Thread t = new Thread(this::listenLoop);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Guides the user through the calibration process.
     *
     * @param ui Graphical interface to user
     */
    public void calibrate(InputUI ui) {
        final int training_size = TRAINING_WINDOWS_PER_STATE * TRAINING_OFFSET
                            + (TRAINING_WINDOW - TRAINING_OFFSET);

        ui.showMessage("<html><center>Calibrating.<br>Think a neutral thought.</center></html>");
        //flush();
        List<double[]> neutral = new ArrayList<double[]>(training_size);
        awaitData(neutral, training_size);

        ui.showMessage("<html><center>Calibrating.<br>Think an active thought.</center></html>");
        //flush();
        List<double[]> active = new ArrayList<double[]>(training_size);
        awaitData(active, training_size);

        train(neutral, active);
    }

    /**
     * Convert a double[] of features to an Instance.
     */
	private Instance makeInstance(double[] d) {
		// FIXME magic number of features
		Instance inst = new Instance(46);
		for(int i = 0; i < d.length; i++)
			inst.setValue(i, d[i]);
		inst.setDataset(trainingSet);
		return inst;
	}

    /**
     * Accepts BCI input and appends it to data until data is at least size
     * elements long.
     *
     * FIXME Should this make it *exactly* size elements long?
     */
    private void awaitData(List<double[]> data, int size) {
        while (data.size() < size)
            data.addAll(Arrays.asList(collector.getData()));
    }

    /**
     * Converts a window of samples to classifier features.
     */
    private double[] convertToFeatures(List<double[]> samples) {
        double[][] left = new double[3][samples.size()];
        double[][] right = new double[3][samples.size()];
		for(int i = 0; i < samples.size(); i++){
			left[0][i] = samples.get(i)[6];
			left[1][i] = samples.get(i)[5];
			left[2][i] = samples.get(i)[3];
			right[0][i] = samples.get(i)[7];
			right[1][i] = samples.get(i)[8];
			right[2][i] = samples.get(i)[10];
		}
		return ExtractFeatures.extractFeatures(left, right);
    }

    /**
     * Trains the classifier on sample data.
     */
    private void train(List<double[]> neutral, List<double[]> active) {
	    trainingSet = new Instances("trainingSet", attributes, 0);
		trainingSet.setClassIndex(45);

        for (int i = 0; i+TRAINING_WINDOW < neutral.size(); i += TRAINING_OFFSET) {
            Instance inst = makeInstance(convertToFeatures(neutral.subList(i, i+TRAINING_WINDOW)));
            inst.setValue(45, "negative");
            trainingSet.add(inst);
        }
        for (int i = 0; i+TRAINING_WINDOW < active.size(); i += TRAINING_OFFSET) {
            Instance inst = makeInstance(convertToFeatures(active.subList(i, i+TRAINING_WINDOW)));
            inst.setValue(45, "positive");
            trainingSet.add(inst);
        }
		try {
		    classifier.buildClassifier(trainingSet);
		} catch (Exception e) {
		    // TODO What???
		    e.printStackTrace();
		    System.err.println("CLASSIFIER FAILED");
		}
    }

    /**
     * Loop forever, getting input from BCICollector, classifying it, and
     * reporting active states.
     */
	private void listenLoop() {
	    List<double[]> data = new LinkedList<double[]>();
	    int numPositive = 0;

        // FIXME stopping condition?
	    while (true) {
	        awaitData(data, LISTENING_WINDOW);
	        // TODO
            Instance inst = makeInstance(convertToFeatures(data.subList(0, LISTENING_WINDOW)));
            inst.setClassValue(45);
            int klass = 0;
            try {
                klass = (int)classifier.classifyInstance(inst);
            } catch (Exception e) {
                System.err.println("Failed to classify instance: " + inst);
                e.printStackTrace();
            }
            if (klass == 1) { // positive
                if (++numPositive >= LISTENING_MIN_POSITIVE) {
                    notifyListeners();
                    // TODO cooldown?
                    numPositive = 0;
                }
            } else {
                numPositive = 0;
            }

            // To "advance" the data, clear the data at the start of this
            // window.
            // Next iteration will pull in more data.
            data.subList(0, LISTENING_OFFSET).clear();
	    }
	}
}
