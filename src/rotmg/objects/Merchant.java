package rotmg.objects;

import flash.LineBuilder;
import flash.StringMap;
import flash.Vector;
import flash.XML;
import flash.display.BitmapData;
import flash.geom.ColorTransform;
import rotmg.map.AbstractMap;
import rotmg.model.AddSpeechBalloonVO;
import rotmg.signals.AddSpeechBalloonSignal;
import rotmg.util.IntPoint;

/**
 * Almost a 100% match, except that it's abstract because it doesnt have the getPanel() method
 */
public class Merchant extends SellableObject {

    private static final int NONE_MESSAGE = 0;

    private static final int NEW_MESSAGE = 1;

    private static final int MINS_LEFT_MESSAGE = 2;

    private static final int ITEMS_LEFT_MESSAGE = 3;

    private static final int DISCOUNT_MESSAGE = 4;

    private static final double T = 1;

    public int merchandiseType = -1;

    public int count = -1;

    public int minsLeft = -1;

    public int discount = 0;

    public BitmapData merchandiseTexture = null;

    public int untilNextMessage = 0;

    public double alpha = 1.0;

    private AddSpeechBalloonSignal addSpeechBalloon;

    private StringMap stringMap;

    private boolean firstUpdate = true;

    private int messageIndex = 0;

    private ColorTransform ct;

    public Merchant(XML param1) {
        super(param1);
        this.ct = new ColorTransform(1, 1, 1, 1);
        this.addSpeechBalloon = AddSpeechBalloonSignal.getInstance();
        this.stringMap = StringMap.getInstance();
        this.isInteractive = true;
    }

    @Override
    public void setPrice(int param1) {
        super.setPrice(param1);
        this.untilNextMessage = 0;
    }

    @Override
    public void setRankReq(int param1) {
        super.setRankReq(param1);
        this.untilNextMessage = 0;
    }

    @Override
    public boolean addTo(AbstractMap param1, double param2, double param3) {
        if (super.addTo(param1, param2, param3)) {
            return true;
        }
        param1.merchLookup.put(new IntPoint((int) this.x, (int) this.y), this);
        return false;
    }

    @Override
    public void removeFromMap() {
        IntPoint loc1 = new IntPoint((int) this.x, (int) this.y);
        if (this.map.merchLookup.get(loc1) == this) {
            this.map.merchLookup.put(loc1, null);
        }
        super.removeFromMap();
    }

    public AddSpeechBalloonVO getSpeechBalloon(int param1) {
        LineBuilder loc2 = null;
        int loc3 = 0;
        int loc4 = 0;
        int loc5 = 0;
        switch (param1) {
            case NEW_MESSAGE:
                loc2 = new LineBuilder().setParams("Merchant.new");
                loc3 = 15132390;
                loc4 = 16777215;
                loc5 = 5931045;
                break;
            case MINS_LEFT_MESSAGE:
                if (this.minsLeft == 0) {
                    loc2 = new LineBuilder().setParams("Merchant.goingSoon");
                } else if (this.minsLeft == 1) {
                    loc2 = new LineBuilder().setParams("Merchant.goingInOneMinute");
                } else {
                    loc2 = new LineBuilder().setParams("Merchant.goingInNMinutes", "{\"minutes\":this.minsLeft}");

                }
                loc3 = 5973542;
                loc4 = 16549442;
                loc5 = 16549442;
                break;
            case ITEMS_LEFT_MESSAGE:
                loc2 = new LineBuilder().setParams("Merchant.limitedStock", " {\"count\":this.count}");
                loc3 = 5973542;
                loc4 = 16549442;
                loc5 = 16549442;
                break;
            case DISCOUNT_MESSAGE:
                loc2 = new LineBuilder().setParams("Merchant.discount", "{\"discount\":this.discount}");
                loc3 = 6324275;
                loc4 = 16777103;
                loc5 = 16777103;
                break;
            default:
                return null;
        }
        loc2.setStringMap(this.stringMap);
        return new AddSpeechBalloonVO(this, loc2.getString(), "", false, false, loc3, 1, loc4, 1, loc5, 6, true, false);
    }

    @Override
    public boolean update(int time, int dt) {

        super.update(time, dt);


        this.untilNextMessage = this.untilNextMessage - dt;
        if (this.untilNextMessage > 0) {
            return false;
        }
        this.untilNextMessage = 5000;
        Vector<Integer> loc3 = new Vector<>();
        if (this.minsLeft == 2147483647) {
            loc3.add(NEW_MESSAGE);
        } else if ((this.minsLeft >= 0) && (this.minsLeft <= 5)) {
            loc3.add(MINS_LEFT_MESSAGE);
        }
        if ((this.count >= 1) && (this.count <= 2)) {
            loc3.add(ITEMS_LEFT_MESSAGE);
        }
        if (this.discount > 0) {
            loc3.add(DISCOUNT_MESSAGE);
        }
        if (loc3.length == 0) {
            return false;
        }
        this.messageIndex = ++this.messageIndex % loc3.length;
        int loc4 = loc3.get(this.messageIndex);
        this.addSpeechBalloon.dispatch(this.getSpeechBalloon(loc4));
        return false;
    }

    @Override
    public String soldObjectName() {
        return ObjectLibrary.typeToDisplayId.get(this.merchandiseType);
    }

    @Override
    public String soldObjectInternalName() {
        XML loc1 = ObjectLibrary.xmlLibrary.get(this.merchandiseType);
        return String.valueOf(loc1.getIntAttribute("id"));
    }

    public int getSellableType() {
        return this.merchandiseType;
    }

    @Override
    public BitmapData getIcon() {
        /**BaseSimpleText loc3 = null;
         BaseSimpleText loc4 = null;
         BitmapData loc1 = ObjectLibrary.getRedrawnTextureFromType(this.merchandiseType, 80, true);
         XML loc2 = ObjectLibrary.xmlLibrary.get(this.merchandiseType);
         if (loc2.hasOwnProperty("Doses")) {
         loc1 = loc1.clone();
         loc3 = new BaseSimpleText(12, 16777215, false, 0, 0);
         loc3.text = String(loc2.Doses);
         loc3.updateMetrics();
         loc1.draw(loc3, DOSE_MATRIX);
         }
         if (loc2.hasOwnProperty("Quantity")) {
         loc1 = loc1.clone();
         loc4 = new BaseSimpleText(12, 16777215, false, 0, 0);
         loc4.text = String(loc2.Quantity);
         loc4.updateMetrics();
         loc1.draw(loc4, DOSE_MATRIX);
         }
         return loc1;*/
        return null;
    }

    public int getTex1Id(int param1) {
        XML loc2 = ObjectLibrary.xmlLibrary.get(this.merchandiseType);
        if (loc2 == null) {
            return param1;
        }
        if (loc2.getValue("Activate").equals("Dye") && loc2.hasOwnProperty("Tex1")) {
            return loc2.getIntValue("Tex1");
        }
        return param1;
    }

    public int getTex2Id(int param1) {
        XML loc2 = ObjectLibrary.xmlLibrary.get(this.merchandiseType);
        if (loc2 == null) {
            return param1;
        }
        if (loc2.getValue("Activate").equals("Dye") && loc2.hasOwnProperty("Tex2")) {
            return loc2.getIntValue("Tex2");
        }
        return param1;
    }

    protected BitmapData getTexture(int param2) {
        if ((this.alpha == 1) && (this.size == 100)) {
            return this.merchandiseTexture;
        }
        BitmapData loc3 = ObjectLibrary.getRedrawnTextureFromType(this.merchandiseType, this.size, false, false);
        if (this.alpha != 1) {
            this.ct.alphaMultiplier = (int) this.alpha;
        }
        return loc3;
    }

    public void setMerchandiseType(int param1) {
        this.merchandiseType = param1;
        //this.merchandiseTexture = ObjectLibrary.getRedrawnTextureFromType(this.merchandiseType, 100, false);
    }

}
