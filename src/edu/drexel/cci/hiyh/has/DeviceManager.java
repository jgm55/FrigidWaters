package edu.drexel.cci.hiyh.has;

import java.util.Collections;
import java.util.List;

import edu.drexel.cci.hiyh.has.device.Device;

public class DeviceManager {
    private final List<Device> devices = new ArrayList<Device>();

    public List<Device> getDevices() {
        return Collections.unmodifiableList(devices);
    }
}
