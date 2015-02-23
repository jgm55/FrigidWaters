package edu.drexel.cci.hiyh.has;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.device.DummyDevice;

public class DeviceManager {
    private final List<Device> devices = new ArrayList<Device>();

    public DeviceManager() {
        devices.add(new DummyDevice("Dummy Device"));
    }

    public List<Device> getDevices() {
        return Collections.unmodifiableList(devices);
    }
}
