package rotmg.appengine;

public class SavedNewsItem {

    private static final String FAME = "fame";
    public String title;
    public String tagline;
    public String link;
    public int date;
    private String iconName;

    public SavedNewsItem(String param1, String param2, String param3, String param4, int param5) {
        super();
        this.iconName = param1;
        this.title = param2;
        this.tagline = param3;
        this.link = param4;
        this.date = param5;
    }

}
