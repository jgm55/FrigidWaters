package edu.drexel.cci.hiyh.controller;

import java.util.List;

import edu.drexel.cci.hiyh.bci.BCIInputSource;
import edu.drexel.cci.hiyh.has.DeviceManager;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.ui.ConsoleUI;
import edu.drexel.cci.hiyh.ui.InputUI;
import edu.drexel.cci.hiyh.ui.BooleanInputSource;
import edu.drexel.cci.hiyh.ui.MouseInputSource;
import edu.drexel.cci.hiyh.ui.MultiInputSource;
import edu.drexel.cci.hiyh.ui.ScrollUI;
import edu.drexel.cci.hiyh.util.Pair;

public class Controller {
    private final DeviceManager dm;
    private final InputUI ui;

    public Controller(DeviceManager dm, InputUI ui) {
        this.dm = dm;
        this.ui = ui;
    }

    private static UserInput<Pair<Device.Action, List<Object>>> chooseDeviceAction(List<Device> devices) {
        // choose a device
        return UserInput.fromList(devices)
                 // given the device, choose an action
                 .flatMap(d -> UserInput.fromList(d.getAvailableActions()))
                 // choose parameter values
                 .flatMap(a -> UserInput.ofParamTypes(a.getParameterTypes())
                                // tack on the Action--we'll need that too
                                        .map(params -> new Pair<Device.Action, List<Object>>(a, params)));
    }

    public void loop() {
        try {
            while (true) {
                ui.await("Activate to begin");
                chooseDeviceAction(dm.getDevices()).get(ui).ifPresent(p -> p.first.invoke(p.second));
            }
        } catch (InterruptedException e) {
            // Closed by interrupt
        }
    }

    public static void main(String[] args) {
        /*
        BooleanInputSource insrc;
        if (args.length > 0 && args[0].equals("-nobci"))
            insrc = new MouseInputSource();
        else
            insrc = new MultiInputSource(
                        new BCIInputSource(),
                        new MouseInputSource()
                    );
        */

        Controller mainController = new Controller(
                new DeviceManager(),
                //new ScrollUI(insrc)
                new ConsoleUI()
        );

        mainController.loop();
    }

}
