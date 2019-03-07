package rotmg.objects;

import flash.XML;
import rotmg.GameSprite;
import rotmg.map.AbstractMap;
import rotmg.sound.SoundEffectLibrary;
import rotmg.util.PointUtil;

public class Container extends GameObject implements IInteractiveObject {

    public boolean isLoot;

    public boolean canHaveSoulbound;

    public String ownerId;

    public Container(XML param1) {
        super(param1);
        this.isInteractive = true;
        this.isLoot = param1.hasOwnProperty("Loot");
        this.canHaveSoulbound = param1.hasOwnProperty("CanPutSoulboundObjects");
        this.ownerId = "";
    }

    public void setOwnerId(String param1) {
        this.ownerId = param1;
        this.isInteractive = this.ownerId.equals("") || this.isBoundToCurrentAccount();
    }

    public boolean isBoundToCurrentAccount() {
        return this.map.player.accountId.equals(this.ownerId);
    }

    @Override
    public boolean addTo(AbstractMap param1, double param2, double param3) {
        if (!super.addTo(param1, param2, param3)) {
            return false;
        }
        if (this.map.player == null) {
            return true;
        }
        double loc4 = PointUtil.distanceXY(this.map.player.x, this.map.player.y, param2, param3);
        if (this.isLoot && (loc4 < 10)) {
            SoundEffectLibrary.play("loot_appears");
        }
        return true;
    }

    @Override
    public rotmg.ui.panels.Panel getPanel(GameSprite param1) {
		/*Player loc2 = param1 != null && param1.map ? param1.map.player_ : null;
		ContainerGrid loc3 = new ContainerGrid(this, loc2);
		return loc3;*/

        return null;
    }


}
