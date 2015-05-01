package edu.drexel.cci.hiyh.has.driver.insteon;

import java.io.IOException;

/**
 * Manages an Insteon home automation device.
 */
public abstract class AbstractDriver {

    private PLM plm;

    /**
     * 3-byte permanent address of the device.
     */
    private final byte[] address;

    public AbstractDriver(PLM plm, byte[] address) {
        this.plm = plm;
        this.address = address;
    }
    protected void command(byte cmd1, byte cmd2, byte[] data) throws IOException {
    	byte[] a = new byte[data.length+3];
    	a[0]=(byte)0x0F;
    	a[1]=cmd1;
    	a[2]=cmd2;
    	for (int i=0;i<data.length;i++) {
    		a[i+3]=data[i];
    	}
        plm.directMessage(Util.concat(address, a));
    }
    
    protected void command(byte cmd1, byte cmd2) throws IOException {
        plm.directMessage(Util.concat(address, new byte[] {0x0F, cmd1, cmd2}));
    }
    
    protected byte[] readCommand(byte cmd1, byte cmd2) throws IOException {
        return plm.writeRead(Util.concat(address, new byte[] {0x0F, cmd1, cmd2}));
    }
    
    protected byte[] readCommand(byte cmd1, byte cmd2, byte[] data) throws IOException {
    	byte[] a = new byte[data.length+3];
    	a[0]=(byte)0x0F;
    	a[1]=cmd1;
    	a[2]=cmd2;
    	for (int i=0;i<data.length;i++) {
    		a[i+3]=data[i];
    	}
        return plm.writeRead(Util.concat(address, a));
    }
}
