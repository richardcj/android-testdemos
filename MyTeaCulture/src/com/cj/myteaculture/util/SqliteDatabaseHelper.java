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
 * sqlite数据库操作类
 * @author caojian
 *
 */
public class SqliteDatabaseHelper {
	/*用于管理和操作Sqlite数据库*/
	private SQLiteDatabase sqlitedb;
	/*由SQLiteOpenHelper继承过来，用于实现数据库的创建和更新，表的创建*/
	private MySqliteOpenHelper sqliteopen;
	//======================
	/*要创建的数据库*/
//	private static final String DB_NAME="teaculture.db";
	/*数据库版本号*/
	private static final int VERSION=1;
	/*创建表tb_contents*/
	private static final String SQL_CREATE_TABLE="create table tb_teacontents(id integer primary key,title varchar(30),source varchar(20),create_time varchar(20),type varchar(5))";
	
	/**
	 * 继承SQLiteOpenHelper类，在构造方法中分别需要传入Context,数据库名称,CursorFactory(一般传入null
	 * 
	 * 为默认数据库),数据库版本号(不能为负数)。在SQLiteOpenHelper中首先执行的是onCreate方法
	 * 
	 * 在构造函数时并没有真正创建数据库
	 * 
	 * 而在调用getWritableDatabase或者getReadableDatabase方法时才真正去创建数据库
	 * 
	 * 返回一个SQLiteDatabase对象。
	 * 
	 * 数据存储到了data/data/应用包名/databases
	 * 
	 * @author caojian
	 * 
	 */
	public class MySqliteOpenHelper extends SQLiteOpenHelper{

		/**
		 * 构造方法
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
		 * 数据库第一次创建时执行
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TABLE);
		}

		/**
		 * 数据库版本号变更时执行
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
	 * 创建数据库，返回数据库对象
	 * @param context
	 * @param name
	 */
	public SqliteDatabaseHelper(Context context,String name){
		sqliteopen=new MySqliteOpenHelper(context, name);
		sqlitedb=sqliteopen.getWritableDatabase();
	}
	
	/**
	 * 执行带有占位符的insert、update、delete语句，更新数据库，返回true或false
	 * @param sql
	 * @param bindArgs ？对应的值
	 * @return
	 */
	public boolean executeUpdate(String sql,String[] bindArgs){
		boolean flag=false;
		try {
			if(bindArgs==null){
				sqlitedb.execSQL(sql);				
			}else{
				sqlitedb.execSQL(sql,bindArgs);
//				Log.i("sql===", "数据插入执行。。。");
			}
			flag=true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return flag;
	}
	
	/**
	 * 执行占位符的select语句，返回list集合
	 * @param sql
	 * @param selectArgs ？对应的参数
	 * @return
	 */
	public List<Map<String,String>> executeQuery(String sql,String[] selectionArgs){
		Cursor cursor=selectCursor(sql, selectionArgs);
		return cursorToList(cursor);
	}
	
	/**
	 * 根据sql语句，返回查询的cursor对象
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Cursor selectCursor(String sql,String[] selectionArgs){
		return sqlitedb.rawQuery(sql, selectionArgs);
	}
	
	/**
	 * 将cursor对象转换为list集合
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
	 * 关闭sqlite数据库连接
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























