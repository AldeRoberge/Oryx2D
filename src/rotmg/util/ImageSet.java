package rotmg.util;

import java.util.ArrayList;
import java.util.List;

import flash.display.BitmapData;

/**
 * This is a 100% match
 */
public class ImageSet {

    public List<BitmapData> images;

    public ImageSet() {
        this.images = new ArrayList<>();
    }

    public void add(BitmapData image) {
        this.images.add(image);
    }

    public BitmapData random() {
        return this.images.get((int) (Math.random() * this.images.size()));
    }

    public void addFromBitmapData(BitmapData bitmapData, int width, int height) {
        int maxX = bitmapData.width / width;
        int maxY = bitmapData.height / height;

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                this.images.add(BitmapUtil.cropToBitmapData(bitmapData, x * width, y * height, width, height));
            }
        }
    }

}
