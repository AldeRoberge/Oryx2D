package rotmg.background;

/**
 * 100% match
 */
public class Background {

    public static final int NO_BACKGROUND = 0;

    public static final int STAR_BACKGROUND = 1;

    public static final int NEXUS_BACKGROUND = 2;

    public static final int NUM_BACKGROUND = 3;

    public Background() {
        super();
    }

    public static Background getBackground(int param1) {
        switch (param1) {
            case NO_BACKGROUND:
                return null;
            case STAR_BACKGROUND:
                return new StarBackground();
            case NEXUS_BACKGROUND:
                return new NexusBackground();
            default:
                return null;
        }
    }

}
