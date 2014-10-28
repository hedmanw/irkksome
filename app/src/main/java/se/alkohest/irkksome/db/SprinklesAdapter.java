package se.alkohest.irkksome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.AnnotationStripper;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Sprinkles;

public class SprinklesAdapter {
    private static final Log log = Log.getInstance(SprinklesAdapter.class);

    public static void initialize(Context context) {
        final String createStatement = SQLMapper.getCreateStatement(IrkksomeConnectionEB.class);
        Sprinkles sprinkles = Sprinkles.init(context);
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

    public static <T extends AbstractBean> T findById(Class<T> entityBean, long id) {
        return getSingle(entityBean, "SELECT * FROM " + AnnotationStripper.getTableName(entityBean) + " WHERE ID=?", new String[]{String.valueOf(id)}).get();
    }

    public static <T extends AbstractBean> CursorList<T> getAll(Class<T> entity) {
        return Query.all(entity).get();
    }

    public static <T extends AbstractBean> OneQuery<T> getSingle(Class<T> bean, String selection, String[] selectionArgs) {
        return Query.one(bean, selection, selectionArgs);
    }
}
