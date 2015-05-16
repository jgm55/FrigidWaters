package edu.drexel.cci.hiyh.has.device.insteon;

import java.util.List;

import edu.drexel.cci.hiyh.has.device.Action;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.driver.insteon.Command;
import edu.drexel.cci.hiyh.has.driver.insteon.IRDriver;

public class IR extends Device {
	private final IRDriver driver;

    public IR(String name, IRDriver driver) {
        super(name);
        this.driver = driver;
        for (Command c : driver.getCommands()) {
        	Action a = new Action(c.getName()) {
        		@Override
        		public void invoke(List<Object> args) {
        			driver.sendCommand(c);
        		}
        	};
        	addAction(a);
        }
    }   

}
