package oryx2D.level;

import flash.Vector;
import flash.display.BitmapData;
import oryx2D.graphics.Screen;
import rotmg.map.AbstractMap;
import rotmg.objects.GameObject;
import rotmg.objects.Square;

public class Level {

    public static BitmapData debug = new BitmapData("/oryx2D/textures/debug/cross.png");

    public static final int TILE_SIZE = 8;

    protected Vector<Square> tiles = AbstractMap.squares;

    protected int width, height;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /*
    Render level
    https://www.youtube.com/watch?v=lNFAJPydlbU
     */
    public void render(Screen screen) {
        // Deal with tiles as tile position (not pixel)

        // Used to draw exactly how needed (everything inside of screen)
        int halfTile = (TILE_SIZE / 2);

        int tilePositionTopLeft = screen.xOffset / TILE_SIZE;
        int tilePositionTopRight = ((screen.xOffset + screen.width) / TILE_SIZE) + halfTile;

        int tilePositionBottomLeft = screen.yOffset / TILE_SIZE;
        int tilePositionBottomRight = ((screen.yOffset + screen.height) / TILE_SIZE) + halfTile;

        for (int x = tilePositionTopLeft; x < tilePositionTopRight; x++) {
            for (int y = tilePositionBottomLeft; y < tilePositionBottomRight; y++) {
                Square t = (this.getTile(x, y));
                if ((t != null) && (t.texture != null)) {
                    screen.render(x, y, t.texture);
                }
            }
        }

        try {
            for (GameObject g : AbstractMap.goDict) {
                if (g.texture != null) {
                    //screen.render(g.x, g.y, debug);
                    screen.render(g.x, g.y, g.texture);
                }
            }
        } catch (Exception e) {
        }


    }

    public Square getTile(int x, int y) {
        for (Square t : this.tiles) {
            if ((t.x == x) && (t.y == y)) {
                return t;
            }
        }
        return null;
    }

}
