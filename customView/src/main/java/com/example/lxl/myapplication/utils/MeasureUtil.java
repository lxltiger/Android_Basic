package com.example.lxl.myapplication.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 *
 * 
 * @author Aige
 * @since 2014/11/19
 */
public final class MeasureUtil {

	public static int getScreenWidth(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
    public static int getScreenHeight(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

}
