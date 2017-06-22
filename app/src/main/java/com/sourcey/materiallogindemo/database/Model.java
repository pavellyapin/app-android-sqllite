package com.sourcey.materiallogindemo.database;

import android.content.ContentValues;

/**
 * Exposes helper methods to convert a model to arbitrary objects needed from Android for different cases
 */
public interface Model {
	ContentValues getContentValues();
}