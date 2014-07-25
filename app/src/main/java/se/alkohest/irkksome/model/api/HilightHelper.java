package se.alkohest.irkksome.model.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oed on 7/24/14.
 */
public class HilightHelper {

    private List<String> strings;

    public HilightHelper() {
        strings = new ArrayList<>();
    }

    public void addHilight(String regex) {
        strings.add(regex);
    }

    public void removeHilight(String regex) {
        strings.remove(regex);
    }

    public boolean checkMessage(String message) {
        for (String r : strings) {
            if (message.contains(r) || message.matches(r)) {
                return true;
            }
        }
        return false;
    }
}
