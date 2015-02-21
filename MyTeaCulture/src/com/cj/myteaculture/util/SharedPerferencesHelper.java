package com.cj.myteaculture.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ʹ��SharedPerference������ɾ�Ĳ�
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
	 * ���ݼ��ַ������洢һ���ַ���ֵ
	 * 
	 * @param key
	 * @param value
	 * @return �����ύ�Ƿ�ɹ�
	 */
	public boolean putString(String key, String value) {
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * ���ݼ�ֵ��ȡvalueֵ��û���ҵ�����null
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return prefs.getString(key, null);
	}
	
	/**
	 * ���ݼ��ַ������洢һ����ֵ
	 * @param key
	 * @param value
	 * @return �����ύ�Ƿ�ɹ�
	 */
	public boolean putInt(String key,int value){
		editor.putInt(key, value);
		return editor.commit();
	}
	
	/**
	 * ���ݼ�ֵ��ȡvalueֵ�����û���ҵ�����-1
	 * @param key
	 * @return
	 */
	public int getInt(String key){
		return prefs.getInt(key, -1);
	}
	
	/**
	 * ���sharedpreferences����
	 */
	public void clear(){
		editor.clear();
		editor.commit();
	}
	
	/**
	 * �رյ�ǰ����
	 */
	public void close(){
		prefs=null;
	}
}
























