package se.alkohest.irkksome.model.impl

import android.content.ContentValues
import se.alkohest.irkksome.orm.BeanEntity
import se.alkohest.irkksome.orm.OneToMany
import se.alkohest.irkksome.orm.Table

@Table("oneToManyExample")
public class GradleIsPrettyWorthless implements BeanEntity {
    @OneToMany
    List<FuckYouGradle> someRelation;

    @Override
    long getId() {
        return 0
    }

    @Override
    void setId(long id) {

    }

    @Override
    ContentValues createRow(long dependentPK) {
        return null
    }
}