package edu.drexel.cci.hiyh.has.driver.insteon;

import java.io.IOException;

/**
 * Manages an Insteon home automation devices that run on X10.
 */
public abstract class AbstractX10Driver {

    private PLM plm;


    public AbstractX10Driver(PLM plm) {
        this.plm = plm;
    }
    
    protected void x10Command(char house, int id) throws IOException {
    	byte addr = Util.toX10(house, id);
        plm.x10Message(new byte[] {addr, 0x00});
    }
}
