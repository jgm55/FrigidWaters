package edu.drexel.cci.hiyh.has.device;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.drexel.cci.hiyh.ui.Displayable;

public abstract class Device implements Displayable {
    public final String name;
    private final List<Action> actions = new ArrayList<Action>();

    protected Device(String name) {
        this.name = name;
    }

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

        @Override
        public java.awt.Image getDisplayImage() {
            // TODO
            return null;
        }
    }

    protected void addAction(Action a) {
        actions.add(a);
    }

    public List<Action> getAvailableActions() {
        return actions.stream()
               .filter(Action::isAvailable)
               .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public java.awt.Image getDisplayImage() {
        // TODO
        return null;
    }
}
