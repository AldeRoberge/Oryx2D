package rotmg.objects;

import flash.XML;
import rotmg.objects.animation.AnimatedChar;
import rotmg.pets.data.PetVO;
import rotmg.pets.data.PetsModel;
import rotmg.signals.TextPanelMessageUpdateSignal;
import rotmg.util.AnimatedChars;
import rotmg.util.MaskedImage;

public class Pet extends GameObject implements IInteractiveObject {

    public PetVO vo;
    public AnimatedChar skin;
    public AnimatedChar defaultSkin;
    public int skinId;
    public boolean isDefaultAnimatedChar = false;
    private TextPanelMessageUpdateSignal textPanelUpdateSignal;
    private PetsModel petsModel;

    public Pet(XML param1) {
        super(param1);
        this.isInteractive = true;
        this.textPanelUpdateSignal = TextPanelMessageUpdateSignal.getInstance();
        this.petsModel = PetsModel.getInstance();
        this.petsModel.getActivePet();
    }

	/*public ToolTip getTooltip() {
		ToolTip loc1 = new TextToolTip(3552822, 10197915, TextKey.CLOSEDGIFTCHEST_TITLE, TextKey.TEXTPANEL_GIFTCHESTISEMPTY, 200);
		return loc1;
	}*/



    public void setSkin(int param1) {
        MaskedImage loc5 = null;
        this.skinId = param1;
        XML loc2 = ObjectLibrary.getXMLfromId(ObjectLibrary.getIdFromType(param1));
        String loc3 = loc2.child("AnimatedTexture").getValue("File");
        int loc4 = loc2.child("AnimatedTexture").getIntValue("Index");
        if (this.skin == null) {
            this.isDefaultAnimatedChar = true;
            this.skin = AnimatedChars.getAnimatedChar(loc3, loc4);
            this.defaultSkin = this.skin;
        } else {
            this.skin = AnimatedChars.getAnimatedChar(loc3, loc4);
        }
        this.isDefaultAnimatedChar = this.skin == this.defaultSkin;
        loc5 = this.skin.imageFromAngle(0, AnimatedChar.STAND, 0);
        this.animatedChar = this.skin;
        this.texture = loc5.image;
        this.mask = loc5.mask;
    }

    public void setDefaultSkin() {
        MaskedImage loc1 = null;
        this.skinId = -1;
        if (this.defaultSkin == null) {
            return;
        }
        loc1 = this.defaultSkin.imageFromAngle(0, AnimatedChar.STAND, 0);
        this.isDefaultAnimatedChar = true;
        this.animatedChar = this.defaultSkin;
        this.texture = loc1.image;
        this.mask = loc1.mask;
    }


}
