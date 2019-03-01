package rotmg.messaging;

import java.util.List;
import java.util.Random;

import org.osflash.signals.Signal;

import flash.display.DisplayObject;
import rotmg.AGameSprite;
import rotmg.messaging.data.SlotObjectData;
import rotmg.net.Server;
import rotmg.net.SocketServer;
import rotmg.objects.GameObject;
import rotmg.objects.Player;
import rotmg.objects.Projectile;

public class GameServerConnection {

	public static final int FAILURE = 0;
	public static final int CREATE_SUCCESS = 22;
	public static final int CREATE = 74;
	public static final int PLAYERSHOOT = 52;
	public static final int MOVE = 11;
	public static final int PLAYERTEXT = 37;
	public static final int TEXT = 88;
	public static final int SERVERPLAYERSHOOT = 63;
	public static final int DAMAGE = 21;
	public static final int UPDATE = 27;
	public static final int UPDATEACK = 26;
	public static final int NOTIFICATION = 56;
	public static final int NEWTICK = 100;
	public static final int INVSWAP = 82;
	public static final int USEITEM = 30;
	public static final int SHOWEFFECT = 34;
	public static final int HELLO = 10;
	public static final int GOTO = 58;
	public static final int INVDROP = 60;
	public static final int INVRESULT = 85;
	public static final int RECONNECT = 44;
	public static final int PING = 77;
	public static final int PONG = 14;
	public static final int MAPINFO = 59;
	public static final int LOAD = 3;
	public static final int PIC = 84;
	public static final int SETCONDITION = 12;
	public static final int TELEPORT = 24;
	public static final int USEPORTAL = 40;
	public static final int DEATH = 35;
	public static final int BUY = 81;
	public static final int BUYRESULT = 45;
	public static final int AOE = 79;
	public static final int GROUNDDAMAGE = 93;
	public static final int PLAYERHIT = 49;
	public static final int ENEMYHIT = 69;
	public static final int AOEACK = 83;
	public static final int SHOOTACK = 48;
	public static final int OTHERHIT = 16;
	public static final int SQUAREHIT = 101;
	public static final int GOTOACK = 6;
	public static final int EDITACCOUNTLIST = 78;
	public static final int ACCOUNTLIST = 80;
	public static final int QUESTOBJID = 89;
	public static final int CHOOSENAME = 95;
	public static final int NAMERESULT = 98;
	public static final int CREATEGUILD = 104;
	public static final int GUILDRESULT = 55;
	public static final int GUILDREMOVE = 8;
	public static final int GUILDINVITE = 75;
	public static final int ALLYSHOOT = 62;
	public static final int ENEMYSHOOT = 94;
	public static final int REQUESTTRADE = 46;
	public static final int TRADEREQUESTED = 91;
	public static final int TRADESTART = 31;
	public static final int CHANGETRADE = 87;
	public static final int TRADECHANGED = 19;
	public static final int ACCEPTTRADE = 42;
	public static final int CANCELTRADE = 15;
	public static final int TRADEDONE = 9;
	public static final int TRADEACCEPTED = 5;
	public static final int CLIENTSTAT = 96;
	public static final int CHECKCREDITS = 68;
	public static final int ESCAPE = 53;
	public static final int FILE = 65;
	public static final int INVITEDTOGUILD = 28;
	public static final int JOINGUILD = 99;
	public static final int CHANGEGUILDRANK = 102;
	public static final int PLAYSOUND = 51;
	public static final int GLOBAL_NOTIFICATION = 18;
	public static final int RESKIN = 36;
	public static final int PETUPGRADEREQUEST = 47;
	public static final int ACTIVE_PET_UPDATE_REQUEST = 50;
	public static final int ACTIVEPETUPDATE = 13;
	public static final int NEW_ABILITY = 103;
	public static final int PETYARDUPDATE = 20;
	public static final int EVOLVE_PET = 25;
	public static final int DELETE_PET = 38;
	public static final int HATCH_PET = 92;
	public static final int ENTER_ARENA = 76;
	public static final int IMMINENT_ARENA_WAVE = 17;
	public static final int ARENA_DEATH = 61;
	public static final int ACCEPT_ARENA_DEATH = 1;
	public static final int VERIFY_EMAIL = 57;
	public static final int RESKIN_UNLOCK = 97;
	public static final int PASSWORD_PROMPT = 64;
	public static final int QUEST_FETCH_ASK = 23;
	public static final int QUEST_REDEEM = 39;
	public static final int QUEST_FETCH_RESPONSE = 90;
	public static final int QUEST_REDEEM_RESPONSE = 4;
	public static final int PET_CHANGE_FORM_MSG = 7;
	public static final int KEY_INFO_REQUEST = 66;
	public static final int KEY_INFO_RESPONSE = 41;
	public static final int CLAIM_LOGIN_REWARD_MSG = 33;
	public static final int LOGIN_REWARD_MSG = 86;
	public static final int QUEST_ROOM_MSG = 67;
	public static final int PET_CHANGE_SKIN_MSG = 105;

	public static GameServerConnection instance;
	public Signal changeMapSignal;
	public AGameSprite gs;
	public Server server;
	public int gameId;
	public boolean createCharacter;
	public int charId;
	public int keyTime;
	public byte[] key;
	public byte[] mapJSON; // is a String in AS3's client TODO
	public boolean isFromArena = false;
	public int lastTickId = -1;
	//public JitterWatcher jitterWatcher;
	public SocketServer serverConnection;

	public OutstandingBuy outstandingBuy = null;
	public Random rand;
	public DisplayObject jitterWatcher;

	public GameServerConnection() {
		super();
	}

	public void chooseName(String name) {
	}

	public void createGuild(String name) {
	}

	public void connect() {
	}

	public void disconnect() {
	}

	public void checkCredits() {
	}

	public void escape() {
	}

	public void useItem(int time, int objectId, int slotId, int itemId, double x, double y, int useType) {
	}

	public boolean useItem_new(GameObject itemOwner, int slotId) {
		return false;
	}

	public void enableJitterWatcher() {
	}

	public void disableJitterWatcher() {
	}

	public void editAccountList(int accountListId, boolean add, int objectId) {
	}

	public void guildRemove(String param1) {
	}

	public void guildInvite(String param1) {
	}

	public void requestTrade(String param1) {
	}

	public void changeTrade(boolean[] param1) {
	}

	public void acceptTrade(boolean[] param1, boolean[] param2) {
	}

	public void cancelTrade() {
	}

	public void joinGuild(String param1) {
	}

	public void changeGuildRank(String param1, int param2) {
	}

	public boolean isConnected() {
		return false;
	}

	public void teleport(int param1) {
	}

	public void usePortal(int param1) {
	}

	public int getNextDamage(int param1, int param2) {
		return 0;
	}

	public void groundDamage(int param1, double param2, double param3) {
	}

	public void playerShoot(int param1, Projectile param2) {
	}

	public void playerHit(int param1, int param2) {
	}

	public void enemyHit(int param1, int param2, int param3, boolean param4) {
	}

	public void otherHit(int param1, int param2, int param3, int param4) {
	}

	public void squareHit(int param1, int param2, int param3) {
	}

	public void playerText(String param1) {
	}

	public boolean invSwap(Player player, GameObject sourceObj, int slotId1, int itemId1, GameObject targetObj,
			int slotId2, int itemId2) {
		return false;
	}

	public boolean invSwapRaw(Player player, int objectId1, int slotId1, int objectType1, int objectId2, int slotId2,
			int objectType2) {
		return false;
	}

	public boolean invSwapPotion(Player param1, GameObject param2, int param3, int param4, GameObject param5,
			int param6, int param7) {
		return false;
	}

	public void invDrop(GameObject param1, int param2, int param3) {
	}

	public void setCondition(int param1, double param2) {
	}

	public void buy(int param1, int param2) {
	}

	public void questFetch() {
	}

	public void questRedeem(String param1, List<SlotObjectData> param2) {
	}

	public void keyInfoRequest(int param1) {
	}

	public void claimRewardsMessageHack(String claimKey, String type) {
	}

	public void gotoQuestRoom() {
	}

	public void petCommand(int commandId, int petId) {
	}

}
