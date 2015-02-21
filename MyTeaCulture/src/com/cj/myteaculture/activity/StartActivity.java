package com.cj.myteaculture.activity;

import com.cj.myteaculture.config.Const;
import com.cj.myteaculture.util.NetWorkStateHelper;
import com.cj.myteaculture.util.SharedPerferencesHelper;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

public class StartActivity extends Activity {
	/*最外层RelativeLayout*/
	private RelativeLayout mStartLayout;
	/*SharePreferenceHelper操作类*/
	private SharedPerferencesHelper spHelper;
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				goActivity(MainActivity.class);
				break;
			case 1:
				goActivity(GuideActivity.class);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		mStartLayout=(RelativeLayout) this.findViewById(R.id.rl_splash_start);	
		
		initData();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		spHelper=new SharedPerferencesHelper(this);
//		if(NetWorkStateHelper.isNetWorkConnected(StartActivity.this)){
			setAnimation();
			int isFirst=spHelper.getInt("isFirstRun");
			Log.i("first", String.valueOf(isFirst));
			if(isFirst==Const.NOT_FIRST){
				handler.sendEmptyMessageDelayed(0, 1000);
			}else {
				handler.sendEmptyMessageDelayed(1, 1000);
			}
		}
//	}
	
	/**
	 * Splash做一个动画，进入主界面
	 */
	private void setAnimation(){
		AlphaAnimation aa=new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		mStartLayout.setAnimation(aa);
		mStartLayout.startAnimation(aa);
	}
	
	/**
	 * 跳转到某个activity
	 * @param cls
	 */
	public void goActivity(Class<?> cls){
		Intent intent=new Intent(this,cls);
		startActivity(intent);
		this.finish();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}












