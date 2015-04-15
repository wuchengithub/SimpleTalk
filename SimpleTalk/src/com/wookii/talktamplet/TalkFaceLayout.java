package com.wookii.talktamplet;

import java.util.ArrayList;

import org.apache.http.util.LangUtils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wookii.simpletalk.R;

public class TalkFaceLayout extends ViewGroup implements TextWatcher{

	public static final String TAG = "TalkFaceLayout";
	private GestureDetector gestureDetector;
	public boolean isCanSend = false;
	private EditText talkFaceInput;
	private TextView mInputText;
	private ArrayList<View> mInputTextCharList = new ArrayList<View>();
	private Typeface typeface;
	private int commMeasuredHeight;
	private int commMeasuredWidth;
	private LinearLayout talkFaceChatLayout;

	public TalkFaceLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public TalkFaceLayout(Context context) {
		super(context);
		init(context);
		
	}
	private void init(Context context) {
		// TODO Auto-generated method stub
		gestureDetector = new GestureDetector(context, new MySimpleOnGestureListener());
		setLongClickable(true);
		Log.i(TAG, "child Count:" + getChildCount());
		typeface = Typeface.createFromAsset(getContext().getAssets(), "mw3.otf");
	}
	
	
	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new RelativeLayout.LayoutParams(getContext(), attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		/** 
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式 
         */  
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);  
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);  
        // 计算出所有的childView的宽和高  
        measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(sizeWidth, sizeHeight);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		final int count = getChildCount();
		int charCount = 0;
		int inputCharX = 0;
		int inputCharY = 0;
		int lineCount = 0;
		int fixHeight = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                RelativeLayout.LayoutParams st =
                        (RelativeLayout.LayoutParams) child.getLayoutParams();
                int measuredHeight = child.getMeasuredHeight();
                int measuredWidth = child.getMeasuredWidth();
                //Log.i(TAG, "onlayout:" + st.leftMargin);
				if (child.getId() == R.id.text_chart) {
					fixHeight = (commMeasuredHeight - measuredHeight) / 4;
					if(lineCount == 0){
						inputCharY += st.topMargin;
						lineCount ++;
					}
					charCount ++;
					//处理第一个字符
					if(charCount == 1) {
						inputCharX += st.leftMargin + 10;
					}
					//处理回车
					if("\n".equals(((TextView) child).getText())) {
						Log.i(TAG, "onlayout:" + "回车换行");
						lineCount ++;
						charCount = 0;
						inputCharX = 0;
						fixHeight = -10;
						inputCharY += measuredHeight/2;
					} else {
						int l2 = inputCharX;
						Log.i(TAG, "fixHeight:" + fixHeight);
						int t2 = fixHeight + inputCharY;
						child.layout(l2, t2, measuredWidth + l2, measuredHeight + t2);
						inputCharX += measuredWidth;
					}
				} else {
					if(child.getId() == R.id.talk_face_chat) {
						Log.i(TAG, "talk_face_chat:" );
					}
					child.layout(st.leftMargin, st.topMargin, measuredWidth + st.leftMargin, measuredHeight + st.topMargin);
					initTalkFaceInput(child, st.leftMargin, st.topMargin, measuredWidth + st.leftMargin, measuredHeight + st.topMargin);
				}
            }
        }
        
	}
	/**
	 * 设置监听等初始化工作
	 * @param child
	 * @param j 
	 * @param i 
	 * @param topMargin 
	 * @param leftMargin 
	 */
	private void initTalkFaceInput(View child, int leftMargin, int topMargin, int i, int j) {
		// TODO Auto-generated method stub
		if(child.getId() == R.id.talk_face_input && talkFaceInput == null) {
			EditText child2 = (EditText) child;
			setTalkFaceInput(child2);
		} else if(child.getId() == R.id.talk_face_chat && talkFaceChatLayout == null){
			talkFaceChatLayout = (LinearLayout) child;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}
	
	
	class MySimpleOnGestureListener extends SimpleOnGestureListener{
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if(velocityY < 0 && isCanSend ) {
				inputViewAnima();
				Log.i(TAG, "velocityY:" + velocityY);
			}
			
			return false;
		}
	}


	@SuppressLint("NewApi")
	public void inputViewAnima() {
		// TODO Auto-generated method stub
		
		ValueAnimator anim = ValueAnimator.ofFloat(1f, 0f, 1f);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float f = (float)animation.getAnimatedValue();
				talkFaceInput.setAlpha(f);
				if(f == 0) {
					addChat(talkFaceInput);
					talkFaceInput.setText("");
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
				//addChat();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		anim.start();
	}

	protected void addChat(EditText talkFaceInput) {
		// TODO Auto-generated method stub
		if(talkFaceChatLayout != null) {
			TextView chat = new TextView(getContext());
			chat.setText(talkFaceInput.getText());
			talkFaceChatLayout.addView(chat);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		/*if(count > 0) {
			addNewCharSequence(s, start, start + count);
		} else {
			delCharSequence();
		}*/
	}


	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if(s.length() != 0) {
			//s.clear();
			isCanSend = true;
		} else {
			Log.i(TAG, "text is empty");
			isCanSend = false;
			//clearAllText();
		}
	}


	private void clearAllText() {
		// TODO Auto-generated method stub
		for (View view : mInputTextCharList) {
			removeView(view);
		}
		mInputTextCharList.clear();
	}

	private void addNewCharSequence(CharSequence s, int start, int count) {
		// TODO Auto-generated method stub
		int leftMargin = ((RelativeLayout.LayoutParams)talkFaceInput.getLayoutParams()).leftMargin;
		int topMargin = ((RelativeLayout.LayoutParams)talkFaceInput.getLayoutParams()).topMargin;
		for(int i = start ; i < count; i ++) {
			TextView mInputTextChar = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.input_text_char_layout, null);
			mInputTextChar.setTypeface(typeface);
			mInputTextChar.setText(s.charAt(i) + "");
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = leftMargin;
			layoutParams.topMargin = topMargin;
			mInputTextCharList.add(mInputTextChar);
			addView(mInputTextChar, layoutParams);
		}
	}
	
	private void delCharSequence() {
		int size = mInputTextCharList.size();
		removeView(mInputTextCharList.get(size -1));
		mInputTextCharList.remove(size -1);
	}
	
	public void setTalkFaceInput(EditText editText) {
		// TODO Auto-generated method stub
		this.talkFaceInput = editText;
		commMeasuredHeight = talkFaceInput.getMeasuredHeight();
		commMeasuredWidth = talkFaceInput.getMeasuredWidth();
		editText.addTextChangedListener(this);
		talkFaceInput.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark/*android.R.color.transparent*/));
		talkFaceInput.setTypeface(typeface);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
    
	
	
}
