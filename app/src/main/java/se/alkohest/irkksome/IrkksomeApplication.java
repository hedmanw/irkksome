package se.alkohest.irkksome;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import se.alkohest.irkksome.db.SQLMapper;
import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by wilhelm 2014-10-28.
 */
public class IrkksomeApplication extends Application {
    private static final Log log = Log.getInstance(IrkksomeApplication.class);

    @Override
    public void onCreate() {
        super.onCreate();
        final String createStatement = SQLMapper.getCreateStatement(IrkksomeConnectionEB.class);
        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                long time = System.currentTimeMillis();
                log.i("STARTED MIGRATION...");
                execute(sqLiteDatabase, createStatement);
//                execute(sqLiteDatabase, SQLMapper.getCreateStatement(IrcServerEB.class));
                log.i("FINISHED MIGRATION. Took " + (System.currentTimeMillis() - time) + " ms.");
            }
        });
    }

    private static void execute(SQLiteDatabase sqLiteDatabase, String command) {
        log.i("Executing: " + command);
        sqLiteDatabase.execSQL(command);
    }
}
