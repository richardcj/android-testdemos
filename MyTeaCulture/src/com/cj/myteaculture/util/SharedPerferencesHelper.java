package com.cj.myteaculture.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 使用SharedPerference进行增删改查
 * 
 * @author caojian
 * 
 */
public class SharedPerferencesHelper {
	private final static String prefsFileName = "SharedPreferencesHelper";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;

	public SharedPerferencesHelper(Context context) {
		prefs = context.getSharedPreferences(prefsFileName,
				Context.MODE_PRIVATE);
		editor = prefs.edit();
	}

	/**
	 * 根据键字符串，存储一个字符串值
	 * 
	 * @param key
	 * @param value
	 * @return 返回提交是否成功
	 */
	public boolean putString(String key, String value) {
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * 根据键值获取value值，没有找到返回null
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return prefs.getString(key, null);
	}
	
	/**
	 * 根据键字符串，存储一个数值
	 * @param key
	 * @param value
	 * @return 返回提交是否成功
	 */
	public boolean putInt(String key,int value){
		editor.putInt(key, value);
		return editor.commit();
	}
	
	/**
	 * 根据键值获取value值，如果没有找到返回-1
	 * @param key
	 * @return
	 */
	public int getInt(String key){
		return prefs.getInt(key, -1);
	}
	
	/**
	 * 清空sharedpreferences数据
	 */
	public void clear(){
		editor.clear();
		editor.commit();
	}
	
	/**
	 * 关闭当前对象
	 */
	public void close(){
		prefs=null;
	}
}
























