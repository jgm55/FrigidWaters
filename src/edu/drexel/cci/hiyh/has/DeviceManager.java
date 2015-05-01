package edu.drexel.cci.hiyh.has;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jssc.SerialPortList;

import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.device.DummyDevice;
import edu.drexel.cci.hiyh.has.device.insteon.DimmerLight;
import edu.drexel.cci.hiyh.has.driver.insteon.PLM;
import edu.drexel.cci.hiyh.has.driver.insteon.Dimmer;
//11D3C0
public class DeviceManager {
    private final List<Device> devices = new ArrayList<Device>();

    public DeviceManager() {
        devices.add(new DummyDevice("Dummy Device 1"));

        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length > 0) {
            PLM plm = new PLM(portNames[0]);
            try {
                plm.open();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            Dimmer driver = new Dimmer(plm, new byte[] {0x11, (byte)0xD3, (byte)0xC0});//{0x26, (byte)0x98, (byte)0x87});
            devices.add(new DimmerLight("Light", driver));
        } else {
            System.err.println("PLM not found. Adding another Dummy Device.");
            devices.add(new DummyDevice("Dummy Device 2"));
        }
    }

    public List<Device> getDevices() {
        return Collections.unmodifiableList(devices);
    }
}
