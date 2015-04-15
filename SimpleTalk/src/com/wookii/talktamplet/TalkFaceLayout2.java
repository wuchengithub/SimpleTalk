package com.wookii.talktamplet;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.wookii.simpletalk.R;

public class TalkFaceLayout2 extends RelativeLayout implements TextWatcher{

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
	private Animator defaultAppearingAnim;
	private Animator defaultDisappearingAnim;
	private Animator defaultChangingAppearingAnim;
	private Animator defaultChangingDisappearingAnim;
	private OnFlingListener listener;

	public TalkFaceLayout2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public TalkFaceLayout2(Context context) {
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
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		initTalkFaceInput();
	}
	private void initTalkFaceInput() {
		// TODO Auto-generated method stub
		if(talkFaceInput == null){
			setTalkFaceInput((EditText) findViewById( R.id.talk_face_input));
		}
		if(talkFaceChatLayout == null) {
			talkFaceChatLayout = (LinearLayout) findViewById( R.id.talk_face_chat);
			LayoutTransition transitioner = new LayoutTransition();
			talkFaceChatLayout.setLayoutTransition(transitioner);
			transitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
			transitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
			//add
			defaultAppearingAnim  = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f).
	                setDuration(transitioner.getDuration(LayoutTransition.APPEARING));
			defaultAppearingAnim.addListener(new AnimatorListenerAdapter() {
	            public void onAnimationEnd(Animator anim) {
	                View view = (View) ((ObjectAnimator) anim).getTarget();
	                view.setAlpha(1f);
	            }
	        });
			transitioner.setAnimator(LayoutTransition.APPEARING, defaultAppearingAnim);
			
			//out
			transitioner.setDuration(LayoutTransition.DISAPPEARING, 2000);
	        defaultDisappearingAnim = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f).
	                setDuration(transitioner.getDuration(LayoutTransition.DISAPPEARING));
	        defaultDisappearingAnim.addListener(new AnimatorListenerAdapter() {
	            public void onAnimationEnd(Animator anim) {
	                View view = (View) ((ObjectAnimator) anim).getTarget();
	                view.setRotationX(0f);
	            }
	        });
	        transitioner.setAnimator(LayoutTransition.DISAPPEARING, defaultDisappearingAnim);
	        
	        //change add
	        PropertyValuesHolder pvhLeft =
	                PropertyValuesHolder.ofInt("left", 0, 1);
	        PropertyValuesHolder pvhTop =
	                PropertyValuesHolder.ofInt("top", 0, 1);
	        PropertyValuesHolder pvhRight =
	                PropertyValuesHolder.ofInt("right", 0, 1);
	        PropertyValuesHolder pvhBottom =
	                PropertyValuesHolder.ofInt("bottom", 0, 1);
	        PropertyValuesHolder pvhScaleX =
	                PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
	        PropertyValuesHolder pvhScaleY =
	                PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
	        defaultChangingAppearingAnim = ObjectAnimator.ofPropertyValuesHolder(
	                        this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).
	                setDuration(transitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
	        transitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, defaultChangingAppearingAnim);
	        defaultChangingAppearingAnim.addListener(new AnimatorListenerAdapter() {
	            public void onAnimationEnd(Animator anim) {
	                View view = (View) ((ObjectAnimator) anim).getTarget();
	                view.setScaleX(1f);
	                view.setScaleY(1f);
	            }
	        });
	        
	        //change dis
	        defaultChangingDisappearingAnim = ObjectAnimator.ofPropertyValuesHolder(
	                         this, pvhLeft, pvhTop, pvhRight, pvhBottom).
	                 setDuration(transitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
	        //transitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, defaultChangingDisappearingAnim);
		} else {
			Log.i(TAG, "talkFaceChatLayout child s count:" + talkFaceChatLayout.getChildCount());
			if(talkFaceChatLayout.getChildCount() > 3) {
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if(mInputTextCharList != null &&  mInputTextCharList.size() != 0) {
							//int index = mInputTextCharList.size() - 1;
							talkFaceChatLayout.removeViewAt(0);
							mInputTextCharList.remove(0);
							Log.i(TAG, "remove!");
						}
					}
				}, 2000);
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
			Log.i(TAG, "velocityX:" + velocityX + "/" + "velocityY:" + velocityY);
			
			float x = Math.abs(velocityX);
			float y = Math.abs(velocityY);
			if(x > y) {//左右
				boolean isLeft = true;
				if(velocityX > 0) {
					isLeft = false;
				}
				if(listener != null) {
					listener.onFlingLeft(isLeft);
				}
			} else {
				//上下
				if(velocityY < 0 && isCanSend ) {
					String string = talkFaceInput.getText().toString();
					MessageHelper.getInstance(getContext()).sendMessageForSimple(string);
					inputViewAnima(string);
					if(listener != null) {
						listener.onFlingUpSendData();
					}
				}
			}
			
			return false;
		}
	}

	public void setOnFlingListener(OnFlingListener listener){
		this.listener = listener;
	}
	public interface OnFlingListener {
		public void onFlingLeft(boolean isLeft);
		public void onFlingUpSendData();
	}

	@SuppressLint("NewApi")
	public void inputViewAnima(final String content) {
		// TODO Auto-generated method stub
		
		ValueAnimator anim = ValueAnimator.ofFloat(1f, 0f, 1f);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float f = (float)animation.getAnimatedValue();
				talkFaceInput.setAlpha(f);
				if(f == 0) {
					addChat(content);
					talkFaceInput.setText("");
				}
			}
		});
		anim.start();
	}

	protected void addChat(String content) {
		// TODO Auto-generated method stub
		if(talkFaceChatLayout != null) {
			TextView chat = new TextView(getContext());
			chat.setText(content);
			mInputTextCharList.add(chat);
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

	public void updateConversationLayout(List<EMMessage> data) {
		clearTalkFaceChatLayout();
		for (EMMessage emMessage : data) {
			if(emMessage == null) return;
			Log.i(TAG, "emMessage:" + emMessage);
			TextMessageBody body = (TextMessageBody)emMessage.getBody();
			inputViewAnima("ME: TO " + emMessage.getTo() + " say," + body.getMessage());
		}
	}

	private void clearTalkFaceChatLayout() {
		if(talkFaceChatLayout != null) {
			talkFaceChatLayout.removeAllViews();
		}
	}
	
    
	
	
}
