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

public class Controller {
    private final DeviceManager dm;
    private final InputUI ui;

    // TODO move to utility
    private static final class Pair<A,B> {
        public final A first;
        public final B second;
        public Pair(A a, B b) { first = a; second = b; }
    }

    public Controller(DeviceManager dm, InputUI ui) {
        this.dm = dm;
        this.ui = ui;
    }

    private static UserInput<Pair<Device.Action, Object[]>> chooseDeviceAction(List<Device> devices) {
        // choose a device
        return UserInput.fromList(devices)
                 // given the device, choose an action
                 .flatMap(d -> UserInput.fromList(d.getActions()))
                 // choose parameter values
                 .flatMap(a -> UserInput.ofClasses(a.getParameterTypes())
                                // tack on the Action--we'll need that too
                                        .map(params -> new Pair<Device.Action, Object[]>(a, params)));
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
        BooleanInputSource insrc;
        if (args.length > 0 && args[0].equals("-nobci"))
            insrc = new MouseInputSource();
        else
            insrc = new MultiInputSource(
                        new BCIInputSource(),
                        new MouseInputSource()
                    );

        Controller mainController = new Controller(
                new DeviceManager(),
                new ScrollUI(insrc)
        );

        mainController.loop();
    }

}
