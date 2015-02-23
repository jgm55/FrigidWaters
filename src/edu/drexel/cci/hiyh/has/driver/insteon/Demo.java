package edu.drexel.cci.hiyh.has.driver.insteon;

import java.io.IOException;

import jssc.SerialPortList;

/**
 * Quick, brittle demo of the basics, using a LightDevice. Turns the light on
 * and then off.
 */
public class Demo {
    private static final PLM plm;

    static {
        // Assume that it's the first available serial port.
        plm = new PLM(SerialPortList.getPortNames()[0]);
    }

    public static void main(String args[]) throws IOException {
        open();
        Dimmer dev = getDimmer();

        dev.turnOn();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {}
        dev.turnOff();

        plm.close();
    }

    public static void open() throws IOException {
        plm.open();
    }

    public static Dimmer getDimmer() {
        // Assume that our light device has this ID.
        return new Dimmer(plm, new byte[] {0x26, (byte)0x98, (byte)0x87});
    }
}
