package edu.drexel.cci.hiyh.has;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.drexel.cci.hiyh.has.device.ComputerDevice;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.device.DummyDevice;
import edu.drexel.cci.hiyh.has.device.insteon.DimmerLight;
import edu.drexel.cci.hiyh.has.device.insteon.IR;
import edu.drexel.cci.hiyh.has.driver.insteon.Command;
import edu.drexel.cci.hiyh.has.driver.insteon.ComputerDriver;
import edu.drexel.cci.hiyh.has.driver.insteon.Dimmer;
import edu.drexel.cci.hiyh.has.driver.insteon.IRDriver;
import edu.drexel.cci.hiyh.has.driver.insteon.PLM;

public class ConfigReader {
	
	String addr;
	Pattern p;
	Pattern a;
	PLM plm;
	public ConfigReader(PLM plm, String address) {
		addr=address;
		p=Pattern.compile("\t");
		a=Pattern.compile("\\.");
		this.plm=plm;
	}
	
	
	public List<Device> read() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(addr));
		List<Device> devices = new ArrayList<Device>();
		for (String line : lines) {
			String[] parts = p.split(line);
			if((line.startsWith("Dummy") || line.startsWith("dummy")) && parts.length>=2) {
				devices.add(new DummyDevice(parts[1]));
			}
			else if((line.startsWith("Computer") || line.startsWith("computer")) && parts.length>=2) {
				devices.add(new ComputerDevice(parts[1], new ComputerDriver()));
			}
			else if((line.startsWith("Light") || line.startsWith("light")) && parts.length>=3) {
				String[] address = a.split(parts[2]);
				byte b1 = (byte) Integer.parseInt(address[0], 16);
				byte b2 = (byte) Integer.parseInt(address[1], 16);
				byte b3 = (byte) Integer.parseInt(address[2], 16);
				Dimmer driver = new Dimmer(plm, new byte[] {b1,b2,b3});
				devices.add(new DimmerLight(parts[1], driver));
			}
			else if((line.startsWith("IR") || line.startsWith("ir")) && parts.length>=2 && parts.length%2==0) {
				Command[] commands = new Command[parts.length/2 -1];
				int index = 0;
				for(int i=2;i+1<parts.length;i+=2) {
					Command c = new Command(parts[i],parts[i+1].charAt(0), Integer.parseInt(parts[i+1].substring(1)));
					commands[index]=c;
					index++;
				}
				IRDriver driver = new IRDriver(plm, commands);
				devices.add(new IR(parts[1], driver));
			}
			else{
				System.out.println(String.format("Could not process \"%s\"\n",line));
			}
		}
		return devices;
	}
}
