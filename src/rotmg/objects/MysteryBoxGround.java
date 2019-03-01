package rotmg.objects;

import alde.flash.utils.XML;
import rotmg.GameSprite;
import rotmg.ui.panels.Panel;
import rotmg.view.MysteryBoxPanel;

public class MysteryBoxGround extends GameObject implements IInteractiveObject {

	public MysteryBoxGround(XML param1) {
		super(param1);
		this.isInteractive = true;
	}

	@Override
	public Panel getPanel(GameSprite param1) {
		return new MysteryBoxPanel(param1, this.objectType);
	}
}
