package rotmg.objects;

import flash.Vector;
import flash.XML;
import flash.display.BitmapData;
import rotmg.util.BitmapUtil;

public class Wall extends GameObject {

    private static final Vector<Double> UVT = new Vector<>(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0);

    private static final Vector<Double> sqX = new Vector<>(0.0, 1.0, 0.0, -1.0);

    private static final Vector<Double> sqY = new Vector<>(-1.0, 0.0, 1.0, 0.0);

    private BitmapData topTexture = null;

    public Wall(XML param1) {
        super(param1);
        this.hasShadow = false;
        TextureData loc2 = ObjectLibrary.typeToTopTextureData.get(this.objectType);
        this.topTexture = loc2.getTexture(0);
    }

    @Override
    public void setObjectId(int param1) {
        super.setObjectId(param1);
        TextureData loc2 = ObjectLibrary.typeToTopTextureData.get(this.objectType);
        this.topTexture = loc2.getTexture(param1);
    }

    @Override
    public int getColor() {
        return BitmapUtil.mostCommonColor(this.topTexture);
    }

}
