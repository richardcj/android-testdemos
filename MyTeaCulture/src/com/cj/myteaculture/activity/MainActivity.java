package com.cj.myteaculture.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cj.myteaculture.adapter.MainFragmentPagerAdapter;
import com.cj.myteaculture.config.UrlConfig;
import com.cj.myteaculture.fragment.ContentFragment;
import com.cj.myteaculture.global.MyApplication;
import com.cj.myteaculture.util.HttpClientHelper;
import com.cj.myteaculture.util.JsonHelper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	/* 导航栏 */
	private LinearLayout mNavagationLayout;
	/* 存储每一个Fragment的ViewPager */
	private ViewPager mMainContentViewPager;
	/* 导航头的文本内容集合 */
	private TextView[] arr_tabspec;
	/* ViewPager的适配器数据源 */
	private List<Fragment> fragments;
	/* 主界面的ViewPager适配器 */
	private MainFragmentPagerAdapter mainFragmentPagerAdapter;
	/* 网络访问地址URL */
	private String[] urlStr;
	/* 全局图片缓存 */
	protected Map<String, Bitmap> cacheImageMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();
		initTabSpec();
	}

	/**
	 * 初始化网络数据
	 */
	private void initData() {
		cacheImageMap = ((MyApplication) this.getApplication())
				.getCacheImgMap();

		urlStr = new String[6];
		urlStr[0] = UrlConfig.JSON_LIST_DATA_0;
		urlStr[1] = UrlConfig.JSON_LIST_DATA_1;
		urlStr[2] = UrlConfig.JSON_LIST_DATA_2;
		urlStr[3] = UrlConfig.JSON_LIST_DATA_3;
		urlStr[4] = UrlConfig.JSON_LIST_DATA_4;
		urlStr[5] = "";

		new LoadPageDataTask(MainActivity.this).execute(urlStr[0], urlStr[1],
				urlStr[2], urlStr[3], urlStr[4]);
	}

	/**
	 * 初始化导航标签
	 */
	private void initTabSpec() {
		mNavagationLayout = (LinearLayout) this
				.findViewById(R.id.ll_main_navagation);
		arr_tabspec = new TextView[mNavagationLayout.getChildCount()];
		for (int i = 0; i < arr_tabspec.length; i++) {
			TextView view = (TextView) mNavagationLayout.getChildAt(i);

			/*此处先设置，可以避免对导航栏的一些初始化*/
			if (i == 5) {
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						goSearchActivity();
					}
				});
				break;
			}

			arr_tabspec[i] = view;
			if (i == 0) {
				arr_tabspec[i].setEnabled(false);
				arr_tabspec[i].setTextColor(Color.parseColor("#3d9d01"));
			} else {
				arr_tabspec[i].setEnabled(true);
				arr_tabspec[i].setTextColor(Color.GRAY);
				arr_tabspec[i].setBackgroundColor(Color.parseColor("#ebebeb"));
			}
			arr_tabspec[i].setTag(i);
			arr_tabspec[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mMainContentViewPager.setCurrentItem(
							(Integer) v.getTag(), false);
					/*if ((Integer) v.getTag() != 5) {
						
					} else { // 点击搜索
						goSearchActivity();
					}*/
				}
			});
		}
	}
	

	/**
	 * 初始化ViewPager布局
	 */
	private void initLayout(Map<String, List<Map<String, Object>>> result) {
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < arr_tabspec.length; i++) {
			Fragment fragment = new ContentFragment(urlStr[i],
					result.get(urlStr[i]), cacheImageMap);
			fragments.add(fragment);
		}
		mainFragmentPagerAdapter = new MainFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		mMainContentViewPager = (ViewPager) this
				.findViewById(R.id.vp_main_content);
		mMainContentViewPager.setAdapter(mainFragmentPagerAdapter);
		mMainContentViewPager
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						Log.i(TAG, "===测试滑动屏幕的位置position:"+position);
						if (position == 5) {
							goSearchActivity();
							/* 跳到搜索页，并设置返回页的内容(viewpager的第5界面为空)。 */
							arr_tabspec[4].performClick();
							return;
						}
						for (int i = 0; i < arr_tabspec.length-1; i++) {
							if (i == position) {
								arr_tabspec[i].setEnabled(false);
								arr_tabspec[i].setTextColor(Color
										.parseColor("#3d9d01"));
								arr_tabspec[i]
										.setBackgroundResource(R.drawable.tabbg);
							} else {
								arr_tabspec[i].setEnabled(true);
								arr_tabspec[i].setTextColor(Color.GRAY);
								arr_tabspec[i].setBackgroundColor(Color
										.parseColor("#ebebeb"));
							}
						}
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {

					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});
	}

	/**
	 * 跳转到搜索页
	 */
	private void goSearchActivity() {
		Intent intent = new Intent(MainActivity.this, FunctionActivity.class);
		intent.putExtra("titleStrTag", "0"); // 0表示茶百科
		startActivity(intent);
	}

	/**
	 * 返回键事件处理
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/* event.getRepeatCount()==0 */
		/* 点后退键的时候，为了防止点得过快，触发两次后退事件，故做此设置。 */
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exitApplication();
		}
		return true;
	}

	/**
	 * 弹出对话框，退出应用程序
	 */
	private void exitApplication() {
		AlertDialog.Builder dialog = new Builder(MainActivity.this);
		dialog.setTitle("操作提示");
		dialog.setMessage("是否退出");
		dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				System.exit(0);
			}
		});
		dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}

	/**
	 * 加载页面数据任务类
	 * 
	 * @author caojian
	 * 
	 */
	public class LoadPageDataTask extends AsyncTask<String, Void, Object> {
		private Context context;
		ProgressDialog dialog;

		public LoadPageDataTask(Context context) {
			this.context = context;
			dialog = new ProgressDialog(context);
			dialog.setTitle("网络访问");
			dialog.setMessage("正在加载中...");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected Object doInBackground(String... params) {
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			for (int i = 0; i < params.length; i++) {
				/* 根据每个不同URL获取数据 */
				String jsonString = HttpClientHelper.loadTextFromUrlByGet(
						params[i], "utf-8");
				/* Json字符串转化为List<Map<String, Object>>集合 */
				List<Map<String, Object>> list = JsonHelper.jsonStringToList(
						jsonString, new String[] { "title", "source",
								"nickname", "create_time", "wap_thumb", "id" },
						"data");
				map.put(params[i], list);
			}
			return map;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			initLayout((Map<String, List<Map<String, Object>>>) result);
			dialog.dismiss();
		}
	}
}
