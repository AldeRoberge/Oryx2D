package rotmg.particles;

import utils.flash.Vector;
import utils.flash.display.BitmapData;
import utils.flash.geom.Matrix;
import utils.flash.geom.Vector3D;
import rotmg.objects.BasicObject;
import rotmg.objects.Square;
import rotmg.objects.animation.Animations;
import rotmg.util.GraphicsUtil;

/**
 * 100% match
 */
public class XMLParticle extends BasicObject {

    public BitmapData texture = null;

    public Animations animations = null;

    public int size;

    public double durationLeft;

    public Vector3D moveVec;

    protected GraphicsBitmapFill bitmapFill;

    protected GraphicsPath path;

    protected Vector<Double> vS;

    protected Vector<Double> uvt;

    protected Matrix fillMatrix;

    public XMLParticle(ParticleProperties param1) {

        super();

        this.bitmapFill = new GraphicsBitmapFill(null, null, false, false);
        this.path = new GraphicsPath(GraphicsUtil.QUAD_COMMANDS, null);
        this.vS = new Vector<>();
        this.uvt = new Vector<>();
        this.fillMatrix = new Matrix();
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
            return false;
        }
        this.x = this.x + (this.moveVec.x * loc3);
        this.y = this.y + (this.moveVec.y * loc3);
        return true;
    }

    @Override
    public void draw(Vector<IGraphicsData> param1, Camera param2, int param3) {
        BitmapData loc7 = null;
        BitmapData loc4 = this.texture;
        if (this.animations != null) {
            loc7 = this.animations.getTexture(param3);
            if (loc7 != null) {
                loc4 = loc7;
            }
        }
        loc4 = TextureRedrawer.redraw(loc4, this.size, true, 0);
        int loc5 = loc4.width;
        int loc6 = loc4.height;
        this.vS.length = 0;
        this.vS.add(this.posS.get(3) - (loc5 / 2), this.posS.get(4) - loc6, this.posS.get(3) + (loc5 / 2), this.posS.get(4) - loc6, this.posS.get(3) + (loc5 / 2), this.posS.get(4), this.posS.get(3) - (loc5 / 2), this.posS.get(4));
        this.path.data = this.vS;
        this.bitmapFill.bitmapData = loc4;
        this.fillMatrix.identity();
        this.fillMatrix.translate(this.vS.get(0), this.vS.get(1));
        this.bitmapFill.matrix = this.fillMatrix;
        param1.add(this.bitmapFill);
        param1.add(this.path);
        param1.add(GraphicsUtil.END_FILL);
    }


}
