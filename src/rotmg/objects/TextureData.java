package rotmg.objects;

import utils.flash.Vector;
import utils.flash.display.BitmapData;
import utils.flash.utils.Dictionary;
import rotmg.objects.animation.AnimatedChar;


/**
 * This is a 99.9% match.
 * I made it abstract. Use TextureDataConcrete.
 */
public abstract class TextureData {

    public BitmapData texture = null;
    public BitmapData mask = null;
    public AnimatedChar animatedChar = null;
    public Vector<TextureData> randomTextureData = null;
    public Dictionary<Integer, TextureData> altTextures = null; //ID, XML
    public int remoteTextureDir;
    public EffectProperties effectProps = null;

    // Utility method for default int
    public BitmapData getTexture() {
        return this.getTexture(0);
    }

    public abstract BitmapData getTexture(int id);

    public abstract TextureData getAltTextureData(int id);

}
