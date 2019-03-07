package rotmg.objects;

import flash.Vector;
import flash.XML;
import flash.consumer.SignalConsumer;
import flash.display.BitmapData;
import flash.utils.Dictionary;
import rotmg.appengine.RemoteTexture;
import rotmg.objects.animation.AnimatedChar;
import rotmg.util.AnimatedChars;
import rotmg.util.AssetLibrary;
import rotmg.util.AssetLoader;
import rotmg.util.MaskedImage;

public class TextureDataConcrete extends TextureData {

    public static boolean remoteTexturesUsed = false;

    private Boolean isUsingLocalTextures;

    public TextureDataConcrete(XML param1) {
        super();
        this.isUsingLocalTextures = this.getWhetherToUseLocalTextures();
        if (param1.hasOwnProperty("Texture")) {
            this.parse(param1.child("Texture"), param1.getAttribute("id"));
        } else if (param1.hasOwnProperty("AnimatedTexture")) {
            this.parse(param1.child("AnimatedTexture"), param1.getAttribute("id"));
        } else if (param1.hasOwnProperty("RemoteTexture")) {
            this.parse(param1.child("RemoteTexture"));
        } else if (param1.hasOwnProperty("RandomTexture")) {
            this.parse(param1.child("RandomTexture"), param1.getAttribute("id"));
        } else {
            this.parse(param1);
        }
        for (XML loc2 :
                param1.children("AltTexture")) {
            this.parse(loc2);
        }
        if (param1.hasOwnProperty("Mask")) {
            this.parse(param1.child("Mask"));
        }
        if (param1.hasOwnProperty("Effect")) {
            this.parse(param1.child("Effect"));
        }
    }

    // TODO this is a debug implementation
    @Override
    public BitmapData getTexture(int param1) {
        if (this.randomTextureData == null) {
            return this.texture;
        } else {
            return this.randomTextureData.get(0).getTexture();
        }
    }

    @Override
    public TextureData getAltTextureData(int param1) {
        if (this.altTextures == null) {
            return null;
        }
        return this.altTextures.get(param1);
    }

    private boolean getWhetherToUseLocalTextures() {
		/*
		ApplicationSetup loc1 = ApplicationSetup.getInstance();
		return loc1.useLocalTextures();
		 */

        return false;
    }


    private void parse(XML xml) {
        this.parse(xml, "");
    }

    private void parse(XML xml, String param2) {
        MaskedImage image = null;
        RemoteTexture remoteTexture = null;
        switch (xml.name()) {
            case "Texture":
                try {
                    this.texture = AssetLibrary.getImageFromSet(xml.getValue("File"), xml.getIntValue("Index"));
                } catch (Error error) {
                    throw new Error("Error loading Texture for " + param2 + " -  name");
                }
                break;
            case "Mask":
                this.mask = AssetLibrary.getImageFromSet(xml.getValue("File"), xml.getIntValue("Index"));
                break;
            case "Effect":
                this.effectProps = new EffectProperties(xml);
                break;
            case "AnimatedTexture":
                this.animatedChar = AnimatedChars.getAnimatedChar(xml.getValue("File"), xml.getIntValue("Index"));
                try {
                    image = this.animatedChar.imageFromAngle(0, AnimatedChar.STAND, 0);
                    this.texture = image.image;
                    this.mask = image.mask;
                } catch (Error error) {
                    throw new Error("Error loading AnimatedTexture for " + param2 + " -  name");
                }
                break;
            case "RemoteTexture":
                //System.err.println("Remote textures are not fully implemented!");
                this.texture = AssetLibrary.getImageFromSet("lofiObj3", 255);
                if (this.isUsingLocalTextures) {
                    remoteTexture = new RemoteTexture(xml.getValue("Id"), xml.getValue("Instance"), new SignalConsumer<>(this::onRemoteTexture));
                    remoteTexture.run();
                    if (!AssetLoader.currentXmlIsTesting) {
                        remoteTexturesUsed = true;
                    }
                }
                this.remoteTextureDir = xml.hasOwnProperty("Right") ? AnimatedChar.RIGHT : AnimatedChar.DOWN;
                break;
            case "RandomTexture":
                try {
                    this.randomTextureData = new Vector<>();
                    for (XML childXML : xml.children()) {
                        this.randomTextureData.push(new TextureDataConcrete(childXML));
                    }
                } catch (Error error) {
                    throw new Error("Error loading RandomTexture for " + param2);
                }
                break;
            case "AltTexture":
                if (this.altTextures == null) {
                    this.altTextures = new Dictionary<>();
                }
                this.altTextures.put(xml.getIntAttribute("id"), new TextureDataConcrete(xml));
        }
    }

    private void onRemoteTexture(BitmapData param1) {
        if (param1.width > 16) {
            AnimatedChars.add("remoteTexture", param1, null, param1.width / 7, param1.height, param1.width, param1.height, this.remoteTextureDir);
            this.animatedChar = AnimatedChars.getAnimatedChar("remoteTexture", 0);
            this.texture = this.animatedChar.imageFromAngle(0, AnimatedChar.STAND, 0).image;
        } else {
            this.texture = param1;
        }
    }


}
