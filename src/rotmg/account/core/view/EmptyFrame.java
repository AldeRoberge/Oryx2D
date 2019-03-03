package rotmg.account.core.view;

import utils.flash.consumer.EventConsumer;
import utils.flash.events.Event;
import utils.flash.events.MouseEvent;
import utils.flash.text.TextFieldAutoSize;
import utils.flash.text.TextFormatAlign;
import rotmg.WebMain;
import rotmg.pets.util.PetsViewAssetFactory;
import rotmg.pets.view.components.DialogCloseButton;
import rotmg.pets.view.components.PopupWindowBackground;
import rotmg.text.view.stringBuilder.LineBuilder;
import rotmg.text.view.stringBuilder.StaticStringBuilder;
import rotmg.text.view.stringBuilder.TextFieldDisplayConcrete;
import utils.osflash.signals.Signal;

public class EmptyFrame extends Sprite {

    public static final int TEXT_MARGIN = 20;

    public Signal register;

    public Signal cancel;

    protected double modalWidth;

    protected double modalHeight;

    protected DialogCloseButton closeButton;

    protected Sprite background;

    protected Sprite backgroundContainer;

    protected TextFieldDisplayConcrete title;

    protected TextFieldDisplayConcrete desc;

    public EmptyFrame(int param1, int param2, String param3) {
        super();
        this.modalWidth = param1;
        this.modalHeight = param2;
        this.x = (WebMain.STAGE.stageWidth / 2) - (this.modalWidth / 2);
        this.y = (WebMain.STAGE.stageHeight / 2) - (this.modalHeight / 2);
        if (!param3.equals("")) {
            this.setTitle(param3, true);
        }
        if (this.background == null) {
            this.backgroundContainer = new Sprite();
            this.background = this.makeModalBackground();
            this.backgroundContainer.addChild(this.background);
            this.addChild(this.backgroundContainer);
        }
        if (!param3.equals("")) {
            this.setTitle(param3, true);
        }
    }

    private void onRemovedFromStage(Event param1) {
        this.removeEventListener(Event.REMOVED_FROM_STAGE, new EventConsumer<>(this::onRemovedFromStage));
        if (this.closeButton != null) {
            this.closeButton.removeEventListener(MouseEvent.CLICK, new EventConsumer<>(this::onCloseClick));
        }
    }

    public void setWidth(double param1) {
        this.modalWidth = param1;
        this.x = (WebMain.STAGE.stageWidth / 2) - (this.modalWidth / 2);
        this.refreshBackground();
    }

    public void setHeight(double param1) {
        this.modalHeight = param1;
        this.y = (WebMain.STAGE.stageHeight / 2) - (this.modalHeight / 2);
        this.refreshBackground();
    }

    public void setTitle(String param1, boolean param2) {
        if ((this.title != null) && (this.title.parent != null)) {
            this.removeChild(this.title);
        }
        if (param1 != null) {
            this.title = this.getText(param1, TEXT_MARGIN, 5, param2);
            this.addChild(this.title);
        } else {
            this.title = null;
        }
    }

    public void setDesc(String param1, boolean param2) {
        if (param1 != null) {
            if ((this.desc != null) && (this.desc.parent != null)) {
                this.removeChild(this.desc);
            }
            this.desc = this.getText(param1, TEXT_MARGIN, 50, param2);
            this.addChild(this.desc);
        }
    }

    public void setCloseButton(boolean param1) {
        if ((this.closeButton == null) && param1) {
            this.closeButton = PetsViewAssetFactory.returnCloseButton(this.modalWidth);
            this.closeButton.addEventListener(MouseEvent.CLICK, new EventConsumer<>(this::onCloseClick));
            this.addEventListener(Event.REMOVED_FROM_STAGE, new EventConsumer<>(this::onRemovedFromStage));
            this.addChild(this.closeButton);
        } else if ((this.closeButton != null) && !param1) {
            this.removeChild(this.closeButton);
            this.closeButton = null;
        }
    }

    protected TextFieldDisplayConcrete getText(String param1, int param2, int param3, boolean param4) {
        TextFieldDisplayConcrete loc5 = null;
        loc5 = new TextFieldDisplayConcrete().setSize(16).setColor(16777215).setTextWidth(this.modalWidth - (TEXT_MARGIN * 2));
        loc5.setBold(true);
        if (param4) {
            loc5.setStringBuilder(new StaticStringBuilder(param1));
        } else {
            loc5.setStringBuilder(new LineBuilder().setParams(param1));
        }
        loc5.setWordWrap(true);
        loc5.setMultiLine(true);
        loc5.setAutoSize(TextFieldAutoSize.CENTER);
        loc5.setHorizontalAlign(TextFormatAlign.CENTER);

        loc5.x = param2;
        loc5.y = param3;
        return loc5;
    }

    protected Sprite makeModalBackground() {
        this.x = (WebMain.STAGE.stageWidth / 2) - (this.modalWidth / 2);
        this.y = (WebMain.STAGE.stageHeight / 2) - (this.modalHeight / 2);
        PopupWindowBackground loc1 = new PopupWindowBackground();
        loc1.draw(this.modalWidth, this.modalHeight, PopupWindowBackground.TYPE_DEFAULT_GREY);
        if (this.title != null) {
            loc1.divide(PopupWindowBackground.HORIZONTAL_DIVISION, 30);
        }
        return loc1;
    }

    public void alignAssets() {
        this.desc.setTextWidth(this.modalWidth - (TEXT_MARGIN * 2));
        this.title.setTextWidth(this.modalWidth - (TEXT_MARGIN * 2));
    }

    protected void refreshBackground() {
        this.backgroundContainer.removeChild(this.background);
        this.background = this.makeModalBackground();
        this.backgroundContainer.addChild(this.background);
    }

    public void onCloseClick(MouseEvent param1) {
    }

}
