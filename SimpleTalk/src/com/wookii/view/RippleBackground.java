package com.wookii.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wookii.simpletalk.R;

public class RippleBackground extends RelativeLayout {

	private Paint paint;
	private int rippleColor;
	private Context context;
	private int y;
	private int x;
	private RadiusUpdateListener listener;

	public RippleBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		this.context = context;
		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground);
	    rippleColor = typedArray.getColor(R.styleable.RippleBackground_rb_color, getResources().getColor(R.color.rippelColor));
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(rippleColor);
		typedArray.recycle();
	}

	@SuppressWarnings("unused")
	public void ripple(int x, int y) {
		final MyCircleView myCircleView = new MyCircleView(context,x,y,rippleColor);
		LayoutParams rippleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		Log.i("x.y", x + "/" + y);
		addView(myCircleView, rippleParams);
		int max = Math.max(getWidth(), getHeight());
		ValueAnimator anim = ValueAnimator.ofFloat(1, max + 30);
		anim.setDuration(300);
		anim.setInterpolator(new AccelerateInterpolator());
		anim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float value = (float)animation.getAnimatedValue();
				myCircleView.setRadius(value);
				myCircleView.invalidate();
				if(listener != null) {
					listener.onRadiusUpdate(value);
				}
			}
		});
		
		anim.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				if(listener != null) {
					listener.onRadiusEnd(animation);
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		anim.start();
		
	}
	
	public void setOnRadiusUpdateListener(RadiusUpdateListener listener){
		this.listener = listener;
	}
	
	public interface RadiusUpdateListener{
		public abstract void onRadiusUpdate(float values);

		public abstract void onRadiusEnd(Animator animation);
	}
	class MyCircleView extends View {

		private int x;
		private int y;
		private float radius;
		private int rippleColor;

		public MyCircleView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}
		
		public void setRadius(float radius) {
			// TODO Auto-generated method stub
			this.radius = radius;
		}

		public MyCircleView(Context context) {
			// TODO Auto-generated constructor stub
			super(context);
		}

		public MyCircleView(Context context, int x, int y, int rippleColor) {
			// TODO Auto-generated constructor stub
			super(context);
			this.x = x;
			this.y = y;
			this.rippleColor = rippleColor;
		}

		@Override
		 protected void onDraw(Canvas canvas) {
		  // TODO Auto-generated method stub
		  super.onDraw(canvas);
		  //绘制黑色背景
		  canvas.drawColor(Color.TRANSPARENT);
		  //创建画笔
		  Paint paint = new Paint();
		  //设置画笔颜色为红色
		  paint.setColor(rippleColor);
		  //画圆
		  canvas.drawCircle(x, y, radius, paint);
		 }
	}

	public void ripple(View v) {
		// TODO Auto-generated method stub
		x = (int) (v.getX() + v.getWidth()/2);
		y = (int) (v.getY() + v.getHeight()/2);
		ripple(x, y);
	}
}
