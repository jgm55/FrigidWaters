package edu.drexel.cci.hiyh.util;

import java.awt.Image;
import java.io.IOException;
import java.util.Optional;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static Optional<Image> load(String name) {
        try {
            return Optional.of(ImageIO.read(ImageLoader.class.getResource(name)));
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Couldn't load image " + name);
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
