package flash;

// TODO implement this (boilerplate code)
public class LineBuilder {

    String s;

    StringMap stringMap;

    public LineBuilder setParams(String s) {
        this.s = s;

        return this;
    }

    public LineBuilder setParams(String s, String s1) {
        this.s = s1;

        return this;
    }

    public void setStringMap(StringMap stringMap) {
        this.stringMap = stringMap;
    }

    public String getString() {
        return s;
    }
}
