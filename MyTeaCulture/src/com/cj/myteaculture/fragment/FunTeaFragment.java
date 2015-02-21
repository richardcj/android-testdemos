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
	/* ������ */
	private EditText mSearchEditText;
	/* �����ύ��ť */
	private ImageView mSearchImageView;
	/* �������� */
	private TextView mHotSearchTextView;
	/* �ҵ��ղ� */
	private TextView mMyCollectTextView;
	/* �ҵ������¼ */
	private TextView mMyBrowsertTextView;
	/* �汾 */
	private TextView mCopyRighTextView;
	/* ���� */
	private TextView mFeedbackTextView;
	/* �Զ��������ĵ��������� */
	private FunTeaOnClickListenerImpl funTeaOnClickListenerImpl;
	/* �ӿڻص����ͣ���activity���� */
	private onFunctionItemClickListener funItemClickListener;
	/* �༭�򶯻����� */
	private Animation shakeAnimation;

	public FunTeaFragment() {

	}

	/**
	 * ��ʼ���ӿ�ʵ��
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
					/* û�������ݣ�����ҡ�ζ��� */
					mSearchEditText.startAnimation(shakeAnimation);
				}
			} else if (vId == mHotSearchTextView.getId()) {
				funItemClickListener.onItemClick(5, "��");
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
	 * ������activity����ʱ�����ص��Ľӿ�
	 * 
	 * @author caojian
	 * 
	 */
	public interface onFunctionItemClickListener {
		/**
		 * ���������Լ��������ʱ���� (�ڹ������ý�����)
		 * 
		 * @param tagTitle
		 *            �������ı�ʶ
		 * @param text
		 *            ����ʱ�����ı�ֵ���������Ϊnull
		 */
		public void onItemClick(int tagTitle, String text);
	}

}
