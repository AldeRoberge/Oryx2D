package rotmg.particles;

import flash.Vector;
import flash.display.BitmapData;
import rotmg.objects.BasicObject;

public class BaseParticle extends BasicObject {

    public double timeLeft = 0;

    public double spdX;

    public double spdY;

    public double spdZ;

    protected Vector<Double> vS;


    public BaseParticle(BitmapData bitmapData) {
        this.vS = new Vector<>(8);
        this.objectId = getNextFakeObjectId();
    }

    public void initialize(double totalTime, double speedX, double speedY, double speedZ, int zPos) {
        this.timeLeft = totalTime;
        this.spdX = speedX;
        this.spdY = speedY;
        this.spdZ = speedZ;
        this.z = zPos;
    }

    @Override
    public void removeFromMap() {
        this.map = null;
        this.square = null;
    }


}
