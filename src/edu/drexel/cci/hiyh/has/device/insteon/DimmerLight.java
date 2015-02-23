package edu.drexel.cci.hiyh.has.device.insteon;

import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.driver.insteon.Dimmer;

import java.io.IOException;

public class DimmerLight implements Device {
    private final Dimmer driver;

    public DimmerLight(final Dimmer driver) {
        this.driver = driver;
    }

    @Action
    public void turnOn() throws IOException {
        driver.setIntensity((byte)0xff, true);
    }

    @Action
    public void turnOff() throws IOException {
        driver.setIntensity((byte)0x00, true);
    }

    @Action
    public void setLevel(byte level) throws IOException {
        driver.setIntensity(level, true);
    }
}
