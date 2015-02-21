package com.cj.myteaculture.fragment;

import com.cj.myteaculture.activity.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class FunTeaFragment extends Fragment {

	private static final String TAG = "FunTeaFragment===";
	/* 搜索框 */
	private EditText mSearchEditText;
	/* 搜索提交按钮 */
	private ImageView mSearchImageView;
	/* 热门搜索 */
	private TextView mHotSearchTextView;
	/* 我的收藏 */
	private TextView mMyCollectTextView;
	/* 我的浏览记录 */
	private TextView mMyBrowsertTextView;
	/* 版本 */
	private TextView mCopyRighTextView;
	/* 反馈 */
	private TextView mFeedbackTextView;
	/* 自定义这个类的单击监听器 */
	private FunTeaOnClickListenerImpl funTeaOnClickListenerImpl;
	/* 接口回调类型，与activity交互 */
	private onFunctionItemClickListener funItemClickListener;
	/* 编辑框动画对象 */
	private Animation shakeAnimation;

	public FunTeaFragment() {

	}

	/**
	 * 初始化接口实例
	 * 
	 * @param context
	 */
	public FunTeaFragment(Context context) {
		funItemClickListener = (onFunctionItemClickListener) context;
		shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		funTeaOnClickListenerImpl = new FunTeaOnClickListenerImpl();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_functiontea, null);
		mSearchImageView = (ImageView) view
				.findViewById(R.id.iv_function_search);
		mSearchEditText = (EditText) view.findViewById(R.id.et_function_search);
		mHotSearchTextView = (TextView) view
				.findViewById(R.id.tv_function_hotsearch);
		mMyCollectTextView = (TextView) view
				.findViewById(R.id.tv_function_mycollect);
		mMyBrowsertTextView = (TextView) view
				.findViewById(R.id.tv_function_myvisitrecord);
		mCopyRighTextView = (TextView) view
				.findViewById(R.id.tv_function_copyright);
		mFeedbackTextView = (TextView) view
				.findViewById(R.id.tv_function_feedback);

		mSearchImageView.setOnClickListener(funTeaOnClickListenerImpl);
		mHotSearchTextView.setOnClickListener(funTeaOnClickListenerImpl);
		mMyCollectTextView.setOnClickListener(funTeaOnClickListenerImpl);
		mMyBrowsertTextView.setOnClickListener(funTeaOnClickListenerImpl);
		mCopyRighTextView.setOnClickListener(funTeaOnClickListenerImpl);
		mFeedbackTextView.setOnClickListener(funTeaOnClickListenerImpl);
		return view;
	}

	private class FunTeaOnClickListenerImpl implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int vId = v.getId();
			if (vId == mSearchImageView.getId()) {
				String searchStr = mSearchEditText.getText().toString();
				if (!searchStr.equals("")) {
					funItemClickListener.onItemClick(5, searchStr);
				} else {
					/* 没输入内容，进行摇晃动画 */
					mSearchEditText.startAnimation(shakeAnimation);
				}
			} else if (vId == mHotSearchTextView.getId()) {
				funItemClickListener.onItemClick(5, "茶");
			} else if (vId == mMyCollectTextView.getId()) {
				funItemClickListener.onItemClick(1, null);
			} else if (vId == mMyBrowsertTextView.getId()) {
				funItemClickListener.onItemClick(2, null);
			} else if (vId == mCopyRighTextView.getId()) {
				funItemClickListener.onItemClick(3, null);
			} else if (vId == mFeedbackTextView.getId()) {
				funItemClickListener.onItemClick(4, null);
			}
		}

	}

	/**
	 * 用在与activity交互时，被回调的接口
	 * 
	 * @author caojian
	 * 
	 */
	public interface onFunctionItemClickListener {
		/**
		 * 用于搜索以及其它项单机时监听 (在功能设置界面中)
		 * 
		 * @param tagTitle
		 *            点击对象的标识
		 * @param text
		 *            搜索时传递文本值，其它情况为null
		 */
		public void onItemClick(int tagTitle, String text);
	}

}
