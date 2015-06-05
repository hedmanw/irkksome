package se.alkohest.irkksome.util;

import java.util.HashMap;
import java.util.Map;

public class ColorProvider {
    private static final int[] COLORS = {
            0xfff44336,
            0xffe91e63,
            0xff9c27b0,
            0xff673ab7,
            0xff3f51b5,
            0xff2196f3,
            0xff03a9f4,
            0xff00bcd4,
            0xff009688,
            0xff4caf50,
            0xff8bc34a,
            0xffcddc39,
            0xffffc107,
            0xffff9800,
            0xffff5722,
    };

    private static final ColorProvider INSTANCE = new ColorProvider();
    private static final Map<String, Integer> NICK_COLORS = new HashMap<>(100);

    public static ColorProvider getInstance() {
        return INSTANCE;
    }

    public int getColor(String name) {
        final Integer color = NICK_COLORS.get(name);
        if (color != null) {
            return color;
        }
        else {
            final int newColor = getColor(name.hashCode());
            NICK_COLORS.put(name, newColor);
            return newColor;
        }
    }

    public int getColor(int hash) {
        int positiveHash = Math.abs(hash);
        return COLORS[positiveHash% COLORS.length];
    }
}
