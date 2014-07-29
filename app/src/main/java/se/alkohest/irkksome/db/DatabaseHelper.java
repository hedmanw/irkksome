package se.alkohest.irkksome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.impl.IrcChannelEB;
import se.alkohest.irkksome.model.impl.IrcMessageEB;
import se.alkohest.irkksome.model.impl.IrcServerEB;
import se.alkohest.irkksome.model.impl.IrcUserEB;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "irkksome.db";
    private static final int DATABASE_VERSION = 1;
    public static final Log log = Log.getInstance(SQLiteOpenHelper.class);
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        log.i("Init DBHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        log.i("DB ONCREATE");
        final String[] create = SQLMapper.getFullCreateStatement(new Class[] {IrcServerEB.class, IrcChannelEB.class, IrcMessageEB.class, IrcUserEB.class, IrkksomeConnectionEB.class});
        for (String createStatement : create) {
            log.i(createStatement);
            db.execSQL(createStatement);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Alter table statements
    }
}
