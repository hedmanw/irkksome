package se.alkohest.irkksome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.orm.AnnotationStripper;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Sprinkles;

public class SprinklesAdapter {
    private static Log log = Log.getInstance(SprinklesAdapter.class);

    public static void initialize(Context context) {
        Sprinkles sprinkles = Sprinkles.init(context);
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                long time = System.currentTimeMillis();
                log.i("STARTED MIGRATION...");
                execute(sqLiteDatabase, SQLMapper.getCreateStatement(IrkksomeConnectionEB.class));
//                execute(sqLiteDatabase, SQLMapper.getCreateStatement(IrcServerEB.class));
                log.i("FINISHED MIGRATION. Took " + (System.currentTimeMillis() - time) + " ms.");
            }
        });
    }

    private static void execute(SQLiteDatabase sqLiteDatabase, String command) {
        log.i("Executing: " + command);
        sqLiteDatabase.execSQL(command);
    }

    public static <T extends Model> T findById(Class<T> entityBean, long id) {
        return getSingle(entityBean, "SELECT * FROM " + AnnotationStripper.getTableName(entityBean) + " WHERE ID=?", new String[]{String.valueOf(id)}).get();
    }

    public static <T extends Model> CursorList<T> getAll(Class<T> entity) {
        return Query.all(entity).get();
    }

    public static <T extends Model> OneQuery<T> getSingle(Class<T> bean, String selection, String[] selectionArgs) {
        return Query.one(bean, selection, selectionArgs);
    }
}
