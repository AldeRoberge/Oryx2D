package rotmg.objects.animation;

import flash.XML;
import rotmg.objects.TextureData;
import rotmg.objects.TextureDataConcrete;

/**
 * This is a 100% match.
 */
public class FrameData {

    public int time;

    public TextureData textureData;

    public FrameData(XML xml) {
        this.time = (int) (xml.getDoubleAttribute("time") * 1000);
        this.textureData = new TextureDataConcrete(xml);
    }

}
