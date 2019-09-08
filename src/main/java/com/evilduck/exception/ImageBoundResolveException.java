package com.evilduck.exception;

import java.awt.image.BufferedImage;

public class ImageBoundResolveException extends RuntimeException {

    private final BufferedImage image;

    public ImageBoundResolveException(final BufferedImage image) {
        super("Unable to resolve image boundaries for image with dimensions: x -> " + image.getWidth() + ", y -> " + image.getHeight());
        this.image = image;
    }

}
