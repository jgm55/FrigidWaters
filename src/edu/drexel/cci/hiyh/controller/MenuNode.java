package edu.drexel.cci.hiyh.controller;

import edu.drexel.cci.hiyh.ui.Displayable;

import java.util.List;

public abstract class MenuNode {

    public MenuNode(List<Displayable> displayableList) {
        // create some ui element with displayables
    }

    public abstract void success(int index);
    public abstract void failure();
}
