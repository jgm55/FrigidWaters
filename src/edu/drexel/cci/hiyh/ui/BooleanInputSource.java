package edu.drexel.cci.hiyh.ui;

public interface BooleanInputSource {
    public interface Listener {
        public void onBooleanInput();
    }

    public void addListener(Listener l);
    public void removeListener(Listener l);
}
