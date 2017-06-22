package com.sourcey.materiallogindemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 *
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
	public static final String NAME = DatabaseHelper.class.getSimpleName();

	public static final class OnUpdateEvent {
		public final String tableName;

		public OnUpdateEvent(String tableName) {
			this.tableName = tableName != null ? tableName : "";
		}
	}

	public final String databaseName;
	public final int databaseVersion;

	public DatabaseHelper(Context context, String name, int version) {
		super(context, name, null, version);
		databaseName = name;
		databaseVersion = version;
	}

	public void destroy() {
		close();
	}


	public void createTable(final String tableName, SQLiteDatabase database) {
		if (TextUtils.isEmpty(tableName)) {
			return;
		}

		if (database != null && !database.isReadOnly()) {
			String statement = "";

			// add 'else if' validations for more tables
			if (UserTable.NAME.equals(tableName)) {
				statement = UserTable.createStatement;
			}

			if (NotesTable.NAME.equals(tableName)) {
				statement = NotesTable.createStatement;
			}


			if (!TextUtils.isEmpty(statement)) {
				database.execSQL(statement);
			}
		}
	}

	public void deleteTable(final String tableName, final SQLiteDatabase database) {
		if (TextUtils.isEmpty(tableName)) {
			return;
		}

		// TODO add more '&&' validations if more tables
		//if (!UserTable.NAME.equals(tableName)) {
		//	return;
		//}

		if (database != null && !database.isReadOnly()) {
			database.execSQL("DROP TABLE IF EXISTS " + tableName + ";");
		}
	}

	public void updateTable(final String tableName, final List<? extends Model> rows) throws SQLiteException {
		if (TextUtils.isEmpty(tableName) || rows == null || rows.size() == 0) {
			return;
		}

		//if (!UserTable.NAME.equals(tableName)) {
		//	return;
		//}

		final SQLiteDatabase database = getWritableDatabase();

		if (database != null) {
			database.beginTransaction();
			try {
				database.delete(tableName, null, null);

				for (Model row : rows) {
					final ContentValues contentValues = row.getContentValues();
					if (contentValues != null && contentValues.size() > 0) {
						database.insert(tableName, null, contentValues);
					}
				}
				database.setTransactionSuccessful();
			}
			finally {
				database.endTransaction();
			}

			//if (UserTable.NAME.equals(tableName)) {
				// notify listeners
				EventBus.getDefault().post(new OnUpdateEvent(UserTable.NAME));
			//}

		}
	}

	/**
	 * Validates if a table exists
	 * <p/>
	 * Will ignore if {@code tableName} is empty or null
	 *
	 * @return
	 */
	public boolean hasTable(final String tableName) {
		if (TextUtils.isEmpty(tableName)) {
			return false;
		}

		final SQLiteDatabase database = getReadableDatabase();

		if (database != null) {
			final Cursor cursor = database.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master "
				+ "WHERE tbl_name = '" + tableName + "';", null);
			if (cursor != null) {
				boolean hasTable = false;
				if (cursor.getCount() > 0) {
					hasTable = true;
				}
				cursor.close();
				return hasTable;
			}
		}
		return false;
	}

	public boolean insert(String tableName, ContentValues contentValues) throws SQLiteConstraintException {
		if (contentValues == null /*|| (!UserTable.NAME.equals(tableName)) */) {
			return false;
		}
		final SQLiteDatabase database = getWritableDatabase();
		if (database != null) {
			try {
				database.insert(tableName, null, contentValues);
			}
			catch (SQLiteConstraintException e) {
				database.insert(tableName, null, contentValues);
			}


			// database.insertWithOnConflict(tableName, null, model.toContentValues(), SQLiteDatabase.CONFLICT_REPLACE);

			//if (UserTable.NAME.equals(tableName)) {
				// notify listeners
				EventBus.getDefault().post(new OnUpdateEvent(UserTable.NAME));
			//}


			return true;
		}
		return false;
	}

	public boolean insertNotes(String username) throws SQLiteConstraintException {

		Bundle bundle = new Bundle();

		for (int i = 0; i < 7; i++) {
			for (int y = 0; y < 24; y++) {

				NotesTable.Row model = toNotesModel(username, i, y);
				ContentValues contentValues = model.contentValues;
				insert(NotesTable.NAME, contentValues);


			}


		}


	return true;
	}

	private NotesTable.Row toNotesModel(String userName , int day , int hour) {
		Bundle bundle = new Bundle();

		bundle.putInt(BaseColumns._ID, getNextID(NotesTable.NAME));
		bundle.putString(NotesTable.USERNAME, userName);
		bundle.putString(NotesTable.NOTE, "");
		bundle.putInt(NotesTable.DAY, day);
		bundle.putInt(NotesTable.HOUR, hour);

		return new NotesTable.Row(bundle);
	}

	public boolean delete(String tableName, String where) {
		//if (!UserTable.NAME.equals(tableName)) {
		//	return false;
		//}

		final SQLiteDatabase database = getWritableDatabase();
		if (database != null) {
			int result = database.delete(tableName, where, null);

			//if (UserTable.NAME.equals(tableName)) {
				EventBus.getDefault().post(new OnUpdateEvent(UserTable.NAME));
			//}

			return true;
		}
		return false;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {


		deleteTable(UserTable.NAME, database);

		createTable(UserTable.NAME, database);


		deleteTable(NotesTable.NAME, database);

		createTable(NotesTable.NAME, database);

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// when upgrading safest way is wiping tables and recreating with new/updated columns
		deleteTable(UserTable.NAME, database);
		createTable(UserTable.NAME, database);

		deleteTable(NotesTable.NAME, database);
		createTable(NotesTable.NAME, database);
	}

	@Override
	public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// when downgrading safest way is wiping tables and recreating with new/updated columns
		deleteTable(UserTable.NAME, database);
		createTable(UserTable.NAME, database);

		deleteTable(NotesTable.NAME, database);
		createTable(NotesTable.NAME, database);

	}

	public List<Model> checkUserExists(String tableName , String userName) {
		if (!UserTable.NAME.equals(tableName)) {
			return null;
		}

		final SQLiteDatabase database = getReadableDatabase();

		if (database == null) {
			return null;
		}

		Cursor cursor = database.
				rawQuery("SELECT * FROM " + tableName + " WHERE userName = ? ", new String[] {userName});
		if (cursor != null && cursor.moveToFirst()) {
			final ArrayList<Model> models = new ArrayList<>(cursor.getCount());

			do {
				try {
					if (UserTable.NAME.equals(tableName)) {
						models.add(new UserTable.Row(cursor));
					}
				}
				catch (IllegalArgumentException e) {
					// not interested
					e.printStackTrace();
				}
			}
			while (cursor.moveToNext());

			cursor.close();
			return models;
		}

		return null;
	}

	public boolean updateNote(String tableName , String userName , int day , int hour , String note) {
		if (!NotesTable.NAME.equals(tableName)) {
			return false;
		}

		final SQLiteDatabase database = getReadableDatabase();

		if (database == null) {
			return false;
		}

		database.execSQL("UPDATE " + tableName + " SET note = ?" +
				" WHERE userName = ? AND day = " + day + " AND hour = " + hour , new String[] {note , userName});

		return true;
	}

	public int getNextID(String tableName) {
		//if (!UserTable.NAME.equals(tableName)) {
			//return null;
		//}

		final SQLiteDatabase database = getReadableDatabase();

		if (database == null) {
			//return null;
		}

		Cursor cursor = database.
				rawQuery("SELECT MAX(_id) FROM " + tableName, null);
		if (cursor != null && cursor.moveToFirst()) {
			final ArrayList<Model> models = new ArrayList<>(cursor.getCount());

			int nextID = cursor.getInt(0) + 1;

			/*do {
				try {
					if (UserTable.NAME.equals(tableName)) {
						models.add(new UserTable.Row(cursor));
					}
				}
				catch (IllegalArgumentException e) {
					// not interested
					e.printStackTrace();
				}
			}
			while (cursor.moveToNext());*/

			cursor.close();
			return nextID;
		}

		return 0;
	}

	public List<Model> getDayNotes(String tableName , String userName , int day) {
		if (!NotesTable.NAME.equals(tableName)) {
			return null;
		}

		final SQLiteDatabase database = getReadableDatabase();

		if (database == null) {
			return null;
		}

		Cursor cursor = database.
				rawQuery("SELECT * FROM " + tableName + " WHERE userName = ? AND day = " + day, new String[] {userName});
		if (cursor != null && cursor.moveToFirst()) {
			final ArrayList<Model> models = new ArrayList<>(cursor.getCount());

			do {
				try {
					if (NotesTable.NAME.equals(tableName)) {
						models.add(new NotesTable.Row(cursor));
					}
				}
				catch (IllegalArgumentException e) {
					// not interested
					e.printStackTrace();
				}
			}
			while (cursor.moveToNext());

			cursor.close();
			return models;
		}
		return null;
	}

	public List<Model> getData(String tableName , String firstName , String lastName) {
		if (!NotesTable.NAME.equals(tableName)) {
			return null;
		}

		final SQLiteDatabase database = getReadableDatabase();

		if (database == null) {
			return null;
		}

		Cursor cursor = database.
				rawQuery("SELECT * FROM " + tableName + " WHERE firstName = ? AND lastName = ?", new String[] {firstName, lastName});
		if (cursor != null && cursor.moveToFirst()) {
			final ArrayList<Model> models = new ArrayList<>(cursor.getCount());

			do {
				try {
					if (NotesTable.NAME.equals(tableName)) {
						models.add(new NotesTable.Row(cursor));
					}
				}
				catch (IllegalArgumentException e) {
					// not interested
					e.printStackTrace();
				}
			}
			while (cursor.moveToNext());

			cursor.close();
			return models;
		}
		return null;
	}
}