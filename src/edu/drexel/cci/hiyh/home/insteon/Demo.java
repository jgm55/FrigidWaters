package edu.drexel.cci.hiyh.home.insteon;

import java.io.IOException;

import jssc.SerialPortList;

/**
 * Quick, brittle demo of the basics, using a LightDevice. Turns the light on
 * and then off.
 */
public class Demo {
    public static void main(String args[]) throws IOException {
        // Assume that it's the first available serial port.
        PLM plm = new PLM(SerialPortList.getPortNames()[0]);
        plm.open();
        // And that our light device has this ID.
        LightDevice dev = new LightDevice(plm, new byte[] {0x26, (byte)0x98, (byte)0x87});

        dev.on();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {}
        dev.off();

        plm.close();
    }
}
