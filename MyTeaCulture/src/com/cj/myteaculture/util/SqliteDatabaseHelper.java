package com.cj.myteaculture.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * sqlite���ݿ������
 * @author caojian
 *
 */
public class SqliteDatabaseHelper {
	/*���ڹ���Ͳ���Sqlite���ݿ�*/
	private SQLiteDatabase sqlitedb;
	/*��SQLiteOpenHelper�̳й���������ʵ�����ݿ�Ĵ����͸��£���Ĵ���*/
	private MySqliteOpenHelper sqliteopen;
	//======================
	/*Ҫ���������ݿ�*/
//	private static final String DB_NAME="teaculture.db";
	/*���ݿ�汾��*/
	private static final int VERSION=1;
	/*������tb_contents*/
	private static final String SQL_CREATE_TABLE="create table tb_teacontents(id integer primary key,title varchar(30),source varchar(20),create_time varchar(20),type varchar(5))";
	
	/**
	 * �̳�SQLiteOpenHelper�࣬�ڹ��췽���зֱ���Ҫ����Context,���ݿ�����,CursorFactory(һ�㴫��null
	 * 
	 * ΪĬ�����ݿ�),���ݿ�汾��(����Ϊ����)����SQLiteOpenHelper������ִ�е���onCreate����
	 * 
	 * �ڹ��캯��ʱ��û�������������ݿ�
	 * 
	 * ���ڵ���getWritableDatabase����getReadableDatabase����ʱ������ȥ�������ݿ�
	 * 
	 * ����һ��SQLiteDatabase����
	 * 
	 * ���ݴ洢����data/data/Ӧ�ð���/databases
	 * 
	 * @author caojian
	 * 
	 */
	public class MySqliteOpenHelper extends SQLiteOpenHelper{

		/**
		 * ���췽��
		 * @param context
		 * @param name
		 * @param factory
		 * @param version
		 */
		public MySqliteOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		public MySqliteOpenHelper(Context context, String name, int version){  
	        this(context,name,null,version);  
	    }  
	  
	    public MySqliteOpenHelper(Context context, String name){  
	        this(context,name,VERSION);  
	    }  

		/**
		 * ���ݿ��һ�δ���ʱִ��
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TABLE);
		}

		/**
		 * ���ݿ�汾�ű��ʱִ��
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(newVersion>oldVersion){
				db.execSQL("drop table if exists tb_contents");
				this.onCreate(db);
			}
		}
		
	}
	
	/**
	 * �������ݿ⣬�������ݿ����
	 * @param context
	 * @param name
	 */
	public SqliteDatabaseHelper(Context context,String name){
		sqliteopen=new MySqliteOpenHelper(context, name);
		sqlitedb=sqliteopen.getWritableDatabase();
	}
	
	/**
	 * ִ�д���ռλ����insert��update��delete��䣬�������ݿ⣬����true��false
	 * @param sql
	 * @param bindArgs ����Ӧ��ֵ
	 * @return
	 */
	public boolean executeUpdate(String sql,String[] bindArgs){
		boolean flag=false;
		try {
			if(bindArgs==null){
				sqlitedb.execSQL(sql);				
			}else{
				sqlitedb.execSQL(sql,bindArgs);
//				Log.i("sql===", "���ݲ���ִ�С�����");
			}
			flag=true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return flag;
	}
	
	/**
	 * ִ��ռλ����select��䣬����list����
	 * @param sql
	 * @param selectArgs ����Ӧ�Ĳ���
	 * @return
	 */
	public List<Map<String,String>> executeQuery(String sql,String[] selectionArgs){
		Cursor cursor=selectCursor(sql, selectionArgs);
		return cursorToList(cursor);
	}
	
	/**
	 * ����sql��䣬���ز�ѯ��cursor����
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Cursor selectCursor(String sql,String[] selectionArgs){
		return sqlitedb.rawQuery(sql, selectionArgs);
	}
	
	/**
	 * ��cursor����ת��Ϊlist����
	 * @param cursor
	 * @return
	 */
	public List<Map<String, String>> cursorToList(Cursor cursor){
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		String[] arrColumnName=cursor.getColumnNames();
		while(cursor.moveToNext()){
			Map<String,String> map=new HashMap<String, String>();
			for(int i=0;i<arrColumnName.length;i++){
				String col_values=cursor.getString(i);
				map.put(arrColumnName[i], col_values);
			}
			list.add(map);
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * �ر�sqlite���ݿ�����
	 */
	public void close(){
		if(sqliteopen!=null){
			sqliteopen.close();
			sqliteopen=null;
		}
		if(sqlitedb!=null){
			sqlitedb.close();
			sqlitedb=null;
		}
	}
}























