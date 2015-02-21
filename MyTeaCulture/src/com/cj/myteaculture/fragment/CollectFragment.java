package com.cj.myteaculture.fragment;

import java.util.List;
import java.util.Map;

import com.cj.myteaculture.activity.R;
import com.cj.myteaculture.fragment.base.BaseListFragment;
import com.cj.myteaculture.util.SqliteDatabaseHelper;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class CollectFragment extends BaseListFragment {

	private static final String TAG = "CollectFragment";
	/* ListView数据源 */
	private List<Map<String, String>> pageDataList;
	/* sqlite数据库操作类 */
	private SqliteDatabaseHelper db;
	/* ListView的适配器 */
	private CollectAdapter collectAdapter;
	/* 首页广告容器 */
	private RelativeLayout mImagesLayout;

	public CollectFragment() {

	}

	/**
	 * 收藏和浏览的数据初始化
	 * 
	 * @param type
	 * @param context
	 */
	public CollectFragment(String type, Context context) {
		db = new SqliteDatabaseHelper(context, "myteaculture");
		if ("1".equals(type)) {
			String sqlStr = "select * from tb_teacontents";
			pageDataList = db.executeQuery(sqlStr, null);
			Log.i(TAG, "获取浏览数据count:"+pageDataList.size());
		} else {
			String sqlStr = "select * from tb_teacontents where type=?";
			pageDataList = db.executeQuery(sqlStr, new String[] { "2" });
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		/* 调用父类构造方法，初始化view布局和listview */
		super.onCreateView(inflater, container, savedInstanceState);
		mImagesLayout=(RelativeLayout) view.findViewById(R.id.rl_frgcontent_container);	
		mImagesLayout.setVisibility(View.GONE);
		/*不设置下拉加载*/
		listView.setPullLoadEnable(false);
		if(pageDataList!=null&&pageDataList.size()>0){
			collectAdapter=new CollectAdapter(getActivity(),pageDataList);
			listView.setAdapter(collectAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String idStr =pageDataList.get((int)id).get("_id").toString();
					goContentActivity(idStr);
				}
			});
		}
		/*最终返回这个view对象*/
		return view;
	}

	/**
	 * 收藏或浏览记录数据源适配器
	 * 
	 * @author caojian
	 * 
	 */
	public class CollectAdapter extends BaseAdapter {
		
		private Context context;
		private List<Map<String, String>> list;
		public CollectAdapter(Context context,List<Map<String, String>> list){
			this.context=context;
			this.list=list;
		}

		@Override
		public int getCount() {
			return pageDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return pageDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=LayoutInflater.from(context).inflate(R.layout.item_frgcontent_listview, null);
				viewHolder.title=(TextView) convertView.findViewById(R.id.tv_frgcontent_itemtitle);
				viewHolder.source=(TextView) convertView.findViewById(R.id.tv_frgcontent_itemSource);
				viewHolder.create_time=(TextView) convertView.findViewById(R.id.tv_frgcontent_itemCreateTime);
				convertView.setTag(viewHolder);				
			}else {
				viewHolder=(ViewHolder) convertView.getTag();
			}
			
			Map<String, String> map=list.get(position);
			String title = map.get("title").toString();
			String source=map.get("source").toString();
			String create_time=map.get("create_time").toString();
			
			viewHolder.title.setText(title);
			viewHolder.source.setText(source);
			viewHolder.create_time.setText(create_time);
			return convertView;
		}
		
		
		private class ViewHolder{
			private TextView title;
			private TextView source;
			private TextView create_time;
		}

	}

	/**
	 * 上拉刷新
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	/**
	 * 下拉加载
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	/**
	 * 关闭sqlite数据库连接资源
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}
}
