package edu.drexel.cci.hiyh.ui;

import java.util.Arrays;
import java.util.Scanner;

/** Console UI demo. */
public class DemoUI {
    private final Scanner reader = new Scanner(System.in);
    private final DeviceManager dm;

    public DemoUI(DeviceManager dm) {
        this.dm = dm;
    }

    public Object menu(Object[] options) {
        System.out.println("Select one:");
        int i = 0;
        for (Object o : options)
            System.out.printf("%d. %s", i++, o);
        // XXX THIS IS JUST A DEMO, SO I DON'T CARE
        return options[reader.nextInt()];
    }

    public byte getByte() {
        System.out.println("Enter a byte: ");
        return reader.nextByte();
    }

    public Object get(Class<?> c) {
        if (c == Byte.class)
            return getByte();
        return null;
    }

    public void run() {
        Device d = menu(dm.getDevices());
        Method m = menu(Arrays.asList(d.class.getMethods()).stream()
                        .filter(m -> m.isAnnotationPresent(Action.class))
                        .toArray());
        m.invoke(d, Arrays.asList(m.getParameterTypes()).stream()
                    .map(this::get).toArray());
    }

    public static void main(String[] args) {
        // TODO get ye dm
        new DemoUI(dm).run();
    }

}
