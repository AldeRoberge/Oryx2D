package rotmg.particles;

import flash.Vector;
import flash.display.BitmapData;
import flash.geom.Vector3D;
import rotmg.objects.BasicObject;
import rotmg.objects.Square;
import rotmg.objects.animation.Animations;

/**
 * 10% match
 */
public class XMLParticle extends BasicObject {

    public BitmapData texture = null;

    public Animations animations = null;

    public int size;

    public double durationLeft;

    public Vector3D moveVec;

    protected Vector<Double> vS;

    protected Vector<Double> uvt;

    public XMLParticle(ParticleProperties param1) {

        super();

        this.vS = new Vector<>();
        this.uvt = new Vector<>();
        this.objectId = getNextFakeObjectId();
        this.size = param1.size;
        this.z = param1.z;
        this.durationLeft = param1.duration;
        this.texture = param1.textureData.getTexture(this.objectId);
        if (param1.animationsData != null) {
            this.animations = new Animations(param1.animationsData);
        }
        this.moveVec = new Vector3D();
        double loc2 = Math.PI * 2 * Math.random();
        this.moveVec.x = Math.cos(loc2) * 0.1 * 5;
        this.moveVec.y = Math.sin(loc2) * 0.1 * 5;
    }

    public boolean moveTo(double param1, double param2) {
        Square loc3 = this.map.getSquare(param1, param2);
        if (loc3 == null) {
            return false;
        }
        this.x = param1;
        this.y = param2;
        this.square = loc3;
        return true;
    }

    @Override
    public boolean update(int param1, int param2) {
        double loc3 = 0;
        loc3 = param2 / 1000;
        this.durationLeft = this.durationLeft - loc3;
        if (this.durationLeft <= 0) {
            return true;
        }
        this.x = this.x + (this.moveVec.x * loc3);
        this.y = this.y + (this.moveVec.y * loc3);
        return false;
    }


}
