package edu.drexel.cci.hiyh.ui;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.function.Consumer;

public class ScrollView extends Panel implements InputUI{
	private Label headerLabel;
	private Timer timer;
	
	private Object currentObject;
	
	private	ArrayList<String> scrollObjects = new ArrayList<String>(){{
		add("Lights");
		add("Television");
		add("Heat");		
	}};

	public ScrollView(ArrayList<String> obj){
//		objs = obj;
		currentObject = scrollObjects.get(0);
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
		int idx = scrollObjects.indexOf(currentObject) + 1;
		if(idx > scrollObjects.size() - 1){
			idx = 0;   
		}        
		headerLabel.setText(scrollObjects.get(idx));
		headerLabel.setSize(headerLabel.getPreferredSize());
		headerLabel.setAlignment(Label.CENTER);

		currentObject = scrollObjects.get(idx);	
	}


	public void previousObject(){ 		
		int idx = scrollObjects.indexOf(currentObject) - 1;
		
		if(idx < 0){
			idx = scrollObjects.size() - 1;   
		}
		
		headerLabel.setText(scrollObjects.get(idx));
		headerLabel.setSize(headerLabel.getPreferredSize());
		headerLabel.setAlignment(Label.CENTER);
		currentObject = scrollObjects.get(idx);		

	}
	
	public String getCurrentObject(){
		return (String)currentObject;
	}
	
	public int getCurrentObjectIndex(){
		return scrollObjects.indexOf(currentObject);
	}
	
    public <T extends Displayable> void select(List<T> items, Consumer<T> success, Consumer<Void> cancel) {
        System.out.println("Select one, or enter an index out of range to cancel:");
        int i = 0;
        for (T o : items)
            System.out.printf("%d. %s\n", i++, o);

//        int s = reader.nextInt();
        //Assumes items and the list of scroll objects have corresponding index
        int s = getCurrentObjectIndex();
        
        if (s >= 0 && s < items.size())
            success.accept(items.get(s));
        else
            cancel.accept(null);
    }

    
    @Override
    /**
     * {@inheritDoc}
     */
    public <T> void get(Class<T> c, Consumer<T> success, Consumer<Void> cancel) {
        // Note that this doesn't actually include a cancel option.
        if (c.equals(Byte.class))
            success.accept((T) getCurrentObject());
        else
            throw new IllegalArgumentException("Can't handle class: " + c);
    }




}
