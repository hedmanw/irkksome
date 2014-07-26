package se.alkohest.irkksome.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.alkohest.irkksome.orm.AnnotationStripper;
import se.alkohest.irkksome.orm.BeanEntity;
import se.alkohest.irkksome.orm.OneToMany;

public class SQLMapper {
    private static final Map<Class, String> sqlTypes = new HashMap<Class, String>() {{
        put(String.class, "TEXT");
        put(Integer.class, "INTEGER");
        put(Long.class, "INTEGER");
        put(Double.class, "DOUBLE");
        put(boolean.class, "BOOLEAN");
        put(int.class, "INTEGER");
        put(long.class, "INTEGER");
        put(double.class, "DOUBLE");
        put(Date.class, "THROW NEW ASSHOLEEXCEPTION()");
    }};

    private static final Map<Class, SqlCreateStatement> sqlCreateCache = new HashMap<>();

    public static String getFullCreateStatement(Class[] persistedBeans) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Class<? extends BeanEntity> bean : persistedBeans) {
            getCreateStatement(bean);
        }
        for (SqlCreateStatement sqlCreateStatement : sqlCreateCache.values()) {
            stringBuilder.append(sqlCreateStatement.toString());
        }
        return stringBuilder.toString();
    }

    private static SqlCreateStatement getCreateStatement(Class<? extends BeanEntity> bean) {
        SqlCreateStatement cachedStatement = sqlCreateCache.get(bean);
        if (cachedStatement != null) {
            return cachedStatement;
        }

        String tableName = AnnotationStripper.getTable(bean);
        Field[] fields = bean.getDeclaredFields();
        List<String> columns = new ArrayList<>();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length == 0) {
                columns.add(getCreateForColumn(field.getName(), getSQLType(field.getType())));
            }
            final OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (oneToMany != null) {
                SqlCreateStatement manyStatement = getCreateStatement(oneToMany.value());
                manyStatement.addColumn(getCreateForColumn(tableName.substring(2) + "_id", getSQLType(long.class)));
            }
        }

        final SqlCreateStatement createStatement = makeCreateStatement(tableName, columns);
        sqlCreateCache.put(bean, createStatement);
        return createStatement;
    }

    private static String getCreateForColumn(String columnName, String type) {
        return ", " + columnName + ' ' + type + " NOT NULL";
    }

    private static String getSQLType(Class fieldClass) {
        final String sqlType = sqlTypes.get(fieldClass);
        if (sqlType == null) {
            return sqlTypes.get(long.class);
        }
        else {
            return sqlType;
        }
    }

    private static SqlCreateStatement makeCreateStatement(String tableName, List<String> columns) {
        return new SqlCreateStatement(tableName, columns);
    }

    private static class SqlCreateStatement {
        private String table;
        private List<String> columns;

        private SqlCreateStatement(String table, List<String> columns) {
            this.table = table;
            this.columns = columns;
        }

        private void addColumn(String column) {
            columns.add(column);
        }

        private String columns() {
            StringBuilder stringBuilder = new StringBuilder();
            for (String column : columns) {
                stringBuilder.append(column);
            }
            return stringBuilder.toString();
        }

        @Override
        public String toString() {
            return "CREATE TABLE " + table + "(id INTEGER PRIMARY KEY AUTOINCREMENT" + columns() + ");";
        }
    }
}
