package se.alkohest.irkksome.model.impl

import se.alkohest.irkksome.orm.BeanEntity
import se.alkohest.irkksome.orm.Table

@Table(value = "some_table")
public class FuckYouGradle implements BeanEntity {
    private String string;
    private int count;

    @Override
    void setId(long id) {

    }

    @Override
    long getId() {
        return 0
    }
}