package edu.drexel.cci.hiyh.home.insteon;

import java.io.IOException;

public class LightDevice extends Device {

    // TODO properly implement querying, or better yet, listening
    private boolean on = false;
    
    public LightDevice(PLM plm, byte[] address) {
        super(plm, address);
    }

    public void setIntensity(byte value, boolean fade) throws IOException {
        command((byte)(fade ? 0x11 : 0x13), value);
        // Java doesn't have unsigned, but value "is". Dumb.
        on = value != 0;
    }

    public void setIntensity(byte value) throws IOException {
        setIntensity(value, true);
    }

    public void turnOn() throws IOException {
        setIntensity((byte)0xFF);
    }

    public void turnOff() throws IOException {
        command((byte)0x13, (byte)0);
        on = false;
    }

    public void toggle() throws IOException {
        if (on)
            turnOff();
        else
            turnOn();
    }
}
