package edu.drexel.cci.hiyh.has.driver.insteon;

import java.io.IOException;

public class IRDriver extends AbstractX10Driver {
	
	private Command[] commands;
	public IRDriver(PLM plm) {
		super(plm);
		commands = new Command[0];
	}
	
	public IRDriver(PLM plm, Command[] commands) {
		super(plm);
		this.commands = commands;
	}
	
	public Command[] getCommands() {
		return commands;
	}
	
	public void sendCommand(Command c) {
		try {
			x10Command(c.getHouse(), c.getId());
		} catch (IOException e) {
			// TODO ??
		}
	}
	
	public void sendCommand(int index) {
		try {
			x10Command(commands[index].getHouse(), commands[index].getId());
		} catch (IOException e) {
			// TODO ??
		}
	}
	
}
