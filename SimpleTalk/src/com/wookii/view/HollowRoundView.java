package com.wookii.view;

import org.jivesoftware.smackx.muc.DeafOccupantInterceptor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;

import com.wookii.simpletalk.R;

public class HollowRoundView extends View {

	private static final String TAG = null;
	private int hollowLineColor;
	private Paint paint;
	private float sweepAngle = 325f;
	private final static float CHIPPING_ANGLE = 20;
	private float chippingAngle = CHIPPING_ANGLE;
	private int width;
	private int viewStartWidth = -1;
	private int viewStartHeight;
	private HollowChangeEndListener listener;

	public HollowRoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.HollowRound);
		hollowLineColor = typedArray.getColor(
				R.styleable.HollowRound_hollow_line_color, getResources()
						.getColor(R.color.rippelColor));
		paint = new Paint();
		paint.setColor(hollowLineColor);
		paint.setStrokeWidth(1);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(viewStartWidth  == -1){
			viewStartWidth = getWidth();
		} 
		canvas.drawColor(Color.TRANSPARENT);
		paint.setStyle(Paint.Style.STROKE);
		// 左弧线
		RectF leftOval = new RectF();
		leftOval.left = 0;
		leftOval.right = viewStartWidth;
		leftOval.top = 0;
		leftOval.bottom = getHeight();
		canvas.drawArc(leftOval, 90, 180 - chippingAngle, false, paint);
		
		// 右弧线
		RectF rightOval = new RectF();
		rightOval.left = getWidth() - viewStartWidth;
		rightOval.right = getWidth();
		rightOval.top = 0;
		rightOval.bottom = getHeight();
		canvas.drawArc(rightOval, -90 + chippingAngle, 180, false, paint);

	}

	public void changeHollow() {
		// TODO Auto-generated method stub
		AnimatorSet s1 = new AnimatorSet();

		ValueAnimator va = ValueAnimator.ofFloat(CHIPPING_ANGLE, 0)
				.setDuration(200);
		va.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float f = (float) animation.getAnimatedValue();
				chippingAngle = f;
				invalidate();
			}
		});

		ValueAnimator widthA = ValueAnimator.ofFloat(getWidth(), width);
		widthA.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float value = (float)animation.getAnimatedValue();
				LayoutParams layoutParams = HollowRoundView.this.getLayoutParams();
	            layoutParams.width = (int) value;
	            HollowRoundView.this.setLayoutParams(layoutParams);
			}
		});
		widthA.setDuration(1000);
		widthA.setStartDelay(300);
		widthA.setInterpolator(new DecelerateInterpolator());
		widthA.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				if(listener != null) {
					listener.onChangeEnd(HollowRoundView.this);
				}
			}
		});
		s1.playSequentially(va, widthA);
		s1.start();
	}

	public void setChangeWidth(int i) {
		// TODO Auto-generated method stub
		width = i + 100;
	}
	
	
	public void setOnHollowChangeEndListener(HollowChangeEndListener listener) {
		this.listener = listener;
	}
	public interface HollowChangeEndListener {
		public abstract void onChangeEnd(HollowRoundView view);
	}
}
