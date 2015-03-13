package edu.drexel.cci.hiyh.ui;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Demonstration/testing UI that uses the command line.
 *
 * Note that continued use of this interface will "stack leak" due to the
 * general callback interface.
 */
public class ConsoleUI implements InputUI {
    private final Scanner reader = new Scanner(System.in);

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void await(Runnable target, String message) {
        System.out.println(message);
        reader.nextLine();
        target.run();
    }

    @Override
    public <T extends Displayable> void select(List<T> items, Consumer<T> success, Runnable cancel) {
        System.out.println("Select one, or enter an index out of range to cancel:");
        int i = 0;
        for (T o : items)
            System.out.printf("%d. %s\n", i++, o);

        int s = reader.nextInt();
        // grab and discard newline
        reader.nextLine();
        if (s >= 0 && s < items.size())
            success.accept(items.get(s));
        else
            cancel.run();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public <T> void get(Class<T> c, Consumer<T> success, Runnable cancel) {
        // Note that this doesn't actually include a cancel option.
        if (c.equals(Byte.class))
            success.accept(c.cast(getByte()));
        else
            throw new IllegalArgumentException("Can't handle class: " + c);
    }

    private Byte getByte() {
        System.out.println("Enter a byte: ");
        return reader.nextByte();
    }
}
