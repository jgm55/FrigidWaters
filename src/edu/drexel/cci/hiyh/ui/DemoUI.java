package edu.drexel.cci.hiyh.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.drexel.cci.hiyh.has.DeviceManager;
import edu.drexel.cci.hiyh.has.device.Device;

/** Console UI demo. */
public class DemoUI {
    private final Scanner reader = new Scanner(System.in);
    private final DeviceManager dm;

    public DemoUI(DeviceManager dm) {
        this.dm = dm;
    }

    /** Display a menu of objects to the user and have them select one.
     * @param options objects to display
     * @return the selected object
     */
    public <T> T menu(List<T> options) {
        System.out.println("Select one:");
        int i = 0;
        for (T o : options)
            System.out.printf("%d. %s\n", i++, o);
        // XXX THIS IS JUST A DEMO, SO I DON'T CARE
        return options.get(reader.nextInt());
    }

    /**
     * Prompts the user for a byte.
     * @return a byte entered by the user
     */
    public byte getByte() {
        System.out.println("Enter a byte: ");
        return reader.nextByte();
    }

    /**
     * Prompts for and returns an Object.
     * @param c the requested class
     * @return the entered Object
     */
    public <T> T get(Class<T> c) {
        if (c == byte.class)
            return c.cast(getByte());
        throw new IllegalArgumentException("Can't handle class: " + c);
    }

    /**
     * Prompts the user to select and perform a single Device.Action.
     */
    public void run() {
        // Choose a device
        Device d = menu(dm.getDevices());
        // Choose an action
        Device.Action a = menu(d.getActions());
        // 1. Get the array of parameter types
        // 2. Map get to this list (i.e. get a parameter for each type)
        // 3. Call the action with these parameters
        a.invoke(Arrays.stream(a.getParameterTypes())
                 .map(this::get).toArray());
    }

    public static void main(String[] args) {
        new DemoUI(new DeviceManager()).run();
    }

}
