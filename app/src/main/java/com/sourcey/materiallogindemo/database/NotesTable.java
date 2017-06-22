package com.sourcey.materiallogindemo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

/**
 * Contact table
 */
public class NotesTable implements BaseColumns {
	public static final String NAME = NotesTable.class.getSimpleName();

	public static final String NOTE = "note";
	public static final String USERNAME = "userName";
	public static final String DAY = "day";
	public static final String HOUR = "hour";

	public static final class Row implements com.sourcey.materiallogindemo.database.Model {
		public static final Row EMPTY = new Row(new Bundle());

		public final boolean isEmpty;
		public final Bundle bundle;
		public final ContentValues contentValues;
		public final int id;
		public final String note;
		public final String userName;
		public final int day;
		public final int hour;


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
			note = this.bundle.getString(NotesTable.NOTE, "");
			userName = this.bundle.getString(NotesTable.USERNAME, "");
			day = this.bundle.getInt(NotesTable.DAY);
			hour = this.bundle.getInt(NotesTable.HOUR);

			contentValues = new ContentValues();
			contentValues.put(BaseColumns._ID, id);
			contentValues.put(NotesTable.NOTE, note);
			contentValues.put(NotesTable.USERNAME, userName);
			contentValues.put(NotesTable.DAY, day);
			contentValues.put(NotesTable.HOUR, hour);

		}

		/**
		 * Creates a valid instance in case cursor is valid ({@code isEmpty = false}) or an empty one ({@code isEmpty = true}
		 *
		 * @param cursor not null
		 */
		public Row(@NonNull Cursor cursor) {
			id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
			note = cursor.getString(cursor.getColumnIndex(NotesTable.NOTE));
			userName = cursor.getString(cursor.getColumnIndex(NotesTable.USERNAME));
			day = cursor.getInt(cursor.getColumnIndex(NotesTable.DAY));
			hour = cursor.getInt(cursor.getColumnIndex(NotesTable.HOUR));


			bundle = new Bundle();
			bundle.putInt(BaseColumns._ID, id);
			bundle.putString(NotesTable.NOTE, note);
			bundle.putString(NotesTable.USERNAME, userName);
			bundle.putInt(NotesTable.DAY, day);
			bundle.putInt(NotesTable.HOUR, hour);


			contentValues = new ContentValues();
			contentValues.put(BaseColumns._ID, id);
			contentValues.put(NotesTable.NOTE, note);
			contentValues.put(NotesTable.USERNAME, userName);
			contentValues.put(NotesTable.DAY, day);
			contentValues.put(NotesTable.HOUR, hour);


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
	public static final String[] projection = {BaseColumns._ID, NOTE, USERNAME , DAY , HOUR};

	public static final String createStatement = "CREATE TABLE " + NAME + " ("
		+ BaseColumns._ID + " INTEGER PRIMARY KEY, "
		+ NOTE + " TEXT ," +
			USERNAME + " TEXT," + DAY + " INTEGER , " + HOUR + " INTEGER )"
		;
}