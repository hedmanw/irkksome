package se.alkohest.irkksome.orm;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class AnnotationStripper {
    public static String getTable(BeanEntity bean) {
        return getTable(bean.getClass());
    }

    public static String getTable(Class<? extends BeanEntity> beanClass) {
        final Table annotation = beanClass.getAnnotation(Table.class);
        if (annotation == null) {
            return null;
        }
        else {
            return annotation.value();
        }
    }

    public static String[] getColumnHeads(Class<? extends BeanEntity> beanClass) {
        Field[] fields = beanClass.getDeclaredFields();
        String[] fieldNames = new String[fields.length + 1];
        int index = 0;
        fieldNames[index++] = "id";
        for (Field field : fields) {
            final Transient isTransient = field.getAnnotation(Transient.class);
            if (isTransient == null) {
                fieldNames[index] = field.getName();
                index++;
            }
        }
        return fieldNames;
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends BeanEntity> getOneToMany(BeanEntity bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            final OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (oneToMany != null) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                return (Class<? extends BeanEntity>) genericType.getActualTypeArguments()[0];
            }
        }
        return null;
    }
}
