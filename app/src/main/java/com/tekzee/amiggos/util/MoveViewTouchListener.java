package com.tekzee.amiggos.util;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MoveViewTouchListener
	implements View.OnTouchListener
{
	private GestureDetector mGestureDetector;
	private View mView;
	public static float xDirection,ydirection;



	public MoveViewTouchListener(View view)
	{
		mGestureDetector = new GestureDetector(view.getContext(), mGestureListener);
		mView = view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		return mGestureDetector.onTouchEvent(event);
	}

	private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener()
	{
		private float mMotionDownX, mMotionDownY;

		@Override
		public boolean onDown(MotionEvent e)
		{
			mMotionDownX = e.getRawX() - mView.getTranslationX();
			mMotionDownY = e.getRawY() - mView.getTranslationY();
			xDirection =  mMotionDownX;
			ydirection =  mMotionDownY;
			Log.e("x, y,---->",xDirection+"--------"+ydirection);
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			mView.setTranslationX(e2.getRawX() - mMotionDownX);
			mView.setTranslationY(e2.getRawY() - mMotionDownY);
			return true;
		}
	};
}