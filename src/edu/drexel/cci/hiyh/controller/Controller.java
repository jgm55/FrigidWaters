package edu.drexel.cci.hiyh.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.drexel.cci.hiyh.bci.BCICollector;
import edu.drexel.cci.hiyh.bci.BCIInputSource;
import edu.drexel.cci.hiyh.bci.SignalDetector;
import edu.drexel.cci.hiyh.has.DeviceManager;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.ui.CalibrationUI;
//import edu.drexel.cci.hiyh.ui.ConsoleUI;
import edu.drexel.cci.hiyh.ui.InputUI;
import edu.drexel.cci.hiyh.ui.MouseInputSource;
import edu.drexel.cci.hiyh.ui.MultiInputSource;
import edu.drexel.cci.hiyh.ui.ScrollUI;

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
                        MenuUtils.getMenuNodeChain(ui, this,
                                ((Consumer<Object[]>)a::invoke).andThen(Controller.this::loop),
                                a.getParameterTypes());
                    }
                }.run();
            }
        }.run();
    }

    // signature to accept andThen... from actionMenu... ugh
    public void loop(Object... unused) {
        ui.await(this::actionMenu, "Activate to begin");
    }

    public static void main(String[] args) {
        Controller mainController = new Controller(
                new DeviceManager(),
                new ScrollUI(
                    new MultiInputSource(
                        new BCIInputSource(),
                        new MouseInputSource()
                    )
                )
        );

        mainController.loop();
    }

}
