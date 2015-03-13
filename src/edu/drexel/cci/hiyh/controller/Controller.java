package edu.drexel.cci.hiyh.controller;

import java.util.ArrayList;
import java.util.List;

import edu.drexel.cci.hiyh.bci.BCICollector;
import edu.drexel.cci.hiyh.bci.BCIInputSource;
import edu.drexel.cci.hiyh.bci.SignalDetector;
import edu.drexel.cci.hiyh.has.DeviceManager;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.ui.CalibrationUI;
//import edu.drexel.cci.hiyh.ui.ConsoleUI;
import edu.drexel.cci.hiyh.ui.InputUI;
import edu.drexel.cci.hiyh.ui.MouseInputSource;
import edu.drexel.cci.hiyh.ui.ScrollUI;

public class Controller {
    private final DeviceManager dm;
    private final InputUI ui;
    
    private final List<String> calibration = new ArrayList<String>();
    	
    public Controller(DeviceManager dm, InputUI ui) {
        this.dm = dm;
        this.ui = ui;
    	calibration.add("Calibrating Insteon Device");
    }

    public void actionMenu() {
    	new SelectMenuNode<Device>(ui, dm.getDevices()) {
    		public void success(Device d) {
    			new SelectMenuNode<Device.Action>(this, d.getActions()) {
    				public void success(Device.Action a) {
    					MenuUtils.getMenuNodeChain(ui, this, a::invoke, a.getParameterTypes());
    				}
    			}.run();
    		}
    	}.run();
        
    }
    
    public boolean calibrate(){
    	//Build UI 
    	CalibrationUI calUI = new CalibrationUI();
    	BCICollector bci = new BCICollector();
    	SignalDetector detector = bci.getSignalDetector();

    	try {
    		boolean calibrated = detector.getCalibratedWhenTrue();
    		calUI.dismissUI();
    		return calibrated;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    		calUI.dismissUI();
			return false;
		}
    }

    public static void main(String[] args) {
    	Controller mainController = new Controller(new DeviceManager(), new ScrollUI(new BCIInputSource()));
    	if(mainController.calibrate()){
        	mainController.actionMenu();    	
    	}
    }

}
