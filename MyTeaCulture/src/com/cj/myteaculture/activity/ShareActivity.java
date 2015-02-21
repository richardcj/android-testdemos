package com.cj.myteaculture.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * �˽��湦����δʵ��
 * 
 * @author caojian
 * 
 */
public class ShareActivity extends Activity {
	/* �����ͼƬ���� */
	private ImageView mBgImageView;
	/* ˫���˳���־λ */
	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		mBgImageView = (ImageView) this.findViewById(R.id.iv_shared_bg);
		mBgImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onKeyDown(4, null);
			}
		});
	}

	/**
	 * ���¼��������˳�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Timer timer = null;
			if (flag == false) {
				flag = true; // �����˳��ڶ��ε���˳��ı�ʶ
				Toast.makeText(this, "������δ��ͨ���ڰ�һ�η�����һҳ", Toast.LENGTH_SHORT)
						.show();
				timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						flag = false;
					}
				}, 2000);
			}
			else {
				this.finish();
				overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		} 
		return false;
	}
}
