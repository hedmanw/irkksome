package se.alkohest.irkksome.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.exceptions.NoTableAnnotationException;

public class AnnotationStripper {
    public static String getTable(Model bean) {
        return getTableName(bean.getClass());
    }

    public static String getTableName(Class<? extends Model> entityBean) {
        if (entityBean.isAnnotationPresent(Table.class)) {
            return entityBean.getAnnotation(Table.class).value();
        }
        throw new NoTableAnnotationException();
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
