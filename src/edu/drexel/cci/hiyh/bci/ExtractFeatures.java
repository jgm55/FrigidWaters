package edu.drexel.cci.hiyh.bci;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer; 
import org.apache.commons.math3.transform.TransformType;
public class ExtractFeatures {
	
	public double[] extractFeatures(double[][] left, double [][] right){
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
		Complex [] res;
		double[] deltaL = new double[left.length];
		double[] thetaL= new double[left.length];
		double[] alphaL= new double[left.length];
		double[] betaL= new double[left.length];
		double[] gammaL= new double[left.length];
		double[] deltaR= new double[left.length];
		double[] thetaR= new double[left.length];
		double[] alphaR= new double[left.length];
		double[] betaR= new double[left.length];
		double[] gammaR= new double[left.length];
		int index = 0;
		for (double [] lstream : left) {
			res = fft.transform(lstream, TransformType.FORWARD);
			for (int i =0; i< 3*128.0/res.length; i++) {
				deltaL[index]+=res[i].abs();
			}
			for (int i =(int) (3*128.0/res.length)+1; i<  7*128.0/res.length; i++) {
				thetaL[index]+=res[i].abs();
			}
			for (int i =(int) (7*128.0/res.length)+1; i< 13*128.0/res.length; i++) {
				alphaL[index]+=res[i].abs();
			}
			for (int i =(int) (13*128.0/res.length)+1; i< 20*128.0/res.length; i++) {
				betaL[index]+=res[i].abs();
			}
			for (int i =(int) (13*128.0/res.length)+1; i< 40*128.0/res.length; i++) {
				gammaL[index]+=res[i].abs();
			}
			index++;
		}
		index = 0;
		for (double [] rstream : right) {
			res = fft.transform(rstream, TransformType.FORWARD);
			for (int i =43; i< res.length; i++) {
				deltaR[index]+=res[i].abs();
			}
			for (int i =18; i< 43; i++) {
				thetaR[index]+=res[i].abs();
			}
			for (int i =10; i< 18; i++) {
				alphaR[index]+=res[i].abs();
			}
			for (int i =6; i< 10; i++) {
				betaR[index]+=res[i].abs();
			}
			for (int i =3; i< 10; i++) {
				gammaR[index]+=res[i].abs();
			}
			index++;
		}
		double[] output = new double[5*left.length*right.length];
		for (int i=0;i<deltaL.length;i++) {
			for(int j=0;j<deltaR.length;j++) {
				output[i*deltaR.length+j] = (deltaL[i]-deltaR[j])/(deltaL[i]+deltaR[j]);
			}
		}
		for (int i=0;i<thetaL.length;i++) {
			for(int j=0;j<thetaR.length;j++) {
				output[i*thetaR.length+j + (left.length*right.length)] = (thetaL[i]-thetaR[j])/(thetaL[i]+thetaR[j]);
			}
		}
		for (int i=0;i<alphaL.length;i++) {
			for(int j=0;j<alphaR.length;j++) {
				output[i*alphaR.length+j + (2*left.length*right.length)] = (alphaL[i]-alphaR[j])/(alphaL[i]+alphaR[j]);
			}
		}
		for (int i=0;i<betaL.length;i++) {
			for(int j=0;j<betaR.length;j++) {
				output[i*betaR.length+j + (3*left.length*right.length)] = (betaL[i]-betaR[j])/(betaL[i]+betaR[j]);
			}
		}
		for (int i=0;i<gammaL.length;i++) {
			for(int j=0;j<gammaR.length;j++) {
				output[i*gammaR.length+j + (4*left.length*right.length)] = (gammaL[i]-gammaR[j])/(gammaL[i]+gammaR[j]);
			}
		}
		return output;
		
	}
	
	/*public static void main(String [] args) {
		/*BufferedReader is = null;
		try {
			is = new BufferedReader(new FileReader("C:\\Users\\Kyle\\Desktop\\data"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Stream<String> lines = is.lines();
	    double [] r= new double[128];
	    int index =0;
	    Iterator<String> iter = lines.iterator();
	    while (iter.hasNext() && index<128) {
	    	r[index]=Double.parseDouble(iter.next());
	    	index++;
	    }
		int size = 256;
		double[] a = {1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1};
		/*for (int i=0;i<256;i+=1) {
			a[i]=Math.sin((i/4.0)*2*Math.PI);// + Math.sin((i/4.0)*Math.PI);
		}
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
		Complex[] res = fft.transform(a, TransformType.FORWARD);
		int i =0;
		for(Complex c : res) {
			System.out.println(i +" "+ c);
			i++;
		}
	}*/
	
}
