package se.alkohest.irkksome.db;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.AnnotationStripper;
import se.alkohest.irkksome.orm.Inherits;
import se.alkohest.irkksome.orm.Nullable;
import se.alkohest.irkksome.orm.OneToMany;
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
        put(Date.class, "INTEGER");
    }};

    private static final Map<Class, SqlCreateStatement> sqlCreateCache = new HashMap<>();

    public static String[] getFullCreateStatement(Class[] classes) {
        return new String[0];
    }

    public static String getCreateStatement(Class<? extends AbstractBean> bean) {
        return getCreateStatementGraph(bean).toString();
    }

    private static SqlCreateStatement getCreateStatementGraph(Class<? extends AbstractBean> bean) {
        SqlCreateStatement cachedStatement = sqlCreateCache.get(bean);
        if (cachedStatement != null) {
            return cachedStatement;
        }

        String tableName = AnnotationStripper.getTableName(bean);
        StringBuilder stringBuilder = new StringBuilder();

        if (bean.isAnnotationPresent(Inherits.class)) {
            String parent = bean.getAnnotation(Inherits.class).value();
            stringBuilder.append(", ").append(parent).append(" INTEGER NOT NULL");
        }

        Field[] fields = bean.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                String fieldName = field.getAnnotation(Column.class).value();
                if (field.isAnnotationPresent(OneToMany.class)) {
                    SqlCreateStatement childStatement = getCreateStatementGraph(field.getAnnotation(OneToMany.class).value());
                    childStatement.addColumn(", " + fieldName + " INTEGER NOT NULL");
                }
                else {
                    stringBuilder.append(", ");
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
        }
        final SqlCreateStatement statement = new SqlCreateStatement(tableName, stringBuilder);
        sqlCreateCache.put(bean, statement);
        return statement;
    }

    private static String getSQLType(Field field) {
        return sqlTypes.get(field.getType());
    }

    private static class SqlCreateStatement {
        private final String tableName;
        private final StringBuilder columns;

        private SqlCreateStatement(String tableName, StringBuilder columns) {
            this.tableName = tableName;
            this.columns = columns;
        }

        private void addColumn(String column) {
            columns.append(column);
        }

        @Override
        public String toString() {
            return "CREATE TABLE " + tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT" + columns.toString() + ");";
        }
    }
}
