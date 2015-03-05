package edu.drexel.cci.hiyh.controller;

import edu.drexel.cci.hiyh.ui.InputUI;

/**
 * A MenuNode that has the user enter an item from a class. Convenience class.
 */
public abstract class GetMenuNode<T> extends MenuNode<T> {
    private final Class<T> c;

    public GetMenuNode(InputUI ui, MenuNode<?> parent, Class<T> c) {
        super(ui, parent);
        this.c = c;
    }

    public GetMenuNode(MenuNode<?> parent, Class<T> c) {
        super(parent);
        this.c = c;
    }

    public GetMenuNode(InputUI ui, Class<T> c) {
        super(ui);
        this.c = c;
    }

    @Override
    public void run() {
        ui.get(c, this::success, this::cancel);
    }
}
