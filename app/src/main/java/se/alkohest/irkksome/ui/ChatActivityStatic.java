package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;

public class ChatActivityStatic {
    public static void onCreate(Activity activity) {
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getActionBar().setHomeButtonEnabled(true);
    }

    public static void showNickChangeDialog(Context context, final Server activeServer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText popup = new EditText(context);
        builder.setTitle("Change nick on " + activeServer.getBackingBean().getUrl());
        builder.setView(popup);
        builder.setPositiveButton(R.string.change_nick, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activeServer.changeNick(popup.getText().toString());
            }
        });
        builder.create().show();
    }

    public static void showJoinChannelDialog(Context context, final Server activeServer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText popup = new EditText(context);
        builder.setTitle("Join channel on " + activeServer.getBackingBean().getUrl());
        builder.setView(popup);
        builder.setPositiveButton(R.string.join_channel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activeServer.joinChannel(popup.getText().toString());
            }
        });
        builder.create().show();
    }
}
