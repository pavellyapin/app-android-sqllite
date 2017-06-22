package com.sourcey.materiallogindemo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

/**
 * User table
 */
public class UserTable implements BaseColumns {
	public static final String NAME = UserTable.class.getSimpleName();

	public static final String USERNAME = "userName";
	public static final String PASSWORD = "password";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String CITY = "city";
	public static final String EMAIL = "email";

	/**
	 * Presents a single row from {@link UserTable} table
	 */
	public static final class Row implements Model {
		public static final Row EMPTY = new Row(new Bundle());

		public final boolean isEmpty;
		public final Bundle bundle;
		public final ContentValues contentValues;
		public final int id;
		public final String userName;
		public final String password;
		public final String firstName;
		public final String lastName;
		public final String city;
		public final String email;


		/**
		 * Create a valid instance if bundle is not null and not empty ({@code isEmpty = false}
		 * or an empty one ({@code isEmpty = true})
		 *
		 * @param bundle
		 */
		public Row(Bundle bundle) {
			this.bundle = bundle != null ? bundle : Bundle.EMPTY;

			isEmpty = this.bundle.isEmpty();

			id = this.bundle.getInt(BaseColumns._ID, 0);
			userName = this.bundle.getString(UserTable.USERNAME, "");
			password = this.bundle.getString(UserTable.PASSWORD, "");
			firstName = this.bundle.getString(UserTable.FIRST_NAME, "");
			lastName = this.bundle.getString(UserTable.LAST_NAME, "");
			city = this.bundle.getString(UserTable.CITY, "");
			email = this.bundle.getString(UserTable.EMAIL, "");

			contentValues = new ContentValues();
			contentValues.put(BaseColumns._ID, id);
			contentValues.put(UserTable.USERNAME, userName);
			contentValues.put(UserTable.PASSWORD, password);
			contentValues.put(UserTable.FIRST_NAME, firstName);
			contentValues.put(UserTable.LAST_NAME, lastName);
			contentValues.put(UserTable.CITY, city);
			contentValues.put(UserTable.EMAIL, email);

		}

		/**
		 * Creates a valid instance in case cursor is valid ({@code isEmpty = false}) or an empty one ({@code isEmpty = true}
		 *
		 * @param cursor not null
		 */
		public Row(@NonNull Cursor cursor) {
			id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
			userName = cursor.getString(cursor.getColumnIndex(UserTable.USERNAME));
			password = cursor.getString(cursor.getColumnIndex(UserTable.PASSWORD));
			firstName = cursor.getString(cursor.getColumnIndex(UserTable.FIRST_NAME));
			lastName = cursor.getString(cursor.getColumnIndex(UserTable.LAST_NAME));
			city= cursor.getString(cursor.getColumnIndex(UserTable.CITY));
			email = cursor.getString(cursor.getColumnIndex(UserTable.EMAIL));


			bundle = new Bundle();
			bundle.putInt(BaseColumns._ID, id);
			bundle.putString(UserTable.USERNAME, userName);
			bundle.putString(UserTable.PASSWORD, password);
			bundle.putString(UserTable.FIRST_NAME, firstName);
			bundle.putString(UserTable.LAST_NAME, lastName);
			bundle.putString(UserTable.CITY, city);
			bundle.putString(UserTable.EMAIL, email);


			contentValues = new ContentValues();
			contentValues.put(BaseColumns._ID, id);
			contentValues.put(UserTable.USERNAME, userName);
			contentValues.put(UserTable.PASSWORD, password);
			contentValues.put(UserTable.FIRST_NAME, firstName);
			contentValues.put(UserTable.LAST_NAME, lastName);
			contentValues.put(UserTable.CITY, city);
			contentValues.put(UserTable.EMAIL, email);


			isEmpty = bundle.isEmpty();
		}

		@Override
		public boolean equals(Object object) {
			if (object == null) {
				return false;
			}

			if (getClass() != object.getClass()) {
				return false;
			}

			if (id < 0) {
				return false;
			}

			return id == ((Row) object).id;
		}

		@Override
		public ContentValues getContentValues() {
			return contentValues;
		}
	}

	/**
	 * Column names which we need when querying table data results
	 */
	public static final String[] projection = {BaseColumns._ID, USERNAME,PASSWORD, FIRST_NAME , LAST_NAME, CITY , EMAIL};

	public static final String createStatement = "CREATE TABLE " + NAME + " ("
		+ BaseColumns._ID + " INTEGER PRIMARY KEY, "
		+ USERNAME + " TEXT ," + PASSWORD + " TEXT ," +
			FIRST_NAME + " TEXT," + LAST_NAME + " TEXT," + CITY + " TEXT," + EMAIL + " TEXT )"
		;
}