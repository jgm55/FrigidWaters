package edu.drexel.cci.hiyh.ui;

import java.awt.Image;
import java.util.Optional;

/**
 * Interface for classes that can be displayed in an InputUI.
 */
public interface Displayable {
    public default String getDisplayText() {
        return toString();
    }
    public default Optional<Image> getDisplayImage() {
        return Optional.empty();
    }
}
