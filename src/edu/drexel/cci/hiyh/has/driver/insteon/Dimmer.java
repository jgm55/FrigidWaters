package edu.drexel.cci.hiyh.has.driver.insteon;

import java.io.IOException;

public class Dimmer extends AbstractDriver {

    public Dimmer(PLM plm, byte[] address) {
        super(plm, address);
    }

    public void setIntensity(byte value, boolean fade) throws IOException {
        command((byte)(fade ? 0x11 : 0x13), value);
        // Java doesn't have unsigned, but value "is". Dumb.
        //on = value != 0;
    }
    
    public boolean getValue() throws IOException {
    	byte[] a = readCommand((byte)0x19, (byte)0x00);
    	int i =0;
    	for (byte b : a) {
    		System.out.print(0xff & b);
    		System.out.print(" ");
    		if (i%10==0) {
    			System.out.println();
    		}
    		i++;
    	}
    	System.out.println();
    	System.out.println(a[a.length-1]);
    	return a[a.length-1]==(byte)-1;
    }
    
    public byte[] getDesc() throws IOException {
    	byte[] a = readCommand((byte) 0x03, (byte) 0x00);
    	return a;
    }
    
    /*public void setDesc() throws IOException {
    	command((byte) 0x03, (byte) 0x02, new byte{});
    }*/

    /*
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
    */
}
