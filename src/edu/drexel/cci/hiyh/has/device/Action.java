package edu.drexel.cci.hiyh.has.device;

import java.util.ArrayList;
import java.util.List;

import edu.drexel.cci.hiyh.ui.Displayable;

public abstract class Action implements Displayable {
    public final String name;

    protected Action(String name) {
        this.name = name;
    }

    public List<ParamType<?>> getParameterTypes() {
        return new ArrayList<ParamType<?>>();
    }

    public boolean isAvailable() {
        return true;
    }

    public abstract void invoke(List<Object> args);

    @Override
    public String toString() {
        return name;
    }
}
