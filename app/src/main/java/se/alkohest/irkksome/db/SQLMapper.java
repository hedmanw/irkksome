package se.alkohest.irkksome.db;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.AnnotationStripper;
import se.alkohest.irkksome.orm.Nullable;
import se.alkohest.irkksome.orm.OneToOne;
import se.emilsjolander.sprinkles.annotations.Column;

public class SQLMapper {
    private static final Map<Class, String> sqlTypes = new HashMap<Class, String>() {{
        put(String.class, "TEXT");
        put(Integer.class, "INTEGER");
        put(Long.class, "INTEGER");
        put(Double.class, "DOUBLE");
        put(boolean.class, "INTEGER");
        put(int.class, "INTEGER");
        put(long.class, "INTEGER");
        put(double.class, "DOUBLE");
        put(Date.class, "THROW NEW ASSHOLEEXCEPTION()");
    }};


    public static String[] getFullCreateStatement(Class[] classes) {
        return new String[0];
    }

    public static String getCreateStatement(Class<? extends AbstractBean> bean) {
        String tableName = AnnotationStripper.getTableName(bean);

        StringBuilder stringBuilder = new StringBuilder();
        Field[] fields = bean.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                stringBuilder.append(", ");
                String fieldName = field.getAnnotation(Column.class).value();
                stringBuilder.append(fieldName).append(" ");
                String type;
                if (field.isAnnotationPresent(OneToOne.class)) {
                    type = sqlTypes.get(long.class);
                }
                else {
                    type = getSQLType(field);
                }
                stringBuilder.append(type);
                if (!field.isAnnotationPresent(Nullable.class)) {
                    stringBuilder.append(" NOT NULL");
                }
            }
        }
        return "CREATE TABLE " + tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT" + stringBuilder.toString() + ");";
    }

    private static String getSQLType(Field field) {
        return sqlTypes.get(field.getType());
    }
}
