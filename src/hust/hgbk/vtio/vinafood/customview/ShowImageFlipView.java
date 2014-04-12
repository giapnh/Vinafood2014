package hust.hgbk.vtio.vinafood.customview;

import hust.hgbk.vtio.vinafood.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class ShowImageFlipView extends ViewFlipper {

	Animation mRightInAnimation;
	Animation mRightOutAnimation;
	Animation mLeftInAnimation;
	Animation mLeftOutAnimation;
	GestureDetector mGestureDetector;

	public ShowImageFlipView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		setBackgroundColor(android.R.color.transparent);

		mRightInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
		mRightOutAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_right_out);
		mLeftInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_left_in);
		mLeftOutAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_left_out);

		final int minScaledFlingVelocity = ViewConfiguration.get(context)
		        .getScaledMinimumFlingVelocity();
		// 10 = fudge by experimentation

		mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				if (Math.abs(velocityX) > minScaledFlingVelocity) {
					final boolean right = velocityX < 0;
					if (right) {
						setInAnimation(mRightInAnimation);
						setOutAnimation(mRightOutAnimation);
						showNext();
					} else {
						setInAnimation(mLeftInAnimation);
						setOutAnimation(mLeftOutAnimation);
						showPrevious();
					}
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mGestureDetector.onTouchEvent(ev)) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
