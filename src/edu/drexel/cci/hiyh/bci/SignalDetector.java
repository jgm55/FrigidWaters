package edu.drexel.cci.hiyh.bci;

import java.util.ArrayList;
import java.util.List;

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
	
	private static FastVector classVals =new FastVector(2);
	static {
		classVals.addElement("negative");
		classVals.addElement("positive");
	}
	
    private static final FastVector attributes = new FastVector(45);
	static {
	    for (int i = 0; i < 45; i++)
	        attributes.addElement(new Attribute("" + i));
	    attributes.addElement(new Attribute("theClass", classVals));
	}
	

	private static Instances trainingSet = new Instances("trainingSet", attributes, 0);
	
	static{
		trainingSet.setClassIndex(45);
	}

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
	
	boolean first = true;
	public synchronized boolean process(double[][] data) {
		for(double[] d: data){
			totalData.add(d);
		}
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
			if (totalData.size() >= NUM_SAMPLES /2 && first ) {
				System.out.println("done training neutral");
				first=false;
			}
			if(totalData.size() >= NUM_SAMPLES ){	
				System.out.println(totalData.size());
				double[][] listOfFeatureListsNuetral = new double[45][10];
				double[][] listOfFeatureListsAccept = new double[45][10];
				getFeatureList(totalData, 0, NUM_SAMPLES/2, listOfFeatureListsNuetral);
				getFeatureList(totalData, NUM_SAMPLES/2, NUM_SAMPLES, listOfFeatureListsAccept);
				
				for(double[] d : listOfFeatureListsNuetral){
					Instance inst = makeInstance(d);
					inst.setValue(45, "negative");
					trainingSet.add(inst);
				}
				for(double[] d : listOfFeatureListsAccept){
					Instance inst = makeInstance(d);
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
				totalData.clear();
		        setCalibrated(true);
				System.out.println("calibrated");
			}
			return false;
		}
		if(totalData.size() >= 128){
			try {
		    	//Instances instances = new Instances("testingSet", attributes, 0);
		    	double[][] listOfFeatures = new double[3][1];
		    	getFeatureList(totalData,totalData.size() - 128, totalData.size(), listOfFeatures);
		    	Instance inst = makeInstance(listOfFeatures[0]);
		    	inst.setDataset(trainingSet);
		    	inst.setClassValue(45);
		    	if (classifier.classifyInstance(inst) == 1){
					totalData.clear();
		    		return true;
		    	}
		    } catch (Exception e) {
		        // TODO What???
		    	e.printStackTrace();
		    }
			totalData.clear();
		}
		return false;
	}

	private Instance makeInstance(double[] d) {
		Instance inst = new Instance(46);
		for(int i=0;i<d.length;i++){
			inst.setValue(i, d[i]);
		}
		inst.setDataset(trainingSet);
		//inst.setClassValue(45);BAD MAYBE
		return inst;
	}

	private void getFeatureList(List<double[]> data, int start, int end,
			double[][] listOfFeatureLists) {
		ExtractFeatures extractor = new ExtractFeatures();
		for(int j=0;j<(end - start) / 128;j++){
			double[][]left = new double[3][128];
			double[][]right = new double[3][128];
			for(int i=0;i<128; i++){
				right[0][i] = data.get(start + i + 128 * j)[7];
				right[1][i] = data.get(start + i+ 128 * j)[8];
				right[2][i] = data.get(start + i+ 128 * j)[10];
				
				left[0][i] = data.get(start + i+ 128 * j)[6];
				left[1][i] = data.get(start + i+ 128 * j)[5];
				left[2][i] = data.get(start + i+ 128 * j)[3];
			}
			double[] listOfFeatures = extractor.extractFeatures(left, right);
			listOfFeatureLists[j] = listOfFeatures;
		}
		
	}
}
