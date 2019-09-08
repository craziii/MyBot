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

    /*private BufferedImage autoCropEmptySpace(final BufferedImage image) {


        // TODO: Repeat this for every row and take highest black row!
        int upperBorder = -1;
        boolean borderFound = false;
        do {
            upperBorder++;
            boolean blackPixelRowFound = true;
            for (int column = 0; column < imageWidth; column++) {
                if (image.getRGB(column, upperBorder) != blackRGB) {
                    blackPixelRowFound = false;
                    break;
                }
            }
            if (blackPixelRowFound) borderFound = true;
        } while (!borderFound);

//        final int imageHeight = image.getHeight();
//        final int imageWidth = image.getWidth();
//        final int blackRGB = black.getRGB();
//
//        int upperYStop = -1;
//        int leftBound = -1;
//        boolean upperYStopFound = false;
//        for (int y = 0; y < imageHeight; y++) {
//            for (int x = 0; x < imageWidth; x++) {
//                if (image.getRGB(x, y) != blackRGB) {
//                    if (!upperYStopFound) {
//                        upperYStop = y - 1;
//                    }
//                    leftBound = x - 1;
//                }
//            }
//        }

        int lowerYStop = -1;
        int rightBound = -1;
        boolean lowerYStopFound = false;
        for (int y = imageHeight; y >= 0; y--) {
            for (int x = imageWidth; x >= 0; x--) {
                if (image.getRGB(x, y) != blackRGB) {
                    if (!lowerYStopFound) {
                        lowerYStop = y + 1;
                    }
                    rightBound = x + 1;
                }
            }
        }

    }

    private boolean imageRowIsColor(final BufferedImage image,
                                    final int row,
                                    final Color color) {
        for (int x = 0; x < image.getWidth(); x++) {
            if (image.getRGB(x, row) != color.getRGB()) return false;
        }
        return true;
    }

    private boolean imageColumnIsColor(final BufferedImage image,
                                       final int column,
                                       final Color color) {
        for (int y = 0; y < image.getHeight(); y++) {
            if (image.getRGB(column, y) != color.getRGB()) return false;
        }
        return true;
    }

    private BufferedImage resizeImage(final BufferedImage image) {
        int xLeft = -1;
        int xRight = -1;
        boolean leftBoundFound = false;
        for (int i = 0; i < image.getWidth(); i++) {
            if (imageColumnIsColor(image, i, BLACK)) {
                if (leftBoundFound) {
                    xRight = i;
                } else {
                    xLeft = i;
                    leftBoundFound = true;
                }
            }
        }
        int yTop = -1;
        int yBottom = -1;
        boolean topBoundFound = false;
        for (int i = 0; i < image.getHeight(); i++) {
            if (imageRowIsColor(image, i, BLACK)) {
                if (topBoundFound) {
                    yBottom = i;
                } else {
                    yTop = i;
                    topBoundFound = true;
                }
            }
        }
        return image.getSubimage(xLeft, xRight, xRight - xLeft, yBottom - yTop);
    }

    private Bounds getImageBounds(final BufferedImage image) {
        final int imageHeight = image.getHeight();
        final int imageWidth = image.getWidth();
        final int blackRGB = black.getRGB();

        Integer upperYBound = -1;
        Integer leftBound = -1;

        Integer lowerYBound = -1;
        Integer rightBound = -1;

        for (int i = 0; i < 2; i++) {
            boolean searchingVerticalBound = true;
            final boolean firstBound = i == 0;
            final int yStart = firstBound ? 0 : imageHeight;
            final int xStart = firstBound ? 0 : imageWidth;
            final Integer currentVerticalBound = firstBound ? upperYBound : lowerYBound;
            final Integer currentHorizontalBound = firstBound ? leftBound : rightBound;
            for (int y = yStart; y < imageHeight; y = firstBound ? y + 1 : y - 1) {
                for (int x = xStart; x < imageWidth; x = firstBound ? x + 1 : x - 1) {
                    if (image.getRGB(x, y) != blackRGB) {
                        if (searchingVerticalBound) {
                            upperYBound = y - 1;
                            searchingVerticalBound = false;
                        }
                        leftBound = x - 1;
                    }
                }
            }
            if (upperYBound == -1 || lowerYBound == -1 || leftBound == -1 || rightBound == -1)
                throw new ImageBoundResolveException(image);
        }


    }


    private class Bounds {

        private final Point upperLeftBound;
        private final Point lowerRightBound;

        private Bounds(final Point upperLeftBound,
                       final Point lowerRightBound) {
            this.upperLeftBound = upperLeftBound;
            this.lowerRightBound = lowerRightBound;
        }

        public Point getUpperLeftBound() {
            return upperLeftBound;
        }

        public Point getLowerRightBound() {
            return lowerRightBound;
        }
    }*/


    private File writeAndGetImageFile(final BufferedImage fImage,
                                      final String fileName) throws IOException {
        final File fImageFile = new File(fileName);
        ImageIO.write(fImage, "jpeg", fImageFile);
        return fImageFile;
    }

}
