package rotmg.map;

import flash.Vector;
import flash.display.BitmapData;
import flash.utils.Dictionary;
import rotmg.AGameSprite;
import rotmg.background.Background;
import rotmg.model.GameModel;
import rotmg.objects.BasicObject;
import rotmg.objects.GameObject;
import rotmg.objects.Merchant;
import rotmg.objects.Party;
import rotmg.objects.Player;
import rotmg.objects.Square;
import rotmg.parameters.Parameters;
import rotmg.particles.ParticleEffect;
import rotmg.util.IntPoint;
import utils.osflash.signals.Signal;

/**
 * 99% match
 */
public class AbstractMap {

    public AGameSprite gs;
    public String name;
    public Player player = null;
    public boolean showDisplays;
    public int width;
    public int height;
    public int back;
    public Background background = null;
    public Vector<Square> squareList;
    public Dictionary<IntPoint, Merchant> merchLookup;
    public Party party = null;
    public Quest quest = null;
    public Signal<Boolean> signalRenderSwitch;
    public boolean isPetYard = false;
    public double mouseX;
    public double mouseY;
    protected boolean allowPlayerTeleport;
    protected boolean wasLastFrameGpu = false;

    public static final Vector<Square> squares = new Vector<>(); // Tiles
    public static final Dictionary<Integer, BasicObject> boDict = new Dictionary<>(); // Basic Objects
    public static final Dictionary<Integer, GameObject> goDict = new Dictionary<>(); // Game Objects

    public static final String CLOTH_BAZAAR = "Cloth Bazaar";

    public static final String NEXUS = "Nexus";

    public static final String DAILY_QUEST_ROOM = "Daily Quest Room";

    public static final String DAILY_LOGIN_ROOM = "Daily Login Room";

    public static final String PET_YARD_1 = "Pet Yard";

    public static final String PET_YARD_2 = "Pet Yard 2";

    public static final String PET_YARD_3 = "Pet Yard 3";

    public static final String PET_YARD_4 = "Pet Yard 4";

    public static final String PET_YARD_5 = "Pet Yard 5";

    public static final String GUILD_HALL = "Guild Hall";

    public static final String NEXUS_EXPLANATION = "Nexus_Explanation";

    public static final String VAULT = "Vault";
    private static final Vector<String> VISIBLE_SORT_FIELDS = new Vector<>("sortVal_", "objectId_");

    private static final Vector<Integer> VISIBLE_SORT_PARAMS = new Vector<>(16, 16);
    public static boolean forceSoftwareRender = false;
    public static BitmapData texture;
    public boolean ifDrawEffectFlag = true;

    //private RollingMeanLoopMonitor loopMonitor;
    public Vector<BasicObject> visible;
    public Vector<BasicObject> visibleUnder;
    public Vector<Square> visibleSquares;
    public Vector<Square> topSquares;
    private boolean inUpdate = false;
    private Vector<BasicObject> objsToAdd;
    private Vector<Integer> idsToRemove;
    private Dictionary<String, Boolean> forceSoftwareMap;
    private boolean lastSoftwareClear = false;

    public AbstractMap(AGameSprite param1) {
        super();
        this.objsToAdd = new Vector<>();
        this.idsToRemove = new Vector<>();
        this.forceSoftwareMap = new Dictionary<>();
        //this.darkness = new EmbeddedAssets.DarknessBackground();
        this.visible = new Vector<>();
        this.visibleUnder = new Vector<>();
        this.visibleSquares = new Vector<>();
        this.topSquares = new Vector<>();
        this.gs = param1;
        this.party = new Party(this);
        this.quest = new Quest(this);
        //this.loopMonitor = RollingMeanLoopMonitor.getInstance();
        GameModel.getInstance().gameObjects = goDict;
        this.forceSoftwareMap.put(PET_YARD_1, true);
        this.forceSoftwareMap.put(PET_YARD_2, true);
        this.forceSoftwareMap.put(PET_YARD_3, true);
        this.forceSoftwareMap.put(PET_YARD_4, true);
        this.forceSoftwareMap.put(PET_YARD_5, true);
        this.forceSoftwareMap.put("Nexus", true);
        this.forceSoftwareMap.put("Tomb of the Ancients", true);
        this.forceSoftwareMap.put("Tomb of the Ancients (Heroic)", true);
        this.forceSoftwareMap.put("Mad Lab", true);
        this.forceSoftwareMap.put("Guild Hall", true);
        this.forceSoftwareMap.put("Guild Hall 2", true);
        this.forceSoftwareMap.put("Guild Hall 3", true);
        this.forceSoftwareMap.put("Guild Hall 4", true);
        this.forceSoftwareMap.put("Cloth Bazaar", true);
        this.forceSoftwareMap.put("Santa Workshop", true);
        this.wasLastFrameGpu = Parameters.isGpuRender();
    }

    public void setProps(int param1, int param2, String param3, int param4, boolean param5, boolean param6) {
        this.width = param1;
        this.height = param2;
        this.name = param3;
        this.back = param4;
        this.allowPlayerTeleport = param5;
        this.showDisplays = param6;
    }

    public void initialize() {

        //squares.length = width * height;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                squares.add(new Square(this, x, y));
            }
        }

        this.isPetYard = this.name.substring(0, 8).equals("Pet Yard");
    }


    public void dispose() {
        this.gs = null;
        this.background = null;
        for (Square loc1 : this.squareList) {
            loc1.dispose();
        }
        this.squareList.length = 0;
        this.squareList = null;
        squares.length = 0;
        squares.clear();
        for (GameObject loc2 : goDict) {
            loc2.dispose();
        }
        goDict.clear();
        for (BasicObject loc3 : boDict) {
            loc3.dispose();
        }
        boDict.clear();
        this.merchLookup = null;
        this.player = null;
        this.party = null;
        this.quest = null;
        this.objsToAdd = null;
        this.idsToRemove = null;
    }


    public void update(int param1, int param2) {
        this.inUpdate = true;
        for (BasicObject loc3 : goDict) {
            if (loc3.update(param1, param2)) {
                this.idsToRemove.add(loc3.objectId);
            }
        }
        for (BasicObject loc3 : boDict) {
            if (loc3.update(param1, param2)) {
                this.idsToRemove.add(loc3.objectId);
            }
        }
        this.inUpdate = false;
        for (BasicObject loc3 : this.objsToAdd) {
            this.internalAddObj(loc3);
        }
        this.objsToAdd.length = 0;
        for (int loc4 : this.idsToRemove) {
            this.internalRemoveObj(loc4);
        }
        this.idsToRemove.length = 0;
        this.party.update(param1, param2);
    }

    public void setGroundTile(int x, int y, int tileType) {
        int yi = 0;
        int ind = 0;
        Square n = null;
        Square square = this.getSquare(x, y);
        square.setTileType(tileType);
        int xend = x < (this.width - 1) ? x + 1 : x;
        int yend = y < (this.height - 1) ? y + 1 : y;
        for (int xi = x > 0 ? x - 1 : x; xi <= xend; xi++) {
            for (yi = y > 0 ? y - 1 : y; yi <= yend; yi++) {
                ind = xi + (yi * this.width);
                //squares.set(ind, n);
            }
        }
    }


    public void addObj(BasicObject param1, double param2, double param3) {
        param1.x = param2;
        param1.y = param3;
        if (param1 instanceof ParticleEffect) {
            ((ParticleEffect) param1).reducedDrawEnabled = Parameters.data.particleEffect == 0;
        }
        if (this.inUpdate) {
            this.objsToAdd.add(param1);
        } else {
            this.internalAddObj(param1);
        }
    }

    public void internalAddObj(BasicObject param1) {
        if (param1.addTo(this, param1.x, param1.y)) {
            return;
        }
        Dictionary loc2 = param1 instanceof GameObject ? goDict : boDict;
        if (loc2.get(param1.objectId) != null) {
            if (!this.isPetYard) {
                return;
            }
        }
        loc2.put(param1.objectId, param1);
    }


    public void removeObj(int param1) {
        if (this.inUpdate) {
            this.idsToRemove.add(param1);
        } else {
            this.internalRemoveObj(param1);
        }
    }

    public void internalRemoveObj(int param1) {
        BasicObject loc3 = goDict.get(param1);
        if (loc3 == null) {
            loc3 = boDict.get(param1);
            if (loc3 == null) {
                return;
            }
        }
        loc3.removeFromMap();
        boDict.remove(param1);
    }

    public Square getSquare(double par1, double par2) {

        int param1 = (int) par1;
        int param2 = (int) par2;

        if ((param1 < 0) || (param1 >= this.width) || (param2 < 0) || (param2 >= this.height)) {
            return null;
        }
        int loc3 = param1 + (param2 * this.width);
        Square loc4 = squares.get(loc3);
        if (loc4 == null) {
            loc4 = new Square(this, param1, param2);
            squares.put(loc3, loc4);
            this.squareList.add(loc4);
        }
        return loc4;
    }

    public Square lookupSquare(double param1, double param2) {
        if ((param1 < 0) || (param1 >= this.width) || (param2 < 0) || (param2 >= this.height)) {
            return null;
        }
        return squares.get((int) (param1 + (param2 * this.width)));
    }


}