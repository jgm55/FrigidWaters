package edu.drexel.cci.hiyh.ui;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import edu.drexel.cci.hiyh.has.DeviceManager;
import edu.drexel.cci.hiyh.has.device.Device;

/** Console UI demo. */
public class DemoUI {
    private final Scanner reader = new Scanner(System.in);
    private final DeviceManager dm;

    public DemoUI(DeviceManager dm) {
        this.dm = dm;
    }

    public <T> T menu(List<T> options) {
        System.out.println("Select one:");
        int i = 0;
        for (T o : options)
            System.out.printf("%d. %s\n", i++, o);
        // XXX THIS IS JUST A DEMO, SO I DON'T CARE
        return options.get(reader.nextInt());
    }

    public byte getByte() {
        System.out.println("Enter a byte: ");
        return reader.nextByte();
    }

    public Object get(Class<?> c) {
        if (c == byte.class)
            return getByte();
        return null;
    }

    public void run() {
        Device d = menu(dm.getDevices());
        Device.Action a = menu(d.getActions());
        a.invoke(Arrays.stream(a.getParameterTypes())
                 .map(this::get).toArray());
    }

    public static void main(String[] args) {
        new DemoUI(new DeviceManager()).run();
    }

}
