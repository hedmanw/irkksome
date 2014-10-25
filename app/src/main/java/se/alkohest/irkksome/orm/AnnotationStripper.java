package se.alkohest.irkksome.orm;

import java.lang.reflect.Field;

import se.emilsjolander.sprinkles.annotations.Table;

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

    public static Class<? extends AbstractBean> getOneToMany(BeanEntity bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            final OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (oneToMany != null) {
                return oneToMany.value();
            }
        }
        return null;
    }

    public static Class<? extends AbstractBean> getOneToOne(BeanEntity bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            final OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (oneToOne != null) {
                return oneToOne.value();
            }
        }
        return null;
    }
}
