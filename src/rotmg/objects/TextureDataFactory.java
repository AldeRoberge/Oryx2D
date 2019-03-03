package rotmg.objects;

import utils.flash.XML;

public class TextureDataFactory {

    public TextureData create(XML param1) {
        return new TextureDataConcrete(param1);
    }

}
