package se.alkohest.irkksome.model.api;

import java.util.ArrayList;
import java.util.List;

import se.alkohest.irkksome.model.entity.HilightItem;
import se.alkohest.irkksome.model.enumerations.HilightLevel;
import se.alkohest.irkksome.model.impl.HilightItemEB;

public class HilightManager {
    private List<HilightItem> hilightItems;

    public HilightManager() {
        hilightItems = new ArrayList<>(); // TODO: read from DAO
    }

    public void addHilight(String regex, HilightLevel level) {
        HilightItem hilightItem = new HilightItemEB(); // Move to DAO
        hilightItem.setString(regex);
        hilightItem.setLevel(level);
        hilightItems.add(hilightItem);
    }

    public HilightLevel getHilightLevel(String message) {
        for (HilightItem hilightItem : hilightItems) {
            if (message.contains(hilightItem.getString()) /*|| message.matches(hilightItem)*/) {
                return hilightItem.getLevel();
            }
        }
        return HilightLevel.UNREAD;
    }
}
