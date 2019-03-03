package rotmg.util;


import rotmg.objects.ObjectLibrary;
import utils.flash.XML;
import utils.flash.display.BitmapData;

import java.awt.*;

public class FameUtil {

    public static final int MAX_STARS = 70;

    public static final Integer[] STARS = new Integer[]{20, 150, 400, 800, 2000};

    private static final Color lightBlueCT = new Color(138, 152, 222);

    private static final Color darkBlueCT = new Color(49, 77, 219);

    private static final Color redCT = new Color(193, 39, 45);

    private static final Color orangeCT = new Color(247, 147, 30);

    private static final Color yellowCT = new Color(255, 255, 0);

    public static final Color[] COLORS = new Color[]{lightBlueCT, darkBlueCT, redCT, orangeCT, yellowCT};

    public FameUtil() {
        super();
    }

    public static int maxStars() {
        return ObjectLibrary.playerChars.size() * STARS.length;
    }

    public static int numStars(int param1) {
        int loc2 = 0;
        while ((loc2 < STARS.length) && (param1 >= STARS[loc2])) {
            loc2++;
        }
        return loc2;
    }

    public static int nextStarFame(int param1, int param2) {
        int loc3 = Math.max(param1, param2);
        int loc4 = 0;
        while (loc4 < STARS.length) {
            if (STARS[loc4] > loc3) {
                return STARS[loc4];
            }
            loc4++;
        }
        return -1;
    }

    public static int numAllTimeStars(int param1, int param2, XML param3) {
        int loc4 = 0;
        int loc5 = 0;
        for (XML loc6 : param3.children("ClassStats")) {
            if (param1 == loc6.getIntAttribute("objectType")) {
                loc5 = loc6.getIntValue("BestFame");
            } else {
                loc4 = loc4 + FameUtil.numStars(loc6.getIntValue("BestFame"));
            }
        }
        loc4 = loc4 + FameUtil.numStars(Math.max(loc5, param2));
        return loc4;
    }


    public static BitmapData getFameIcon() {
        return AssetLibrary.getImageFromSet("lofiObj3", 224);
    }

}
