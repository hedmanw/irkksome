package se.alkohest.irkksome;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by wilhelm 2014-10-28.
 */
public class IrkksomeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final String createStatement = "CREATE TABLE Connection(id INTEGER PRIMARY KEY AUTOINCREMENT, host TEXT NOT NULL, port INTEGER NOT NULL, nickname TEXT NOT NULL, username TEXT, realname TEXT, useSSL INTEGER NOT NULL, useSSH INTEGER NOT NULL, sshHost TEXT, sshUser TEXT, sshPort INTEGER NOT NULL, sshKeySaved INTEGER NOT NULL, lastUsed INTEGER NOT NULL)";
        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                performMigration(sqLiteDatabase, createStatement);
            }
        });
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                performMigration(sqLiteDatabase, "ALTER TABLE Connection ADD COLUMN irssiPassword TEXT;");
            }
        });
    }

    private static void performMigration(SQLiteDatabase sqLiteDatabase, String command) {
        long time = System.currentTimeMillis();
        Log.d("irkksome", "STARTED MIGRATION...");
        execute(sqLiteDatabase, command);
        Log.d("irkksome", "FINISHED MIGRATION. Took " + (System.currentTimeMillis() - time) + " ms.");
    }

    private static void execute(SQLiteDatabase sqLiteDatabase, String command) {
        Log.d("irkksome", "Executing: " + command);
        sqLiteDatabase.execSQL(command);
    }
}
