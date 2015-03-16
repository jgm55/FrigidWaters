package edu.drexel.cci.hiyh.controller;

import edu.drexel.cci.hiyh.ui.InputUI;

/**
 * A MenuNode that just waits for the user to activate.
 */
public abstract class AwaitMenuNode extends MenuNode<Void> {
    private final String message;

    public AwaitMenuNode(InputUI ui, MenuNode<?> parent, String message) {
        super(ui, parent);
        this.message = message;
    }

    public AwaitMenuNode(MenuNode<?> parent, String message) {
        super(parent);
        this.message = message;
    }

    public AwaitMenuNode(InputUI ui, String message) {
        super(ui);
        this.message = message;
    }

    @Override
    public void run() {
        ui.await(this::success, message);
    }

    // grr typing
    public void success() {
        this.success(null);
    }
}
