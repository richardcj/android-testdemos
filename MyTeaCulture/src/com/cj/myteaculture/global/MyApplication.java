package com.cj.myteaculture.global;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application {
	/*���绺�������ĸ���ͼƬmap<��ַ,bitmap>*/
	private Map<String, Bitmap> cacheImgMap=null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		cacheImgMap=new HashMap<String, Bitmap>();
	}
	
	public Map<String, Bitmap> getCacheImgMap() {
		return cacheImgMap;
	}
}
