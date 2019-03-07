package rotmg.util;


import flash.display.BitmapData;
import flash.utils.Dictionary;
import rotmg.util.redrawers.GlowRedrawer;

public class TextureRedrawer {

    public static final int magic = 12;

    public static final int minSize = 2 * magic;
    //public static final GlowFilter OUTLINE_FILTER = new GlowFilter(0, 0.8, 1.4, 1.4, 255, BitmapFilterQuality.LOW, false, false);
    private static final int BORDER = 4;
    public static BitmapData sharedTexture = null;
    private static Dictionary<Integer, Dictionary<Integer, BitmapData>> cache = new Dictionary<>();
    private static Dictionary<Double, Dictionary<BitmapData, BitmapData>> faceCache = new Dictionary<>();
    private static Dictionary<BitmapData, Dictionary<String, BitmapData>> redrawCaches = new Dictionary<>();
    private static BitmapData colorTexture1 = new BitmapDataSpy(1, 1, false);

    private static BitmapData colorTexture2 = new BitmapDataSpy(1, 1, false);

    public TextureRedrawer() {
        super();
    }

    public static BitmapData redraw(BitmapData param1, int param2, boolean param3, int param4) {
        return redraw(param1, param2, param3, param4, false, 5.0, 0);
    }

    public static BitmapData redraw(BitmapData param1, int param2, boolean param3, int param4, boolean param5) {
        return redraw(param1, param2, param3, param4, param5, 5.0, 0);
    }

    public static BitmapData redraw(BitmapData param1, int param2, boolean param3, int param4, boolean param5, double param6, int param7) {
        String loc8 = getHash(param2, param3, param4, param6, param7);
        if (param5 && isCached(param1, loc8)) {
            return redrawCaches.get(param1).get(loc8);
        }
        BitmapData loc9 = resize(param1, null, param2, param3, 0, 0, param6);
        loc9 = GlowRedrawer.outlineGlow(loc9, param4, 1.4, param5, param7);
        if (param5) {
            cache(param1, loc8, loc9);
        }
        return loc9;
    }

    private static String getHash(int param1, boolean param2, int param3, double param4, int param5) {
        return param1 + "," + param3 + "," + param2 + "," + param4 + "," + param5;
    }

    private static void cache(BitmapData param1, String param2, BitmapData param3) {
        if (!(redrawCaches.containsKey(param1))) {
            redrawCaches.put(param1, new Dictionary<>());
        }
        redrawCaches.get(param1).put(param2, param3);
    }

    private static boolean isCached(BitmapData param1, String param2) {
        if (redrawCaches.containsKey(param1)) {
            if (redrawCaches.containsValue(param2)) {
                return true;
            }
        }
        return false;
    }

    public static BitmapData resize(BitmapData param1, BitmapData param2, int param3, boolean param4, int param5, int param6) {
        return resize(param1, param2, param3, param4, param5, param6, 5);
    }

    public static BitmapData resize(BitmapData param1, BitmapData param2, int param3, boolean param4, int param5, int param6, double param7) {

        // TODO implement

        return param1;
    }

    public static BitmapData redrawSolidSquare(int param1, int param2) {

        //TODO implement

        return null;
    }

    public static void clearCache() {
        for (Dictionary<Integer, BitmapData> loc2 : cache) {
            for (BitmapData loc1 : loc2) {
                loc1.dispose();
            }
        }
        cache = new Dictionary<>();
        for (Dictionary<BitmapData, BitmapData> loc3 : faceCache) {
            for (BitmapData loc1 : loc3) {
                loc1.dispose();
            }
        }
        faceCache = new Dictionary<>();
    }

    public static BitmapData redrawFace(BitmapData param1, double param2) {


        // TODO implement

        return param1;

    }

    private static BitmapData getTexture(int param1, BitmapData param2) {
        BitmapData loc3 = null;
        int loc4 = (param1 >> 24) & 255;
        int loc5 = param1 & 16777215;

        switch (loc4) {
            case 0:
                loc3 = param2;
                break;
            case 1:
                // param2.setPixel(0, 0, loc5);
                loc3 = param2;
                break;
            case 4:
                loc3 = AssetLibrary.getImageFromSet("textile4x4", loc5);
                break;
            case 5:
                loc3 = AssetLibrary.getImageFromSet("textile5x5", loc5);
                break;
            case 9:
                loc3 = AssetLibrary.getImageFromSet("textile9x9", loc5);
                break;
            case 10:
                loc3 = AssetLibrary.getImageFromSet("textile10x10", loc5);
                break;
            case 255:
                loc3 = sharedTexture;
                break;
            default:
                loc3 = param2;
        }
        return loc3;
    }

    private static BitmapData retexture(BitmapData param1, BitmapData param2, int param3, int param4) {

        //TODO implement

        return param1;
    }


}