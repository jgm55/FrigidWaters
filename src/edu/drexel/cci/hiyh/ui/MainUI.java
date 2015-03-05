package edu.drexel.cci.hiyh.ui;

//import edu.drexel.cci.hiyh.home.insteon.LightDevice;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import javax.swing.JFrame;


public class MainUI extends JFrame implements ActionListener{
//	TODO clean up components and don't make them all global. Will be done by cycle 3
	Button left_arrow, right_arrow, selectApplicationButton, confirmButton, cancelButton, calibrationContinueButton, calibrationCompleteButton, calibrationFailedButton;
	private Label headerLabel;
	private Label popupLabel;
	private Label calibrationLabel;
	private ArrayList<Component> currentComps = new ArrayList<Component>();
	
	private Panel controlPanel_main;
	private Panel controlPanel_popup;
	private Panel calibrationPanel;
	private ScrollView currentScrollView;
	
	private Frame mainFrame;
	private Frame popupFrame;
	
	private	ArrayList<String> objs = new ArrayList<String>(){{
		add("Lights");
		add("Television");
		add("Heat");		
	}};

//	private LightDevice ldev;
	
//	public MainUI(LightDevice ldev){

	public MainUI(){

//	    this.ldev = ldev;
		System.out.println("STARTED!!");
		popupFrame = new Frame("");
		
		buildMainUI();
		buildCalibrationUI();
		buildPopupUI();
		
	}
	
//  Place actions here

public void actionPerformed(ActionEvent e){
		String str = e.getActionCommand();
		
		switch(str){
			case ">":
				currentScrollView.stopTimer();
				currentScrollView.nextObject();
				currentScrollView.startTimer();
				break;
			case "<":
				currentScrollView.stopTimer();
				currentScrollView.previousObject();
				currentScrollView.startTimer();
				break;
			case "Select":
				currentScrollView.stopTimer();
				String selectedObject = currentScrollView.getCurrentObject();

				popupLabel.setText("Change state of " + selectedObject);
				popupFrame.setVisible(true);
				break;
			case "Confirm":				
				currentScrollView.getCurrentObject();
				switch(currentScrollView.getCurrentObject()) {
				    case "Lights":
//				        try {
//				            ldev.toggle();
//				        } catch (IOException exc) {
//				            // XXX Dumb simple way for prototype. Do NOT do this
//				            // in final version!
//				            exc.printStackTrace();
//				            System.exit(1);
//				        }
				        break;
				    default:
				    	break;
				}
				popupFrame.setVisible(false);
				currentScrollView.startTimer();
				break;
			case "Cancel":
				popupFrame.setVisible(false);
				currentScrollView.startTimer();

				break;
			case "Finish Calibration":
				clearMainFrame();
				buildTestCalibrationUI();
				break;
			case "Successful Calibration":
				clearMainFrame();
				buildSelectionUI();
				break;
			case "Calibration Failed":
				clearMainFrame();
				buildCalibrationUI();
				break;
				

						
		}
				
	}
	

//	UI Layout functions
	private void buildMainUI(){
		this.setSize(400,200);

		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose(); 
				System.exit(0);
			}   
		});

		this.setVisible(true);

	}
	
	private void buildPopupUI(){
		popupFrame.setSize(400,200);
		popupFrame.setLayout(new GridLayout(2, 1));
		
		popupLabel = new Label();
		popupLabel.setAlignment(Label.CENTER);
		popupLabel.setText("Change State of");
		
		controlPanel_popup = new Panel();
		controlPanel_popup.setLayout(new FlowLayout());
		
		confirmButton = new Button("Confirm");
		cancelButton = new Button("Cancel");
		
		confirmButton.addActionListener(this);
		cancelButton.addActionListener(this);

		controlPanel_popup.add(confirmButton);
		controlPanel_popup.add(cancelButton);
		
		popupFrame.add(popupLabel);
		popupFrame.add(controlPanel_popup);


	}
	
	private void buildSelectionUI(){
		this.setLayout(new GridLayout(2, 1));
		currentScrollView = new ScrollView(objs);
				
		controlPanel_main = new Panel();
		controlPanel_main.setLayout(new FlowLayout());

		left_arrow = new Button("<");
		right_arrow = new Button(">");
		selectApplicationButton = new Button("Select");

		left_arrow.addActionListener(this);
		right_arrow.addActionListener(this);
		selectApplicationButton.addActionListener(this);
		controlPanel_main.add(left_arrow);
		controlPanel_main.add(selectApplicationButton);
		controlPanel_main.add(right_arrow); 

		setSize(400,200);
		setVisible(true);

		this.add(currentScrollView);		
		this.add(controlPanel_main);
		
		currentComps.add(headerLabel);
		currentComps.add(currentScrollView);
		this.repaint();
		this.validate();
//
//		Timed();
	}

	
	
	
	
	
	
	private void buildCalibrationUI(){
		

		System.out.println("Building calibration...");
		this.setLayout(new GridLayout(3, 1));

		calibrationLabel = new Label();
		calibrationLabel.setAlignment(Label.CENTER);
		calibrationLabel.setText("Calibration");
		
		Label instructionLabel = new Label();
		instructionLabel.setAlignment(Label.CENTER);
		instructionLabel.setText("Please think a strong 'accept' thought to continue.");
		
		calibrationPanel = new Panel();
		calibrationPanel.setLayout(new FlowLayout());
		
		calibrationContinueButton = new Button("Finish Calibration");		
		calibrationContinueButton.addActionListener(this);
		
		calibrationPanel.add(calibrationContinueButton);
		
		calibrationPanel.setVisible(true);
		
		this.add(calibrationLabel);
		this.add(instructionLabel);

		this.add(calibrationPanel);
		currentComps.add(calibrationLabel);
		currentComps.add(instructionLabel);
		currentComps.add(calibrationPanel);
		
		this.repaint();
		this.validate();

	}
	
	public void buildTestCalibrationUI(){
		this.setLayout(new GridLayout(2, 1));

		calibrationLabel = new Label();
		calibrationLabel.setAlignment(Label.CENTER);
		calibrationLabel.setText("Testing Calibration...");
		
		calibrationPanel = new Panel();
		calibrationPanel.setLayout(new FlowLayout());
		
		calibrationCompleteButton = new Button("Successful Calibration");		
		calibrationCompleteButton.addActionListener(this);
		
		calibrationFailedButton = new Button("Calibration Failed");		
		calibrationFailedButton.addActionListener(this);
		
		calibrationPanel.add(calibrationCompleteButton);
		calibrationPanel.add(calibrationFailedButton);
		
		calibrationPanel.setVisible(true);
		
		this.add(calibrationLabel);
		this.add(calibrationPanel);
		currentComps.add(calibrationLabel);
		currentComps.add(calibrationPanel);
		
		this.repaint();
		this.validate();
	}
	
	public void clearMainFrame(){
		for(int i = 0; i < currentComps.size(); i++){
			this.remove(currentComps.get(i));
		}
		
		currentComps.clear();		
		this.repaint();
		this.validate();

	}
	
	
}
