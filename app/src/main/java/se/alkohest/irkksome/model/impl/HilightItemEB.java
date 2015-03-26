package se.alkohest.irkksome.model.impl;

import se.alkohest.irkksome.model.entity.HilightItem;
import se.alkohest.irkksome.model.enumerations.HilightLevel;
import se.alkohest.irkksome.orm.AbstractBean;

public class HilightItemEB extends AbstractBean implements HilightItem {
    private HilightLevel level;
    private String string;

    @Override
    public void setLevel(HilightLevel level) {
        this.level = level;
    }

    @Override
    public HilightLevel getLevel() {
        return level;
    }

    @Override
    public void setString(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }
}
