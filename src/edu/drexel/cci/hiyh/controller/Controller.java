package edu.drexel.cci.hiyh.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import edu.drexel.cci.hiyh.bci.SignalDetector;
import edu.drexel.cci.hiyh.has.DeviceManager;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.device.Action;
import edu.drexel.cci.hiyh.ui.BooleanInputSource;
import edu.drexel.cci.hiyh.ui.ConsoleUI;
import edu.drexel.cci.hiyh.ui.Displayable;
import edu.drexel.cci.hiyh.ui.InputUI;
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

    private static UserInput<Action> handleDeviceOrAction(Displayable d) {
        // XXX ugh
        if (d instanceof Device)
            return UserInput.fromList(((Device)d).getAvailableActions());
        else
            return UserInput.of((Action)d);
    }

    private static UserInput<Pair<Action, List<Object>>> chooseDeviceAction(List<Displayable> devices) {
        // choose a device (or maybe a cached action)
        return UserInput.fromList(devices)
                 // given the device, choose an action
                 // given an action, just use that action
                 .flatMap(Controller::handleDeviceOrAction)
                 // choose parameter values
                 .flatMap(a -> UserInput.ofParamTypes(a.getParameterTypes())
                                // tack on the Action--we'll need that too
                                        .map(params -> new Pair<Action, List<Object>>(a, params)));
    }

    public void loop() {
        try {
            ArrayList<Action> cache = new ArrayList<Action>();
            while (true) {
                ui.await("Activate to begin");
                ArrayList<Displayable> c = new ArrayList<Displayable>(cache);
                c.addAll(dm.getDevices());
                Optional<Pair<Action, List<Object>>> p = chooseDeviceAction(c).get(ui);
                if (p.isPresent()) {
                    cache.remove(p.get().first);
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
