package rotmg.objects;

import static flash.utils.timer.getTimer.getTimer;

import flash.Vector;
import flash.XML;
import flash.display.BitmapData;
import flash.geom.Point;
import flash.geom.Vector3D;
import flash.utils.Dictionary;
import rotmg.map.AbstractMap;
import rotmg.messaging.data.WorldPosData;
import rotmg.objects.animation.AnimatedChar;
import rotmg.objects.animation.Animations;
import rotmg.objects.animation.AnimationsData;
import rotmg.parameters.Parameters;
import rotmg.parameters.Parameters.Data;
import rotmg.particles.ParticleEffect;
import rotmg.pets.data.PetVO;
import rotmg.pets.data.PetsModel;
import rotmg.sound.SoundEffectLibrary;
import rotmg.util.AssetLibrary;
import rotmg.util.BitmapUtil;
import rotmg.util.ConditionEffect;
import utils.ConversionUtil;


public class GameObject extends BasicObject {

    public static final int ATTACK_PERIOD = 300;
    private static final double ZERO_LIMIT = 0.00001;
    private static final double NEGATIVE_ZERO_LIMIT = -ZERO_LIMIT;
    private static final int DEFAULT_HP_BAR_Y_OFFSET = 6;

    public BitmapData nameBitmapData = null;
    public ObjectProperties props;
    public String name = "";
    public double radius = 0.5;
    public double facing = 0;
    public boolean flying = false;
    public double attackAngle = 0;
    public int attackStart = 0;
    public AnimatedChar animatedChar = null;
    public BitmapData texture = null;
    public BitmapData mask = null;
    public Vector<TextureData> randomTextureData = null;
    public ParticleEffect effect = null;
    public Animations animations = null;
    public boolean dead = false;
    public int deadCounter = 0;
    public int maxHP = 200;
    public int hp = 200;
    public int size = 100;
    public int level = -1;
    public int defense = 0;
    public Vector<Integer> slotTypes = null;
    public Vector<Integer> equipment = null;
    public Vector<Integer> lockedSlot = null;
    public Vector<Integer> condition;
    public boolean isInteractive = false;
    public int objectType;
    public int sinkLevel = 0;
    public BitmapData hallucinatingTexture = null;
    public FlashDescription flash = null;
    public int connectType = -1;
    protected BitmapData portrait = null;
    protected Dictionary<BitmapData, BitmapData> texturingCache = null;
    protected int tex1Id = 0;

    protected int tex2Id = 0;
    protected int lastTickUpdateTime = 0;
    protected int myLastTickId = -1;
    protected Point posAtTick;
    protected Point tickPosition;
    protected Vector<Double> vS;
    protected Vector<Double> uvt;
    private boolean isShocked;
    private boolean isShockedTransformSet = false;
    private boolean isCharging;
    private boolean isChargingTransformSet = false;
    private int nextBulletId = 1;
    private double sizeMult = 1;
    private boolean isStunImmune = false;
    private boolean isParalyzeImmune = false;
    private boolean isDazedImmune = false;
    private boolean isStasisImmune = false;
    private boolean ishpScaleSet = false;
    private Vector<BitmapData> icons = null;

    protected Vector3D moveVec;

    public GameObject(XML param1) {
        super();
        int loc4 = 0;
        this.props = ObjectLibrary.defaultProps;
        this.condition = new Vector<>(0, 0);
        this.posAtTick = new Point();
        this.tickPosition = new Point();
        this.vS = new Vector<>();
        this.uvt = new Vector<>();
        if (param1 == null) {
            return;
        }
        this.objectType = param1.getIntAttribute("type");
        this.props = ObjectLibrary.propsLibrary.get(this.objectType);
        this.hasShadow = this.props.shadowSize > 0;
        TextureData loc2 = ObjectLibrary.typeToTextureData.get(this.objectType);
        this.texture = loc2.texture;
        this.mask = loc2.mask;
        this.animatedChar = loc2.animatedChar;
        this.randomTextureData = loc2.randomTextureData;
        if (loc2.effectProps != null) {
            this.effect = ParticleEffect.fromProps(loc2.effectProps, this);
        }
        if (this.texture != null) {
            this.sizeMult = this.texture.height / 8;
        }
        AnimationsData loc3 = ObjectLibrary.typeToAnimationsData.get(this.objectType);
        if (loc3 != null) {
            this.animations = new Animations(loc3);
        }
        this.z = this.props.z;
        this.flying = this.props.flying;
        if (param1.hasOwnProperty("MaxHitPoints")) {
            this.hp = this.maxHP = param1.getIntValue("MaxHitPoints");
        }
        if (param1.hasOwnProperty("Defense")) {
            this.defense = param1.getIntValue("Defense");
        }
        if (param1.hasOwnProperty("SlotTypes")) {
            this.slotTypes = ConversionUtil.toIntVector(param1.getValue("SlotTypes"));
            this.equipment = new Vector<>(this.slotTypes.length);
            loc4 = 0;
            while (loc4 < this.equipment.length) {
                this.equipment.put(loc4, -1);
                loc4++;
            }
            this.lockedSlot = new Vector<>(this.slotTypes.length);
        }
        if (param1.hasOwnProperty("Tex1")) {
            this.tex1Id = param1.getIntValue("Tex1");
        }
        if (param1.hasOwnProperty("Tex2")) {
            this.tex2Id = param1.getIntValue("Tex2");
        }
        if (param1.hasOwnProperty("StunImmune")) {
            this.isStunImmune = true;
        }
        if (param1.hasOwnProperty("ParalyzeImmune")) {
            this.isParalyzeImmune = true;
        }
        if (param1.hasOwnProperty("DazedImmune")) {
            this.isDazedImmune = true;
        }
        if (param1.hasOwnProperty("StasisImmune")) {
            this.isStasisImmune = true;
        }
        this.props.loadSounds();
    }

    public static int damageWithDefense(int param1, int param2, boolean param3, Vector<Integer> param4) {
        int loc5 = param2;
        if (param3 || ((param4.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.ARMORBROKEN_BIT) != 0)) {
            loc5 = 0;
        } else if ((param4.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.ARMORED_BIT) != 0) {
            loc5 = loc5 * 2;
        }
        int loc6 = (param1 * 3) / 20;
        int loc7 = Math.max(loc6, param1 - loc5);
        if ((param4.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.INVULNERABLE_BIT) != 0) {
            loc7 = 0;
        }
        if ((param4.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.PETRIFIED_BIT) != 0) {
            loc7 = (int) (loc7 * 0.9);
        }
        if ((param4.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.CURSE_BIT) != 0) {
            loc7 = (int) (loc7 * 1.2);
        }
        return loc7;
    }

    public void setObjectId(int param1) {
        TextureData loc2 = null;
        this.objectId = param1;
        if (this.randomTextureData != null) {
            loc2 = this.randomTextureData.get(0); //objectId % this.randomTextureData.length
            this.texture = loc2.texture;
            this.mask = loc2.mask;
            this.animatedChar = loc2.animatedChar;
        }
    }

    public void setTexture(int param1) {
        TextureData loc2 = ObjectLibrary.typeToTextureData.get(param1);
        if (loc2 == null) {
            //System.err.println("Error, could not find data for object : " + param1);
        } else {
            this.texture = loc2.texture;
            this.mask = loc2.mask;
            this.animatedChar = loc2.animatedChar;
        }
    }

    public void setAltTexture(int param1) {
        TextureData loc3 = null;
        TextureData loc2 = ObjectLibrary.typeToTextureData.get(this.objectType);
        if (param1 == 0) {
            loc3 = loc2;
        } else {
            loc3 = loc2.getAltTextureData(param1);
            if (loc3 == null) {
                return;
            }
        }
        this.texture = loc3.texture;
        this.mask = loc3.mask;
        this.animatedChar = loc3.animatedChar;
        if (this.effect != null) {
            this.map.removeObj(this.effect.objectId);
            this.effect = null;
        }
        if (!Data.noParticlesMaster && (loc3.effectProps != null)) {
            this.effect = ParticleEffect.fromProps(loc3.effectProps, this);
            if (this.map != null) {
                this.map.addObj(this.effect, this.x, this.y);
            }
        }
    }

    public void setTex1(int param1) {
        if (param1 == this.tex1Id) {
            return;
        }
        this.tex1Id = param1;
        this.texturingCache = new Dictionary();
        this.portrait = null;
    }

    public void setTex2(int param1) {
        if (param1 == this.tex2Id) {
            return;
        }
        this.tex2Id = param1;
        this.texturingCache = new Dictionary();
        this.portrait = null;
    }

    public void playSound(int param1) {
        SoundEffectLibrary.play(this.props.sounds.get(param1));
    }

    @Override
    public void dispose() {
        BitmapData loc2 = null;
        Dictionary loc3 = null;
        BitmapData loc5 = null;
        this.texture = null;
        if (this.portrait != null) {
            this.portrait.dispose();
            this.portrait = null;
        }
        if (this.texturingCache != null) {
            for (Object loc1 : this.texturingCache) {
                loc2 = (BitmapData) loc1;
                if (loc2 != null) {
                    loc2.dispose();
                } else {
                    loc3 = (Dictionary) loc1;
                    for (Object loc4 : loc3) {
                        loc5 = (BitmapData) loc4;
                        if (loc5 != null) {
                            loc5.dispose();
                        }
                    }
                }
            }
            this.texturingCache = null;
        }

        this.slotTypes = null;
        this.equipment = null;
        this.lockedSlot = null;
        if (this.nameBitmapData != null) {
            this.nameBitmapData.dispose();
            this.nameBitmapData = null;
        }

        this.vS = null;
        this.uvt = null;

        this.icons = null;

    }

    public boolean isQuiet() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.QUIET_BIT) != 0;
    }

    public boolean isWeak() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.WEAK_BIT) != 0;
    }

    public boolean isSlowed() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.SLOWED_BIT) != 0;
    }

    public boolean isSick() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.SICK_BIT) != 0;
    }

    public boolean isDazed() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.DAZED_BIT) != 0;
    }

    public boolean isStunned() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.STUNNED_BIT) != 0;
    }

    public boolean isBlind() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.BLIND_BIT) != 0;
    }

    public boolean isDrunk() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.DRUNK_BIT) != 0;
    }

    public boolean isConfused() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.CONFUSED_BIT) != 0;
    }

    public boolean isStunImmune() {
        return ((this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.STUN_IMMUNE_BIT) != 0) || this.isStunImmune;
    }

    public boolean isInvisible() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.INVISIBLE_BIT) != 0;
    }

    public boolean isParalyzed() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.PARALYZED_BIT) != 0;
    }

    public boolean isSpeedy() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.SPEEDY_BIT) != 0;
    }

    public boolean isNinjaSpeedy() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.NINJA_SPEEDY_BIT) != 0;
    }

    public boolean isHallucinating() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.HALLUCINATING_BIT) != 0;
    }

    public boolean isHealing() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.HEALING_BIT) != 0;
    }

    public boolean isDamaging() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.DAMAGING_BIT) != 0;
    }

    public boolean isBerserk() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.BERSERK_BIT) != 0;
    }

    public boolean isPaused() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.PAUSED_BIT) != 0;
    }

    public boolean isStasis() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.STASIS_BIT) != 0;
    }

    public boolean isStasisImmune() {
        return this.isStasisImmune || ((this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.STASIS_IMMUNE_BIT) != 0);
    }

    public boolean isInvincible() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.INVINCIBLE_BIT) != 0;
    }

    public boolean isInvulnerable() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.INVULNERABLE_BIT) != 0;
    }

    public boolean isArmored() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.ARMORED_BIT) != 0;
    }

    public boolean isArmorBroken() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.ARMORBROKEN_BIT) != 0;
    }

    public boolean isArmorBrokenImmune() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.ARMORBROKEN_IMMUNE_BIT) != 0;
    }

    public boolean isSlowedImmune() {
        return (this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.SLOWED_IMMUNE_BIT) != 0;
    }

    public boolean isUnstable() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.UNSTABLE_BIT) != 0;
    }

    public boolean isShowPetEffectIcon() {
        return (this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.PET_EFFECT_ICON) != 0;
    }

    public boolean isDarkness() {
        return (this.condition.get(ConditionEffect.CE_FIRST_BATCH) & ConditionEffect.DARKNESS_BIT) != 0;
    }

    public boolean isParalyzeImmune() {
        return this.isParalyzeImmune || ((this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.PARALYZED_IMMUNE_BIT) != 0);
    }

    public boolean isDazedImmune() {
        return this.isDazedImmune || ((this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.DAZED_IMMUNE_BIT) != 0);
    }

    public boolean isPetrified() {
        return (this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.PETRIFIED_BIT) != 0;
    }

    public boolean isPetrifiedImmune() {
        return (this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.PETRIFIED_IMMUNE_BIT) != 0;
    }

    public boolean isCursed() {
        return (this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.CURSE_BIT) != 0;
    }

    public boolean isCursedImmune() {
        return (this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.CURSE_IMMUNE_BIT) != 0;
    }

    public boolean isSilenced() {
        return (this.condition.get(ConditionEffect.CE_SECOND_BATCH) & ConditionEffect.SILENCED_BIT) != 0;
    }


    public String getName() {
        return (this.name == null) || this.name.equals("") ? ObjectLibrary.typeToDisplayId.get(this.objectType) : this.name;
    }

    public int getColor() {
        if (this.props.color != -1) {
            return this.props.color;
        }
        return BitmapUtil.mostCommonColor(this.texture);
    }

    public int getBulletId() {
        int loc1 = this.nextBulletId;
        this.nextBulletId = (this.nextBulletId + 1) % 128;
        return loc1;
    }

    public double distTo(WorldPosData param1) {
        double loc2 = param1.x - this.x;
        double loc3 = param1.y - this.y;
        return Math.sqrt((loc2 * loc2) + (loc3 * loc3));
    }

    public void toggleShockEffect(boolean param1) {
        if (param1) {
            this.isShocked = true;
        } else {
            this.isShocked = false;
            this.isShockedTransformSet = false;
        }
    }

    public void toggleChargingEffect(boolean param1) {
        if (param1) {
            this.isCharging = true;
        } else {
            this.isCharging = false;
            this.isChargingTransformSet = false;
        }
    }

    @Override
    public boolean addTo(AbstractMap param1, double param2, double param3) {
        this.map = param1;
        this.posAtTick.x = this.tickPosition.x = param2;
        this.posAtTick.y = this.tickPosition.y = param3;
        if (!this.moveTo(param2, param3)) {
            this.map = null;
            return false;
        }
        if (this.effect != null) {
            this.map.addObj(this.effect, param2, param3);
        }
        return true;
    }

    @Override
    public void removeFromMap() {
        if (this.props.isStatic && (this.square != null)) {
            if (this.square.obj == this) {
                this.square.obj = null;
            }
            this.square = null;
        }
        if (this.effect != null) {
            this.map.removeObj(this.effect.objectId);
        }
        super.removeFromMap();
        this.dispose();
    }

    public boolean moveTo(double param1, double param2) {
        Square loc3 = this.map.getSquare(param1, param2);
        if (loc3 == null) {
            return false;
        }
        this.x = param1;
        this.y = param2;
        if (this.props.isStatic) {
            if (this.square != null) {
                this.square.obj = null;
            }
            loc3.obj = this;
        }
        this.square = loc3;

        return true;
    }

    @Override
    public boolean update(int time, int dt) {
        int tickDT = 0;
        double pX = 0;
        double pY = 0;
        boolean moving = false;
        if (!((this.moveVec.x == 0) && (this.moveVec.y == 0))) {
            if (this.myLastTickId < this.map.gs.gsc.lastTickId) {
                this.moveVec.x = 0;
                this.moveVec.y = 0;
                this.moveTo(this.tickPosition.x, this.tickPosition.y);
            } else {
                tickDT = time - this.lastTickUpdateTime;
                pX = this.posAtTick.x + (tickDT * this.moveVec.x);
                pY = this.posAtTick.y + (tickDT * this.moveVec.y);
                this.moveTo(pX, pY);
                moving = true;
            }
        }
        if (this.props.whileMoving != null) {
            if (!moving) {
                this.z = this.props.z;
                this.flying = this.props.flying;
            } else {
                this.z = this.props.whileMoving.z;
                this.flying = this.props.whileMoving.flying;
            }
        }
        return true;
    }

    public void onGoto(double param1, double param2, int param3) {
        this.moveTo(param1, param2);
        this.lastTickUpdateTime = param3;
        this.tickPosition.x = param1;
        this.tickPosition.y = param2;
        this.posAtTick.x = param1;
        this.posAtTick.y = param2;
        this.moveVec.x = 0;
        this.moveVec.y = 0;
    }

    public void onTickPos(double x, double y, int tickTime, int tickId) {
        try {
            if (this.myLastTickId < this.map.gs.gsc.lastTickId) {
                this.moveTo(this.tickPosition.x, this.tickPosition.y);
            }
            this.lastTickUpdateTime = this.map.gs.lastUpdate;
            this.tickPosition.x = x;
            this.tickPosition.y = y;
            this.posAtTick.x = this.x;
            this.posAtTick.y = this.y;
            this.moveVec.x = (this.tickPosition.x - this.posAtTick.x) / tickTime;
            this.moveVec.y = (this.tickPosition.y - this.posAtTick.y) / tickTime;
            this.myLastTickId = tickId;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void damage(boolean param1, int param2, Vector<Integer> param3, boolean param4, Projectile param5) {
        this.damage(param1, param2, param3, param4, param5, false);
    }

    public void damage(boolean param1, int param2, Vector<Integer> param3, boolean param4, Projectile param5, boolean param6) {
        int loc8 = 0;
        ConditionEffect loc10 = null;
        PetsModel loc12 = null;
        PetVO loc13 = null;
        String loc14 = null;
        Vector<Integer> loc15 = null;
        boolean loc16 = false;
        boolean loc7 = false;
        if (param4) {
            this.dead = true;
        } else if (param3 != null) {
            loc8 = 0;
            for (int loc9 : param3) {
                loc10 = null;
                if ((param5 != null) && (param5.projProps.isPetEffect != null) && (param5.projProps.isPetEffect.get(loc9) != null)) {
                    loc12 = PetsModel.getInstance();
                    loc13 = loc12.getActivePet();

                } else {
                    switch (loc9) {
                        case ConditionEffect.NOTHING:
                            break;
                        case ConditionEffect.WEAK:
                        case ConditionEffect.SICK:
                        case ConditionEffect.BLIND:
                        case ConditionEffect.HALLUCINATING:
                        case ConditionEffect.DRUNK:
                        case ConditionEffect.CONFUSED:
                        case ConditionEffect.STUN_IMMUNE:
                        case ConditionEffect.INVISIBLE:
                        case ConditionEffect.SPEEDY:
                        case ConditionEffect.BLEEDING:
                        case ConditionEffect.STASIS_IMMUNE:
                        case ConditionEffect.NINJA_SPEEDY:
                        case ConditionEffect.UNSTABLE:
                        case ConditionEffect.DARKNESS:
                        case ConditionEffect.PETRIFIED_IMMUNE:
                        case ConditionEffect.SILENCED:
                            loc10 = ConditionEffect.effects.get(loc9);
                            break;
                        case ConditionEffect.QUIET:
                            if (this.map.player == this) {
                                this.map.player.mp = 0;
                            }
                            loc10 = ConditionEffect.effects.get(loc9);
                            break;
                        case ConditionEffect.STASIS:

                            break;
                        case ConditionEffect.SLOWED:

                            break;
                        case ConditionEffect.ARMORBROKEN:

                            break;
                        case ConditionEffect.STUNNED:

                            break;
                        case ConditionEffect.DAZED:

                            break;
                        case ConditionEffect.PARALYZED:

                            break;
                        case ConditionEffect.PETRIFIED:

                            break;
                        case ConditionEffect.CURSE:

                            break;
                        case ConditionEffect.GROUND_DAMAGE:
                            loc7 = true;
                    }
                    if (loc10 != null) {
                        if (loc9 < ConditionEffect.NEW_CON_THREASHOLD) {
                            if ((this.condition.get(ConditionEffect.CE_FIRST_BATCH) | loc10.bit) == this.condition.get(ConditionEffect.CE_FIRST_BATCH)) {
                                continue;
                            }
                            this.condition.put(ConditionEffect.CE_FIRST_BATCH, this.condition.get(ConditionEffect.CE_FIRST_BATCH) | loc10.bit);
                        } else {
                            if ((this.condition.get(ConditionEffect.CE_SECOND_BATCH) | loc10.bit) == this.condition.get(ConditionEffect.CE_SECOND_BATCH)) {
                                continue;
                            }
                            this.condition.put(ConditionEffect.CE_SECOND_BATCH, this.condition.get(ConditionEffect.CE_SECOND_BATCH) | loc10.bit);
                        }
                        loc14 = loc10.localizationKey;
                        loc8 = loc8 + 500;
                    }
                }
            }
        }
        if (!(this.props.isEnemy && Data.disableEnemyParticles) && !(!this.props.isEnemy && Data.disablePlayersHitParticles)) {
        }
        if (!param1 && ((Data.noEnemyDamage && this.props.isEnemy) || (Data.noAllyDamage && this.props.isPlayer))) {
            return;
        }

    }


    protected BitmapData getHallucinatingTexture() {
        if (this.hallucinatingTexture == null) {
            this.hallucinatingTexture = AssetLibrary.getImageFromSet("lofiChar8x8", (int) (Math.random() * 239));
        }
        return this.hallucinatingTexture;
    }

    public void useAltTexture(String param1, int param2) {
        this.texture = AssetLibrary.getImageFromSet(param1, param2);
        this.sizeMult = this.texture.height / 8;
    }

    public void setAttack(int param1, double param2) {
        this.attackAngle = param2;
        this.attackStart = getTimer();
    }


    private boolean bHPBarParamCheck() {
        return (Parameters.data.HPBar != 0) && ((Parameters.data.HPBar == 1) || ((Parameters.data.HPBar == 2) && this.props.isEnemy) || ((Parameters.data.HPBar == 3) && ((this == this.map.player) || this.props.isEnemy)) || ((Parameters.data.HPBar == 4) && (this == this.map.player)) || ((Parameters.data.HPBar == 5) && this.props.isPlayer));
    }


    public void clearTextureCache() {
        this.texturingCache = new Dictionary<>();
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + " id: " + this.objectId + " type: " + ObjectLibrary.typeToDisplayId.get(this.objectType) + " pos: " + this.x + ", " + this.y + "]";
    }
}