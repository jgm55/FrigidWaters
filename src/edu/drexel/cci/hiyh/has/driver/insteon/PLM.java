package edu.drexel.cci.hiyh.has.driver.insteon;

import java.io.IOException;

import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Communicates with a PowerLinc Modem.
 */
public class PLM {

    private SerialPort port;

    public PLM(String portName) {
        port = new SerialPort(portName);
    }

    public void open() throws IOException {
        try {
            port.openPort();
            port.setParams(SerialPort.BAUDRATE_19200,
                           SerialPort.DATABITS_8,
                           SerialPort.STOPBITS_1,
                           SerialPort.PARITY_NONE);
        } catch (SerialPortException ex) {
            throw new IOException(ex);
        }
    }

    public void close() throws IOException {
        try {
            port.closePort();
        } catch (SerialPortException ex) {
            throw new IOException(ex);
        }
    }

    private void write(byte[] msg) throws IOException {
        try {
            port.writeBytes(msg);
        } catch (SerialPortException ex) {
            throw new IOException(ex);
        }
    }
    
    private byte[] read() throws IOException {
        try {
        	byte[] a = port.readBytes(10);
        	byte[] b = port.readBytes();
        	if (b==null) {
        		b=new byte[0];
        	}
        	byte[] c = new byte[b.length+1];
        	c[0]=a[0];
        	for (int i=0;i<b.length;i++) {
        		c[i+1]=b[i];
        	}
        	return c;
        } catch (SerialPortException ex) {
            throw new IOException(ex);
        }
    }

    public void directMessage(byte[] msg) throws IOException {
        final byte[] hdr = new byte[] { 0x02, 0x62 };
        write(Util.concat(hdr, msg));
    }
    
    public byte[] writeRead(byte[] msg) throws IOException {
        final byte[] hdr = new byte[] { 0x02, 0x62 };
        write(Util.concat(hdr, msg));
        return read();
    }

    // TODO ???
    // public List<Device> discoverDevices()

}
