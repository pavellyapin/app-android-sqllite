package com.sourcey.materiallogindemo.util;

import android.app.Activity;

public class Views {
	public static boolean isActivityNull(Activity activity) {
		return activity == null || activity.isFinishing() || activity.isDestroyed();
	}
}