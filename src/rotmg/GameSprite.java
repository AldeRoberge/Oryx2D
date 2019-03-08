package rotmg;

import static flash.utils.timer.getTimer.getTimer;

import rotmg.core.model.MapModel;
import rotmg.core.model.PlayerModel;
import rotmg.core.service.GoogleAnalytics;
import rotmg.map.AbstractMap;
import rotmg.maploading.signals.HideMapLoadingSignal;
import rotmg.maploading.signals.MapLoadedSignal;
import rotmg.messaging.GameServerConnectionConcrete;
import rotmg.messaging.incoming.MapInfo;
import rotmg.net.Server;
import rotmg.objects.GameObject;
import rotmg.parameters.Parameters;
import rotmg.signals.ShowProTipSignal;
import utils.osflash.signals.Signal;

public class GameSprite extends AGameSprite {

    public boolean isNexus = false;

    public MapModel mapModel;

    public Signal showPackage;

    private GameObject focus;
    private int frameTimeSum = 0;
    private int frameTimeCount = 0;
    private boolean isGameStarted;
    private int displaysPosY = 4;
    private double packageY;
    private GoogleAnalytics googleAnalytics;

    public GameSprite(Server param1, int param2, boolean param3, int param4, int param5, byte[] param6, PlayerModel param7, byte[] param8, boolean param9) {
        super();
        this.showPackage = new Signal();
        this.model = param7;
        this.map = new AbstractMap(this);
        this.gsc = new GameServerConnectionConcrete(this, param1, param2, param3, param4, param5, param6, param8, param9);
        this.mui = new MapUserInput(this);
    }

    public static void dispatchMapLoaded(MapInfo param1) {
        MapLoadedSignal loc2 = MapLoadedSignal.getInstance();
        loc2.dispatch(param1);
    }

    private static void hidePreloader() {
        HideMapLoadingSignal loc1 = HideMapLoadingSignal.getInstance();
        loc1.dispatch();
    }

    @Override
    public void setFocus(GameObject param1) {
        param1 = this.map.player;
        this.focus = param1;
    }

    @Override
    public void applyMapInfo(MapInfo param1) {
        this.map.setProps(param1.width, param1.height, param1.name, param1.background, param1.allowPlayerTeleport, param1.showDisplays);
        dispatchMapLoaded(param1);
    }

    @Override
    public void initialize() {
        ShowProTipSignal loc4 = null;
        this.map.initialize();
        this.isNexus = this.map.name.equals(AbstractMap.NEXUS);
        Parameters.save();
        hidePreloader();
    }

    private boolean isPetMap() {
        return true;
    }

    public void connect() {
        if (!this.isGameStarted) {
            this.isGameStarted = true;
            this.gsc.connect();
            this.lastUpdate = getTimer();
        }
    }

    public void disconnect() {
        if (this.isGameStarted) {
            this.isGameStarted = false;
            this.map.dispose();
            this.gsc.disconnect();
        }
    }
    @Override
    public boolean evalIsNotInCombatMapArea() {
        return this.map.name.equals(AbstractMap.NEXUS) || this.map.name.equals(AbstractMap.VAULT) || this.map.name.equals(AbstractMap.GUILD_HALL) || this.map.name.equals(AbstractMap.CLOTH_BAZAAR) || this.map.name.equals(AbstractMap.NEXUS_EXPLANATION) || this.map.name.equals(AbstractMap.DAILY_QUEST_ROOM);
    }

}
