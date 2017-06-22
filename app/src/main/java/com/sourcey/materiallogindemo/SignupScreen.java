package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.sourcey.materiallogindemo.database.DatabaseHelper;
import com.sourcey.materiallogindemo.database.UserTable;
import com.sourcey.materiallogindemo.util.Devices;
import com.sourcey.materiallogindemo.util.Views;

/**
 *
 */
public class SignupScreen extends Fragment implements View.OnClickListener{
	public static final String NAME = SignupScreen.class.getSimpleName();

	private static final String MODEL_KEY = "modelKey";
	private static final String ID_KEY = "idKey";
	private static final String TAG = "SignupActivity";

	/**
	 * Using factory pattern by exposing a static method for creating a new fragment instance
	 *
	 * @param model Model to load. If not null, will use it to show whatever provided, i.e. 'SignupScreen' button was clicked.
	 * If null will treat as new addition, i.e. 'Add' button was clicked
	 * @return
	 */
	public static SignupScreen newInstance(UserTable.Row model) {
		SignupScreen fragment = new SignupScreen();

		// add the model only if it not null
		if (model != null) {
			Bundle bundle = new Bundle();
			bundle.putBundle(MODEL_KEY, model.bundle);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	private EditText _nameText;
	private EditText _emailText;
	private EditText _passwordText;
	private Button btn_signup;
	private TextView link_login;


	// bigger than 0 if editing a contact, 0 if new contact
	private int id;
	@Override

	public void onClick(final View view) {

		signup();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.activity_signup, container, false);

		//setContentView(R.layout.activity_signup);
		_nameText = (EditText) rootView.findViewById(R.id.input_name);
		_nameText.requestFocus();
		_emailText = (EditText) rootView.findViewById(R.id.input_email);
		_passwordText = (EditText) rootView.findViewById(R.id.input_password);
		btn_signup = (Button) rootView.findViewById(R.id.btn_signup);
		link_login = (Button) rootView.findViewById(R.id.btn_login);

		btn_signup.setOnClickListener(this);


		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//enable action bar menu
		//setHasOptionsMenu(true);

		if (savedInstanceState != null && savedInstanceState.containsKey(MODEL_KEY)) {
			// from saved instance state
			try {
				//updateUi(new UserTable.Row(savedInstanceState.getBundle(MODEL_KEY)));
			}
			catch (IllegalArgumentException e) {
				// not interested
			}
			id = savedInstanceState.getInt(ID_KEY);
		}
		else {
			Bundle arguments = getArguments();
			if (arguments != null && arguments.containsKey(MODEL_KEY)) {
				// there is data provided (i.e. edit an item rather add a new item)
				UserTable.Row model = new UserTable.Row(arguments.getBundle(MODEL_KEY));
				id = model.id;
				//updateUi(model);
			}
		}
	}





	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save current values to restore when rotating
		outState.putBundle(MODEL_KEY, toUserModel().bundle);
		outState.putInt(ID_KEY, id);
	}

	private UserTable.Row toUserModel() {
		Bundle bundle = new Bundle();
		bundle.putInt(BaseColumns._ID, 0);
		bundle.putString(UserTable.USERNAME, _nameText.getText().toString());
		bundle.putString(UserTable.PASSWORD, _passwordText.getText().toString());
		bundle.putString(UserTable.FIRST_NAME, _passwordText.getText().toString());
		bundle.putString(UserTable.LAST_NAME, _passwordText.getText().toString());
		bundle.putString(UserTable.CITY, _passwordText.getText().toString());
		bundle.putString(UserTable.EMAIL, _passwordText.getText().toString());

		return new UserTable.Row(bundle);
	}

	public void signup() {
		Log.d(TAG, "Signup");

		if (!validate()) {
			onSignupFailed();
			return;
		}

		btn_signup.setEnabled(false);

	/*	final ProgressDialog progressDialog = new ProgressDialog(SignupScreen.this,
				R.style.AppTheme_Dark_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Creating Account...");
		progressDialog.show();



		String name = _nameText.getText().toString();
		String email = _emailText.getText().toString();
		String password = _passwordText.getText().toString();*/

		Activity activity = getActivity();
		UserTable.Row model = toUserModel();
		ContentValues contentValues = model.contentValues;

		// TODO: Implement your own signup logic here.

		DatabaseHelper databaseHelper = ((Application) activity.getApplication()).getDatabaseHelper();
		databaseHelper.insert(UserTable.NAME, contentValues);

// close self
		activity.getFragmentManager().popBackStackImmediate(NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);

						//onSignupSuccess();

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

	public void onSignupSuccess() {

		btn_signup.setEnabled(true);
		//setResult(RESULT_OK, null);
		//getActivity().finish();

	}

	public void onSignupFailed() {
		Toast.makeText(getActivity().getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

		btn_signup.setEnabled(true);
	}
}
