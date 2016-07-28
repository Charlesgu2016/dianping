package com.demo.charile.dianping.dianping_client.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

//ʵ�ֱ�ǵ�д�����ȡ
public class SharedUtils {
	private static final String FILE_NAME = "dianping";
	private static final String MODE_NAME = "welcome";

	// ��ȡboolean���͵�ֵ
	public static boolean getWelcomeBoolean(Context context) {
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
				.getBoolean(MODE_NAME, false);
	}

	// д��Boolean���͵�ֵ
	public static void putWelcomeBoolean(Context context, boolean isFirst) {
		Editor editor = context.getSharedPreferences(FILE_NAME,
				Context.MODE_APPEND).edit();
		editor.putBoolean(MODE_NAME, isFirst);
		editor.commit();
	}

	// д��һ��String���͵����
	public static void putCityName(Context context, String cityName) {

		Editor editor = context.getSharedPreferences(FILE_NAME,
				Context.MODE_APPEND).edit();
		editor.putString("cityName", cityName);
		editor.commit();
	}

	// ��ȡString���͵�ֵ
	public static String getCityName(Context context) {

		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
				.getString("cityName", "ѡ�����");
	}
}
