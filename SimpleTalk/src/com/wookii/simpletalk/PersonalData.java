package com.wookii.simpletalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PersonalData {

	private static final String FILE_NAME ="personal_data";
	private static final int MODE = Context.MODE_PRIVATE;
	private static final String KEY_TOKEN = "token";
	private static final String USER_NAME = "user_name";
	private static final String PASS_WORD = "pass_word";
	public static void setToken(Context context, String token){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
		Editor edit = sharedPreferences.edit();
		edit.putString(KEY_TOKEN, token);
		edit.commit();
	}
	
	public static void setUserNmae(Context context, String userName){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
		Editor edit = sharedPreferences.edit();
		edit.putString(USER_NAME, userName);
		edit.commit();
	}
	
	public static void setPassword(Context context, String password){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
		Editor edit = sharedPreferences.edit();
		edit.putString(PASS_WORD, password);
		edit.commit();
	}
	
	public static String getUserName(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
		return sharedPreferences.getString(USER_NAME, "");
	}
	public static String getPassWord(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
		return sharedPreferences.getString(PASS_WORD, "");
	}
	public static String getToken(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
		String token = sharedPreferences.getString(KEY_TOKEN, null);
		if(token == null){
			throw new RuntimeException("token is null ，出现这个问题，请确保成功登陆后在调用此方法，在此之前，可以使用 isLogin进行判断");
		}
		return token;
	}
	
	public static boolean isLogin(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE);
		String token = sharedPreferences.getString(KEY_TOKEN, null);
		return  token == null ? false : true; 
	}
	
	
}
