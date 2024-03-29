package edu.drexel.cci.hiyh.ui;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBooleanInputSource implements BooleanInputSource {
    private final List<Listener> listeners = new ArrayList<Listener>();

    public synchronized void addListener(Listener l) {
        listeners.add(l);
    }

    public synchronized void removeListener(Listener l) {
        listeners.remove(l);
    }

    protected synchronized void notifyListeners() {
        // copy list of listeners so that they are free to modify it
        for (Listener l : new ArrayList<Listener>(listeners)) {
            l.onBooleanInput();
        }
    }
}
