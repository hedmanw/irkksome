package se.alkohest.irkksome.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toolbar;

import se.alkohest.irkksome.R;

/**
 * Created by Johan on 3/1/2016.
 */
public class BaseActivity extends Activity {
    private Toolbar actionBarToolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    protected Toolbar getActionBarToolbar() {
        if (actionBarToolbar == null) {
            actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (actionBarToolbar != null) {
                setActionBar(actionBarToolbar);
            }
        }
        return actionBarToolbar;
    }
}
