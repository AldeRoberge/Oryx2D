package utils.flash.display;

import utils.flash.geom.ColorTransform;
import utils.flash.geom.Matrix;
import utils.flash.geom.Point;
import utils.flash.geom.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class BitmapData {

    public int width;
    public int height;

    public int[] pixels;

    public BufferedImage image;

    public Rectangle rect;

    public BitmapData(String path) {
        try {
            BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.pixels = new int[this.width * this.height];
            image.getRGB(0, 0, this.width, this.height, this.pixels, 0, this.width); //write rgb pixels to pixels array
        } catch (Exception e) {
            System.err.println("Error with file : " + path + ", width : " + this.width + ", height : " + this.height + ".");
            e.printStackTrace();
        }
    }

    public BitmapData(BufferedImage image) {

        this.image = image;

        this.width = image.getWidth();
        this.height = image.getHeight();

        this.pixels = new int[this.width * this.height];

        image.getRGB(0, 0, this.width, this.height, this.pixels, 0, this.width); //write rgb pixels to pixels array

        this.updateWidthAndHeight();
    }

    /**
     * Used by BitmapDataSpy
     */
    public BitmapData(int width, int height, boolean param3, int param4) {
        this(width, height);
    }

    public BitmapData(int width, int height) {
        this(new BufferedImage(width, height, 1));
        this.updateWidthAndHeight();
    }

    public BitmapData(int i, int i1, boolean b, double v) {

    }

    private void updateWidthAndHeight() {
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();

        this.rect = new Rectangle(0, 0, this.width, this.height);
    }


    @Override
    public BitmapData clone() {
        return new BitmapData(this.image);
    }

    /**
     * From AS3 API :
     * <p>
     * This method copies a rectangular area of a source image to a rectangular
     * area of the same size at the destination point of the destination BitmapData object.
     * <p>
     * This method is not implemented as it should. TODO
     */
    public void copyPixels(BitmapData sourceImage, Rectangle rectangle, Point point) {
        this.image = sourceImage.image;
    }

    public void dispose() {
        this.image = null;
    }

    public void draw(BitmapData param1, Matrix loc4, Object o, Object o1, Object o2, boolean b) {
    }

    public void draw(IBitmapDrawable nameText, Object o) {
    }

    public void setPixel(int i, int i1, double loc5) {
    }

    public void colorTransform(Object rect, ColorTransform param2) {
    }

    public int getPixel32(int loc3, int loc4) {
        return 0;
    }

    public void copyPixels(BitmapData bitmapData, Rectangle param2, Point param3, BitmapData random, Point p0, boolean b) {
    }

    public void draw(DisplayObject gradient, Matrix tempMatrix, Object o, String subtract) {
    }
}
