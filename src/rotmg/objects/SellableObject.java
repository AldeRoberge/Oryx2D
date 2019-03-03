package rotmg.objects;

import utils.flash.XML;
import utils.flash.display.BitmapData;

public class SellableObject extends GameObject {


    public int price = 0;
    public int currency = -1;
    public int rankReq = 0;
    public int guildRankReq = -1;

    public SellableObject(XML objectXML) {
        super(objectXML);
        this.isInteractive = true;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public void setRankReq(int rankReq) {
        this.rankReq = rankReq;
    }

    public String soldObjectName() {
        return null;
    }

    public String soldObjectInternalName() {
        return null;
    }

    public BitmapData getIcon() {
        return null;
    }
}
