package com.sourcey.materiallogindemo;

import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import com.sourcey.materiallogindemo.database.DatabaseHelper;


public class Application extends android.app.Application {
	public static final String NAME = Application.class.getSimpleName();

	private DatabaseHelper databaseHelper;
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}



	@Override
	public void onCreate() {
		super.onCreate();

		// prepare default EventBus instance
		EventBus.builder()
				.sendNoSubscriberEvent(false)
				.sendSubscriberExceptionEvent(false)
				.installDefaultEventBus();

		user = "";

		// set database

		final String databaseName = getString(R.string.database_helper_name);
		final int databaseVersion = getResources().getInteger(R.integer.database_helper_version);
		databaseHelper = new DatabaseHelper(this, databaseName, databaseVersion);
		// kick start the database or it won't do stuff
		databaseHelper.getWritableDatabase();

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
	}

	/**
	 * Can be accessed from anywhere as long as there is a context
	 *
	 * @return
	 */
	public DatabaseHelper getDatabaseHelper() {
		return databaseHelper;
	}
}