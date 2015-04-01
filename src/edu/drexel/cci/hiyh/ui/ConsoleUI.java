package edu.drexel.cci.hiyh.ui;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Demonstration/testing UI that uses the command line.
 */
public class ConsoleUI implements InputUI {
    private final Scanner reader = new Scanner(System.in);

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void await(String message) {
        System.out.println(message);
        reader.nextLine();
    }

    @Override
    public <T extends Displayable> Optional<T> select(List<T> items) {
        System.out.println("Select one, or enter an index out of range to cancel:");
        int i = 0;
        for (T o : items)
            System.out.printf("%d. %s\n", i++, o);

        int s = reader.nextInt();
        // grab and discard newline
        reader.nextLine();
        if (s >= 0 && s < items.size())
            return Optional.of(items.get(s));
        else
            return Optional.empty();
    }

    @Override
    public <T> Optional<T> get(Class<T> c) {
        // Note that this doesn't actually include a cancel option.
        if (c.equals(Byte.class))
            return Optional.of(c.cast(getByte()));
        else
            throw new IllegalArgumentException("Can't handle class: " + c);
    }

    private Byte getByte() {
        System.out.println("Enter a byte: ");
        Byte b = reader.nextByte();
        // grab and discard newline
        reader.nextLine();
        return b;
    }
}
