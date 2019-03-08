package rotmg.objects.animation;

import java.util.ArrayList;
import java.util.List;

import flash.XML;

public class AnimationsData {

    public List<AnimationData> animations;

    public AnimationsData(XML xml) {
        this.animations = new ArrayList<>();
        for (XML animData : xml.children("Animation")) {
            this.animations.add(new AnimationData(animData));
        }
    }

}
