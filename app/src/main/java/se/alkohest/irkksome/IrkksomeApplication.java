package se.alkohest.irkksome;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.security.KeyPair;

import se.alkohest.irkksome.model.api.KeyPairManager;
import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.orm.typeserializer.SSHConnectionTypeSerializer;
import se.alkohest.irkksome.util.KeyProvider;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by wilhelm 2014-10-28.
 */
public class IrkksomeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final String createIrkksomeConnection = "CREATE TABLE Connection(id INTEGER PRIMARY KEY AUTOINCREMENT, host TEXT NOT NULL, port INTEGER NOT NULL, nickname TEXT NOT NULL, username TEXT, realname TEXT, irssiPassword TEXT, useSSL INTEGER NOT NULL, lastUsed INTEGER NOT NULL, sshConnectionData INTEGER NOT NULL);";
        final String createSSHConnection = "CREATE TABLE sshConnection(id INTEGER PRIMARY KEY AUTOINCREMENT, sshHost TEXT, sshUser TEXT, sshPort INTEGER NOT NULL, useKeyPair INTEGER NOT NULL);";
        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());
        sprinkles.registerType(SSHConnection.class, new SSHConnectionTypeSerializer());
//        sprinkles.registerType(SSHConnectionEB.class, new SSHConnectionTypeSerializer());
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                performMigration(sqLiteDatabase, createIrkksomeConnection);
                performMigration(sqLiteDatabase, createSSHConnection);
            }
        });

        KeyPairManager kpm = new KeyPairManager(this);
        if (kpm.hasKeyPair()) {
            try {
                KeyPair keyPair = kpm.getKeyPair();
                KeyProvider.initialize(keyPair.getPublic(), keyPair.getPrivate());
//                KeyProvider.printKeyPair();
            } catch (IOException e) {
                Log.e("irkksome", "Found keys on startup but could not load them.", e);
            }
        }
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
