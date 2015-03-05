package edu.drexel.cci.hiyh.controller;

import edu.drexel.cci.hiyh.ui.InputUI;

/**
 * Menu navigation is represented by a series of MenuNodes. They provide the
 * ability to request user input without blocking and to "back out" should the
 * user cancel a menu selection.
 */
public abstract class MenuNode<T> {
    /**
     * The InputUI that the MenuNode should receive input from.
     */
    protected final InputUI ui;
    /**
     * The previous MenuNode in the chain. Canceling should return to this
     * node.
     */
    protected final MenuNode<?> parent;

    public MenuNode(InputUI ui, MenuNode<?> parent) {
        // Would we ever actually want different UIs for a node and its
        // parent?
        this.ui = ui;
        this.parent = parent;
    }

    public MenuNode(InputUI ui) {
        this(ui, null);
    }

    public MenuNode(MenuNode<?> parent) {
        this(parent.ui, parent);
    }

    /**
     * Begin this node's operation. This should request input from the UI and
     * have it call back to either success() or cancel(), as appropriate.
     */
    public abstract void run();

    /**
     * Accept user input and take action appropriately.
     *
     * @param t user input
     */
    public abstract void success(T t);

    /**
     * Back up to the previous MenuNode, or cancel operation if this is the
     * first one.
     */
    public void cancel(Void v) {
        if (parent != null)
            parent.run();
    }
}
