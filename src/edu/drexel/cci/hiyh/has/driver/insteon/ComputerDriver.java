package edu.drexel.cci.hiyh.has.driver.insteon;

import java.awt.AWTException;
import java.awt.Robot;

public class ComputerDriver {

    private Robot r;
    
    public ComputerDriver() {
    	try {
			r = new Robot();
			r.setAutoDelay(40);
		} catch (AWTException e) {
			System.err.println("Problem instantiating robot");
		}
    }
    
    public void keyboardCommand(int c) {
    	r.keyPress(c);
    	r.keyRelease(c);
    }
    
    public void mouseCommand(int b) {
    	r.mousePress(b);
    	r.mouseRelease(b);
    }
    
    public void mouseMove(int x, int y) {
    	r.mouseMove(x, y);
    }
    
    public void  mouseWheel(int amt) {
    	r.mouseWheel(amt);
    }
}
