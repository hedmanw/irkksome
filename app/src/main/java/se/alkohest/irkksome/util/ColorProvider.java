package se.alkohest.irkksome.util;

import android.graphics.Color;

public class ColorProvider {
    private static ColorProvider INSTANCE = new ColorProvider();

    public static ColorProvider getInstance() {
        return INSTANCE;
    }

    public int getColor(float[] hsv) {
        return Color.HSVToColor(hsv);
    }
}
