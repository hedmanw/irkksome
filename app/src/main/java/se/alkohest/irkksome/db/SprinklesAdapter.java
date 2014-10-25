package se.alkohest.irkksome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.impl.IrcServerEB;
import se.alkohest.irkksome.model.impl.IrcUserEB;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Sprinkles;

public class SprinklesAdapter {
    Log log = Log.getInstance(getClass());

    public SprinklesAdapter(Context context) {
        Sprinkles sprinkles = Sprinkles.init(context);
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                long time = System.currentTimeMillis();
                log.i("STARTED MIGRATION...");
                execute(sqLiteDatabase, SQLMapper.getCreateStatement(IrkksomeConnectionEB.class));
//                execute(sqLiteDatabase, SQLMapper.getCreateStatement(IrcServerEB.class));
                log.i("FINISHED MIGRATION. Took " + (time-System.currentTimeMillis()) + " ms.");
            }
        });
    }

    private void execute(SQLiteDatabase sqLiteDatabase, String command) {
        log.i("Executing: " + command);
        sqLiteDatabase.execSQL(command);
    }

    public OneQuery read(Class bean, String selection, String[] selectionArgs) {
        return Query.one(bean, selection, selectionArgs);
    }
}
