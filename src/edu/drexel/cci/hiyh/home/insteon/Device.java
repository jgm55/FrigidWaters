package edu.drexel.cci.hiyh.home.insteon;

import java.io.IOException;

/**
 * Manages an Insteon home automation device.
 */
public class Device {

    private PLM plm;

    /**
     * 3-byte permanent address of the device.
     */
    private final byte[] address;

    public Device(PLM plm, byte[] address) {
        this.plm = plm;
        this.address = address;
    }

    protected void command(byte cmd1, byte cmd2) throws IOException {
        plm.directMessage(Util.concat(address, new byte[] {0x0F, cmd1, cmd2}));
    }
}
