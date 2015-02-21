package com.cj.myteaculture.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkStateHelper {
	/**
	 * �ж��Ƿ�����������
	 * @param context
	 * @return
	 */
	public static Boolean isNetWorkConnected(Context context){
		Boolean flag=false;
		if(context!=null){
			ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo=manager.getActiveNetworkInfo();
			if(networkInfo!=null){
				flag=networkInfo.isConnected();
			}
		}
		return flag;
	}
	
	/**
	 * �ж�wifi�����Ƿ�����
	 * @param context
	 * @return
	 */
	public static Boolean isWifiConnected(Context context){
		Boolean flag=false;
		if(context!=null){
			ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(networkInfo!=null){
				flag = networkInfo.isConnected();
			}
		}
		return flag;
	}
	
	/**
	 * �ж�MOBILE�����Ƿ����
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {  
	    if (context != null) {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mMobileNetworkInfo = mConnectivityManager  
	                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
	        if (mMobileNetworkInfo != null) {  
	            return mMobileNetworkInfo.isConnected();  
	        }  
	    }  
	    return false;  
	}
	
	/**
	 * ��ȡ��ǰ�������ӵ�������Ϣ
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {  
	    if (context != null) {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {  
	            return mNetworkInfo.getType();  
	        }  
	    }  
	    return -1;  
	}
}






















