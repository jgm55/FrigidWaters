package edu.drexel.cci.hiyh.has.device;

import java.awt.event.KeyEvent;
import java.util.List;

import edu.drexel.cci.hiyh.has.driver.insteon.ComputerDriver;

public class ComputerDevice extends Device {

	private final ComputerDriver cd;
	
	private final Action left = new Action("Left") {
        @Override
        public void invoke(List<Object> args) {
        	cd.keyboardCommand(KeyEvent.VK_LEFT);
        }
    };
    
    private final Action right = new Action("Right") {
        @Override
        public void invoke(List<Object> args) {
        	cd.keyboardCommand(KeyEvent.VK_RIGHT);
        }
    };
	
	public ComputerDevice(String name, ComputerDriver cd) {
		super(name);
		this.cd = cd;
		addAction(left);
		addAction(right);
	}

}
