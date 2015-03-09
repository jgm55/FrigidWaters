package edu.drexel.cci.hiyh.bci;

import java.util.Arrays;
import java.util.Collections;


public class SignalDetector {
	private double[] calibrations = new double[14];
	private double[] ranges = new double[14];
	private double[][] store = new double[14][500];
	private boolean calibrated = false;
	private int index = 0;
	
	public SignalDetector() {
	}
	
	
	public boolean process(double[][] data) {
		if (!calibrated) {
			for (int i = 0;i<data[0].length; i++) {
				for(int j =0;j<data.length;j++) {
					store[j][index]=data[j][i];
				}
				index++;
				if (index>= store[0].length) {
					calibrated = true;
					for (int j=0; j<calibrations.length;j++) {
						calibrations[j]=max(store[j]);
						ranges[j]=max(store[j])-min(store[j]);
					}
					System.out.println("calibrated");
				}
			}
			return false;
		}
		int i= 0;
		for (double[] variable : data) {
			//double pChange =(max(variable) - min(variable));///min(variable);
	        if(0.5*ranges[i]+calibrations[i]< max(variable)) {
	        	return true;
	        }
	        i++;
		}
		return false;
	}
	
	private double avg(double[] a) {
		double sum = Double.NaN;
		for (double d : a) {
			if (Double.isNaN(sum)){
				sum=d;
			} else {
				sum+=d;
			}
		}
		return sum/a.length;
	}
	private double max(double[] a) {
		double max = Double.NaN;
		for (double d : a) {
			if (Double.isNaN(max) || d>max){
				max=d;
			}
		}
		return max;
	}
	
	private double min(double[] a) {
		double min = Double.NaN;
		for (double d : a) {
			if (Double.isNaN(min) || d<min){
				min=d;
			}
		}
		return min;
	}
}
