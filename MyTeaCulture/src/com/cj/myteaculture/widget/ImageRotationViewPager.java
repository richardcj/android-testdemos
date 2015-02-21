package com.cj.myteaculture.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ImageRotationViewPager extends ViewPager {

	private static final String TAG = "ImageRotationViewPager";
	/* ����ʱ�����µĵ� */
	PointF downP = new PointF();
	/* ����ʱ����ǰ�ĵ� */
	PointF curP = new PointF();
	/* �Զ����¼��ӿ� */
	OnSingleClickListener onSingleClickListener;

	public ImageRotationViewPager(Context context) {
		super(context);
	}

	public ImageRotationViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * �����¼��ӿڶ���
	 * 
	 * @param onSingleClickListener
	 */
	public void setOnSingleClickListener(
			OnSingleClickListener onSingleClickListener) {
		this.onSingleClickListener = onSingleClickListener;
	}

	@Override
	public boolean onInterceptHoverEvent(MotionEvent event) {
		/* �����ش����¼������λ�õ�ʱ�򣬷���true�� */
		/* ˵����onTouch�����ڴ˿ؼ�������ִ�д˿ؼ���onTouchEvent */
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		/* ÿ�δ���onTouchEvent�¼������µ�ǰ������ */
		curP.x = ev.getX();
		curP.y = ev.getY();

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			/* ����ʱ�����꣬�мǲ����� downP = curP �������ڸı�curP��ʱ��downPҲ��ı� */
			downP.x = ev.getX();
			downP.y = ev.getY();
			/* �˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ���ҵĲ������и��� */
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (ev.getAction() == MotionEvent.ACTION_DOWN)
			;
		{
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			/* ��upʱ���жϰ��º�̧���Ƿ�Ϊͬһ���㣬�����ͬһ���㣬��ִ���Զ������¼� */
			if (downP.x == curP.x && downP.y == curP.y) {
				onTouchSingleClick();
			}
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * �����¼�
	 */
	public void onTouchSingleClick() {
		if (onSingleClickListener != null) {
			Log.i(TAG, "===onSingleClick====");
			onSingleClickListener.onSingleClick();
		}
	}

	/**
	 * �Զ��嵥���¼��ӿ�
	 * 
	 * @author caojian
	 * 
	 */
	public interface OnSingleClickListener {
		void onSingleClick();
	}

}
