package se.alkohest.irkksome.orm;

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

}
