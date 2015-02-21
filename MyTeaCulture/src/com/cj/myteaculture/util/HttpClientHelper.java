package com.cj.myteaculture.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.R.string;
import android.util.Log;


/**
 * ���װ�httpclient����
 * 
 * @author caojian
 * 
 */
public class HttpClientHelper {

	/**
	 * ͨ��get���󷵻��ֽ�����
	 * 
	 * @param url
	 * @return
	 */
	public static byte[] loadByteFromUrlByGet(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entry = response.getEntity();
				return EntityUtils.toByteArray(entry);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ͨ��get���󷵻�������
	 * 
	 * @param url
	 * @return
	 */
	public static InputStream loadFileFromUrlByGet(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entry = response.getEntity();
				return entry.getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ͨ��get���󷵻��ַ���
	 * 
	 * @param url
	 * @return
	 */
	public static String loadTextFromUrlByGet(String url,String charset) {
		String result="";
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entry = response.getEntity();
				result = EntityUtils.toString(entry,charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("httpclient===", result);
		return result;
	}

	/**
	 * ͨ��post���󷵻��ַ���
	 * 
	 * @param url
	 *            ����url
	 * @param params
	 *            url�����Ĳ���
	 * @return
	 */
	public static String loadTextFromUrlByPost(String url,
			Map<String, String> params) {
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response;
		try {
			List<NameValuePair> listParams = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : params.entrySet()) {
				NameValuePair nameValuePair = new BasicNameValuePair(
						entry.getKey(), entry.getValue());
				listParams.add(nameValuePair);
			}
			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(
					listParams, "utf-8");
			httpPost.setEntity(urlEncoded);
			response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
