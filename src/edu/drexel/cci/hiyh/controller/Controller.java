package edu.drexel.cci.hiyh.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import edu.drexel.cci.hiyh.bci.SignalDetector;
import edu.drexel.cci.hiyh.has.DeviceManager;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.device.Action;
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
    private final ArrayList<Device> cache;

    public Controller(DeviceManager dm, InputUI ui) {
        this.dm = dm;
        this.ui = ui;
        cache = new ArrayList<Device>();
    }

    private static UserInput<Pair<Action, List<Object>>> chooseDeviceAction(List<Device> devices) {
        // choose a device
        return UserInput.fromList(devices)
                 // given the device, choose an action
                 .flatMap(d -> UserInput.fromList(d.getAvailableActions()))
                 // choose parameter values
                 .flatMap(a -> UserInput.ofParamTypes(a.getParameterTypes())
                                // tack on the Action--we'll need that too
                                        .map(params -> new Pair<Action, List<Object>>(a, params)));
    }

    public void loop() {
        try {
            while (true) {
                ui.await("Activate to begin");
                ArrayList<Device> c = new ArrayList<Device>(cache);
                c.addAll(dm.getDevices());
                Optional<Pair<Action, List<Object>>> p = chooseDeviceAction(c).get(ui);
                if (p.isPresent()) {
                	if (cache.contains(p.get().first)) {
                		cache.remove(p.get().first);
                	}
                	cache.add(0, p.get().first);
                	if (cache.size() > 2) {
                		cache.remove(cache.size()-1);
                	}
                	p.get().first.invoke(p.get().second);
                }
            }
        } catch (InterruptedException e) {
            // Closed by interrupt
        }
    }

    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        InputUI ui;

        if (argsList.contains("-cli")) {
            ui = new ConsoleUI();
        } else {
            BooleanInputSource insrc;
            if (argsList.contains("-nobci"))
                insrc = new MouseInputSource();
            else
                insrc = new MultiInputSource(
                            new SignalDetector(),
                            new MouseInputSource()
                        );
            ui = new ScrollUI(insrc);
        }

        Controller mainController = new Controller(
                new DeviceManager(),
                ui
        );

        mainController.loop();
    }

}
