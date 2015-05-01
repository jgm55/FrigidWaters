package edu.drexel.cci.hiyh.has.device.insteon;

import edu.drexel.cci.hiyh.has.device.ActionMethod;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.driver.insteon.Dimmer;

import java.io.IOException;

public class DimmerLight extends Device {
    private final Dimmer driver;

    public DimmerLight(String name, Dimmer driver) {
        super(name);
        this.driver = driver;
    }

    @ActionMethod(name="Turn On")
    public void turnOn() throws IOException {
    	for (byte i=(byte)0;i<=0xff;i++) {
    		driver.setIntensity((byte)0xff, true);
    	}
    }

    @ActionMethod(name="Turn Off")
    public void turnOff() throws IOException {
        driver.setIntensity((byte)0x00, true);
    }
    
    @ActionMethod(name="Get Value")
    public void getValue() throws IOException {
    	System.out.println(driver.getValue());
    }
    
    @ActionMethod(name="Get Description")
    public void getDesc() throws IOException {
    	for (byte b : driver.getDesc()) {
    		System.out.print(b&0xff);
    		System.out.print(" ");
    	}
    	System.out.println();
    }
    
    /*@ActionMethod(name="Set Description")
    public void getDesc() throws IOException {
    	driver.
    }*/

    //@ActionMethod(name="Set Light Level")
    public void setLevel(Byte level) throws IOException {
        driver.setIntensity(level, true);
    }
}
