package com.cj.myteaculture.adapter;

import java.util.List;

import com.cj.myteaculture.activity.GuideActivity;
import com.cj.myteaculture.activity.MainActivity;
import com.cj.myteaculture.activity.R;
import com.cj.myteaculture.config.Const;
import com.cj.myteaculture.util.SharedPerferencesHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * �Զ���PagerAdapter��ʵ������ҳ
 * 
 * @author caojian
 * 
 */
public class GuidePagerAdapter extends PagerAdapter {
	/* PagerAdapter����Դ */
	List<View> views = null;
	/* ��ǰ������ */
	Context context = null;
	/*SharedPreferences���浱ǰ��ʶ*/
	private SharedPerferencesHelper spHelper;

	public GuidePagerAdapter(Context context, List<View> views) {
		this.context = context;
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager)container).addView(views.get(position),0);
		Log.i("===positionλ��===", "λ��"+position);
		
		//�ж��Ƿ������һ������
		if(position==views.size()-1){
			Log.i("===btn_guide_ok===", "���õ��"+position);
			Button mEntryButton = (Button)container.findViewById(R.id.btn_guide_ok);
			/*���ã���ת��������*/
			mEntryButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.i("===onclick====", "�����¼�");
					/* ���ñ�ʶ����ʾ������ʾ����ҳ */
					spHelper = new SharedPerferencesHelper(context);
					spHelper.putInt(Const.IS_FIRST_RUN, Const.NOT_FIRST);
					Intent intent=new Intent(context,MainActivity.class);
					context.startActivity(intent);
					((Activity)context).finish();
				}
			});
		}
		return views.get(position);
	}

}
























