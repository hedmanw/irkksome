package se.alkohest.irkksome.util;

public class ColorProvider {
    private static int[] colors = {
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

    private static ColorProvider INSTANCE = new ColorProvider();

    public static ColorProvider getInstance() {
        return INSTANCE;
    }

    public int getColor(int hash) {
        int positiveHash = Math.abs(hash);
        return colors[positiveHash%colors.length];
    }
}
