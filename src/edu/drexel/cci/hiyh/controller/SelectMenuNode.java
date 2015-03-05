package edu.drexel.cci.hiyh.controller;

import java.util.List;

import edu.drexel.cci.hiyh.ui.Displayable;
import edu.drexel.cci.hiyh.ui.InputUI;

/**
 * A MenuNode that has the user select an item from a list. Convenience class.
 */
public abstract class SelectMenuNode<T extends Displayable> extends MenuNode<T> {
    private final List<T> items;

    public SelectMenuNode(InputUI ui, MenuNode<?> parent, List<T> items) {
        super(ui, parent);
        this.items = items;
    }

    public SelectMenuNode(MenuNode<?> parent, List<T> items) {
        super(parent);
        this.items = items;
    }

    public SelectMenuNode(InputUI ui, List<T> items) {
        super(ui);
        this.items = items;
    }

    @Override
    public void run() {
        ui.select(items, this::success, this::cancel);
    }
}
