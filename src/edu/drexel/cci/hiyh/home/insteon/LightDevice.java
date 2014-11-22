package edu.drexel.cci.hiyh.home.insteon;

import java.io.IOException;

public class LightDevice extends Device {
    
    public LightDevice(PLM plm, byte[] address) {
        super(plm, address);
    }

    public void setIntensity(byte value, boolean fade) throws IOException {
        command((byte)(fade ? 0x11 : 0x13), value);
    }

    public void setIntensity(byte value) throws IOException {
        setIntensity(value, true);
    }

    public void on() throws IOException {
        setIntensity((byte)0xFF);
    }

    public void off() throws IOException {
        command((byte)0x13, (byte)0);
    }
}
