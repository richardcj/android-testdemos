package com.cj.myteaculture.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


public class JsonHelper {
	/**
	 * 将json字符串转化为所需字符串数组
	 * @param jsonString 
	 * @param key 对应一个key
	 * @return
	 */
	/*例如：{
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
	 * 将json字符串转化为map<String,Object>集合
	 * @param jsonString
	 * @param keyNames 要获取的json字段
	 * @param key
	 * @return
	 */
	/*{
	"data": {
		"id": "7254",
		"title": "\u897f\u6e56\u9f99\u4e95\u2014\u65b0\u8336\u9996\u53d1",
		"source": "\u4e70\u4e70\u8336",
		"wap_content": "所属的类的任何",
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
	 * 将json字符串转化为List<Map<String,Object>>对象
	 * @param jsonString
	 * @param keyNames 要获取的json字段
	 * @param key
	 * @return
	 */
	/*{
    "data": [
        {
            "id": "7255",
            "title": "2014春茶什么时候上市",
            "source": "买买茶",
            "description": "",
            "wap_thumb": "",
            "create_time": "18小时前 ",
            "nickname": "杯中茗"
        },
        {
            "id": "7254",
            "title": "西湖龙井—新茶首发",
            "source": "买买茶",
            "description": "",
            "wap_thumb": "http://s1.sns.maimaicha.com/images/2014/03/27/20140327160436_87396_suolue3.jpg",
            "create_time": "18小时前 ",
            "nickname": "※潇洒哥※"
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
























