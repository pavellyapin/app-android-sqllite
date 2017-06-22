package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sourcey.materiallogindemo.database.*;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private DatabaseHelper databaseHelper;
    private static final int REQUEST_SIGNUP = 0;
    // bigger than 0 if editing a contact, 0 if new contact
    private int id;

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);


        // set database
        databaseHelper = ((Application) this.getApplication()).getDatabaseHelper();

        // force show actionbar 'overflow' button on devices with hardware menu button
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.


        DatabaseHelper databaseHelper = getDatabaseHelper();
        id = databaseHelper.getNextID(UserTable.NAME);

        UserTable.Row model = toUserModel(name, password);
        ContentValues contentValues = model.contentValues;


        databaseHelper.insert(UserTable.NAME, contentValues);
        databaseHelper.insertNotes(name);



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private UserTable.Row toUserModel(String userName , String password) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseColumns._ID, id);
        bundle.putString(UserTable.USERNAME, userName);
        bundle.putString(UserTable.PASSWORD, password);
        bundle.putString(UserTable.FIRST_NAME, password);
        bundle.putString(UserTable.LAST_NAME, password);
        bundle.putString(UserTable.CITY, password);
        bundle.putString(UserTable.EMAIL, password);

        return new UserTable.Row(bundle);
    }

    public void onSignupSuccess() {

        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        ((Application) this.getApplication()).setUser(_nameText.getText().toString());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);

        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}