package rotmg.objects;

import rotmg.map.AbstractMap;
import rotmg.parameters.Parameters;
import rotmg.parameters.Parameters.Data;
import flash.geom.Point;
import flash.geom.Point3D;
import flash.geom.Trig;
import flash.Vector;
import flash.display.BitmapData;
import flash.utils.Dictionary;

/**
 * This class is about 10% done. It requires a lot of graphics stuff.
 */
public class Projectile extends BasicObject {

    private static Dictionary<Integer, Integer> objBullIdToObjId = new Dictionary();

    public ObjectProperties props;

    public ObjectProperties containerProps;

    public ProjectileProperties projProps;

    public BitmapData texture;

    public int bulletId;

    public int ownerId;

    public int containerType;

    public int bulletType;

    public boolean damagesEnemies;

    public boolean damagesPlayers;

    public int damage;

    public String sound;

    public double startX;

    public double startY;

    public int startTime;

    public double angle = 0;

    public Dictionary multiHitDict;

    public Point3D p;
    private Point staticPoint;

    public Projectile() {
        super();
        this.p = new Point3D(100);
        this.staticPoint = new Point();
    }

    public static int findObjId(int param1, int param2) {
        return objBullIdToObjId.get((param2 << 24) | param1);
    }

    public static int getNewObjId(int param1, int param2) {
        int loc3 = getNextFakeObjectId();
        objBullIdToObjId.put((param2 << 24) | param1, loc3);
        return loc3;
    }

    public static void removeObjId(int param1, int param2) {
        objBullIdToObjId.remove((param2 << 24) | param1);
    }

    @Override
    public void dispose() {
        objBullIdToObjId = new Dictionary<>();
    }


    public void reset(int param1, int param2, int param3, int param4, double param5, int param6) {
        this.reset(param1, param2, param3, param4, param5, param6, "", "");
    }

    public void reset(int param1, int param2, int param3, int param4, double param5, int param6, String param7, String param8) {
        double loc11 = 0;
        this.clear();
        this.containerType = param1;
        this.bulletType = param2;
        this.ownerId = param3;
        this.bulletId = param4;
        this.angle = Trig.boundToPI(param5);
        this.startTime = param6;
        this.objectId = getNewObjId(this.ownerId, this.bulletId);
        this.z = 0.5;
        this.containerProps = ObjectLibrary.propsLibrary.get(this.containerType);

        if (this.containerProps == null) { //This is debug TODO
            System.err.println("Error, could not find projectile " + this.containerType + " ObjectLibrary.propsLibrary : " + ObjectLibrary.propsLibrary.size());
        } else {
            this.projProps = this.containerProps.projectiles.get(param2);
            String loc9 = !param7.equals("") && this.projProps.objectId.equals(param8) ? param7 : this.projProps.objectId;
            this.props = ObjectLibrary.getPropsFromId(loc9);
            this.hasShadow = this.props.shadowSize > 0;
            TextureData loc10 = ObjectLibrary.typeToTextureData.get(this.props.type);
            this.texture = loc10.getTexture(this.objectId);
            this.damagesPlayers = this.containerProps.isEnemy;
            this.damagesEnemies = !this.damagesPlayers;
            this.sound = this.containerProps.oldSound;
            this.multiHitDict = !!this.projProps.multiHit ? new Dictionary() : null;
            if (this.projProps.size >= 0) {
                loc11 = this.projProps.size;
            } else {
                loc11 = ObjectLibrary.getSizeFromType(this.containerType);
            }
            this.p.setSize(8 * (loc11 / 100));
            this.damage = 0;
        }
    }

    public void setDamage(int param1) {
        this.damage = param1;
    }

    @Override
    public boolean addTo(AbstractMap param1, double param2, double param3) {
        Player loc4 = null;
        this.startX = param2;
        this.startY = param3;
        if (!super.addTo(param1, param2, param3)) {
            return false;
        }
        if (!this.containerProps.flying && (this.square.sink != 0)) {
            this.z = 0.1;
        } else {
            loc4 = (Player) AbstractMap.goDict.get(this.ownerId);
            if ((loc4 != null) && (loc4.sinkLevel > 0)) {
                this.z = 0.5 - (0.4 * (loc4.sinkLevel / Parameters.MAX_SINK_LEVEL));
            }
        }
        return true;
    }

    public boolean moveTo(double param1, double param2) {
        Square loc3 = this.map.getSquare(param1, param2);
        if (loc3 == null) {
            return false;
        }
        this.x = param1;
        this.y = param2;
        this.square = loc3;
        return true;
    }

    @Override
    public void removeFromMap() {
        super.removeFromMap();
        removeObjId(this.ownerId, this.bulletId);
        this.multiHitDict = null;
        //FreeList.deleteObject(this);
    }

    private void positionAt(int param1, Point param2) {
        double loc5 = 0;
        double loc6 = 0;
        double loc7 = 0;
        double loc8 = 0;
        double loc9 = 0;
        double loc10 = 0;
        double loc11 = 0;
        double loc12 = 0;
        double loc13 = 0;
        double loc14 = 0;
        param2.x = this.startX;
        param2.y = this.startY;
        double loc3 = param1 * (this.projProps.speed / 10000);
        double loc4 = (this.bulletId % 2) == 0 ? 0 : Math.PI;
        if (this.projProps.wavy) {
            loc5 = 6 * Math.PI;
            loc6 = Math.PI / 64;
            loc7 = this.angle + (loc6 * Math.sin(loc4 + ((loc5 * param1) / 1000)));
            param2.x = param2.x + (loc3 * Math.cos(loc7));
            param2.y = param2.y + (loc3 * Math.sin(loc7));
        } else if (this.projProps.parametric) {
            loc8 = (param1 / this.projProps.lifetime) * 2 * Math.PI;
            loc9 = Math.sin(loc8) * ((this.bulletId % 2) != 0 ? 1 : -1);
            loc10 = Math.sin(2 * loc8) * ((this.bulletId % 4) < 2 ? 1 : -1);
            loc11 = Math.sin(this.angle);
            loc12 = Math.cos(this.angle);
            param2.x = param2.x + (((loc9 * loc12) - (loc10 * loc11)) * this.projProps.magnitude);
            param2.y = param2.y + (((loc9 * loc11) + (loc10 * loc12)) * this.projProps.magnitude);
        } else {
            if (this.projProps.boomerang) {
                loc13 = (this.projProps.lifetime * (this.projProps.speed / 10000)) / 2;
                if (loc3 > loc13) {
                    loc3 = loc13 - (loc3 - loc13);
                }
            }
            param2.x = param2.x + (loc3 * Math.cos(this.angle));
            param2.y = param2.y + (loc3 * Math.sin(this.angle));
            if (this.projProps.amplitude != 0) {
                loc14 = this.projProps.amplitude * Math.sin(loc4 + ((param1 / this.projProps.lifetime) * this.projProps.frequency * 2 * Math.PI));
                param2.x = param2.x + (loc14 * Math.cos(this.angle + (Math.PI / 2)));
                param2.y = param2.y + (loc14 * Math.sin(this.angle + (Math.PI / 2)));
            }
        }
    }

    @Override
    public boolean update(int param1, int param2) {
        Vector<Integer> loc5 = null;
        Player loc7 = null;
        boolean loc8 = false;
        boolean loc9 = false;
        boolean loc10 = false;
        int loc11 = 0;
        boolean loc12 = false;
        int loc3 = param1 - this.startTime;
        if (loc3 > this.projProps.lifetime) {
            return false;
        }
        Point loc4 = this.staticPoint;
        this.positionAt(loc3, loc4);
        if (!this.moveTo(loc4.x, loc4.y) || (this.square.tileType == 65535)) {
            if (this.damagesPlayers) {
                this.map.gs.gsc.squareHit(param1, this.bulletId, this.ownerId);
            } else if (this.square.obj != null) {
                if (!Data.noParticlesMaster) {
                    //loc5 = BloodComposition.getColors(this.texture);
                    //this.map.addObj(new HitEffect(loc5, 100, 3, this.angle, this.projProps.speed), loc4.x, loc4.y);
                }
            }
            return false;
        }
        if ((this.square.obj != null) && (!this.square.obj.props.isEnemy || !this.damagesEnemies) && (this.square.obj.props.enemyOccupySquare || (!this.projProps.passesCover && this.square.obj.props.occupySquare))) {
            if (this.damagesPlayers) {
                this.map.gs.gsc.otherHit(param1, this.bulletId, this.ownerId, this.square.obj.objectId);
            } else if (!Data.noParticlesMaster) {
                //loc5 = BloodComposition.getColors(this.texture);
                //this.map.addObj(new HitEffect(loc5, 100, 3, this.angle, this.projProps.speed), loc4.x, loc4.y);
            }
            return false;
        }
        GameObject loc6 = this.getHit(loc4.x, loc4.y);
        if (loc6 != null) {
            loc7 = this.map.player;
            loc8 = loc7 != null;
            loc9 = loc6.props.isEnemy;
            loc10 = loc8 && !loc7.isPaused() && (this.damagesPlayers || (loc9 && (this.ownerId == loc7.objectId)));
            if (loc10) {
                loc11 = GameObject.damageWithDefense(this.damage, loc6.defense, this.projProps.armorPiercing, loc6.condition);
                loc12 = loc6.hp <= loc11;
                if (loc6 == loc7) {
                    this.map.gs.gsc.playerHit(this.bulletId, this.ownerId);
                    loc6.damage(true, loc11, this.projProps.effects, false, this);
                } else if (loc6.props.isEnemy) {
                    this.map.gs.gsc.enemyHit(param1, this.bulletId, loc6.objectId, loc12);
                    loc6.damage(true, loc11, this.projProps.effects, loc12, this);
                } else if (!this.projProps.multiHit) {
                    this.map.gs.gsc.otherHit(param1, this.bulletId, this.ownerId, loc6.objectId);
                }
            }
            if (this.projProps.multiHit) {
                this.multiHitDict.put(loc6, true);
            } else {
                return false;
            }
        }
        return true;
    }

    public GameObject getHit(double param1, double param2) {
        double loc6 = 0;
        double loc7 = 0;
        double loc8 = 0;
        double loc9 = 0;
        double loc3 = Double.MAX_VALUE;
        GameObject loc4 = null;
        for (GameObject loc5 : AbstractMap.goDict) {
            if (!loc5.isInvincible()) {
                if (!loc5.isStasis()) {
                    if ((this.damagesEnemies && loc5.props.isEnemy) || (this.damagesPlayers && loc5.props.isPlayer)) {
                        if (!(loc5.dead || loc5.isPaused())) {
                            loc6 = loc5.x > param1 ? loc5.x - param1 : param1 - loc5.x;
                            loc7 = loc5.y > param2 ? loc5.y - param2 : param2 - loc5.y;
                            if (!((loc6 > loc5.radius) || (loc7 > loc5.radius))) {
                                if (!(this.projProps.multiHit && (this.multiHitDict.get(loc5) != null))) {
                                    if (loc5 == this.map.player) {
                                        return loc5;
                                    }
                                    loc8 = Math.sqrt((loc6 * loc6) + (loc7 * loc7));
                                    loc9 = (loc6 * loc6) + (loc7 * loc7);
                                    if (loc9 < loc3) {
                                        loc3 = loc9;
                                        loc4 = loc5;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return loc4;
    }

    private double getDirectionAngle(double param1) {
        int loc2 = (int) (param1 - this.startTime);
        Point loc3 = new Point();
        this.positionAt(loc2 + 16, loc3);
        double loc4 = loc3.x - this.x;
        double loc5 = loc3.y - this.y;
        return Math.atan2(loc5, loc4);
    }

}
