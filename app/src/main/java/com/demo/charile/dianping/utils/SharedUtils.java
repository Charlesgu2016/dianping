package com.demo.charile.dianping.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SharedUtils {
	
	private static final String FIIL_NAME = "byhands";
	private static final String NODE_NAME = "welcome";
	
	//��ȡBoolean���͵�ֵ
	public static boolean getWelcomeBoolean(Context context){
		
		return context.getSharedPreferences(FIIL_NAME ,Context.MODE_PRIVATE).getBoolean(NODE_NAME, false);
	}
	
	//д��Boolean���͵�ֵ
	public static void putWelcomeBoolean(Context context ,boolean isFirst){
		
		Editor editor = context.getSharedPreferences(FIIL_NAME, Context.MODE_APPEND).edit();
		editor.putBoolean(NODE_NAME, isFirst);
		editor.commit();
	}

	//д��һ��String���͵����
	public static void putCityName(Context context ,String cityName){
		
		Editor editor = context.getSharedPreferences(FIIL_NAME, Context.MODE_APPEND).edit();
		editor.putString("cityName", cityName);
		editor.commit();
	}
	//��ȡString���͵�ֵ
	public static String getCityName(Context context){
		
		return context.getSharedPreferences(FIIL_NAME ,Context.MODE_PRIVATE).getString("cityName", "������Ϣ");
	}
}
