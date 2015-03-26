package se.alkohest.irkksome.model.entity;

import se.alkohest.irkksome.model.enumerations.HilightLevel;
import se.alkohest.irkksome.orm.BeanEntity;

public interface HilightItem extends BeanEntity {

    public void setLevel(HilightLevel level);

    public HilightLevel getLevel();

    public void setString(String string);

    public String getString();
}
