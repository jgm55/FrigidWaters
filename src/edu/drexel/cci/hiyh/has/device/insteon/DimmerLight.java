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
        driver.setIntensity((byte)0xff, true);
    }

    @ActionMethod(name="Turn Off")
    public void turnOff() throws IOException {
        driver.setIntensity((byte)0x00, true);
    }

    //@ActionMethod(name="Set Light Level")
    public void setLevel(Byte level) throws IOException {
        driver.setIntensity(level, true);
    }
}
