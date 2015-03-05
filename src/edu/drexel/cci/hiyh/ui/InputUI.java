package edu.drexel.cci.hiyh.ui;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interface for user interface objects that support obtaining input from the
 * user.
 */
public interface InputUI {
    /**
     * Have the user select an object from a list.
     *
     * @param items List of objects to choose from
     * @param success Called on the chosen item on success
     * @param cancel Called on null when the user cancels the selection
     * TODO make a nullary void function type for that
     */
    public <T extends Displayable> void select(List<T> items, Consumer<T> success, Consumer<Void> cancel);

    /**
     * Have the user select an object of a class (for example, an integer).
     *
     * @param c Class of object to choose
     * @param success Called on the chosen object on success
     * @param failure Called on null when the user cancels the selection
     * TODO make a nullary void function type for that
     * @throws IllegalArgumentException if c is not supported by this InputUI
     */
    public <T> void get(Class<T> c, Consumer<T> success, Consumer<Void> cancel);
}
