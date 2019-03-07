package rotmg;

import rotmg.core.model.PlayerModel;
import rotmg.map.AbstractMap;
import rotmg.messaging.GameServerConnection;
import rotmg.messaging.incoming.MapInfo;
import rotmg.objects.GameObject;
import utils.osflash.signals.Signal;

/**
 * Implemented by GameSprite
 */
public class AGameSprite {

    public final Signal closed = new Signal();

    public boolean isEditor;

    public MapUserInput mui;

    public int lastUpdate;

    public MoveRecords moveRecords;

    public AbstractMap map;

    public PlayerModel model;

    public GameServerConnection gsc;

    public AGameSprite() {
        this.moveRecords = new MoveRecords();
    }

    public void initialize() {
    }

    public void setFocus(GameObject param1) {
    }

    public void applyMapInfo(MapInfo param1) {
    }

    public boolean evalIsNotInCombatMapArea() {
        return false;
    }

}
