package edu.drexel.cci.hiyh.ui;

import edu.drexel.cci.hiyh.home.insteon.LightDevice;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;


public class MainUI extends Frame implements ActionListener{
	Button left_arrow, right_arrow, select, confirm, cancel;
	private Label headerLabel;
	private Label popupLabel;

	private Panel controlPanel_main;
	private Panel controlPanel_popup;

	private Frame mainFrame;
	private Frame popupFrame;
	
	// TODO something less abominable
	private int i = 0;
	private	ArrayList<String> objs = new ArrayList<String>(){{
		add("Lights");
		add("Television");
		add("Heat");		
	}};
	private LightDevice ldev;
	
	public MainUI(LightDevice ldev){
	    this.ldev = ldev;

		mainFrame = new Frame("");
		popupFrame = new Frame("");
		
		BuildMainUI();
		BuildPopupUI();
		
		mainFrame.setVisible(true); 
	}
	
	private void BuildMainUI(){
		mainFrame.setSize(400,200);
		mainFrame.setLayout(new GridLayout(2, 1));

		mainFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose(); 
				System.exit(0);
			}   
		});

		headerLabel = new Label();
		headerLabel.setAlignment(Label.CENTER);
		headerLabel.setText(objs.get(i));
		
		controlPanel_main = new Panel();
		controlPanel_main.setLayout(new FlowLayout());

		left_arrow = new Button("<");
		right_arrow = new Button(">");
		select = new Button("Select");
		
		left_arrow.addActionListener(this);
		right_arrow.addActionListener(this);
		select.addActionListener(this);
		controlPanel_main.add(left_arrow);
		controlPanel_main.add(select);
		controlPanel_main.add(right_arrow); 

		setSize(400,200);
		setVisible(true);
		
		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel_main);
	}
	
	private void BuildPopupUI(){
		popupFrame.setSize(400,200);
		popupFrame.setLayout(new GridLayout(2, 1));

//		popupFrame.addWindowListener(new WindowAdapter(){
//			public void windowClosing(WindowEvent e){
//				dispose(); 
//			}   
//		});

		popupLabel = new Label();
		popupLabel.setAlignment(Label.CENTER);
		popupLabel.setText("Change State of");
		
		controlPanel_popup = new Panel();
		controlPanel_popup.setLayout(new FlowLayout());
		
		confirm = new Button("Confirm");
		cancel = new Button("Cancel");
		
		confirm.addActionListener(this);
		cancel.addActionListener(this);

		controlPanel_popup.add(confirm);
		controlPanel_popup.add(cancel);
		
		popupFrame.add(popupLabel);
		popupFrame.add(controlPanel_popup);


	}
	
	public void actionPerformed(ActionEvent e){
		String str = e.getActionCommand();
		if(str.equals(">")){
			i++;
			if(i > objs.size() -1){
				i = 0;
			}
			headerLabel.setText(objs.get(i));
		}
		else if(str.equals("<")){
			i--;
			if(i < 0){
				i = objs.size() -1;
			}
			headerLabel.setText(objs.get(i));
		}
		else if(str.equals("Select")){
			popupLabel.setText("Change state of " + objs.get(i));
			popupFrame.setVisible(true);
		}else if(str.equals("Confirm")){
			// TODO something more fitting than looking at the header label
			switch(headerLabel.getText()) {
			    case "Lights":
			        try {
			            ldev.toggle();
			        } catch (IOException exc) {
			            // XXX Dumb simple way for prototype. Do NOT do this
			            // in final version!
			            exc.printStackTrace();
			            System.exit(1);
			        }
			        break;
			}
			popupFrame.setVisible(false);
		}else if(str.equals("Cancel")){
			popupFrame.setVisible(false);
		}
		
	}
	
	
	
}
