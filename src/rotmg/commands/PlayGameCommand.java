package rotmg.commands;

import flash.utils.Date;
import rotmg.GameSprite;
import rotmg.account.core.services.GetCharListTask;
import rotmg.appengine.SavedCharacter;
import rotmg.core.model.PlayerModel;
import rotmg.lib.tasks.impl.SocketServerModel;
import rotmg.lib.tasks.tasks.TaskMonitor;
import rotmg.model.GameInitData;
import rotmg.net.Server;
import rotmg.parameters.Parameters;
import rotmg.parameters.Parameters.Data;
import rotmg.pets.data.PetsModel;
import rotmg.servers.api.ServerModel;

public class PlayGameCommand {

    public static final int RECONNECT_DELAY = 2000;

    public GameInitData data;

    public PlayerModel model;

    public PetsModel petsModel;

    public ServerModel servers;

    public GetCharListTask task;

    public TaskMonitor monitor;

    public SocketServerModel socketServerModel;

    public PlayGameCommand() {
        super();
        this.data = GameInitData.getInstance();
        this.model = PlayerModel.getInstance();
        this.petsModel = PetsModel.getInstance();
        this.servers = ServerModel.getInstance();
        this.task = GetCharListTask.getInstance();
        this.monitor = TaskMonitor.getInstance();
    }

    public void execute() {
        if (!this.data.isNewGame) {
            this.socketServerModel.connectDelayMS = PlayGameCommand.RECONNECT_DELAY;
        }
        this.recordCharacterUseInSharedObject();
        this.makeGameView();
        this.updatePet();
    }

    private void updatePet() {
        SavedCharacter loc1 = this.model.getCharacterById(this.model.currentCharId);
        if (loc1 != null) {
            this.petsModel.setActivePet(loc1.getPetVO());
        } else {
            if ((this.model.currentCharId != 0) && (this.petsModel.getActivePet() != null) && !this.data.isNewGame) {
                return;
            }
            this.petsModel.setActivePet(null);
        }
    }

    private void recordCharacterUseInSharedObject() {
        Data.charIdUseMap.put(this.data.charId, new Date().getTime());
        Parameters.save();
    }

    public void makeGameView() {

        Server loc1;

        if (this.data.server != null) {
            loc1 = this.data.server;
        } else {
            loc1 = this.servers.getServer();
        }

        int loc2 = this.data.isNewGame ? this.getInitialGameId() : this.data.gameId;
        boolean loc3 = this.data.createCharacter;
        int loc4 = this.data.charId;
        int loc5 = this.data.isNewGame ? -1 : this.data.keyTime;
        byte[] loc6 = this.data.key;
        this.model.currentCharId = loc4;
        new GameSprite(loc1, loc2, loc3, loc4, loc5, loc6, this.model, null, this.data.isFromArena);
    }

    private int getInitialGameId() {
        int loc1 = 0;
        if (Data.needsTutorial) {
            loc1 = Parameters.TUTORIAL_GAMEID;
        } else if (Data.needsRandomRealm) {
            loc1 = Parameters.RANDOM_REALM_GAMEID;
        } else {
            loc1 = Parameters.NEXUS_GAMEID;
        }
        return loc1;
    }

}
