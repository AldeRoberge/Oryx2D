package oryx2D.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import flash.display.BitmapData;

public class Screen {

    public final int MAP_SIZE = 128;

    public final int width, height;
    public int xOffset, yOffset;

    private BufferedImage image;
    private int[] pixels;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //Image (game view)
        this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData(); // Get data buffer of the image to write pixels to it
    }

    /*
     * Sets all pixels to black
     */
    public void clear() {
        for (int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = 0;
        }
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void render(double xp, double yp, BitmapData bitmapData) {
        this.render((int) (bitmapData.height * xp), (int) (bitmapData.width * yp), bitmapData, 0);
    }


    /**
     * @param xp    x position
     * @param yp    y position
     * @param image to draw
     * @param flip  flip
     */
    public void render(int xp, int yp, BitmapData image, int flip) {
        xp -= this.xOffset;
        yp -= this.yOffset;

        for (int y = 0; y < image.height; y++) {
            int ya = y + yp;
            int ys = y;

            if ((flip == 2) || (flip == 3)) {
                ys = image.height - 1 - y;
            }

            for (int x = 0; x < image.width; x++) {
                int xa = x + xp;
                int xs = x;

                if ((flip == 1) || (flip == 3)) {
                    xs = image.width - 1 - x;
                }

                // Check if BitmapData is outside of the screen
                if ((xa < -image.width) || (xa >= this.width) || (ya < 0) || (ya >= this.height)) {
                    break;
                }

                if (xa < 0) {
                    xa = 0;
                }

                int pixelColor = image.pixels[xs + (ys * image.width)];

                if ((pixelColor != 0) && (pixelColor != 0xFFFF00FF)) { //PINK or transparent
                    this.pixels[xa + (ya * this.width)] = pixelColor;
                }
            }
        }
    }

    public void drawPixels(Graphics g, int width, int height) {
        g.drawImage(this.image, 0, 0, width, height, null); //Draw the image
    }


}
