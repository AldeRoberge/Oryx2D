package rotmg.objects;

import flash.display.BitmapData;

public class FlashDescription {

    public int startTime;

    public int color;

    public int periodMS;

    public int repeats;

    public int targetR;

    public int targetG;

    public int targetB;

    public FlashDescription(int param1, int param2, double param3, int param4) {
        super();
        this.startTime = param1;
        this.color = param2;
        this.periodMS = (int) (param3 * 1000);
        this.repeats = param4;
        this.targetR = (param2 >> 16) & 255;
        this.targetG = (param2 >> 8) & 255;
        this.targetB = param2 & 255;
    }

    public BitmapData apply(BitmapData param1, int param2) {
        int loc3 = (param2 - this.startTime) % this.periodMS;
        double loc4 = Math.sin((loc3 / this.periodMS) * Math.PI);
        double loc5 = loc4 * 0.5;
        return param1.clone();
    }

    public void applyGPUTextureColorTransform(BitmapData param1, int param2) {
        int loc3 = (param2 - this.startTime) % this.periodMS;
        double loc4 = Math.sin((loc3 / this.periodMS) * Math.PI);
        double loc5 = loc4 * 0.5;
    }

    public boolean doneAt(int param1) {
        return param1 > (this.startTime + (this.periodMS * this.repeats));
    }

}
