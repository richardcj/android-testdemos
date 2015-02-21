package com.cj.myteaculture.fragment.base;

import com.cj.myteaculture.activity.ContentActivity;
import com.cj.myteaculture.activity.R;
import com.cj.myteaculture.widget.XListView;
import com.cj.myteaculture.widget.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ������Fragment��ʵ����ListView������ˢ�£���������
 * ����Ҫ��ʾListView���ݵ�Fragment���̳д���
 * @author caojian
 *
 */
public abstract class BaseListFragment extends Fragment implements IXListViewListener {
	private static final String TAG="BaseListFragment===";
	/*�Զ���ListView*/
	protected XListView listView;
	/*�������Fragment����*/
	protected View view;
	/*inflater��䲼����*/
	LayoutInflater mlayoutInflater;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mlayoutInflater=inflater;
		view=mlayoutInflater.inflate(R.layout.fragment_content, null);
		listView=(XListView) view.findViewById(R.id.lv_frgcontent_content);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(false);		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	/**
	 * ��ת������ҳ
	 * @param idStr
	 */
	protected void goContentActivity(String idStr) {
		Intent intent=new Intent(getActivity(),ContentActivity.class);
		intent.putExtra("id", idStr);
		startActivity(intent);
	}

}
