package com.evilduck.entity;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class GraphicsTransformer {

    public File rotateImage(final BufferedImage oImage,
                            final double theta) throws IOException {
        final BufferedImage fImage = new BufferedImage(oImage.getWidth(), oImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        final Graphics2D g2d = (Graphics2D) fImage.getGraphics();

        g2d.rotate(theta,
                oImage.getWidth() / 2,
                oImage.getHeight() / 2);
        g2d.drawRenderedImage(oImage, null);
        g2d.dispose();

        return writeAndGetImageFile(fImage, "rotated.png");

    }

    private File writeAndGetImageFile(final BufferedImage fImage,
                                      final String fileName) throws IOException {
        final File fImageFile = new File(fileName);
        ImageIO.write(fImage, "png", fImageFile);
        return fImageFile;
    }

}
