package rotmg.objects;
//import com.company.assembleegameclient.engine3d.TextureMatrix;
//import com.company.assembleegameclient.objects.GameObject;
//import com.company.assembleegameclient.util.TileRedrawer;

import rotmg.map.AbstractMap;
import rotmg.map.GroundLibrary;
import rotmg.map.GroundProperties;
import flash.Vector;
import flash.display.BitmapData;

//import flash.display.BitmapData;
//import flash.display.IGraphicsData;
//import flash.geom.Vector3D;

public class Square {

    public static final Vector<Double> UVT = new Vector<>(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0);

    private static final int[] LOOKUP = new int[]{26171, 44789, 20333, 70429, 98257, 59393, 33961};

    public AbstractMap map;

    public int x;

    public int y;

    public int tileType = 255;

    public Vector<Double> vin;

    public GameObject obj = null;

    public GroundProperties props;

    public BitmapData texture = null;

    public int sink = 0;

    public int lastDamage = 0;

    public int lastVisible;

    public Square(AbstractMap param1, int param2, int param3) {
        super();
        this.props = GroundLibrary.defaultProps;
        this.map = param1;
        this.x = param2;
        this.y = param3;
        this.vin = new Vector<>((double) this.x, (double) this.y, (double) 0, (double) this.x + 1, (double) this.y, (double) 0, (double) this.x + 1, (double) this.y + 1, (double) 0, (double) this.x, (double) this.y + 1, (double) 0);
    }

    // Not sure this is a good implementation
    private int hash(double p1, double p2) {
        return ((int) p1 * 2949) + (int) p2;
    }

    public void dispose() {
        this.map = null;
        this.vin = null;
        this.obj = null;
        this.texture = null;
    }

    public Square setTileType(int tileType) {
        this.tileType = tileType;
        this.props = GroundLibrary.propsLibrary.get(this.tileType);
        this.texture = GroundLibrary.getBitmapData(this.tileType, this.hash(this.x, this.y));

        return this;
    }

    public boolean isWalkable() {
        return !this.props.noWalk && ((this.obj == null) || !this.obj.props.occupySquare);
    }

    public boolean isSolid() {
        return this.props.noWalk;
    }

}
