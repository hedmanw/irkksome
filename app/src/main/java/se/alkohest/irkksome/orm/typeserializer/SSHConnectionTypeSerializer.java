package se.alkohest.irkksome.orm.typeserializer;

import android.content.ContentValues;
import android.database.Cursor;

import se.alkohest.irkksome.db.SprinklesAdapter;

import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.model.impl.SSHConnectionEB;
import se.emilsjolander.sprinkles.typeserializers.SqlType;
import se.emilsjolander.sprinkles.typeserializers.TypeSerializer;

public class SSHConnectionTypeSerializer implements TypeSerializer<SSHConnection> {
    @Override
    public SSHConnectionEB unpack(Cursor cursor, String name) {
        final int pkValue = cursor.getInt(cursor.getColumnIndexOrThrow(name));
        SSHConnectionEB connection =  null;
        if (pkValue != -1) {
            connection = SprinklesAdapter.findById(SSHConnectionEB.class, pkValue);
        }
        return connection;
    }

    @Override
    public void pack(SSHConnection sshConnectionEB, ContentValues contentValues, String s) {
        if (sshConnectionEB == null) {
            contentValues.put(s, -1);

        }
        else {
            sshConnectionEB.save();
            contentValues.put(s, sshConnectionEB.getId());
        }
    }

    @Override
    public String toSql(SSHConnection sshConnectionEB) {
        return String.valueOf(sshConnectionEB.getId());
    }

    @Override
    public SqlType getSqlType() {
        return SqlType.INTEGER;
    }
}
