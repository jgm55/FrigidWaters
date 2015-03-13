package edu.drexel.cci.hiyh.ui;

public interface BooleanInputSource {
    public interface Listener {
        public void onBooleanInput();
    }

    public default void initAndStart(InputUI ui) {}

    public void addListener(Listener l);
    // Must be safe to be called from l.onBooleanInput() itself
    public void removeListener(Listener l);
}
