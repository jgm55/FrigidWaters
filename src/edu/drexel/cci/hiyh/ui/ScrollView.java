package edu.drexel.cci.hiyh.ui;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class ScrollView extends Panel{
	private Label headerLabel;
	private Timer timer;
	
	private Object currentObject;
	
	private	ArrayList<String> objs = new ArrayList<String>(){{
		add("Lights");
		add("Television");
		add("Heat");		
	}};

	public ScrollView(ArrayList<String> obj){
//		objs = obj;
		currentObject = objs.get(0);
		buildSelectionUI();
	}

	private void buildSelectionUI(){
		this.setLayout(new GridLayout(2, 1));

		headerLabel = new Label();
		headerLabel.setAlignment(Label.CENTER);
		headerLabel.setText((String)currentObject);
		
		this.setLayout(new FlowLayout());

		setSize(400,200);
		setVisible(true);
		
		this.add(headerLabel);
		this.repaint();
		this.validate();

		startTimer();
	}

	
//  UI Cycle
	public void startTimer(){
		System.out.println("Timer Started");
		timer = new Timer();
		timer.schedule( 
				new java.util.TimerTask() {
	        		@Override
	        		public void run() {
	        			System.out.println("running timed code");
	            		nextObject();
	        		}
	    		},
	    		5000, 
	    		5000 
		);
	
	}
	
	public void stopTimer(){
		System.out.println("Timer Stopped");
		timer.cancel();		
	}


	public void nextObject(){
		int idx = objs.indexOf(currentObject) + 1;
		if(idx > objs.size() - 1){
			idx = 0;   
		}        
		headerLabel.setText(objs.get(idx));
		headerLabel.setSize(headerLabel.getPreferredSize());
		headerLabel.setAlignment(Label.CENTER);

		currentObject = objs.get(idx);	
	}


	public void previousObject(){ 		
		int idx = objs.indexOf(currentObject) - 1;
		
		if(idx < 0){
			idx = objs.size() - 1;   
		}
		
		headerLabel.setText(objs.get(idx));
		headerLabel.setSize(headerLabel.getPreferredSize());
		headerLabel.setAlignment(Label.CENTER);
		currentObject = objs.get(idx);		

	}
	
	public String getCurrentObject(){
		return (String)currentObject;
	}



}
