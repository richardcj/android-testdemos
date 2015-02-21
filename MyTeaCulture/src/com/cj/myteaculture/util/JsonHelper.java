package com.cj.myteaculture.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


public class JsonHelper {
	/**
	 * ��json�ַ���ת��Ϊ�����ַ�������
	 * @param jsonString 
	 * @param key ��Ӧһ��key
	 * @return
	 */
	/*���磺{
	data:["a","b","c"],
	return:false
     }*/
	public static String[] jsonStringToArray(String jsonString,String key){
		JSONArray jsonArray=null;
		String[] arrString=null;
		
		try {
			if(key==null){
			jsonArray=new JSONArray(jsonString);
			}else {
				jsonArray=new JSONObject(jsonString).getJSONArray(key);
			}
			arrString=new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				arrString[i]=jsonArray.getString(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrString;
	}
	
	/**
	 * ��json�ַ���ת��Ϊmap<String,Object>����
	 * @param jsonString
	 * @param keyNames Ҫ��ȡ��json�ֶ�
	 * @param key
	 * @return
	 */
	/*{
	"data": {
		"id": "7254",
		"title": "\u897f\u6e56\u9f99\u4e95\u2014\u65b0\u8336\u9996\u53d1",
		"source": "\u4e70\u4e70\u8336",
		"wap_content": "����������κ�",
		"create_time": "19\u5c0f\u65f6\u524d ",
		"author": "\u203b\u6f47\u6d12\u54e5\u203b",
		"weiboUrl": "http:\/\/sns.maimaicha.com\/news\/detail\/7254"
	},
	"errorMessage": "success"
    }*/
	public static Map<String,Object> jsonStringToMap(String jsonString,String[] keyNames,String key){
		JSONObject jsonObject=null;
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			if(key==null){
				jsonObject=new JSONObject(jsonString);
			}else {
				jsonObject=new JSONObject(jsonString).getJSONObject(key);
			}
			for (int i = 0; i < keyNames.length; i++) {
				map.put(keyNames[i], jsonObject.getString(keyNames[i]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * ��json�ַ���ת��ΪList<Map<String,Object>>����
	 * @param jsonString
	 * @param keyNames Ҫ��ȡ��json�ֶ�
	 * @param key
	 * @return
	 */
	/*{
    "data": [
        {
            "id": "7255",
            "title": "2014����ʲôʱ������",
            "source": "�����",
            "description": "",
            "wap_thumb": "",
            "create_time": "18Сʱǰ ",
            "nickname": "������"
        },
        {
            "id": "7254",
            "title": "�����������²��׷�",
            "source": "�����",
            "description": "",
            "wap_thumb": "http://s1.sns.maimaicha.com/images/2014/03/27/20140327160436_87396_suolue3.jpg",
            "create_time": "18Сʱǰ ",
            "nickname": "���������"
        }
        ],
        "errorMessage": "success"
     }*/
	public static List<Map<String, Object>> jsonStringToList(String jsonString,String[] keyNames,String key){
		JSONArray jsonArray=null;
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			if(key==null){
				jsonArray=new JSONArray(jsonString);
			}else {
				jsonArray=new JSONObject(jsonString).getJSONArray(key);
			}
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				Map<String, Object> map=new HashMap<String, Object>();
				for(int j=0;j<keyNames.length;j++){
					map.put(keyNames[j], jsonObject.getString(keyNames[j]));
				}
				list.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
		
	
}
























