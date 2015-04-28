package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.util.Optional;
import java.util.List;

import edu.drexel.cci.hiyh.has.device.ParamType;
import edu.drexel.cci.hiyh.has.device.BoundedInt;

/**
 * Interface for user interface objects that support obtaining input from the
 * user.
 */
public interface InputUI {
    /**
     * Display a message to the user.
     *
     * @param message Message to display
     */
    public void showMessage(String message);

    /**
     * Display a message and wait for the user to signal to begin.
     *
     * @param message Message to show until then
     */
    public void await(String message) throws InterruptedException;

    /**
     * Have the user select an object from a list.
     *
     * @param items List of objects to choose from
     * @return the chosen object, or empty if user cancels
     */
    public <T extends Displayable> Optional<T> select(List<T> items) throws InterruptedException;

    /**
     * Have the user select an object of a given parameter type (for example,
     * a bounded integer).
     *
     * @param c ParamType indicating what kind of object to choose
     * @return the chosen object, or empty if user cancels
     * @throws IllegalArgumentException if c is not supported by this InputUI
     */
    @SuppressWarnings("unchecked")
    public default <T> Optional<T> get(ParamType<T> p) throws InterruptedException {
        if (p instanceof BoundedInt) {
            return (Optional<T>)get((BoundedInt)p);
        }
        throw new UnsupportedOperationException("Not supported: " + p);
    }

    public default Optional<Integer> get(BoundedInt b) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported: " + b);
    }

    /**
     * Temporary (?) hack to allow MouseInputSource to get mouse clicks.
     */
    public default Component getGlassPane() {
        throw new UnsupportedOperationException();
    }
}
