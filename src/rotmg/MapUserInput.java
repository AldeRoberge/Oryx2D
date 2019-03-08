package rotmg;

import flash.XML;
import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import rotmg.application.api.ApplicationSetup;
import rotmg.application.impl.ProductionSetup;
import rotmg.chat.model.ChatMessage;
import rotmg.constants.UseType;
import rotmg.messaging.GameServerConnection;
import rotmg.model.PotionInventoryModel;
import rotmg.objects.GameObject;
import rotmg.objects.ObjectLibrary;
import rotmg.objects.Player;
import rotmg.parameters.Parameters;
import rotmg.parameters.Parameters.Data;
import rotmg.pets.controller.reskin.ReskinPetFlowStartSignal;
import rotmg.signals.AddTextLineSignal;
import rotmg.signals.ExitGameSignal;
import rotmg.signals.GiftStatusUpdateSignal;
import rotmg.signals.SetTextBoxVisibilitySignal;
import rotmg.signals.UseBuyPotionSignal;
import rotmg.util.KeyCodes;

public class MapUserInput {

    private static final int MOUSE_DOWN_WAIT_PERIOD = 175;
    private static boolean arrowWarning = false;

    public GameSprite gs;

    private boolean moveLeft = false;

    private boolean moveRight = false;

    private boolean moveUp = false;

    private boolean moveDown = false;

    private boolean rotateLeft = false;

    private boolean rotateRight = false;

    private boolean mouseDown = false;

    private boolean autofire = false;

    private String currentString = "";

    private boolean specialKeyDown = false;

    private boolean enablePlayerInput = true;

    private GiftStatusUpdateSignal giftStatusUpdateSignal;

    private AddTextLineSignal addTextLine;

    private SetTextBoxVisibilitySignal setTextBoxVisibility;

    private MiniMapZoomSignal miniMapZoom;

    private UseBuyPotionSignal useBuyPotionSignal;

    private PotionInventoryModel potionInventoryModel;

    private ExitGameSignal exitGame;

    private boolean areFKeysAvailable;

    private ReskinPetFlowStartSignal reskinPetFlowStart;

    public MapUserInput(GameSprite param1) {
        super();
        this.gs = param1;
        this.giftStatusUpdateSignal = GiftStatusUpdateSignal.getInstance();
        this.reskinPetFlowStart = ReskinPetFlowStartSignal.getInstance();
        this.addTextLine = AddTextLineSignal.getInstance();
        this.setTextBoxVisibility = SetTextBoxVisibilitySignal.getInstance();
        this.miniMapZoom = MiniMapZoomSignal.getInstance();
        this.useBuyPotionSignal = UseBuyPotionSignal.getInstance();
        this.potionInventoryModel = PotionInventoryModel.getInstance();
        this.exitGame = ExitGameSignal.getInstance();
        ApplicationSetup loc3 = ProductionSetup.getInstance();
        this.areFKeysAvailable = loc3.areDeveloperHotkeysEnabled();
    }


    public void clearInput() {
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
        this.rotateLeft = false;
        this.rotateRight = false;
        this.mouseDown = false;
        this.autofire = false;
        this.setPlayerMovement();
    }

    public void setEnablePlayerInput(boolean param1) {
        if (this.enablePlayerInput != param1) {
            this.enablePlayerInput = param1;
            this.clearInput();
        }
    }
    private void onActivate(Event param1) {
    }

    private void onDeactivate(Event param1) {
        this.clearInput();
    }

    public void onMouseDown(MouseEvent param1) {
        double loc3 = 0;
        int loc4 = 0;
        XML loc5 = null;
        double loc6 = 0;
        double loc7 = 0;
        Player loc2 = this.gs.map.player;
        if (loc2 == null) {
            return;
        }
        if (!this.enablePlayerInput) {
            return;
        }
        if (param1.shiftKey) {
            loc4 = loc2.equipment.get(1);
            if (loc4 == -1) {
                return;
            }
            loc5 = ObjectLibrary.xmlLibrary.get(loc4);
            if ((loc5 == null) || loc5.hasOwnProperty("EndMpCost")) {
                return;
            }
            if (loc2.isUnstable()) {
                loc6 = (Math.random() * 600) - 300;
                loc7 = (Math.random() * 600) - 325;
            } else {
                loc6 = this.gs.map.mouseX;
                loc7 = this.gs.map.mouseY;
            }
            if (Parameters.isGpuRender()) {
                if ((param1.currentTarget == param1.target) || (param1.target == this.gs.map) || (param1.target == this.gs)) {
                    loc2.useAltWeapon(loc6, loc7, UseType.START_USE);
                }
            } else {
                loc2.useAltWeapon(loc6, loc7, UseType.START_USE);
            }
            return;
        }
        loc3 = Math.atan2(this.gs.map.mouseY, this.gs.map.mouseX);
        //}
        if (loc2.isUnstable()) {
            loc2.attemptAttackAngle(Math.random() * 360);
        } else {
            loc2.attemptAttackAngle(loc3);
        }
        this.mouseDown = true;
    }

    public void onMouseUp(MouseEvent param1) {
        this.mouseDown = false;
        Player loc2 = this.gs.map.player;
        if (loc2 == null) {
            return;
        }
        loc2.isShooting = false;
    }

    private void onMouseWheel(MouseEvent param1) {
        if (param1.delta > 0) {
            this.miniMapZoom.dispatch(MiniMapZoomSignal.IN);
        } else {
            this.miniMapZoom.dispatch(MiniMapZoomSignal.OUT);
        }
    }

    private void onEnterFrame(Event param1) {
        Player loc2 = null;
        double loc3 = 0;
        if (this.enablePlayerInput && (this.mouseDown || this.autofire)) {
            loc2 = this.gs.map.player;
            if (loc2 != null) {
                if (loc2.isUnstable()) {
                    loc2.attemptAttackAngle(Math.random() * 360);
                } else {
                    loc3 = Math.atan2(this.gs.map.mouseY, this.gs.map.mouseX);
                    loc2.attemptAttackAngle(loc3);
                }
            }
        }
    }

    private void onKeyDown(KeyboardEvent param1) {

        AddTextLineSignal loc5 = null;
        ChatMessage loc6 = null;
        GameObject loc7 = null;
        double loc8 = 0;
        double loc9 = 0;
        boolean loc10 = false;
        /**this.currentString = this.currentString + param1.keyCode;
         if (this.currentString.equals(UIUtils.EXPERIMENTAL_MENU_PASSWORD.substring(0, this.currentString.length()))) {
         if (this.currentString.length() == UIUtils.EXPERIMENTAL_MENU_PASSWORD.length()) {
         loc5 = AddTextLineSignal.getInstance();
         loc6 = new ChatMessage();
         loc6.name = Parameters.SERVER_CHAT_NAME;
         this.currentString = "";
         UIUtils.SHOW_EXPERIMENTAL_MENU = !UIUtils.SHOW_EXPERIMENTAL_MENU;
         loc6.text = UIUtils.SHOW_EXPERIMENTAL_MENU ? "Experimental menu activated" : "Experimental menu deactivated";
         loc5.dispatch(loc6);
         }
         } else {
         this.currentString = "";
         }*/
        switch (param1.keyCode) {
            case KeyCodes.F1:
            case KeyCodes.F2:
            case KeyCodes.F3:
            case KeyCodes.F4:
            case KeyCodes.F5:
            case KeyCodes.F6:
            case KeyCodes.F7:
            case KeyCodes.F8:
            case KeyCodes.F9:
            case KeyCodes.F10:
            case KeyCodes.F11:
            case KeyCodes.F12:
            case KeyCodes.INSERT:
            case KeyCodes.ALTERNATE:
                break;
            default:

                break;
        }

        Player loc3 = this.gs.map.player;
        if (param1.keyCode == Parameters.data.moveUp) {
            this.moveUp = true;

        } else if (param1.keyCode == Parameters.data.moveDown) {
            this.moveDown = true;

        } else if (param1.keyCode == Parameters.data.moveLeft) {
            this.moveLeft = true;

        } else if (param1.keyCode == Parameters.data.moveRight) {
            this.moveRight = true;

        } else if (param1.keyCode == Parameters.data.rotateLeft) {
            if (!Data.allowRotation) {
                return;
            }
            this.rotateLeft = true;

        } else if (param1.keyCode == Parameters.data.rotateRight) {
            if (!Data.allowRotation) {
                return;
            }
            this.rotateRight = true;

        } else if (param1.keyCode == Parameters.data.resetToDefaultCameraAngle) {
            Data.cameraAngle = Parameters.data.defaultCameraAngle;
            Parameters.save();

        } else if (param1.keyCode == Parameters.data.useSpecial) {
            loc7 = this.gs.map.player;
            if (loc7 == null) {
                return;
            }
            if (!this.specialKeyDown) {
                if (loc3.isUnstable()) {
                    loc8 = (Math.random() * 600) - 300;
                    loc9 = (Math.random() * 600) - 325;
                } else {
                    loc8 = this.gs.map.mouseX;
                    loc9 = this.gs.map.mouseY;
                }
                loc10 = loc3.useAltWeapon(loc8, loc9, UseType.START_USE);
                if (loc10) {
                    this.specialKeyDown = true;
                }
            }

        } else if (param1.keyCode == Parameters.data.autofireToggle) {
            this.gs.map.player.isShooting = this.autofire = !this.autofire;

        } else if (param1.keyCode == Parameters.data.toggleHPBar) {
            Parameters.data.HPBar = Parameters.data.HPBar != 0 ? 0 : 1;

        } else if (param1.keyCode == Parameters.data.toggleProjectiles) {
            Data.disableAllyParticles = !Data.disableAllyParticles;

        } else if (param1.keyCode == Parameters.data.toggleMasterParticles) {
            Data.noParticlesMaster = !Data.noParticlesMaster;

        } else if (param1.keyCode == Parameters.data.useInvSlot1) {
            this.useItem(4);

        } else if (param1.keyCode == Parameters.data.useInvSlot2) {
            this.useItem(5);

        } else if (param1.keyCode == Parameters.data.useInvSlot3) {
            this.useItem(6);

        } else if (param1.keyCode == Parameters.data.useInvSlot4) {
            this.useItem(7);

        } else if (param1.keyCode == Parameters.data.useInvSlot5) {
            this.useItem(8);

        } else if (param1.keyCode == Parameters.data.useInvSlot6) {
            this.useItem(9);

        } else if (param1.keyCode == Parameters.data.useInvSlot7) {
            this.useItem(10);

        } else if (param1.keyCode == Parameters.data.useInvSlot8) {
            this.useItem(11);

        } else if (param1.keyCode == Parameters.data.useHealthPotion) {
			/*if (this.potionInventoryModel.getPotionModel(PotionInventoryModel.HEALTH_POTION_ID).available) {
				this.useBuyPotionSignal.dispatch(new UseBuyPotionVO(PotionInventoryModel.HEALTH_POTION_ID, UseBuyPotionVO.CONTEXTBUY));
			}*/

        } else if (param1.keyCode == Parameters.data.GPURenderToggle) {
            Data.GPURender = !Data.GPURender;

        } else if (param1.keyCode == Parameters.data.useMagicPotion) {
			/*if (this.potionInventoryModel.getPotionModel(PotionInventoryModel.MAGIC_POTION_ID).available) {
				this.useBuyPotionSignal.dispatch(new UseBuyPotionVO(PotionInventoryModel.MAGIC_POTION_ID, UseBuyPotionVO.CONTEXTBUY));
			}*/
        } else if (param1.keyCode == Parameters.data.miniMapZoomOut) {
            this.miniMapZoom.dispatch(MiniMapZoomSignal.OUT);

        } else if (param1.keyCode == Parameters.data.miniMapZoomIn) {
            this.miniMapZoom.dispatch(MiniMapZoomSignal.IN);

        } else if (param1.keyCode == Parameters.data.togglePerformanceStats) {
            this.togglePerformanceStats();

        } else if ((param1.keyCode == Parameters.data.escapeToNexus) || (param1.keyCode == Parameters.data.escapeToNexus2)) {
            this.exitGame.dispatch();
            this.gs.gsc.escape();
            Data.needsRandomRealm = false;
            Parameters.save();

        } else if (param1.keyCode == Parameters.data.options) {
            this.clearInput();

        } else if (param1.keyCode == Parameters.data.toggleCentering) {
            Data.centerOnPlayer = !Data.centerOnPlayer;
            Parameters.save();

        } else if (param1.keyCode == Parameters.data.toggleFullscreen) {

        } else if (param1.keyCode == Parameters.data.interact) {

        } else if (param1.keyCode == Parameters.data.testOne) {
        }
        this.setPlayerMovement();
    }

    private void onKeyUp(KeyboardEvent param1) {
        double loc2 = 0;
        double loc3 = 0;
        if (param1.keyCode == Parameters.data.moveUp) {
            this.moveUp = false;
        } else if (param1.keyCode == Parameters.data.moveDown) {
            this.moveDown = false;

        } else if (param1.keyCode == Parameters.data.moveLeft) {
            this.moveLeft = false;

        } else if (param1.keyCode == Parameters.data.moveRight) {
            this.moveRight = false;

        } else if (param1.keyCode == Parameters.data.rotateLeft) {
            this.rotateLeft = false;

        } else if (param1.keyCode == Parameters.data.rotateRight) {
            this.rotateRight = false;

        } else if (param1.keyCode == Parameters.data.useSpecial) {
            if (this.specialKeyDown) {
                this.specialKeyDown = false;
                if (this.gs.map.player.isUnstable()) {
                    loc2 = (Math.random() * 600) - 300;
                    loc3 = (Math.random() * 600) - 325;
                } else {
                    loc2 = this.gs.map.mouseX;
                    loc3 = this.gs.map.mouseY;
                }
                this.gs.map.player.useAltWeapon(this.gs.map.mouseX, this.gs.map.mouseY, UseType.END_USE);
            }
        }
        this.setPlayerMovement();
    }

    private void setPlayerMovement() {
        Player loc1 = this.gs.map.player;
        if (loc1 != null) {
            if (this.enablePlayerInput) {
                loc1.setRelativeMovement((this.rotateRight ? 1 : 0) - (this.rotateLeft ? 1 : 0), (this.moveRight ? 1 : 0) - (this.moveLeft ? 1 : 0), (this.moveDown ? 1 : 0) - (this.moveUp ? 1 : 0));
            } else {
                loc1.setRelativeMovement(0, 0, 0);
            }
        }
    }

    private void useItem(int param1) {
        /*if (this.tabStripModel.currentSelection.equals(TabStripModel.BACKPACK)) {
            param1 = param1 + GeneralConstants.NUM_INVENTORY_SLOTS;
        }*/
        GameServerConnection.instance.useItem_new(this.gs.map.player, param1);
    }

    private void togglePerformanceStats() {

    }

}
