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
 * 自定义PagerAdapter，实现引导页
 * 
 * @author caojian
 * 
 */
public class GuidePagerAdapter extends PagerAdapter {
	/* PagerAdapter数据源 */
	List<View> views = null;
	/* 当前上下文 */
	Context context = null;
	/*SharedPreferences保存当前标识*/
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
		Log.i("===position位置===", "位置"+position);
		
		//判断是否是最后一个界面
		if(position==views.size()-1){
			Log.i("===btn_guide_ok===", "设置点击"+position);
			Button mEntryButton = (Button)container.findViewById(R.id.btn_guide_ok);
			/*设置，跳转到主界面*/
			mEntryButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.i("===onclick====", "进入事件");
					/* 设置标识，表示不再显示引导页 */
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
























