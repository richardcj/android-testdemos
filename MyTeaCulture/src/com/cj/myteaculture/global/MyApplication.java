package com.cj.myteaculture.global;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application {
	/*网络缓存下来的各种图片map<网址,bitmap>*/
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
