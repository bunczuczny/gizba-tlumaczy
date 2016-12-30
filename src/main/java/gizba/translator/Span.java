package main.java.gizba.translator;

public class Span {

    static final String SPAN_START = "<span";
    static final String SPAN_START_CLOSED = ">";
    static final String SPAN_END = "</span>";

    private String source;
    private int beginIndex;
    private int endIndex;
    private String string;
    private String value;

    Span(String source, int beginIndex, int endIndex) {
        this.source = source;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        string = source.substring(beginIndex, endIndex);
        value = value();
    }

    private String value() {
        int i = string.indexOf(SPAN_START_CLOSED) + 1;
        int j = string.length() - SPAN_END.length();
        return i < 1 || j < i ? "" : string.substring(i, j);
    }

    public String getSource() {
        return source;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public String getString() {
        return string;
    }

    public String getValue() {
        return value;
    }

}
