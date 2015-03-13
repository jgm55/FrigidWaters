package edu.drexel.cci.hiyh.ui;

import java.awt.Component;
import java.util.List;
import java.util.function.Consumer;

/**
 * Interface for user interface objects that support obtaining input from the
 * user.
 */
public interface InputUI {
    /**
     * Display a message to the user.
     *
     * @param message Message to display.
     */
    public void showMessage(String message);

    /**
     * Display a message and wait for the user to signal to begin.
     *
     * @param target Function to run on user's signal
     * @param message Message to show until then
     */
    public void await(Runnable target, String message);

    /**
     * Have the user select an object from a list.
     *
     * @param items List of objects to choose from
     * @param success Called on the chosen item on success
     * @param cancel Called on null when the user cancels the selection
     */
    public <T extends Displayable> void select(List<T> items, Consumer<T> success, Runnable cancel);

    /**
     * Have the user select an object of a class (for example, an integer).
     *
     * @param c Class of object to choose
     * @param success Called on the chosen object on success
     * @param failure Called on null when the user cancels the selection
     * @throws IllegalArgumentException if c is not supported by this InputUI
     */
    public <T> void get(Class<T> c, Consumer<T> success, Runnable cancel);

    /**
     * Temporary (?) hack to allow MouseInputSource to get mouse clicks.
     */
    public default Component getGlassPane() {
        throw new UnsupportedOperationException();
    }
}
