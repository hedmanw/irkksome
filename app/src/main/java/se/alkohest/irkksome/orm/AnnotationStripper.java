package se.alkohest.irkksome.orm;

import android.content.ContentValues;

public class AnnotationStripper {
    public static String getTable(Object bean) {
        final Table annotation = bean.getClass().getAnnotation(Table.class);
        if (annotation == null) {
            return null;
        }
        else {
            return annotation.tableName();
        }
    }

    public static ContentValues getPersistedFields(Object bean) throws IllegalAccessException {
        return null;
    }
}
