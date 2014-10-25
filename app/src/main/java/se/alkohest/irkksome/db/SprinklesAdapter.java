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
        final String[] createStatements = SQLMapper.getFullCreateStatement(new Class[] {IrcUserEB.class});
        Sprinkles sprinkles = Sprinkles.init(context);
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                for (String createStatement : createStatements) {
                    sqLiteDatabase.execSQL(
                            createStatement
                    );
                }
            }
        });
    }

    public OneQuery read(Class bean, String selection, String[] selectionArgs) {
        return Query.one(bean, selection, selectionArgs);
    }
}
