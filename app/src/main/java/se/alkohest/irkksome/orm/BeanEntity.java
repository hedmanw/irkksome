package se.alkohest.irkksome.orm;

import android.content.ContentValues;

public interface BeanEntity {
    public long getId();
    public void setId(long id);
    public ContentValues createRow(long dependentPK);
}
