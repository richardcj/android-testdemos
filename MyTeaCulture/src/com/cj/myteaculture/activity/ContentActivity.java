package com.cj.myteaculture.activity;

import java.util.Map;

import com.cj.myteaculture.config.UrlConfig;
import com.cj.myteaculture.util.HttpClientHelper;
import com.cj.myteaculture.util.JsonHelper;
import com.cj.myteaculture.util.SqliteDatabaseHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentActivity extends Activity implements OnClickListener {

	private static final String TAG = "ContentActivity===";
	/* �����Ŀ��Ѷ�ı��� */
	private TextView mTitleView;
	/* ��Դ */
	private TextView mSourceView;
	/* ����ʱ�� */
	private TextView mCreateTimeView;
	/* ��ҳ���� */
	private WebView mWapContentView;
	/* ��Ѷ����(����) */
	private ImageView mBackImageView;
	/* ��Ѷ����(�ղ�) */
	private ImageView mCollectImageView;
	/* ��Ѷ����(����) */
	private ImageView mShareImageView;
	/* ���������ȡ������ */
	private Map<String, Object> contentMap;
	/* sqlite���ݿ������ */
	SqliteDatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contentitem);

		initLayout();
		initData();
	}

	/**
	 * ��ʼ������ؼ�
	 */
	private void initLayout() {
		mTitleView = (TextView) this.findViewById(R.id.tv_contentitem_title);
		mSourceView = (TextView) this.findViewById(R.id.tv_contentitem_source);
		mCreateTimeView = (TextView) this
				.findViewById(R.id.tv_contentitem_createtime);
		mWapContentView = (WebView) this
				.findViewById(R.id.wv_contentitem_wapcontent);

		mBackImageView = (ImageView) this
				.findViewById(R.id.iv_contentitem_back);
		mCollectImageView = (ImageView) this
				.findViewById(R.id.iv_contentitem_collect);
		mShareImageView = (ImageView) this
				.findViewById(R.id.iv_contentitem_share);

		mBackImageView.setOnClickListener(this);
		mCollectImageView.setOnClickListener(this);
		mShareImageView.setOnClickListener(this);
	}

	/**
	 * ��ʼ����������
	 */
	private void initData() {
		db = new SqliteDatabaseHelper(this, "myteaculture");
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		if (id != null) {
			new ContentTask(ContentActivity.this).execute(UrlConfig.CONTENT
					+ "&id=" + id);
		}
	}

	/**
	 * ��Ŀ��Ѷ��Ϣ���첽����
	 * 
	 * @author caojian
	 * 
	 */
	private class ContentTask extends AsyncTask<String, Void, Object> {
		/* �����Ķ��� */
		private Context context;
		/* ��������ʾ�Ի��� */
		private ProgressDialog pd;

		public ContentTask(Context context) {
			this.context = context;
			pd = new ProgressDialog(context);
			pd.setTitle("���Ժ�");
			pd.setMessage("���ڼ������Ժ�...");
		}

		@Override
		protected void onPreExecute() {
			pd.show();

		}

		@Override
		protected Object doInBackground(String... params) {
			String jsonStr = HttpClientHelper.loadTextFromUrlByGet(params[0],
					"utf-8");
			Map<String, Object> contentMap = JsonHelper.jsonStringToMap(
					jsonStr, new String[] { "id", "title", "source",
							"wap_content", "create_time" }, "data");
			if (contentMap != null && contentMap.size() > 0) {
				/* ����Ѷ����ļ�¼��ӵ�sqlite���ݿ� */
				String id = contentMap.get("id").toString();
				String title = contentMap.get("title").toString();
				String source = contentMap.get("source").toString();
				String create_time = contentMap.get("create_time").toString();
				
				Log.i(TAG, "����ֵ��id="+id+",title="+title+",source="+source+",create_time="+create_time);

				String sqlStr = "insert into tb_teacontents(id,title,source,create_time,type) values(?,?,?,?,?)";
				Boolean flag = db.executeUpdate(sqlStr, new String[] { id,
						title, source, create_time, "1" });
				Log.i(TAG, "�Ƿ��Ѿ����ڸ�����->flag:" + flag);
			}
			return contentMap;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result != null) {
				contentMap = (Map<String, Object>) result;
				mTitleView.setText(contentMap.get("title").toString());
				mSourceView.setText(contentMap.get("source").toString());
				mCreateTimeView.setText(contentMap.get("create_time")
						.toString());
				Log.i("������ʾ===", contentMap.get("wap_content").toString());
				mWapContentView.loadDataWithBaseURL(null,
						contentMap.get("wap_content").toString(), "text/html",
						"utf-8", null);
				
			}
			pd.dismiss();
		}

	}

	/**
	 * ����Ѷ���еķ��ء��ղء��������
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/* ���� */
		case R.id.iv_contentitem_back:
			this.finish();
			break;
		/* �ղ� */
		case R.id.iv_contentitem_collect:
			String sqlStr = "update tb_teacontents set type=? where id=?";
			if (contentMap != null && contentMap.size() > 0) {
				String type = "2";
				String id = contentMap.get("id").toString(); // ��������ĸ�Ϊ�ղ�
				db.executeQuery(sqlStr, new String[] { type, id });
				Toast.makeText(ContentActivity.this, "�ղسɹ�", Toast.LENGTH_LONG)
						.show();
			}
			break;
		/* ���� */
		case R.id.iv_contentitem_share:
			Intent intent = new Intent(ContentActivity.this,
					ShareActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/* �ر�sqlite���ݿ⣬�ͷ���Դ */
		if (db != null) {
			db.close();
		}
	}
}
