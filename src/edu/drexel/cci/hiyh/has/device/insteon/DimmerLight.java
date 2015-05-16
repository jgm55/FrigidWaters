package edu.drexel.cci.hiyh.has.device.insteon;

import java.awt.Image;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import edu.drexel.cci.hiyh.has.device.Action;
import edu.drexel.cci.hiyh.has.device.Device;
import edu.drexel.cci.hiyh.has.device.ParamType;
import edu.drexel.cci.hiyh.has.device.BoundedInt;
import edu.drexel.cci.hiyh.has.driver.insteon.Dimmer;
import edu.drexel.cci.hiyh.util.ImageLoader;

public class DimmerLight extends Device {
    private final Dimmer driver;

    private final Action turnOn = new Action("Turn On") {
        @Override
        public void invoke(List<Object> args) {
            try {
                driver.setIntensity((byte)0xff, true);
            } catch (IOException e) {
                // TODO ???
            }
        }
        @Override
        public Optional<Image> getDisplayImage() {
            return ImageLoader.load("/res/onState.png");
        }
    };

    private final Action turnOff = new Action("Turn Off") {
        @Override
        public void invoke(List<Object> args) {
            try {
                driver.setIntensity((byte)0x00, true);
            } catch (IOException e) {
                // TODO ???
            }
        }
        @Override
        public Optional<Image> getDisplayImage() {
            return ImageLoader.load("/res/offState.png");
        }
    };

    private static final List<ParamType<?>> setLevelParams =
        Collections.unmodifiableList(Arrays.asList(new ParamType<?>[] {new BoundedInt(0, 255)}));
    private final Action setLevel = new Action("Set Light Level") {
        @Override
        public List<ParamType<?>> getParameterTypes() {
            return setLevelParams;
        }
        @Override
        public void invoke(List<Object> args) {
            try {
                driver.setIntensity((byte)(int)args.get(0), true);
            } catch (IOException e) {
                // TODO ???
            }
        }
    };

    public DimmerLight(String name, Dimmer driver) {
        super(name);
        this.driver = driver;
        addAction(turnOn);
        addAction(turnOff);
        addAction(setLevel);
    }

    @Override
    public Optional<Image> getDisplayImage() {
        return ImageLoader.load("/res/lightbulb.png");
    }
}
