package se.alkohest.irkksome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.alkohest.irkksome.model.impl.IrcUserEB;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Sprinkles;

public class SprinklesAdapter {
    public SprinklesAdapter(Context context) {
        Sprinkles sprinkles = Sprinkles.init(context);
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                sqLiteDatabase.execSQL(SQLMapper.getCreateStatement(IrcUserEB.class));
            }
        });
    }

    public OneQuery read(Class bean, String selection, String[] selectionArgs) {
        return Query.one(bean, selection, selectionArgs);
    }
}
