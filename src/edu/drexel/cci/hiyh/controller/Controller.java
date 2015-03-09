package edu.drexel.cci.hiyh.controller;

import edu.drexel.cci.hiyh.has.DeviceManager;
import edu.drexel.cci.hiyh.has.device.Device;
//import edu.drexel.cci.hiyh.ui.ConsoleUI;
import edu.drexel.cci.hiyh.ui.ScrollUI;
import edu.drexel.cci.hiyh.ui.InputUI;

public class Controller {
    private final DeviceManager dm;
    private final InputUI ui;

    public Controller(DeviceManager dm, InputUI ui) {
        this.dm = dm;
        this.ui = ui;
    }

    public void actionMenu() {
        new SelectMenuNode<Device>(ui, dm.getDevices()) {
            public void success(Device d) {
                new SelectMenuNode<Device.Action>(this, d.getActions()) {
                    public void success(Device.Action a) {
                        MenuUtils.getMenuNodeChain(ui, this, a::invoke, a.getParameterTypes());
                    }
                }.run();
            }
        }.run();
    }

    public static void main(String[] args) {
        new Controller(new DeviceManager(), new ScrollUI()).actionMenu();
    }

}
