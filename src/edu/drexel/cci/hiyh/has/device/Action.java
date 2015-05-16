package edu.drexel.cci.hiyh.has.device;

import java.util.ArrayList;
import java.util.List;


public abstract class Action extends Device {
    //public final String name;

    protected Action(String name) {
        super(name);
    	actions.add(this);
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

    @Override
    public java.awt.Image getDisplayImage() {
        // TODO
        return null;
    }
    
    @Override
    public void addAction(Action a) {
    }
    
    @Override
    public List<Action> getAvailableActions() {
    	return actions;
    }
}
