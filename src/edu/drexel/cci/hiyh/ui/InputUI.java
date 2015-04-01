package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.util.Optional;
import java.util.List;

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
     * Have the user select an object of a class (for example, an integer).
     *
     * @param c Class of object to choose
     * @return the chosen object, or empty if user cancels
     * @throws IllegalArgumentException if c is not supported by this InputUI
     */
    public <T> Optional<T> get(Class<T> c) throws InterruptedException;

    /**
     * Temporary (?) hack to allow MouseInputSource to get mouse clicks.
     */
    public default Component getGlassPane() {
        throw new UnsupportedOperationException();
    }
}
