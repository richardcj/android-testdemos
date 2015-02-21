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
	/* ListView����Դ */
	private List<Map<String, String>> pageDataList;
	/* sqlite���ݿ������ */
	private SqliteDatabaseHelper db;
	/* ListView�������� */
	private CollectAdapter collectAdapter;
	/* ��ҳ������� */
	private RelativeLayout mImagesLayout;

	public CollectFragment() {

	}

	/**
	 * �ղغ���������ݳ�ʼ��
	 * 
	 * @param type
	 * @param context
	 */
	public CollectFragment(String type, Context context) {
		db = new SqliteDatabaseHelper(context, "myteaculture");
		if ("1".equals(type)) {
			String sqlStr = "select * from tb_teacontents";
			pageDataList = db.executeQuery(sqlStr, null);
			Log.i(TAG, "��ȡ�������count:"+pageDataList.size());
		} else {
			String sqlStr = "select * from tb_teacontents where type=?";
			pageDataList = db.executeQuery(sqlStr, new String[] { "2" });
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		/* ���ø��๹�췽������ʼ��view���ֺ�listview */
		super.onCreateView(inflater, container, savedInstanceState);
		mImagesLayout=(RelativeLayout) view.findViewById(R.id.rl_frgcontent_container);	
		mImagesLayout.setVisibility(View.GONE);
		/*��������������*/
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
		/*���շ������view����*/
		return view;
	}

	/**
	 * �ղػ������¼����Դ������
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
	 * ����ˢ��
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	/**
	 * ��������
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	/**
	 * �ر�sqlite���ݿ�������Դ
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}
}
