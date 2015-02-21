package com.cj.myteaculture.activity;

import java.util.List;
import java.util.Map;

import com.cj.myteaculture.config.UrlConfig;
import com.cj.myteaculture.fragment.CollectFragment;
import com.cj.myteaculture.fragment.ContentFragment;
import com.cj.myteaculture.fragment.CopyrightFragment;
import com.cj.myteaculture.fragment.FeedBackFragment;
import com.cj.myteaculture.fragment.FunTeaFragment;
import com.cj.myteaculture.fragment.FunTeaFragment.onFunctionItemClickListener;
import com.cj.myteaculture.global.MyApplication;
import com.cj.myteaculture.util.HttpClientHelper;
import com.cj.myteaculture.util.JsonHelper;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 功能设置界面，根据fragment不同，显示不同的内容
 * 
 * @author caojian
 * 
 */
public class FunctionActivity extends FragmentActivity implements
		onFunctionItemClickListener {

	private static String TAG = "FunctionActivity===";
	/* 装载Fragment的容器 */
	private FrameLayout mContainerLayout;
	/* 返回按钮 */
	private ImageView mReturnImageView;
	/* 标题显示文字 */
	private TextView mTitleView;
	/* 标题显示文字集合,0:茶百科；1：我的收藏；2：我的浏览记录；3：版权信息；4：意见反馈；5：搜索内容 */
	private String[] arrTitles = { "茶百科", "我的收藏", "浏览记录", "版本信息", "意见反馈", "" };
	/* Fragment管理器 */
	private FragmentManager fragmentManager;
	/* 每一个Fragment */
	private Fragment fragment;
	/* 缓存图片的map */
	private Map<String, Bitmap> cacheImageMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function);

		int titleId = initData();
		initLayout();
		initFragment(titleId, null);
	}

	/**
	 * 初始化界面数据
	 * 
	 * @return
	 */
	private int initData() {
		cacheImageMap = ((MyApplication) this.getApplication())
				.getCacheImgMap();
		fragmentManager = this.getSupportFragmentManager();
		Intent intent = getIntent();
		String titleStrTag = intent.getStringExtra("titleStrTag");
		int titleTag = 0;
		try {
			titleTag = Integer.valueOf(titleStrTag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return titleTag;
	}

	/**
	 * 初始化Layout中的控件
	 */
	private void initLayout() {
		mContainerLayout = (FrameLayout) this
				.findViewById(R.id.fl_function_fragmentchange);
		mTitleView = (TextView) this.findViewById(R.id.tv_function_title);
		mReturnImageView = (ImageView) this
				.findViewById(R.id.iv_function_return);

		mReturnImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onKeyDown(4, null);
			}
		});
	}

	/**
	 * 当按返回键时fragment开始出栈
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 4) {
			mTitleView.setText(arrTitles[0]);
			/* 如果栈中只有一个Fragment，销毁掉本身的activity */
			if (fragmentManager.getBackStackEntryCount() == 1) {
				finish();
			} else {
				/* 返回上一个fragment */
				fragmentManager.popBackStack();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 构建每一个Fragment
	 * 
	 * @param titleTag
	 * @param titleText
	 */
	private void initFragment(int titleTag, String text) {
		String titleStr = "";
		switch (titleTag) {
		/* 搜索以及功能导航设计界面Fragment */
		case 0: 
			titleStr = arrTitles[0];
			fragment = new FunTeaFragment(FunctionActivity.this);
			break;
		/*我的收藏*/
		case 1:
			titleStr = arrTitles[1];
			fragment=new CollectFragment("2", FunctionActivity.this);
			break;
		/*我的访问记录*/
		case 2: 
			titleStr = arrTitles[2];
			fragment=new CollectFragment("1", FunctionActivity.this);
			break;
		/*版本*/
		case 3:
			titleStr = arrTitles[3];
			fragment= new CopyrightFragment();
			break;
		/*反馈*/
		case 4:
			titleStr = arrTitles[4];
			fragment= new FeedBackFragment();
			break;
		/*搜索*/
		case 5:
			titleStr = arrTitles[5];
			mTitleView.setText(text);
			String urlStr = UrlConfig.SEARCH + "&search=" + text;
			new SearchTask(FunctionActivity.this,urlStr).execute(urlStr);
			return;
		default:
			break;
		}

		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		/* 添加到回退栈中 */
		fragmentTransaction.addToBackStack(titleStr);
		mTitleView.setText(titleStr);
		fragmentTransaction.replace(R.id.fl_function_fragmentchange, fragment);
		fragmentTransaction.commit();
	}

	/**
	 * 实现FunTeaFragment接口的点击
	 */
	@Override
	public void onItemClick(int tagTitle, String text) {
		initFragment(tagTitle, text);
	}

	/**
	 * 搜索数据异步加载处理
	 * @author caojian
	 *
	 */
	public class SearchTask extends AsyncTask<String, Void, Object> {

		/*当前activity的上下文*/
		private Context context;
		/*进度对话框*/
		ProgressDialog pd;
		/*当前搜索请求的URL*/
		private String urlStr;

		private SearchTask(Context context,String urlStr) {
			this.context = context;
			this.urlStr=urlStr;
			pd = new ProgressDialog(context);
			pd.setTitle("请稍后");
			pd.setMessage("正在加载中...");
		}

		@Override
		protected void onPreExecute() {
			pd.show();
		}

		@Override
		protected Object doInBackground(String... params) {
			String jsonStr = HttpClientHelper.loadTextFromUrlByGet(params[0],
					"utf-8");
			List<Map<String, Object>> list = JsonHelper.jsonStringToList(
					jsonStr, new String[] { "title", "source", "nickname",
							"create_time", "wap_thumb", "id" }, "data");
			return list;
		}

		@Override
		protected void onPostExecute(Object result) {
			if(result!=null){
				fragment=new ContentFragment(urlStr,(List<Map<String,Object>>)result,cacheImageMap);
				FragmentTransaction transaction=fragmentManager.beginTransaction();
				transaction.addToBackStack(null);
				transaction.replace(R.id.fl_function_fragmentchange, fragment);
				transaction.commit();
				pd.dismiss();
			}
		}

	}

}
