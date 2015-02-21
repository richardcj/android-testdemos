package com.cj.myteaculture.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ImageRotationViewPager extends ViewPager {

	private static final String TAG = "ImageRotationViewPager";
	/* 触摸时，按下的点 */
	PointF downP = new PointF();
	/* 触摸时，当前的点 */
	PointF curP = new PointF();
	/* 自定义事件接口 */
	OnSingleClickListener onSingleClickListener;

	public ImageRotationViewPager(Context context) {
		super(context);
	}

	public ImageRotationViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置事件接口对象
	 * 
	 * @param onSingleClickListener
	 */
	public void setOnSingleClickListener(
			OnSingleClickListener onSingleClickListener) {
		this.onSingleClickListener = onSingleClickListener;
	}

	@Override
	public boolean onInterceptHoverEvent(MotionEvent event) {
		/* 当拦截触摸事件到达此位置的时候，返回true， */
		/* 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent */
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		/* 每次触发onTouchEvent事件都记下当前的坐标 */
		curP.x = ev.getX();
		curP.y = ev.getY();

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			/* 按下时的坐标，切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变 */
			downP.x = ev.getX();
			downP.y = ev.getY();
			/* 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰 */
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (ev.getAction() == MotionEvent.ACTION_DOWN)
			;
		{
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			/* 在up时，判断按下和抬起是否为同一个点，如果是同一个点，将执行自定义点击事件 */
			if (downP.x == curP.x && downP.y == curP.y) {
				onTouchSingleClick();
			}
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 单击事件
	 */
	public void onTouchSingleClick() {
		if (onSingleClickListener != null) {
			Log.i(TAG, "===onSingleClick====");
			onSingleClickListener.onSingleClick();
		}
	}

	/**
	 * 自定义单机事件接口
	 * 
	 * @author caojian
	 * 
	 */
	public interface OnSingleClickListener {
		void onSingleClick();
	}

}
