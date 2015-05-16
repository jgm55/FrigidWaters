package edu.drexel.cci.hiyh.has;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jssc.SerialPortList;
import edu.drexel.cci.hiyh.has.device.ComputerDevice;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.device.DummyDevice;
import edu.drexel.cci.hiyh.has.device.insteon.DimmerLight;
import edu.drexel.cci.hiyh.has.driver.insteon.ComputerDriver;
import edu.drexel.cci.hiyh.has.driver.insteon.PLM;
import edu.drexel.cci.hiyh.has.driver.insteon.Dimmer;

public class DeviceManager {
    private final List<Device> devices = new ArrayList<Device>();

    public DeviceManager() {
    	
        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length > 0) {
            PLM plm = new PLM(portNames[0]);
            try {
                plm.open();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            ConfigReader cf = new ConfigReader(plm,"Config");
            try {
				for (Device d :cf.read()) {
					devices.add(d);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Could not process config. Adding two Dummy Devices");
				devices.add(new DummyDevice("Dummy Device 1"));
	            devices.add(new DummyDevice("Dummy Device 2"));
			}
        } else {
            System.err.println("PLM not found. Adding Dummy Device and Keyboard.");
            devices.add(new DummyDevice("Dummy Device"));
            devices.add(new ComputerDevice("Keyboard", new ComputerDriver()));
        }
        
    }

    public List<Device> getDevices() {
        return Collections.unmodifiableList(devices);
    }
}
