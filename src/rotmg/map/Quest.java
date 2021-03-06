package rotmg.map;

import static flash.utils.timer.getTimer.getTimer;

import rotmg.objects.GameObject;


public class Quest {

    public AbstractMap map;

    public int objectId = -1;

    private int questAvailableAt = 0;

    private int questOldAt = 0;

    public Quest(AbstractMap param1) {
        super();
        this.map = param1;
    }

    public void setObject(int param1) {
        if ((this.objectId == -1) && (param1 != -1)) {
            this.questAvailableAt = getTimer() + 200;
            this.questOldAt = this.questAvailableAt;
        }
        this.objectId = param1;
    }

    public void completed() {
        this.questAvailableAt = getTimer() + 200;
        this.questOldAt = this.questAvailableAt;
    }

    public GameObject getObject(int param1) {
        if (param1 < this.questAvailableAt) {
            return null;
        }
        return AbstractMap.goDict.get(this.objectId);
    }

    public boolean isNew(int param1) {
        return param1 < this.questOldAt;
    }


}
