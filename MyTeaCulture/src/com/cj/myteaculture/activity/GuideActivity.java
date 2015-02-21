package com.cj.myteaculture.activity;

import java.util.ArrayList;
import java.util.List;

import com.cj.myteaculture.adapter.GuidePagerAdapter;
import com.cj.myteaculture.config.Const;
import com.cj.myteaculture.util.SharedPerferencesHelper;

import android.R.drawable;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity {
	public static final String TAG = "GuideActivity";
	/* SharedPerferences操作类 */
	private SharedPerferencesHelper spHelper;
	/* viewpager控件，轮换图片 */
	private ViewPager mImgViewPager;
	/* 小圆点标识的父容器 */
	private LinearLayout mDotlLayout;
	/* viewpager适配器 */
	private GuidePagerAdapter guidePagerAdapter;
	/* 适配器数据源，存放图片view */
	private List<View> views;
	/* 底部小圆点集合 */
	private ImageView[] dots;
	/* 引导页个数 */
	private int pageCount;
	/* 当前引导页 */
	private int pageCurrentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		initData();
		initLayout();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		pageCount = 3;
		pageCurrentIndex = 0;
		dots = new ImageView[pageCount];
		views=new ArrayList<View>();		
		Log.i(TAG, "====initData======");
	}

	/**
	 * 初始化RelativeLayout界面显示
	 */
	private void initLayout() {
		/* viewpager设置 */
		mImgViewPager = (ViewPager) this.findViewById(R.id.vp_guide_img);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Class<R.drawable> cls = R.drawable.class;
		for (int i = 0; i < pageCount; i++) {
			View view = inflater.inflate(R.layout.guide_content, null);
			LinearLayout mGuideLayout = (LinearLayout) view
					.findViewById(R.id.ll_guide_showimg);
			int imgId = 0;
			try {
				imgId = cls.getDeclaredField("slide" + (i + 1)).getInt(
						R.drawable.slide1);
			} catch (Exception e) {
				imgId = R.drawable.slide1;
				e.printStackTrace();
			}
			Log.i(TAG, "测试==imageId-->" + imgId + ",views-->" + views);
			mGuideLayout.setBackgroundResource(imgId);
			views.add(view);
		}
		guidePagerAdapter = new GuidePagerAdapter(GuideActivity.this, views);
		mImgViewPager.setAdapter(guidePagerAdapter);
		mImgViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				dots[position].setEnabled(false);
				dots[pageCurrentIndex].setEnabled(true);
				pageCurrentIndex = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		/* 引导小圆点设置 */
		mDotlLayout = (LinearLayout) this.findViewById(R.id.ll_guide_dots);
		for (int i = 0; i < pageCount; i++) {
			dots[i]=(ImageView) mDotlLayout.getChildAt(i);
			if(i==0){
				dots[i].setEnabled(false); //设置白色，即为选中
			}else {
				dots[i].setEnabled(true);  //默认都为灰色
			}
		}
	}

}


























