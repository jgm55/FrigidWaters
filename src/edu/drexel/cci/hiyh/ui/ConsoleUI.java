package edu.drexel.cci.hiyh.ui;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import edu.drexel.cci.hiyh.has.device.BoundedInt;

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

    public Optional<Integer> get(BoundedInt b) {
        System.out.printf("Enter an int between %d and %d, or outside the range to cancel:\n", b.lower, b.upper);
        int i = reader.nextInt();
        // grab and discard newline
        reader.nextLine();
        return Optional.of(i).filter(a -> a >= b.lower && a <= b.upper);
    }
}
