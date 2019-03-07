package rotmg.util.redrawers;


import flash.display.BitmapData;
import flash.utils.Dictionary;


//TODO make outlineglow actually outline glow
public class GlowRedrawer {

    private static final int GRADIENT_MAX_SUB = 2631720;

    private static Dictionary<BitmapData, Dictionary<String, BitmapData>> glowHashes = new Dictionary();

    public GlowRedrawer() {
        super();
    }

    public static BitmapData outlineGlow(BitmapData param1, double param2) {
        return outlineGlow(param1, param2, 1.4, false, 0);
    }

    public static BitmapData outlineGlow(BitmapData param1, double param2, double param3, boolean param4, int param5) {
        /*String loc6 = getHash(param2, param3, param5);
        if (param4 && isCached(param1, loc6)) {
            return glowHashes.get(param1).get(loc6);
        }
        BitmapData loc7 = param1.clone();
        tempMatrix.identity();
        tempMatrix.scale(param1.width / 256, param1.height / 256);
        loc7.draw(gradient, tempMatrix, null, BlendMode.SUBTRACT);
        Bitmap loc8 = new Bitmap(param1);
        loc7.draw(loc8, null, null, BlendMode.ALPHA);
        TextureRedrawer.OUTLINE_FILTER.blurX = param3;
        TextureRedrawer.OUTLINE_FILTER.blurY = param3;
        TextureRedrawer.OUTLINE_FILTER.color = param5;
        loc7.applyFilter(loc7, loc7.rect, PointUtil.ORIGIN, TextureRedrawer.OUTLINE_FILTER);
        if (param2 != 4294967295.0) {
            if (Parameters.isGpuRender() && (param2 != 0)) {
                GLOW_FILTER_ALT.color = param2;
                loc7.applyFilter(loc7, loc7.rect, PointUtil.ORIGIN, GLOW_FILTER_ALT);
            } else {
                GLOW_FILTER.color = param2;
                loc7.applyFilter(loc7, loc7.rect, PointUtil.ORIGIN, GLOW_FILTER);
            }
        }
        if (param4) {
            cache(param1, param2, param3, loc7, param5);
        }
        return loc7;*/

        return param1;
    }

    private static void cache(BitmapData param1, double param2, double param3, BitmapData param4, int param5) {
        Dictionary loc7 = null;
        String loc6 = getHash(param2, param3, param5);
        if (glowHashes.contains(param1)) {
            glowHashes.get(param1).put(loc6, param4);
        } else {
            loc7 = new Dictionary<>();
            loc7.put(loc6, param4);
            glowHashes.put(param1, loc7);
        }
    }

    private static boolean isCached(BitmapData param1, String param2) {
        Dictionary loc3 = null;
        if (glowHashes.contains(param1)) {
            loc3 = glowHashes.get(param1);
            if (loc3.contains(param2)) {
                return true;
            }
        }
        return false;
    }

    private static String getHash(double param1, double param2, int param3) {
        return String.valueOf((param2 * 10) + param1 + param3);
    }



}