package edu.drexel.cci.hiyh.bci;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class SignalDetector {
	private boolean calibrated = false;

    private static final FastVector attributes = new FastVector(14);
	static {
	    for (int i = 0; i < 15; i++)
	        attributes.addElement(new Attribute("" + i));
	}

	private Instances trainingSet = new Instances("trainingSet", attributes, 1000);

	private Classifier classifier = new J48();
	
	public synchronized boolean getCalibrated() {
		return calibrated;
	}

	public synchronized void awaitCalibrated() throws InterruptedException {
		while(!calibrated)
			wait();
	}
	
	private synchronized void setCalibrated(boolean cal) {
		calibrated = cal;
		notifyAll();
	}
	
	public synchronized boolean process(double[][] data) {
		if (!calibrated) {
			for (double[] d : data) {
			    Instance inst = new Instance(1, d);
			    // set class: first 500 assumed idle, next 500 assumed active
			    // TODO it right
			    inst.setValue(14, trainingSet.numInstances() < 500 ? 0 : 1);
			    trainingSet.add(inst);
			    if (trainingSet.numInstances() == 500) {
			        System.err.println("Switch to active");
			    } else if (trainingSet.numInstances() >= 1000) {
			        try {
			            classifier.buildClassifier(trainingSet);
			        } catch (Exception e) {
			            // TODO What???
			        }
			        setCalibrated(true);
					System.err.println("calibrated");
			    }
			}
			return false;
		}
		for (double[] d : data) {
		    try {
		        if (classifier.classifyInstance(new Instance(1, d)) == 1)
		            return true;
		    } catch (Exception e) {
		        // TODO What???
		    }
		}
		return false;
	}
}
