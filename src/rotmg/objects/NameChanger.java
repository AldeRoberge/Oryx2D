package rotmg.objects;

import flash.XML;

public class NameChanger extends GameObject implements IInteractiveObject {

	public int rankRequired = 0;

	public NameChanger(XML param1) {
		super(param1);
		this.isInteractive = true;
	}

	public void setRankRequired(int param1) {
		this.rankRequired = param1;
	}

}
