package se.alkohest.irkksome.ui.pubkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import se.alkohest.irkk.util.KeyProvider;
import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.ui.pubkey.fragment.PubkeyDisabledFragment;
import se.alkohest.irkksome.ui.pubkey.fragment.PubkeyEnabledFragment;


public class PubkeyManagementActivity extends Activity implements PubkeyManagementView, PubkeyDisabledFragment.CreatePubkeyPressListener, PubkeyEnabledFragment.PubkeyManagementListener {
    public static final String SSH_CONNECTION_PK = "sshConnectionPK";
    public static final String SSH_CONNECTION_PASSWORD= "sshConnectionPassword";
    private PubkeyManagementPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        if (KeyProvider.hasKeys()) {
            fragment = PubkeyEnabledFragment.newInstance(this);
        } else {
            fragment = PubkeyDisabledFragment.newInstance(this);
        }
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        presenter = new PubkeyManagementPresenterImpl(this);
    }

    @Override
    public void showPasswordDialog(final SSHConnection connection) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter the password for " + connection.toString()).setTitle("SSH password");
        final EditText passwordField = new EditText(this);
        passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(passwordField);
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                connection.setSshPassword(passwordField.getText().toString());
                presenter.executeUploadTask(connection);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showProgress() {
        findViewById(android.R.id.button1).setEnabled(false);
        findViewById(R.id.upload_progress).setVisibility(View.VISIBLE);
    }

    @Override
    public void uploadSuccess() {
        findViewById(R.id.upload_progress).setVisibility(View.GONE);
        Toast.makeText(PubkeyManagementActivity.this, "Key uploaded!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadFailure(String errorMessage) {
        findViewById(R.id.upload_progress).setVisibility(View.GONE);
        findViewById(android.R.id.button1).setEnabled(true);
        Toast.makeText(PubkeyManagementActivity.this, "Bad news! " + errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void uploadPressed() {
        presenter.runUploadTask(getIntent().getLongExtra(SSH_CONNECTION_PK, -1), getIntent().getStringExtra(SSH_CONNECTION_PASSWORD));
    }

    @Override
    public void createPubkey() {
        presenter.createPubkey(this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, PubkeyEnabledFragment.newInstance(this));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
