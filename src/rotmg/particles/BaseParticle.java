package rotmg.particles;

import utils.flash.Vector;
import utils.flash.display.BitmapData;
import utils.flash.geom.Matrix;
import rotmg.objects.BasicObject;
import rotmg.util.GraphicsUtil;

public class BaseParticle extends BasicObject {

    public double timeLeft = 0;

    public double spdX;

    public double spdY;

    public double spdZ;

    protected Vector<Double> vS;

    protected Matrix fillMatrix;

    protected GraphicsPath path;

    protected GraphicsBitmapFill bitmapFill;

    public BaseParticle(BitmapData bitmapData) {
        this.vS = new Vector<>(8);
        this.fillMatrix = new Matrix();
        this.path = new GraphicsPath(GraphicsUtil.QUAD_COMMANDS, null);
        this.bitmapFill = new GraphicsBitmapFill(null, null, false, false);
        this.bitmapFill.bitmapData = bitmapData;
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
    public void draw(Vector<IGraphicsData> graphicsData, Camera camera, int time) {
        double halfW = this.bitmapFill.bitmapData.width / 2;
        double halfH = this.bitmapFill.bitmapData.height / 2;

        this.vS.put(6, this.posS.get(3) - halfW);
        this.vS.put(0, this.posS.get(3) - halfW);

        this.vS.put(3, this.posS.get(4) - halfH);
        this.vS.put(1, this.posS.get(4) - halfH);

        this.vS.put(4, this.posS.get(3) + halfW);
        this.vS.put(2, this.posS.get(3) + halfW);

        this.vS.put(7, this.posS.get(4) + halfH);
        this.vS.put(5, this.posS.get(4) + halfH);

        this.path.data = this.vS;
        this.fillMatrix.identity();
        this.fillMatrix.translate(this.vS.get(0), this.vS.get(1));
        this.bitmapFill.matrix = this.fillMatrix;
        graphicsData.add(this.bitmapFill);
        graphicsData.add(this.path);
        graphicsData.add(GraphicsUtil.END_FILL);
    }

    @Override
    public void removeFromMap() {
        this.map = null;
        this.square = null;
    }


}
