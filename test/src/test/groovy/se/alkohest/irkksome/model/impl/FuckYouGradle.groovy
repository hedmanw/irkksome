package se.alkohest.irkksome.model.impl

import se.alkohest.irkksome.orm.BeanEntity
import se.alkohest.irkksome.orm.Table

@Table(value = "some_table")
public class FuckYouGradle implements BeanEntity {
    @Override
    long getId() {
        return 0
    }
}