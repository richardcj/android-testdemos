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
 * �������ý��棬����fragment��ͬ����ʾ��ͬ������
 * 
 * @author caojian
 * 
 */
public class FunctionActivity extends FragmentActivity implements
		onFunctionItemClickListener {

	private static String TAG = "FunctionActivity===";
	/* װ��Fragment������ */
	private FrameLayout mContainerLayout;
	/* ���ذ�ť */
	private ImageView mReturnImageView;
	/* ������ʾ���� */
	private TextView mTitleView;
	/* ������ʾ���ּ���,0:��ٿƣ�1���ҵ��ղأ�2���ҵ������¼��3����Ȩ��Ϣ��4�����������5���������� */
	private String[] arrTitles = { "��ٿ�", "�ҵ��ղ�", "�����¼", "�汾��Ϣ", "�������", "" };
	/* Fragment������ */
	private FragmentManager fragmentManager;
	/* ÿһ��Fragment */
	private Fragment fragment;
	/* ����ͼƬ��map */
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
	 * ��ʼ����������
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
	 * ��ʼ��Layout�еĿؼ�
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
	 * �������ؼ�ʱfragment��ʼ��ջ
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 4) {
			mTitleView.setText(arrTitles[0]);
			/* ���ջ��ֻ��һ��Fragment�����ٵ������activity */
			if (fragmentManager.getBackStackEntryCount() == 1) {
				finish();
			} else {
				/* ������һ��fragment */
				fragmentManager.popBackStack();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ����ÿһ��Fragment
	 * 
	 * @param titleTag
	 * @param titleText
	 */
	private void initFragment(int titleTag, String text) {
		String titleStr = "";
		switch (titleTag) {
		/* �����Լ����ܵ�����ƽ���Fragment */
		case 0: 
			titleStr = arrTitles[0];
			fragment = new FunTeaFragment(FunctionActivity.this);
			break;
		/*�ҵ��ղ�*/
		case 1:
			titleStr = arrTitles[1];
			fragment=new CollectFragment("2", FunctionActivity.this);
			break;
		/*�ҵķ��ʼ�¼*/
		case 2: 
			titleStr = arrTitles[2];
			fragment=new CollectFragment("1", FunctionActivity.this);
			break;
		/*�汾*/
		case 3:
			titleStr = arrTitles[3];
			fragment= new CopyrightFragment();
			break;
		/*����*/
		case 4:
			titleStr = arrTitles[4];
			fragment= new FeedBackFragment();
			break;
		/*����*/
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
		/* ��ӵ�����ջ�� */
		fragmentTransaction.addToBackStack(titleStr);
		mTitleView.setText(titleStr);
		fragmentTransaction.replace(R.id.fl_function_fragmentchange, fragment);
		fragmentTransaction.commit();
	}

	/**
	 * ʵ��FunTeaFragment�ӿڵĵ��
	 */
	@Override
	public void onItemClick(int tagTitle, String text) {
		initFragment(tagTitle, text);
	}

	/**
	 * ���������첽���ش���
	 * @author caojian
	 *
	 */
	public class SearchTask extends AsyncTask<String, Void, Object> {

		/*��ǰactivity��������*/
		private Context context;
		/*���ȶԻ���*/
		ProgressDialog pd;
		/*��ǰ���������URL*/
		private String urlStr;

		private SearchTask(Context context,String urlStr) {
			this.context = context;
			this.urlStr=urlStr;
			pd = new ProgressDialog(context);
			pd.setTitle("���Ժ�");
			pd.setMessage("���ڼ�����...");
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
