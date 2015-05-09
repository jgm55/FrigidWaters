package edu.drexel.cci.hiyh.bci;

import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class SignalDetector {
	private boolean calibrated = false;
	private final int NUM_SAMPLES = 1240 * 2;
	ArrayList<double[]> totalData = new ArrayList<double[]>();
	
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
			//Left is odd, right is even
			//Indexes we are using:
			/*  O1 = 6
				O2 = 7
				p3 = p7 = 5
				p4 = p8 = 8
				c3 = FC5 = 3
				c4 = FC6 = 10
			*/
			for(double[] d: data){
				totalData.add(d);
			}
			if(totalData.size() >= NUM_SAMPLES ){
				double[][] listOfFeatureListsNuetral = new double[45][10];
				double[][] listOfFeatureListsAccept = new double[45][10];
				getFeatureList(data,0,NUM_SAMPLES / 2, listOfFeatureListsNuetral);
				getFeatureList(data,NUM_SAMPLES / 2,NUM_SAMPLES, listOfFeatureListsAccept);
				
				for(double[] d : listOfFeatureListsNuetral){
					Instance inst = new Instance(1,d);
					inst.setValue(45, 0);
					
					trainingSet.add(inst);
				}
				for(double[] d : listOfFeatureListsAccept){
					Instance inst = new Instance(1,d);
					inst.setValue(45, 0);
					trainingSet.add(inst);
				}
				
				try {
		            classifier.buildClassifier(trainingSet);
		        } catch (Exception e) {
		            // TODO What???
		        	System.err.println("CLASSIFIER FAILED");
		        }
		        setCalibrated(true);
				System.err.println("calibrated");
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

	private void getFeatureList(double[][] data, int start, int end,
			double[][] listOfFeatureLists) {
		
		ExtractFeatures extractor = new ExtractFeatures();
		for(int j=start;j<end / 128;j++){
			double[][]left = new double[3][128];
			double[][]right = new double[3][128];
			for(int i=0;i<128; i++){
				right[i][0] = data[i][7];
				right[i][1] = data[i][8];
				right[i][2] = data[i][10];
				
				left[i][0] = data[i][6];
				left[i][1] = data[i][5];
				left[i][2] = data[i][3];
			}
			double[] listOfFeatures = extractor.extractFeatures(left, right);
			listOfFeatureLists[j-start] = listOfFeatures;
		}
		
	}
}
