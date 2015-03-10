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

public class DeviceManager {
    private final List<Device> devices = new ArrayList<Device>();

    public DeviceManager() {
        devices.add(new DummyDevice("Dummy Device 1"));

        PLM plm = new PLM(SerialPortList.getPortNames()[0]);
        try {
            plm.open();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Dimmer driver = new Dimmer(plm, new byte[] {0x26, (byte)0x98, (byte)0x87});
        devices.add(new DimmerLight("Light 0", driver));
    }

    public List<Device> getDevices() {
        return Collections.unmodifiableList(devices);
    }
}
