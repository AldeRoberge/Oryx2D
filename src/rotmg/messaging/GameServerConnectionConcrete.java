package rotmg.messaging;

import static flash.utils.timer.getTimer.getTimer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flash.RSA;
import flash.XML;
import flash.consumer.EventConsumer;
import flash.consumer.MessageConsumer;
import flash.events.Event;
import flash.events.TimerEvent;
import flash.utils.timer.Timer;
import rotmg.AGameSprite;
import rotmg.account.core.WebAccount;
import rotmg.chat.control.TextHandler;
import rotmg.chat.model.ChatMessage;
import rotmg.classes.model.CharacterClass;
import rotmg.classes.model.ClassesModel;
import rotmg.constants.GeneralConstants;
import rotmg.constants.ItemConstants;
import rotmg.events.KeyInfoResponseSignal;
import rotmg.events.ReconnectEvent;
import rotmg.focus.control.SetGameFocusSignal;
import rotmg.focus.control.UpdateGroundTileSignal;
import rotmg.map.AbstractMap;
import rotmg.map.GroundLibrary;
import rotmg.maploading.signals.ChangeMapSignal;
import rotmg.messaging.data.GroundTileData;
import rotmg.messaging.data.ObjectData;
import rotmg.messaging.data.ObjectStatusData;
import rotmg.messaging.data.StatData;
import rotmg.messaging.impl.ActivePet;
import rotmg.messaging.impl.HatchPetMessage;
import rotmg.messaging.impl.PetUpgradeRequest;
import rotmg.messaging.impl.PetYard;
import rotmg.messaging.impl.ReskinPet;
import rotmg.messaging.incoming.AccountList;
import rotmg.messaging.incoming.AllyShoot;
import rotmg.messaging.incoming.Aoe;
import rotmg.messaging.incoming.BuyResult;
import rotmg.messaging.incoming.ClientStat;
import rotmg.messaging.incoming.CreateSuccess;
import rotmg.messaging.incoming.Damage;
import rotmg.messaging.incoming.Death;
import rotmg.messaging.incoming.EnemyShoot;
import rotmg.messaging.incoming.EvolvedPetMessage;
import rotmg.messaging.incoming.Failure;
import rotmg.messaging.incoming.File;
import rotmg.messaging.incoming.GlobalNotification;
import rotmg.messaging.incoming.Goto;
import rotmg.messaging.incoming.GuildResult;
import rotmg.messaging.incoming.InvResult;
import rotmg.messaging.incoming.InvitedToGuild;
import rotmg.messaging.incoming.MapInfo;
import rotmg.messaging.incoming.NameResult;
import rotmg.messaging.incoming.NewAbilityMessage;
import rotmg.messaging.incoming.NewTick;
import rotmg.messaging.incoming.Notification;
import rotmg.messaging.incoming.Ping;
import rotmg.messaging.incoming.PlaySound;
import rotmg.messaging.incoming.QuestObjId;
import rotmg.messaging.incoming.Reconnect;
import rotmg.messaging.incoming.ServerPlayerShoot;
import rotmg.messaging.incoming.ShowEffect;
import rotmg.messaging.incoming.Text;
import rotmg.messaging.incoming.TradeAccepted;
import rotmg.messaging.incoming.TradeChanged;
import rotmg.messaging.incoming.TradeDone;
import rotmg.messaging.incoming.TradeRequested;
import rotmg.messaging.incoming.TradeStart;
import rotmg.messaging.incoming.Update;
import rotmg.messaging.incoming.pets.DeletePetMessage;
import rotmg.messaging.outgoing.AcceptTrade;
import rotmg.messaging.outgoing.ActivePetUpdateRequest;
import rotmg.messaging.outgoing.AoeAck;
import rotmg.messaging.outgoing.Buy;
import rotmg.messaging.outgoing.CancelTrade;
import rotmg.messaging.outgoing.ChangeGuildRank;
import rotmg.messaging.outgoing.ChangeTrade;
import rotmg.messaging.outgoing.CheckCredits;
import rotmg.messaging.outgoing.ChooseName;
import rotmg.messaging.outgoing.ClaimDailyRewardMessage;
import rotmg.messaging.outgoing.Create;
import rotmg.messaging.outgoing.CreateGuild;
import rotmg.messaging.outgoing.EditAccountList;
import rotmg.messaging.outgoing.EnemyHit;
import rotmg.messaging.outgoing.Escape;
import rotmg.messaging.outgoing.GoToQuestRoom;
import rotmg.messaging.outgoing.GotoAck;
import rotmg.messaging.outgoing.GroundDamage;
import rotmg.messaging.outgoing.GuildInvite;
import rotmg.messaging.outgoing.GuildRemove;
import rotmg.messaging.outgoing.Hello;
import rotmg.messaging.outgoing.InvDrop;
import rotmg.messaging.outgoing.InvSwap;
import rotmg.messaging.outgoing.JoinGuild;
import rotmg.messaging.outgoing.KeyInfoRequest;
import rotmg.messaging.outgoing.Load;
import rotmg.messaging.outgoing.Move;
import rotmg.messaging.outgoing.OtherHit;
import rotmg.messaging.outgoing.OutgoingMessage;
import rotmg.messaging.outgoing.PlayerHit;
import rotmg.messaging.outgoing.PlayerShoot;
import rotmg.messaging.outgoing.PlayerText;
import rotmg.messaging.outgoing.Pong;
import rotmg.messaging.outgoing.RequestTrade;
import rotmg.messaging.outgoing.Reskin;
import rotmg.messaging.outgoing.SetCondition;
import rotmg.messaging.outgoing.ShootAck;
import rotmg.messaging.outgoing.SquareHit;
import rotmg.messaging.outgoing.Teleport;
import rotmg.messaging.outgoing.UseItem;
import rotmg.messaging.outgoing.UsePortal;
import rotmg.messaging.outgoing.arena.EnterArena;
import rotmg.messaging.outgoing.arena.QuestRedeem;
import rotmg.model.GameModel;
import rotmg.model.PotionInventoryModel;
import rotmg.net.Server;
import rotmg.net.SocketServer;
import rotmg.net.impl.Message;
import rotmg.net.impl.MessageCenter;
import rotmg.objects.GameObject;
import rotmg.objects.Merchant;
import rotmg.objects.ObjectLibrary;
import rotmg.objects.Player;
import rotmg.objects.Projectile;
import rotmg.objects.SellableObject;
import rotmg.parameters.Parameters;
import rotmg.pets.controller.PetFeedResultSignal;
import rotmg.pets.controller.UpdateActivePet;
import rotmg.pets.data.PetsModel;
import rotmg.signals.AddSpeechBalloonSignal;
import rotmg.signals.AddTextLineSignal;
import rotmg.signals.GiftStatusUpdateSignal;
import rotmg.sound.SoundEffectLibrary;
import rotmg.util.ConditionEffect;
import rotmg.util.Currency;
import rotmg.util.TextKey;
import utils.ConversionUtil;
import utils.symmetric.ICipher;

public class GameServerConnectionConcrete extends GameServerConnection {

    Logger log = LoggerFactory.getLogger(GameServerConnectionConcrete.class);

    private static final int TO_MILLISECONDS = 1000;
    //private PetUpdater petUpdater;
    private MessageCenter messages;
    private int playerId = -1;
    private Player player;
    private boolean retryConnection = true;
    private GiftStatusUpdateSignal giftChestUpdateSignal;
    private Death death;
    private Timer retryTimer;
    private int delayBeforeReconnect = 2;
    private AddTextLineSignal addTextLine;
    private AddSpeechBalloonSignal addSpeechBalloon;
    private UpdateGroundTileSignal updateGroundTileSignal;
    private SetGameFocusSignal setGameFocus;
    private PetFeedResultSignal petFeedResult;
    private KeyInfoResponseSignal keyInfoResponse;
    private ClassesModel classesModel;
    private GameModel model;
    private UpdateActivePet updateActivePet;
    private PetsModel petsModel;

    private long startTime = 0; // this is currently not set
    private int delayBeforeReconect;


    public GameServerConnectionConcrete(AGameSprite gs, Server server, int gameId, boolean createCharacter, int charId,
                                        int keyTime, byte[] key, byte[] mapJSON, boolean isFromArena) {
        super();

        System.out.println("GameSprite : " + gs);

        this.giftChestUpdateSignal = GiftStatusUpdateSignal.getInstance();
        this.addTextLine = AddTextLineSignal.getInstance();
        this.addSpeechBalloon = AddSpeechBalloonSignal.getInstance();
        this.updateGroundTileSignal = UpdateGroundTileSignal.getInstance();
        this.petFeedResult = PetFeedResultSignal.getInstance();
        this.changeMapSignal = ChangeMapSignal.getInstance();
        this.keyInfoResponse = KeyInfoResponseSignal.getInstance();
        this.setGameFocus = SetGameFocusSignal.getInstance();
        this.classesModel = ClassesModel.getInstance();
        this.serverConnection = SocketServer.getInstance();
        this.messages = MessageCenter.getInstance();
        this.model = GameModel.getInstance();
        /*this.currentArenaRun = CurrentArenaRunModel.getInstance();*/
        this.gs = gs;
        this.server = server;
        this.gameId = gameId;
        this.createCharacter = createCharacter;
        this.charId = charId;
        this.keyTime = keyTime;
        this.key = key;
        this.mapJSON = mapJSON;
        this.isFromArena = isFromArena;/*this.friendModel.setCurrentServer(server);
										this.getPetUpdater();*/
        instance = this;
    }

    private static boolean isStatPotion(int param1) {
        return (param1 == 2591) || (param1 == 5465) || (param1 == 9064)
                || ((param1 == 2592) || (param1 == 5466) || (param1 == 9065))
                || ((param1 == 2593) || (param1 == 5467) || (param1 == 9066))
                || ((param1 == 2612) || (param1 == 5468) || (param1 == 9067))
                || ((param1 == 2613) || (param1 == 5469) || (param1 == 9068))
                || ((param1 == 2636) || (param1 == 5470) || (param1 == 9069))
                || ((param1 == 2793) || (param1 == 5471) || (param1 == 9070))
                || ((param1 == 2794) || (param1 == 5472) || (param1 == 9071))
                || ((param1 == 9724) || (param1 == 9725) || (param1 == 9726) || (param1 == 9727) || (param1 == 9728)
                || (param1 == 9729) || (param1 == 9730) || (param1 == 9731));
    }

    //private function getPetUpdater()

    //public void disconnect()

    // removeServerConnectionListeners()

    @Override
    public void connect() {
        //this.addServerConnectionListeners();
        this.mapMessages();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.name = Parameters.CLIENT_CHAT_NAME;
        chatMessage.text = TextKey.CHAT_CONNECTING_TO;
        String loc2 = this.server.name;

        System.out.println("Connecting to " + this.server.name + ".");

        this.serverConnection.connect(this.server.address, this.server.port);

        this.onConnected();
    }

    /**
     * private void addServerConnectionListeners() {
     * serverConnection.connected.add(this::onConnected);
     * serverConnection.closed.add(this::onClosed);
     * serverConnection.error.add(this::onError);
     * }
     */

    public void mapMessages() {
        MessageCenter loc1 = MessageCenter.getInstance();
        loc1.map(CREATE).toMessage(Create.class);
        loc1.map(PLAYERSHOOT).toMessage(PlayerShoot.class);
        loc1.map(MOVE).toMessage(Move.class);
        loc1.map(PLAYERTEXT).toMessage(PlayerText.class);
        loc1.map(UPDATEACK).toMessage(Message.class);
        loc1.map(INVSWAP).toMessage(InvSwap.class);
        loc1.map(USEITEM).toMessage(UseItem.class);
        loc1.map(HELLO).toMessage(Hello.class);
        loc1.map(INVDROP).toMessage(InvDrop.class);
        loc1.map(PONG).toMessage(Pong.class);
        loc1.map(LOAD).toMessage(Load.class);
        loc1.map(SETCONDITION).toMessage(SetCondition.class);
        loc1.map(TELEPORT).toMessage(Teleport.class);
        loc1.map(USEPORTAL).toMessage(UsePortal.class);
        loc1.map(BUY).toMessage(Buy.class);
        loc1.map(PLAYERHIT).toMessage(PlayerHit.class);
        loc1.map(ENEMYHIT).toMessage(EnemyHit.class);
        loc1.map(AOEACK).toMessage(AoeAck.class);
        loc1.map(SHOOTACK).toMessage(ShootAck.class);
        loc1.map(OTHERHIT).toMessage(OtherHit.class);
        loc1.map(SQUAREHIT).toMessage(SquareHit.class);
        loc1.map(GOTOACK).toMessage(GotoAck.class);
        loc1.map(GROUNDDAMAGE).toMessage(GroundDamage.class);
        loc1.map(CHOOSENAME).toMessage(ChooseName.class);
        loc1.map(CREATEGUILD).toMessage(CreateGuild.class);
        loc1.map(GUILDREMOVE).toMessage(GuildRemove.class);
        loc1.map(GUILDINVITE).toMessage(GuildInvite.class);
        loc1.map(REQUESTTRADE).toMessage(RequestTrade.class);
        loc1.map(CHANGETRADE).toMessage(ChangeTrade.class);
        loc1.map(ACCEPTTRADE).toMessage(AcceptTrade.class);
        loc1.map(CANCELTRADE).toMessage(CancelTrade.class);
        loc1.map(CHECKCREDITS).toMessage(CheckCredits.class);
        loc1.map(ESCAPE).toMessage(Escape.class);
        loc1.map(QUEST_ROOM_MSG).toMessage(GoToQuestRoom.class);
        loc1.map(JOINGUILD).toMessage(JoinGuild.class);
        loc1.map(CHANGEGUILDRANK).toMessage(ChangeGuildRank.class);
        loc1.map(EDITACCOUNTLIST).toMessage(EditAccountList.class);
        loc1.map(ACTIVE_PET_UPDATE_REQUEST).toMessage(ActivePetUpdateRequest.class);
        loc1.map(PETUPGRADEREQUEST).toMessage(PetUpgradeRequest.class);
        loc1.map(ENTER_ARENA).toMessage(EnterArena.class);
        loc1.map(ACCEPT_ARENA_DEATH).toMessage(OutgoingMessage.class);
        loc1.map(QUEST_FETCH_ASK).toMessage(OutgoingMessage.class);
        loc1.map(QUEST_REDEEM).toMessage(QuestRedeem.class);
        loc1.map(KEY_INFO_REQUEST).toMessage(KeyInfoRequest.class);
        loc1.map(PET_CHANGE_FORM_MSG).toMessage(ReskinPet.class);
        loc1.map(CLAIM_LOGIN_REWARD_MSG).toMessage(ClaimDailyRewardMessage.class);
        loc1.map(FAILURE).toMessage(Failure.class).toMethod(new MessageConsumer<>(this::onFailure));
        loc1.map(CREATE_SUCCESS).toMessage(CreateSuccess.class).toMethod(new MessageConsumer<>(this::onCreateSuccess));
        loc1.map(SERVERPLAYERSHOOT).toMessage(ServerPlayerShoot.class)
                .toMethod(new MessageConsumer<>(this::onServerPlayerShoot));
        loc1.map(DAMAGE).toMessage(Damage.class).toMethod(new MessageConsumer<>(this::onDamage));
        loc1.map(UPDATE).toMessage(Update.class).toMethod(new MessageConsumer<>(this::onUpdate));
        loc1.map(NOTIFICATION).toMessage(Notification.class).toMethod(new MessageConsumer<>(this::onNotification));
        loc1.map(GLOBAL_NOTIFICATION).toMessage(GlobalNotification.class)
                .toMethod(new MessageConsumer<>(this::onGlobalNotification));
        loc1.map(NEWTICK).toMessage(NewTick.class).toMethod(new MessageConsumer<>(this::onNewTick));
        loc1.map(SHOWEFFECT).toMessage(ShowEffect.class).toMethod(new MessageConsumer<>(this::onShowEffect));
        loc1.map(GOTO).toMessage(Goto.class).toMethod(new MessageConsumer<>(this::onGoto));
        loc1.map(INVRESULT).toMessage(InvResult.class).toMethod(new MessageConsumer<>(this::onInvResult));
        loc1.map(RECONNECT).toMessage(Reconnect.class).toMethod(new MessageConsumer<>(this::onReconnect));
        loc1.map(PING).toMessage(Ping.class).toMethod(new MessageConsumer<>(this::onPing));
        loc1.map(MAPINFO).toMessage(MapInfo.class).toMethod(new MessageConsumer<>(this::onMapInfo));
        //_loc1.map(PIC).toMessage(Pic.class).toMethod(new MessageConsumer<>(this::onPic));
        loc1.map(DEATH).toMessage(Death.class).toMethod(new MessageConsumer<>(this::onDeath));
        loc1.map(BUYRESULT).toMessage(BuyResult.class).toMethod(new MessageConsumer<>(this::onBuyResult));
        loc1.map(AOE).toMessage(Aoe.class).toMethod(new MessageConsumer<>(this::onAoe));
        loc1.map(ACCOUNTLIST).toMessage(AccountList.class).toMethod(new MessageConsumer<>(this::onAccountList));
        loc1.map(QUESTOBJID).toMessage(QuestObjId.class).toMethod(new MessageConsumer<>(this::onQuestObjId));
        loc1.map(NAMERESULT).toMessage(NameResult.class).toMethod(new MessageConsumer<>(this::onNameResult));
        loc1.map(GUILDRESULT).toMessage(GuildResult.class).toMethod(new MessageConsumer<>(this::onGuildResult));
        loc1.map(ALLYSHOOT).toMessage(AllyShoot.class).toMethod(new MessageConsumer<>(this::onAllyShoot));
        loc1.map(ENEMYSHOOT).toMessage(EnemyShoot.class).toMethod(new MessageConsumer<>(this::onEnemyShoot));
        loc1.map(TRADEREQUESTED).toMessage(TradeRequested.class)
                .toMethod(new MessageConsumer<>(this::onTradeRequested));
        loc1.map(TRADESTART).toMessage(TradeStart.class).toMethod(new MessageConsumer<>(this::onTradeStart));
        loc1.map(TRADECHANGED).toMessage(TradeChanged.class).toMethod(new MessageConsumer<>(this::onTradeChanged));
        loc1.map(TRADEDONE).toMessage(TradeDone.class).toMethod(new MessageConsumer<>(this::onTradeDone));
        loc1.map(TRADEACCEPTED).toMessage(TradeAccepted.class).toMethod(new MessageConsumer<>(this::onTradeAccepted));
        loc1.map(CLIENTSTAT).toMessage(ClientStat.class).toMethod(new MessageConsumer<>(this::onClientStat));
        loc1.map(FILE).toMessage(File.class).toMethod(new MessageConsumer<>(this::onFile));
        loc1.map(INVITEDTOGUILD).toMessage(InvitedToGuild.class)
                .toMethod(new MessageConsumer<>(this::onInvitedToGuild));
        loc1.map(PLAYSOUND).toMessage(PlaySound.class).toMethod(new MessageConsumer<>(this::onPlaySound));
        loc1.map(ACTIVEPETUPDATE).toMessage(ActivePet.class).toMethod(new MessageConsumer<>(this::onActivePetUpdate));
        loc1.map(NEW_ABILITY).toMessage(NewAbilityMessage.class).toMethod(new MessageConsumer<>(this::onNewAbility));
        loc1.map(PETYARDUPDATE).toMessage(PetYard.class).toMethod(new MessageConsumer<>(this::onPetYardUpdate));
        loc1.map(EVOLVE_PET).toMessage(EvolvedPetMessage.class).toMethod(new MessageConsumer<>(this::onEvolvedPet));
        loc1.map(DELETE_PET).toMessage(DeletePetMessage.class).toMethod(new MessageConsumer<>(this::onDeletePet));
        loc1.map(HATCH_PET).toMessage(HatchPetMessage.class).toMethod(new MessageConsumer<>(this::onHatchPet));
		/*_loc1.map(IMMINENT_ARENA_WAVE).toMessage(ImminentArenaWave.class).toMethod(new MessageConsumer<>(this::onImminentArenaWave));
		_loc1.map(ARENA_DEATH).toMessage(ArenaDeath.class).toMethod(new MessageConsumer<>(this::onArenaDeath));
		_loc1.map(VERIFY_EMAIL).toMessage(VerifyEmail.class).toMethod(new MessageConsumer<>(this::onVerifyEmail));
		_loc1.map(RESKIN_UNLOCK).toMessage(ReskinUnlock.class).toMethod(new MessageConsumer<>(this::onReskinUnlock));
		_loc1.map(PASSWORD_PROMPT).toMessage(PasswordPrompt.class).toMethod(new MessageConsumer<>(this::onPasswordPrompt));
		_loc1.map(QUEST_FETCH_RESPONSE).toMessage(QuestFetchResponse.class).toMethod(new MessageConsumer<>(this::onQuestFetchResponse));
		_loc1.map(QUEST_REDEEM_RESPONSE).toMessage(QuestRedeemResponse.class).toMethod(new MessageConsumer<>(this::onQuestRedeemResponse));
		_loc1.map(KEY_INFO_RESPONSE).toMessage(KeyInfoResponse.class).toMethod(new MessageConsumer<>(this::onKeyInfoResponse));
		_loc1.map(LOGIN_REWARD_MSG).toMessage(ClaimDailyRewardResponse.class).toMethod(new MessageConsumer<>(this::onLoginRewardResponse));**/

        //The following is a map that is in a IConfig thing, I put it here for debugging purpose (see CharactersConfig)
        //This one awsell (see ChatConfig)
        loc1.map(GameServerConnection.TEXT).toMessage(Text.class).toHandler(TextHandler.class);

    }

    public void onHatchPet(HatchPetMessage param1) {

    }

    private void onDeletePet(DeletePetMessage param1) {

    }

    private void onNewAbility(NewAbilityMessage param1) {

    }

    private void onPetYardUpdate(PetYard param1) {

    }

    private void onEvolvedPet(EvolvedPetMessage param1) {

    }

    private void onActivePetUpdate(ActivePet param1) {

    }

    private void encryptConnection() {
        this.serverConnection.setOutgoingCipher(new ICipher("6a39570cc9de4ec71d64821894"));
        this.serverConnection.setIncomingCipher(new ICipher("c79332b197f92ba85ed281a023"));

    }

    /**
     * This method uses the Java 7 way of getting a random int between ranges.
     * It serves the same purpose as 'AS3's Random.getNextIntInRange()...
     */
    @Override
    public int getNextDamage(int param1, int param2) {
        return ThreadLocalRandom.current().nextInt(param1, param2 + 1);
    }

    private void create() {
        CharacterClass selectedClass = this.classesModel.getSelected();
        Create create = (Create) this.messages.require(CREATE);
        create.classType = selectedClass.id;
        create.skinType = selectedClass.skins.getSelectedSkin().id;
        this.serverConnection.sendMessage(create);
    }

    private void load() {
        System.out.println("Loading character '" + this.charId + "'...");

        Load load = (Load) this.messages.require(LOAD);
        load.charId = this.charId;
        load.isFromArena = this.isFromArena;

        System.out.println(load);

        this.serverConnection.sendMessage(load);

		/*if (isFromArena) {
		 this.openDialog.dispatch(new BattleSummaryDialog());
		 }*/
    }

    @Override
    public void playerShoot(int time, Projectile proj) {
        PlayerShoot playerShoot = (PlayerShoot) this.messages.require(PLAYERSHOOT);
        playerShoot.time = time;
        playerShoot.bulletId = proj.bulletId;
        playerShoot.containerType = proj.containerType;
        playerShoot.startingPos.x = proj.x;
        playerShoot.startingPos.y = proj.y;
        playerShoot.angle = proj.angle;
        this.serverConnection.sendMessage(playerShoot);
    }

    @Override
    public void playerHit(int bulletId, int objectId) {
        PlayerHit playerHit = (PlayerHit) this.messages.require(PLAYERHIT);
        playerHit.bulletId = bulletId;
        playerHit.objectId = objectId;
        this.serverConnection.sendMessage(playerHit);
    }

    @Override
    public void enemyHit(int time, int bulletId, int targetId, boolean kill) {
        EnemyHit enemyHit = (EnemyHit) this.messages.require(ENEMYHIT);
        enemyHit.time = time;
        enemyHit.bulletId = bulletId;
        enemyHit.targetId = targetId;
        enemyHit.kill = kill;
        this.serverConnection.sendMessage(enemyHit);
    }

    @Override
    public void otherHit(int time, int bulletId, int objectId, int targetId) {
        OtherHit otherHit = (OtherHit) this.messages.require(OTHERHIT);
        otherHit.time = time;
        otherHit.bulletId = bulletId;
        otherHit.objectId = objectId;
        otherHit.targetId = targetId;
        this.serverConnection.sendMessage(otherHit);
    }

    @Override
    public void squareHit(int time, int bulletId, int objectId) {
        SquareHit squareHit = (SquareHit) this.messages.require(SQUAREHIT);
        squareHit.time = time;
        squareHit.bulletId = bulletId;
        squareHit.objectId = objectId;
        this.serverConnection.sendMessage(squareHit);
    }

    public void aoeAck(int time, double x, double y) {
        AoeAck aoeAck = (AoeAck) this.messages.require(AOEACK);
        aoeAck.time = time;
        aoeAck.position.x = x;
        aoeAck.position.y = y;
        this.serverConnection.sendMessage(aoeAck);
    }

    @Override
    public void groundDamage(int time, double x, double y) {
        GroundDamage groundDamage = (GroundDamage) this.messages.require(GROUNDDAMAGE);
        groundDamage.time = time;
        groundDamage.position.x = x;
        groundDamage.position.y = y;
        this.serverConnection.sendMessage(groundDamage);
    }

    public void shootAck(int time) {
        ShootAck shootAck = (ShootAck) this.messages.require(SHOOTACK);
        shootAck.time = time;
        this.serverConnection.sendMessage(shootAck);
    }

    @Override
    public void playerText(String textStr) {
        PlayerText playerTextMessage = (PlayerText) this.messages.require(PLAYERTEXT);
        playerTextMessage.text = textStr;
        this.serverConnection.sendMessage(playerTextMessage);
    }

    @Override
    public boolean invSwap(Player player, GameObject sourceObj, int slotId1, int itemId, GameObject targetObj,
                           int slotId2, int objectType2) {
        if (this.gs == null) {
            return false;
        }
        InvSwap invSwap = (InvSwap) this.messages.require(INVSWAP);
        invSwap.time = this.gs.lastUpdate;
        invSwap.position.x = player.x;
        invSwap.position.y = player.y;
        invSwap.slotObject1.objectId = sourceObj.objectId;
        invSwap.slotObject1.slotId = slotId1;
        invSwap.slotObject1.objectType = itemId;
        invSwap.slotObject2.objectId = targetObj.objectId;
        invSwap.slotObject2.slotId = slotId2;
        invSwap.slotObject2.objectType = objectType2;
        this.serverConnection.sendMessage(invSwap);
        int tempType = sourceObj.equipment.get(slotId1);
        sourceObj.equipment.set(slotId1, targetObj.equipment.get(slotId2));
        targetObj.equipment.set(slotId2, tempType);
        SoundEffectLibrary.play("inventory_move_item");
        return true;
    }

    @Override
    public boolean invSwapPotion(Player player, GameObject sourceObj, int slotId1, int itemId, GameObject targetObj,
                                 int slotId2, int objectType2) {
        if (this.gs == null) {
            return false;
        }
        InvSwap invSwap = (InvSwap) this.messages.require(INVSWAP);
        invSwap.time = this.gs.lastUpdate;
        invSwap.position.x = player.x;
        invSwap.position.y = player.y;
        invSwap.slotObject1.objectId = sourceObj.objectId;
        invSwap.slotObject1.slotId = slotId1;
        invSwap.slotObject1.objectType = itemId;
        invSwap.slotObject2.objectId = targetObj.objectId;
        invSwap.slotObject2.slotId = slotId2;
        invSwap.slotObject2.objectType = objectType2;
        sourceObj.equipment.set(slotId1, ItemConstants.NO_ITEM);
        if (itemId == PotionInventoryModel.HEALTH_POTION_ID) {
            player.healthPotionCount++;
        } else if (itemId == PotionInventoryModel.MAGIC_POTION_ID) {
            player.magicPotionCount++;
        }
        this.serverConnection.sendMessage(invSwap);
        SoundEffectLibrary.play("inventory_move_item");
        return true;
    }

    @Override
    public boolean invSwapRaw(Player player, int objectId1, int slotId1, int objectType1, int objectId2,
                              int slotId2,
                              int objectType2) {
        if (this.gs == null) {
            return false;
        }
        InvSwap loc8 = (InvSwap) this.messages.require(INVSWAP);
        loc8.time = this.gs.lastUpdate;
        loc8.position.x = player.x;
        loc8.position.y = player.y;
        loc8.slotObject1.objectId = objectId1;
        loc8.slotObject1.slotId = slotId1;
        loc8.slotObject1.objectType = objectType1;
        loc8.slotObject2.objectId = objectId2;
        loc8.slotObject2.slotId = slotId2;
        loc8.slotObject2.objectType = objectType2;
        //this.addTextLine.dispatch(ChatMessage.make("",  "INVSWAP;
        this.serverConnection.sendMessage(loc8);
        SoundEffectLibrary.play("inventory_move_item");
        return true;
    }

    @Override
    public void invDrop(GameObject object, int slotId, int objectType) {
        InvDrop invDrop = (InvDrop) this.messages.require(INVDROP);
        invDrop.slotObject.objectId = object.objectId;
        invDrop.slotObject.slotId = slotId;
        invDrop.slotObject.objectType = objectType;
        this.serverConnection.sendMessage(invDrop);
        if ((slotId != PotionInventoryModel.HEALTH_POTION_SLOT) && (slotId != PotionInventoryModel.MAGIC_POTION_SLOT)) {
            object.equipment.put(slotId, ItemConstants.NO_ITEM);
        }
    }

    @Override
    public void useItem(int time, int objectId, int slotId, int objectType, double posX, double posY, int useType) {
        UseItem useItemMess = (UseItem) this.messages.require(USEITEM);
        useItemMess.time = time;
        useItemMess.slotObject.objectId = objectId;
        useItemMess.slotObject.slotId = slotId;
        useItemMess.slotObject.objectType = objectType;
        useItemMess.itemUsePos.x = posX;
        useItemMess.itemUsePos.y = posY;
        useItemMess.useType = useType;
        this.serverConnection.sendMessage(useItemMess);
    }

    @Override
    public boolean useItem_new(GameObject itemOwner, int slotId) {
        int itemId = itemOwner.equipment.get(slotId);
        XML objectXML = ObjectLibrary.xmlLibrary.get(itemId);
        if ((objectXML != null) && !itemOwner.isPaused()
                && (objectXML.hasOwnProperty("Consumable") || objectXML.hasOwnProperty("InvUse"))) {
            this.applyUseItem(itemOwner, slotId, itemId, objectXML);
            SoundEffectLibrary.play("use_potion");
            return true;
        }
        SoundEffectLibrary.play("error");
        return false;
    }

    private void applyUseItem(GameObject owner, int slotId, int objectType, XML itemData) {
        UseItem useItemMess = (UseItem) this.messages.require(USEITEM);
        useItemMess.time = getTimer();
        useItemMess.slotObject.objectId = owner.objectId;
        useItemMess.slotObject.slotId = slotId;
        useItemMess.slotObject.objectType = objectType;
        useItemMess.itemUsePos.x = 0;
        useItemMess.itemUsePos.y = 0;
        this.serverConnection.sendMessage(useItemMess);
        if (itemData.hasOwnProperty("Consumable")) {
            owner.equipment.set(slotId, -1);
        }
    }

    @Override
    public void setCondition(int conditionEffect, double conditionDuration) {
        SetCondition setCondition = (SetCondition) this.messages.require(SETCONDITION);
        setCondition.conditionEffect = conditionEffect;
        setCondition.conditionDuration = conditionDuration;
        this.serverConnection.sendMessage(setCondition);
    }

    public void move(int tickId, Player player) {
        int len = 0;
        int i = 0;
        double x = -1;
        double y = -1;
        if ((player != null) && !player.isPaused()) {
            x = player.x;
            y = player.y;
        }
        Move move = (Move) this.messages.require(MOVE);
        move.tickId = tickId;
        move.time = this.gs.lastUpdate;
        move.newPosition.x = x;
        move.newPosition.y = y;
        int lastMove = this.gs.moveRecords.lastClearTime;
        move.records.clear();
        if ((lastMove >= 0) && ((move.time - lastMove) > 125)) {
            len = Math.min(10, this.gs.moveRecords.records.size());
            for (i = 0; i < len; i++) {
                if (this.gs.moveRecords.records.get(i).time >= (move.time - 25)) {
                    break;
                }
                move.records.add(this.gs.moveRecords.records.get(i));
            }
        }
        this.gs.moveRecords.clear(move.time);
        this.serverConnection.sendMessage(move);
        if (player != null) {
            player.onMove();
        }
    }

    @Override
    public void teleport(int objectId) {
        Teleport teleport = (Teleport) this.messages.require(TELEPORT);
        teleport.objectId = objectId;
        this.serverConnection.sendMessage(teleport);
    }

    @Override
    public void usePortal(int objectId) {
        UsePortal usePortalMess = (UsePortal) this.messages.require(USEPORTAL);
        usePortalMess.objectId = objectId;
        this.serverConnection.sendMessage(usePortalMess);
    }

    @Override
    public void buy(int sellableObjectId, int currencyType) {
        if (this.outstandingBuy != null) {
            return;
        }
        SellableObject sObj = (SellableObject) AbstractMap.goDict.get(sellableObjectId);
        if (sObj == null) {
            return;
        }
        boolean converted = false;
        if (sObj.currency == Currency.GOLD) {
            converted = this.gs.model.getConverted() || (this.player.credits > 100) || (sObj.price > this.player.credits);
        }
        this.outstandingBuy = new OutstandingBuy(sObj.soldObjectInternalName(), sObj.price, sObj.currency, converted);
        Buy buyMesssage = (Buy) this.messages.require(BUY);
        buyMesssage.objectId = sellableObjectId;
        this.serverConnection.sendMessage(buyMesssage);
    }

    public void gotoAck(int time) {
        GotoAck gotoAck = (GotoAck) this.messages.require(GOTOACK);
        gotoAck.time = time;
        this.serverConnection.sendMessage(gotoAck);
    }

    @Override
    public void editAccountList(int accountListId, boolean add, int objectId) {
        EditAccountList eal = (EditAccountList) this.messages.require(EDITACCOUNTLIST);
        eal.accountListId = accountListId;
        eal.add = add;
        eal.objectId = objectId;
        this.serverConnection.sendMessage(eal);
    }

    @Override
    public void chooseName(String name) {
        ChooseName chooseName = (ChooseName) this.messages.require(CHOOSENAME);
        chooseName.name = name;
        this.serverConnection.sendMessage(chooseName);
    }

    @Override
    public void createGuild(String name) {
        CreateGuild createGuild = (CreateGuild) this.messages.require(CREATEGUILD);
        createGuild.name = name;
        this.serverConnection.sendMessage(createGuild);
    }

    @Override
    public void guildRemove(String name) {
        GuildRemove guildRemove = (GuildRemove) this.messages.require(GUILDREMOVE);
        guildRemove.name = name;
        this.serverConnection.sendMessage(guildRemove);
    }

    @Override
    public void guildInvite(String name) {
        GuildInvite guildInvite = (GuildInvite) this.messages.require(GUILDINVITE);
        guildInvite.name = name;
        this.serverConnection.sendMessage(guildInvite);
    }

    @Override
    public void requestTrade(String name) {
        RequestTrade requestTrade = (RequestTrade) this.messages.require(REQUESTTRADE);
        requestTrade.name = name;
        this.serverConnection.sendMessage(requestTrade);
    }

    @Override
    public void changeTrade(boolean[] offer) {
        ChangeTrade changeTrade = (ChangeTrade) this.messages.require(CHANGETRADE);
        changeTrade.offer = offer;
        this.serverConnection.sendMessage(changeTrade);
    }

    @Override
    public void acceptTrade(boolean[] myOffer, boolean[] yourOffer) {
        AcceptTrade acceptTrade = (AcceptTrade) this.messages.require(ACCEPTTRADE);
        acceptTrade.myOffer = myOffer;
        acceptTrade.yourOffer = yourOffer;
        this.serverConnection.sendMessage(acceptTrade);
    }

    @Override
    public void cancelTrade() {
        this.serverConnection.sendMessage(this.messages.require(CANCELTRADE));
    }

    @Override
    public void checkCredits() {
        this.serverConnection.sendMessage(this.messages.require(CHECKCREDITS));
    }

    @Override
    public void escape() {
        if (this.playerId == -1) {
            return;
        }
        this.serverConnection.sendMessage(this.messages.require(ESCAPE));
    }

    @Override
    public void joinGuild(String guildName) {
        JoinGuild joinGuild = (JoinGuild) this.messages.require(JOINGUILD);
        joinGuild.guildName = guildName;
        this.serverConnection.sendMessage(joinGuild);
    }

    @Override
    public void changeGuildRank(String name, int rank) {
        ChangeGuildRank changeGuildRank = (ChangeGuildRank) this.messages.require(CHANGEGUILDRANK);
        changeGuildRank.name = name;
        changeGuildRank.guildRank = rank;
        this.serverConnection.sendMessage(changeGuildRank);
    }

    private String rsaEncrypt(String data) {
        return RSA.encrypt(data);
    }

    /**
     * This method needs verification (mapJSON is a String, not a byte[])
     */
    private void onConnected() {
        WebAccount loc1 = (WebAccount) WebAccount.getInstance();
        this.addTextLine.dispatch(ChatMessage.make(Parameters.CLIENT_CHAT_NAME, TextKey.CHAT_CONNECTED));
        this.encryptConnection();
        Hello hello = (Hello) this.messages.require(HELLO);
        hello.buildVersion = Parameters.BUILD_VERSION + "." + Parameters.MINOR_VERSION;
        hello.gameId = this.gameId;
        hello.guid = this.rsaEncrypt(loc1.userId);
        hello.password = this.rsaEncrypt(loc1.password);
        hello.secret = this.rsaEncrypt(loc1.secret);
        hello.keyTime = this.keyTime;
        if (this.key != null) {
            hello.key = this.key;
        } else {
            hello.key = new byte[0];
        }
        if (this.mapJSON != null) {
            hello.mapJSON = this.mapJSON;
        } else {
            hello.mapJSON = new byte[0];
        }
        hello.entrytag = loc1.entryTag;
        hello.gameNet = loc1.gameNetwork;
        hello.gameNetUserId = loc1.gameNetworkUserId;
        hello.playPlatform = loc1.playPlatform;
        hello.platformToken = loc1.platformToken;
        hello.userToken = loc1.token;

        System.out.println("Sending hello packet...");

        this.serverConnection.sendMessage(hello);
    }

    public void onCreateSuccess(CreateSuccess createSuccess) {
        System.out.println("Create success");

        this.playerId = createSuccess.objectId;
        this.charId = createSuccess.charId;
        //this.gs.initialize();
        this.createCharacter = false;
    }

    public void onDamage(Damage damage) {
        int projId = 0;
        AbstractMap map = this.gs.map;
        Projectile proj = null;
        if ((damage.objectId >= 0) && (damage.bulletId > 0)) {
            projId = Projectile.findObjId(damage.objectId, damage.bulletId);
            proj = (Projectile) AbstractMap.boDict.get(projId);
            if ((proj != null) && !proj.projProps.multiHit) {
                map.removeObj(projId);
            }
        }
        GameObject target = AbstractMap.goDict.get(damage.targetId);
        if (target != null) {
            target.damage(false, damage.damageAmount, ConversionUtil.toIntVector(damage.effects), damage.kill, proj);
        }
    }

    public void onServerPlayerShoot(ServerPlayerShoot serverPlayerShoot) {
        boolean needsAck = (serverPlayerShoot.ownerId == this.playerId);
        GameObject owner = AbstractMap.goDict.get(serverPlayerShoot.ownerId);
        if ((owner == null) || owner.dead) {
            if (needsAck) {
                this.shootAck(-1);
            }
            return;
        }
        Projectile proj = new Projectile();
        proj.reset(serverPlayerShoot.containerType, 0, serverPlayerShoot.ownerId, serverPlayerShoot.bulletId,
                serverPlayerShoot.angle, this.gs.lastUpdate);
        proj.setDamage(serverPlayerShoot.damage);
        this.gs.map.addObj(proj, serverPlayerShoot.startingPos.x, serverPlayerShoot.startingPos.y);
        if (needsAck) {
            this.shootAck(this.gs.lastUpdate);
        }
    }

    void onAllyShoot(AllyShoot allyShoot) {
        GameObject owner = AbstractMap.goDict.get(allyShoot.ownerId);
        if ((owner == null) || owner.dead) {
            return;
        }
        Projectile proj = new Projectile();
        proj.reset(allyShoot.containerType, 0, allyShoot.ownerId, allyShoot.bulletId, allyShoot.angle,
                this.gs.lastUpdate);
        this.gs.map.addObj(proj, owner.x, owner.y);
        owner.setAttack(allyShoot.containerType, allyShoot.angle);
    }

    void onEnemyShoot(EnemyShoot enemyShoot) {
        GameObject owner = AbstractMap.goDict.get(enemyShoot.ownerId);
        if ((owner == null) || owner.dead) {
            this.shootAck(-1);
            return;
        }
        for (int i = 0; i < enemyShoot.numShots; i++) {
            Projectile proj = new Projectile();
            double angle = enemyShoot.angle + (enemyShoot.angleInc * i);
            proj.reset(owner.objectType, enemyShoot.bulletType, enemyShoot.ownerId, (enemyShoot.bulletId + i) % 256,
                    angle, this.gs.lastUpdate);
            proj.setDamage(enemyShoot.damage);
            this.gs.map.addObj(proj, enemyShoot.startingPos.x, enemyShoot.startingPos.y);
        }
        this.shootAck(this.gs.lastUpdate);
        owner.setAttack(owner.objectType, enemyShoot.angle + (enemyShoot.angleInc * ((enemyShoot.numShots - 1) / 2)));
    }

    public void onTradeRequested(TradeRequested tradeRequested) {
        this.addTextLine.dispatch(ChatMessage.make("", tradeRequested.name + " wants to "
                + "trade with you.  Type \"/trade " + tradeRequested.name + "\" to trade."));

    }

    public void onTradeStart(TradeStart tradeStart) {
        //this.gs.hudView.startTrade(this.gs, tradeStart);
    }

    public void onTradeChanged(TradeChanged tradeChanged) {
        //this.gs.hudView.tradeChanged(tradeChanged);
    }

    public void onTradeDone(TradeDone tradeDone) {
        //this.gs.hudView.tradeDone();
        this.addTextLine.dispatch(ChatMessage.make("", tradeDone.description));
    }

    public void onTradeAccepted(TradeAccepted tradeAccepted) {
        //this.gs.hudView.tradeAccepted(tradeAccepted);
    }

    private void addObject(ObjectData obj) {
        AbstractMap map = this.gs.map;
        GameObject go = ObjectLibrary.getObjectFromType(obj.objectType);
        if (go == null) {
            return;
        }
        ObjectStatusData status = obj.status;
        go.setObjectId(status.objectId);
        map.addObj(go, status.pos.x, status.pos.y);
        if (go instanceof Player) {
            this.handleNewPlayer((Player) go, map);
        }
        this.processObjectStatus(status, 0, -1);
        if (go.props.isStatic && go.props.occupySquare && !go.props.noMiniMap) {
            //this.updateGameObjectTileSignal.dispatch(new UpdateGameObjectTileVO((int) go.x, (int) go.y, go));
        }
    }

    /**
     * From addObject(ObjectData param1)
     */
    private void handleNewPlayer(Player player, AbstractMap map) {
        this.setPlayerSkinTemplate(player, 0);
        if (player.objectId == this.playerId) {
            this.player = player;
            this.model.player = player;
            map.player = player;
            this.gs.setFocus(player);
            this.setGameFocus.dispatch(String.valueOf(this.playerId));
        }
    }

    private void onUpdate(Update update) {
        int loc3 = 0;
        GroundTileData loc4 = null;
        Message loc2 = this.messages.require(UPDATEACK);
        this.serverConnection.sendMessage(loc2);
        loc3 = 0;
        while (loc3 < update.tiles.length) {
            loc4 = update.tiles[loc3];
            this.gs.map.setGroundTile(loc4.x, loc4.y, loc4.type);
            //this.updateGroundTileSignal.dispatch(new UpdateGroundTileVO(loc4.x, loc4.y, loc4.type));
            loc3++;
        }
        loc3 = 0;
        while (loc3 < update.newObjs.length) {
            this.addObject(update.newObjs[loc3]);
            loc3++;
        }
        loc3 = 0;
        while (loc3 < update.drops.length) {
            this.gs.map.removeObj(update.drops[loc3]);
            loc3++;
        }
    }

    private void onNotification(Notification notification) {
        GameObject go = AbstractMap.goDict.get(notification.objectId);
        if (go != null) {
			/*StringBuilder b = new StringBuilder(notification.message); // Workaround
			text = new QueuedStatusText(go, b, notification.color, 2000);
			this.gs.map.mapOverlay.addQueuedText(text);**/
            if ((go == this.player) && notification.message.equals("Quest Complete!")) {
                this.gs.map.quest.completed();
            }
        }
    }

    private void onGlobalNotification(GlobalNotification notification) {
        switch (notification.text) {
            /*case "yellow":
                ShowKeySignal.getInstance().dispatch(Key.YELLOW);
                break;
            case "red":
                ShowKeySignal.getInstance().dispatch(Key.RED);
                break;
            case "green":
                ShowKeySignal.getInstance().dispatch(Key.GREEN);
                break;
            case "purple":
                ShowKeySignal.getInstance().dispatch(Key.PURPLE);
                break;
            case "showKeyUI":
                ShowKeyUISignal.getInstance().dispatch();
                break;*/
            case "giftChestOccupied":
                this.giftChestUpdateSignal.dispatch(GiftStatusUpdateSignal.HAS_GIFT);
                break;
            case "giftChestEmpty":
                this.giftChestUpdateSignal.dispatch(GiftStatusUpdateSignal.HAS_NO_GIFT);
                break;
            case "beginnersPackage":
        }
    }

    private void onNewTick(NewTick newTick) {
        this.move(newTick.tickId, this.player);
        for (ObjectStatusData objectStatus : newTick.statuses) {
            this.processObjectStatus(objectStatus, newTick.tickTime, newTick.tickId);
        }
        this.lastTickId = newTick.tickId;
    }

    private void onShowEffect(ShowEffect showEffect) {
        //System.out.println("Show effect : " + showEffect);
    }

    /**
     * In Java goto is a reserved keyword
     */
    private void onGoto(Goto gotoPacket) {
        this.gotoAck(this.gs.lastUpdate);
        GameObject go = AbstractMap.goDict.get(gotoPacket.objectId);
        if (go == null) {
            return;
        }
        go.onGoto(gotoPacket.pos.x, gotoPacket.pos.y, this.gs.lastUpdate);
    }

    private void updateGameObject(GameObject go, StatData[] stats, boolean isMyObject) {
        int index = 0;

        Player player = null;
        if (go instanceof Player) {
            player = (Player) go;
        }

        Merchant merchant = null;
        if (go instanceof Merchant) {
            merchant = (Merchant) go;
        }

        for (StatData stat : stats) {
            int value = stat.statValue;
            switch (stat.statType) {
                case StatData.MAX_HP_STAT:
                    go.maxHP = value;
                    continue;
                case StatData.HP_STAT:
                    go.hp = value;
                    continue;
                case StatData.SIZE_STAT:
                    go.size = value;
                    continue;
                case StatData.MAX_MP_STAT:
                    player.maxMP = value;
                    continue;
                case StatData.MP_STAT:
                    player.mp = value;
                    continue;
                case StatData.NEXT_LEVEL_EXP_STAT:
                    player.nextLevelExp = value;
                    continue;
                case StatData.EXP_STAT:
                    player.exp = value;
                    continue;
                case StatData.LEVEL_STAT:
                    go.level = value;
                    continue;
                case StatData.ATTACK_STAT:
                    player.attack = value;
                    continue;
                case StatData.DEFENSE_STAT:
                    go.defense = value;
                    continue;
                case StatData.SPEED_STAT:
                    player.speed = value;
                    continue;
                case StatData.DEXTERITY_STAT:
                    player.dexterity = value;
                    continue;
                case StatData.VITALITY_STAT:
                    player.vitality = value;
                    continue;
                case StatData.WISDOM_STAT:
                    player.wisdom = value;
                    continue;
                case StatData.CONDITION_STAT:
                    go.condition.put(ConditionEffect.CE_FIRST_BATCH, value);
                    continue;
                case StatData.INVENTORY_0_STAT:
                case StatData.INVENTORY_1_STAT:
                case StatData.INVENTORY_2_STAT:
                case StatData.INVENTORY_3_STAT:
                case StatData.INVENTORY_4_STAT:
                case StatData.INVENTORY_5_STAT:
                case StatData.INVENTORY_6_STAT:
                case StatData.INVENTORY_7_STAT:
                case StatData.INVENTORY_8_STAT:
                case StatData.INVENTORY_9_STAT:
                case StatData.INVENTORY_10_STAT:
                case StatData.INVENTORY_11_STAT:
                    go.equipment.put(stat.statType - StatData.INVENTORY_0_STAT, value);
                    continue;
                case StatData.NUM_STARS_STAT:
                    player.numStars = value;
                    continue;
                case StatData.NAME_STAT:
                    if (!go.name.equals(stat.strStatValue)) {
                        go.name = stat.strStatValue;
                        go.nameBitmapData = null;
                    }
                    continue;
                case StatData.TEX1_STAT:
                    go.setTexture(value);
                    continue;
                case StatData.TEX2_STAT:
                    go.setAltTexture(value);
                    continue;
                case StatData.MERCHANDISE_TYPE_STAT:
                    merchant.setMerchandiseType(value);
                    continue;
                case StatData.CREDITS_STAT:
                    player.setCredits(value);
                    continue;
                case StatData.MERCHANDISE_PRICE_STAT:
                    //(SellableObject) go.setPrice(value);
                    continue;
                case StatData.ACTIVE_STAT:
                    //(Portal) go.active = value != 0;
                    continue;
                case StatData.ACCOUNT_ID_STAT:
                    player.accountId = stat.strStatValue;
                    continue;
                case StatData.FAME_STAT:
                    player.fame = value;
                    continue;
                case StatData.MERCHANDISE_CURRENCY_STAT:
                    //(SellableObject) go.setCurrency(value);
                    continue;
                case StatData.CONNECT_STAT:
                    go.connectType = value;
                    continue;
                case StatData.MERCHANDISE_COUNT_STAT:
                    merchant.count = value;
                    merchant.untilNextMessage = 0;
                    continue;
                case StatData.MERCHANDISE_MINS_LEFT_STAT:
                    merchant.minsLeft = value;
                    merchant.untilNextMessage = 0;
                    continue;
                case StatData.MERCHANDISE_DISCOUNT_STAT:
                    merchant.discount = value;
                    merchant.untilNextMessage = 0;
                    continue;
                case StatData.MERCHANDISE_RANK_REQ_STAT:
                    //(SellableObject) go.setRankReq(value);
                    continue;
                case StatData.MAX_HP_BOOST_STAT:
                    player.maxHPBoost = value;
                    continue;
                case StatData.MAX_MP_BOOST_STAT:
                    player.maxMPBoost = value;
                    continue;
                case StatData.ATTACK_BOOST_STAT:
                    player.attackBoost = value;
                    continue;
                case StatData.DEFENSE_BOOST_STAT:
                    player.defenseBoost = value;
                    continue;
                case StatData.SPEED_BOOST_STAT:
                    player.speedBoost = value;
                    continue;
                case StatData.VITALITY_BOOST_STAT:
                    player.vitalityBoost = value;
                    continue;
                case StatData.WISDOM_BOOST_STAT:
                    player.wisdomBoost = value;
                    continue;
                case StatData.DEXTERITY_BOOST_STAT:
                    player.dexterityBoost = value;
                    continue;
                case StatData.OWNER_ACCOUNT_ID_STAT:
                    //(Container) go.setOwnerId(value);
                    continue;
                case StatData.RANK_REQUIRED_STAT:
                    //(NameChanger) go.setRankRequired(value);
                    continue;
                case StatData.NAME_CHOSEN_STAT:
                    player.nameChosen = value != 0;
                    go.nameBitmapData = null;
                    continue;
                case StatData.CURR_FAME_STAT:
                    player.currFame = value;
                    continue;
                case StatData.NEXT_CLASS_QUEST_FAME_STAT:
                    player.nextClassQuestFame = value;
                    continue;
                case StatData.LEGENDARY_RANK_STAT:
                    player.legendaryRank = value;
                    continue;
                case StatData.SINK_LEVEL_STAT:
                    if (!isMyObject) {
                        player.sinkLevel = value;
                    }
                    continue;
                case StatData.ALT_TEXTURE_STAT:
                    go.setAltTexture(value);
                    continue;
                case StatData.GUILD_NAME_STAT:
                    player.setGuildName(stat.strStatValue);
                    continue;
                case StatData.GUILD_RANK_STAT:
                    player.guildRank = value;
                    continue;
                case StatData.BREATH_STAT:
                    player.breath = value;
                    continue;
                case StatData.XP_BOOSTED_STAT:
                    player.xpBoost = value;
                    continue;
                case StatData.XP_TIMER_STAT:
                    player.xpTimer = value * TO_MILLISECONDS;
                    continue;
                case StatData.LD_TIMER_STAT:
                    player.dropBoost = value * TO_MILLISECONDS;
                    continue;
                case StatData.LT_TIMER_STAT:
                    player.tierBoost = value * TO_MILLISECONDS;
                    continue;
                case StatData.HEALTH_POTION_STACK_STAT:
                    player.healthPotionCount = value;
                    continue;
                case StatData.MAGIC_POTION_STACK_STAT:
                    player.magicPotionCount = value;
                    continue;
                case StatData.TEXTURE_STAT:
                    if ((player != null) && (player.skinId != value)) {
                        this.setPlayerSkinTemplate(player, value);
                    }
                    continue;
                case StatData.HASBACKPACK_STAT:
                    //(Player) go.hasBackpack = value;
                    if (isMyObject) {
                        //this.updateBackpackTab.dispatch(value);
                    }
                    continue;
                case StatData.BACKPACK_0_STAT:
                case StatData.BACKPACK_1_STAT:
                case StatData.BACKPACK_2_STAT:
                case StatData.BACKPACK_3_STAT:
                case StatData.BACKPACK_4_STAT:
                case StatData.BACKPACK_5_STAT:
                case StatData.BACKPACK_6_STAT:
                case StatData.BACKPACK_7_STAT:
                    index = (stat.statType - StatData.BACKPACK_0_STAT) + GeneralConstants.NUM_EQUIPMENT_SLOTS
                            + GeneralConstants.NUM_INVENTORY_SLOTS;
                    Player o = (Player) go;
                    o.equipment.put(index, value);
                    continue;
                default:
                    continue;
            }
        }
    }

    private void setPlayerSkinTemplate(Player player, int skinId) {
        Reskin message = (Reskin) this.messages.require(RESKIN);

        message.skinID = skinId;
        message.player = player;
        message.consume();
    }

    private void processObjectStatus(ObjectStatusData objectStatus, int tickTime, int tickId) {
        int oldLevel = 0;
        int oldExp = 0;
        List newUnlocks = null;
        CharacterClass type = null;
        AbstractMap map = this.gs.map;
        GameObject go = AbstractMap.goDict.get(objectStatus.objectId);
        if (go == null) {
            return;
        }
        boolean isMyObject = objectStatus.objectId == this.playerId;
        if ((tickTime != 0) && !isMyObject) {
            go.onTickPos(objectStatus.pos.x, objectStatus.pos.y, tickTime, tickId);
        }

        Player player = null;
        if (go instanceof Player) {
            player = (Player) go;
        }

        if (player != null) {
            oldLevel = player.level;
            oldExp = player.exp;
        }
        this.updateGameObject(go, objectStatus.stats, isMyObject);
		/*if (oldLevel != -1) {
			if (player.level > oldLevel) {
				if (isMyObject) {
					newUnlocks = this.gs.model.getNewUnlocks(player.objectType, player.level);
					player.handleLevelUp(newUnlocks.size() != 0);
					type = this.classesModel.getCharacterClass(player.objectType);
					if (type.getMaxLevelAchieved() < player.level) {
						type.setMaxLevelAchieved(player.level);
					}
				} else {
					player.levelUpEffect("Level Up!");
				}
			} else if (player.exp > oldExp) {
				player.handleExpUp(player.exp - oldExp);
			}
		}*/
    }

    /**
     * Important : This method is not in the new version of the client
     * <p>
     * It's now in ChatConfig
     */
    private void onText(Text text) {
        System.out.println("Received text : " + text.toString());
    }

    private void onInvResult(InvResult invResult) {
        if (invResult.result != 0) {
            this.handleInvFailure();
        }
    }

    private void handleInvFailure() {
        SoundEffectLibrary.play("error");
        //this.gs.hudView.interactPanel.redraw();
    }

    private void onReconnect(Reconnect reconnect) {
        this.disconnect();
        Server server = new Server().setName(reconnect.name)
                .setAddress(reconnect.host.equals("") ? reconnect.host : this.server.address)
                .setPort(!reconnect.host.equals("") ? reconnect.port : this.server.port);
        int gameID = reconnect.gameId;
        boolean createChar = this.createCharacter;
        int charId = this.charId;
        int keyTime = reconnect.keyTime;
        byte[] key = reconnect.key;
        boolean isFromArena = reconnect.isFromArena;
        ReconnectEvent reconnectEvent = new ReconnectEvent(server, gameID, createChar, charId, keyTime, key,
                isFromArena);
        //this.gs.dispatchEvent(reconnectEvent);

        System.out.println("Reconnect event");

    }

    private void onPing(Ping ping) {
        Pong pong = (Pong) this.messages.require(PONG);
        pong.serial = ping.serial;
        pong.time = getTimer();
        this.serverConnection.sendMessage(pong);
    }

    private void parseXML(String xmlString) {
        XML extraXML = new XML(xmlString);
        GroundLibrary.parseFromXML(extraXML);
        ObjectLibrary.parseFromXML(extraXML);
        ObjectLibrary.parseFromXML(extraXML);
    }

    private void onMapInfo(MapInfo mapInfo) {
        System.out.println("AbstractMap info received");

        for (String clientXMLString : mapInfo.clientXML) {
            this.parseXML(clientXMLString);
        }
        for (String extraXMLString : mapInfo.extraXML) {
            this.parseXML(extraXMLString);
        }
        this.gs.applyMapInfo(mapInfo);
        this.rand = new Random(mapInfo.fp);
        if (this.createCharacter) {
            this.create();
        } else {
            this.load();
        }
    }

    //private void onPic(Pic pic) { this.gs.addChild(new PicView(pic.bitmapData)); }

    private void onDeath(Death death) {
        this.death = death;
        /**BitmapData stats = new BitmapData(this.gs.stage.stageWidth, this.gs.stage.stageHeight);
         stats.draw(this.gs);
         death.background = stats;
         if (!this.gs.isEditor) {
         this.handleDeath.dispatch(death);
         }*/

        System.out.println("DEAD");
    }

    private void onBuyResult(BuyResult buyResult) {
        if (buyResult.result == BuyResult.SUCCESS_BRID) {
            if (this.outstandingBuy != null) {
                this.outstandingBuy.record();
            }
        }
        this.outstandingBuy = null;
        switch (buyResult.result) {
            case BuyResult.NOT_ENOUGH_GOLD_BRID:
                log.info("Not enough gold");
                //OpenDialogSignal.getInstance().dispatch(new NotEnoughGoldDialog());
                break;
            case BuyResult.NOT_ENOUGH_FAME_BRID:
                log.info("Not enough fame");
                // OpenDialogSignal.getInstance().dispatch(new NotEnoughFameDialog());
                break;
            default:
                this.addTextLine
                        .dispatch(ChatMessage.make(buyResult.result == BuyResult.SUCCESS_BRID ? Parameters.SERVER_CHAT_NAME
                                : Parameters.ERROR_CHAT_NAME, buyResult.resultString));
        }
    }

    private void onAccountList(AccountList accountList) {
        /**if (accountList.accountListId == 0) {
         this.gs.map.party.setStars(accountList);
         }
         if (accountList.accountListId == 1) {
         this.gs.map.party.setIgnores(accountList);
         }*/
    }

    public void onQuestObjId(QuestObjId questObjId) {
        this.gs.map.quest.setObject(questObjId.objectId);
    }

    public void onAoe(Aoe aoe) {
        this.aoeAck(this.gs.lastUpdate, this.player.x, this.player.y);
    }

    public void onNameResult(NameResult nameResult) {
        //this.gs.dispatchEvent(new NameResultEvent(nameResult));
    }

    public void onGuildResult(GuildResult guildResult) {
        this.addTextLine.dispatch(ChatMessage.make(Parameters.ERROR_CHAT_NAME, guildResult.lineBuilderJSON));
        //this.gs.dispatchEvent(new GuildResultEvent(guildResult.success, guildResult.lineBuilderJSON));
    }

    public void onClientStat(ClientStat clientStat) {
        /**Account account = Account.getInstance();
         account.reportIntStat(clientStat.name, clientStat.value);*/
    }

    public void onFile(File file) {
        System.out.println("Received file : " + file);
        //new FileReference().save(file.file, file.filename);
    }

    void onInvitedToGuild(InvitedToGuild invitedToGuild) {
        /**if (Parameters.stats.showGuildInvitePopup) {
         this.gs.hudView.interactPanel
         .setOverride(new GuildInvitePanel(this.gs, invitedToGuild.name, invitedToGuild.guildName));
         }*/
        this.addTextLine.dispatch(ChatMessage.make("",
                "You have been invited by " + invitedToGuild.name + " to join the guild " + invitedToGuild.guildName
                        + ".\n  If you wish to join type \"/join " + invitedToGuild.guildName + "\""));

        System.out.println("Invited to guild");

    }

    void onPlaySound(PlaySound playSound) {
        GameObject obj = AbstractMap.goDict.get(playSound.ownerId);

        if (obj != null) {
            //obj.playSound(playSound.soundId);
        }
    }

    private void onClosed() {
        if (this.playerId != -1) {
            this.gs.closed.dispatch();
        } else if (this.retryConnection) {
            if (this.delayBeforeReconect < 10) {
                this.retry(this.delayBeforeReconect++);
                this.addTextLine
                        .dispatch(ChatMessage.make(Parameters.ERROR_CHAT_NAME, "Connection failed!  Retrying..."));
            } else {
                this.gs.closed.dispatch();
            }
        }
    }

    private void retry(int time) {
        this.retryTimer = new Timer(time * 1000);

        this.retryTimer.addEventListener(TimerEvent.TIMER_COMPLETE, new EventConsumer<>(this::onRetryTimer));
        this.retryTimer.start();
    }

    private void onRetryTimer(Event e) {
        this.serverConnection.connect(this.server.address, this.server.port);
    }

    private void onError(String error) {
        this.addTextLine.dispatch(ChatMessage.make(Parameters.ERROR_CHAT_NAME, error));
    }

    public void onFailure(Failure event) {
        System.err.println("Received Failure : " + event);

        switch (event.errorId) {
            case Failure.INCORRECT_VERSION:
                this.handleIncorrectVersionFailure(event);
                break;
            case Failure.BAD_KEY:
                this.handleBadKeyFailure(event);
                break;
            case Failure.INVALID_TELEPORT_TARGET:
                this.handleInvalidTeleportTarget(event);
                break;
            default:
                this.handleDefaultFailure(event);
        }
    }

    private void handleInvalidTeleportTarget(Failure event) {
        this.addTextLine.dispatch(ChatMessage.make(Parameters.ERROR_CHAT_NAME, event.errorDescription));
        this.player.nextTeleportAt = 0;
    }

    private void handleBadKeyFailure(Failure event) {
        this.addTextLine.dispatch(ChatMessage.make(Parameters.ERROR_CHAT_NAME, event.errorDescription));
        this.retryConnection = false;
        this.gs.closed.dispatch();
    }

    private void handleIncorrectVersionFailure(Failure event) {
        System.out.println("Client version " + Parameters.BUILD_VERSION + ". Server version: " + event.errorDescription
                + ". Client Update Needed.");

		/*Dialog dialog = new Dialog("Client version: " + Parameters.BUILD_VERSION + "\nServer version: " + event.errorDescription,
				"Client Update Needed", "Ok", null, "/clientUpdate");
		dialog.addEventListener(Dialog.BUTTON1_EVENT, this.onDoClientUpdate);
		this.gs.stage.addChild(dialog);
		this.retryConnection = false;**/
    }

    private void handleDefaultFailure(Failure event) {
        this.addTextLine.dispatch(ChatMessage.make(Parameters.ERROR_CHAT_NAME, event.errorDescription));
    }

    private void onDoClientUpdate(Event event) {

        System.out.println("Update Event");

		/*Dialog dialog = (Dialog) event.currentTarget;
		dialog.parent.removeChild(dialog);
		this.gs.closed.dispatch();**/
    }

}
