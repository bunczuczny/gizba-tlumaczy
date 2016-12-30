package main.java.gizba.translator;

public class SpanBuilder {

    public static Span build(String string, String key) {
        return build(string, key, 0);
    }

    public static Span build(String string, String key, int beginIndex) {
        if (string == null || string.length() == 0) {
            return null;
        }

        key = key == null ? "" : key;
        int i0, i1, i2, i3, open = 0;

        i0 = string.indexOf(Span.SPAN_START + key, beginIndex);
        if (i0 < 0) {
            return null;
        }

        i2 = string.indexOf(Span.SPAN_START_CLOSED, i0);
        if (i2 < 0) {
            return null;
        }

        open++;

        do {
            i3 = string.indexOf(Span.SPAN_END, i2);
            i1 = string.indexOf(Span.SPAN_START, i2);
            if (i1 < 0 || i1 > i3) {
                open--;
                i2 = string.indexOf(Span.SPAN_START_CLOSED, i3);
            } else {
                open++;
                i2 = string.indexOf(Span.SPAN_START_CLOSED, i1);
            }
        } while (open > 0);

        return new Span(string, i0, i3 + Span.SPAN_END.length());
    }

}