package parser;

public class Substring {
    private final String s;
    private final int from;
    private final int to;

    Substring(String s, int from, int to) {
        this.s = s;
        this.from = from;
        this.to = to;
    }

    public String get() {
        return s.substring(from, to);
    }
}
