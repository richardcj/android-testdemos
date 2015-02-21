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
	/* 相关栏目资讯的标题 */
	private TextView mTitleView;
	/* 来源 */
	private TextView mSourceView;
	/* 创建时间 */
	private TextView mCreateTimeView;
	/* 网页内容 */
	private WebView mWapContentView;
	/* 资讯操作(返回) */
	private ImageView mBackImageView;
	/* 资讯操作(收藏) */
	private ImageView mCollectImageView;
	/* 资讯操作(分享) */
	private ImageView mShareImageView;
	/* 网络请求获取的数据 */
	private Map<String, Object> contentMap;
	/* sqlite数据库操作类 */
	SqliteDatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contentitem);

		initLayout();
		initData();
	}

	/**
	 * 初始化界面控件
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
	 * 初始化界面数据
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
	 * 栏目资讯信息的异步处理
	 * 
	 * @author caojian
	 * 
	 */
	private class ContentTask extends AsyncTask<String, Void, Object> {
		/* 上下文对象 */
		private Context context;
		/* 进度条提示对话框 */
		private ProgressDialog pd;

		public ContentTask(Context context) {
			this.context = context;
			pd = new ProgressDialog(context);
			pd.setTitle("请稍后");
			pd.setMessage("正在加载请稍后...");
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
				/* 将资讯浏览的记录添加到sqlite数据库 */
				String id = contentMap.get("id").toString();
				String title = contentMap.get("title").toString();
				String source = contentMap.get("source").toString();
				String create_time = contentMap.get("create_time").toString();
				
				Log.i(TAG, "数据值：id="+id+",title="+title+",source="+source+",create_time="+create_time);

				String sqlStr = "insert into tb_teacontents(id,title,source,create_time,type) values(?,?,?,?,?)";
				Boolean flag = db.executeUpdate(sqlStr, new String[] { id,
						title, source, create_time, "1" });
				Log.i(TAG, "是否已经存在该数据->flag:" + flag);
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
				Log.i("内容显示===", contentMap.get("wap_content").toString());
				mWapContentView.loadDataWithBaseURL(null,
						contentMap.get("wap_content").toString(), "text/html",
						"utf-8", null);
				
			}
			pd.dismiss();
		}

	}

	/**
	 * 对资讯进行的返回、收藏、分享操作
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/* 返回 */
		case R.id.iv_contentitem_back:
			this.finish();
			break;
		/* 收藏 */
		case R.id.iv_contentitem_collect:
			String sqlStr = "update tb_teacontents set type=? where id=?";
			if (contentMap != null && contentMap.size() > 0) {
				String type = "2";
				String id = contentMap.get("id").toString(); // 把浏览过的改为收藏
				db.executeQuery(sqlStr, new String[] { type, id });
				Toast.makeText(ContentActivity.this, "收藏成功", Toast.LENGTH_LONG)
						.show();
			}
			break;
		/* 分享 */
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
		/* 关闭sqlite数据库，释放资源 */
		if (db != null) {
			db.close();
		}
	}
}
