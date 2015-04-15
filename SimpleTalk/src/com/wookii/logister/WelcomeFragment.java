package com.wookii.logister;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.wookii.friend.FriendsHelper;
import com.wookii.simpletalk.PersonalData;
import com.wookii.simpletalk.R;
import com.wookii.view.HollowRoundView;
import com.wookii.view.HollowRoundView.HollowChangeEndListener;
import com.wookii.view.RippleBackground;
import com.wookii.view.RippleBackground.RadiusUpdateListener;

public class WelcomeFragment extends Fragment {

	protected static final String TAG = "WelcomeFragment";
	private Activity activity;
	private OnWelcomeAnimationEndListener listener;
	private String[] friend_url = new String[] {
			"http://img0.pconline.com.cn/pconline/1312/27/4072897_49_thumb.gif",
			"http://img0.pconline.com.cn/pconline/1312/27/4072897_03_thumb.gif",
			"http://img0.pconline.com.cn/pconline/1312/27/4072897_48_thumb.gif" };
	private String[] friend_id = new String[] { "1", "2", "3" };
	private String[] friend_name = new String[] { "SimpleTalk", "米哈哈", "大长今" };
	private String friend_remark;
	private String friend_gender;
	private String friend_last_words;
	private LayoutTransition layoutTransition;
	private ImageView button1, button2;
	private ViewPropertyAnimatorCompat animatorCompat;
	private TextView welText;
	private Typeface typeface;
	private RippleBackground rpb;
	private ObjectAnimator defaultAppearingAnim;
	private TextView line1;
	private TextView line2;
	private TextView line3;
	private TextView line4;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragmnet_welcome, null);
		final HollowRoundView loadingView = (HollowRoundView) view.findViewById(R.id.welcome_loding_view);
		loadingView.setOnHollowChangeEndListener(new HollowChangeEndListener() {
			
			@Override
			public void onChangeEnd(HollowRoundView view) {
				customLoadingAnimUp(view);
			}
		});
		typeface = Typeface.createFromAsset(activity.getAssets(), "mw3.otf");
		line1 = (TextView) view.findViewById(R.id.welcome_text_line1);
		line1.setTypeface(typeface);
		line2 = (TextView) view.findViewById(R.id.welcome_text_line2);
		line2.setTypeface(typeface);
		line3 = (TextView) view.findViewById(R.id.welcome_text_line3);
		line3.setTypeface(typeface);
		line4 = (TextView) view.findViewById(R.id.welcome_text_line4);
		line4.setTypeface(typeface);
		welText = (TextView) view.findViewById(R.id.welcome_text);
		typeface = Typeface.createFromAsset(activity.getAssets(), "mw3.otf");
		welText.setTypeface(typeface);
		ObjectAnimator anim = customLoadingAnim(loadingView);
		rpb = (RippleBackground) view.findViewById(R.id.reipp_b);
		welText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rpb.ripple(v);
			}
		});
		rpb.setOnRadiusUpdateListener(new RadiusUpdateListener() {

			@Override
			public void onRadiusUpdate(float values) {
				// TODO Auto-generated method stub
				showTextLine(values);
			}

			@Override
			public void onRadiusEnd(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
		// checkIn(anim);

		LayoutTransition transitioner = new LayoutTransition();
		RelativeLayout textLineContent = (RelativeLayout) view
				.findViewById(R.id.welcome_line_zone);
		textLineContent.setLayoutTransition(transitioner);
		// add
		defaultAppearingAnim = ObjectAnimator.ofFloat(this, "alpha", 0.5f, 1f);
		defaultAppearingAnim.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				View view = (View) ((ObjectAnimator) animation).getTarget();
				view.setTop(view.getTop() + 1);
				view.setBottom(view.getTop() + view.getHeight() + 1);
			}
		});

		defaultAppearingAnim.addListener(new AnimatorListenerAdapter() {

			public void onAnimationEnd(Animator anim) {
				
			}
		});
		transitioner.setAnimator(LayoutTransition.APPEARING,
				defaultAppearingAnim);
		transitioner.setDuration(300);
		transitioner.setStartDelay(LayoutTransition.APPEARING, 0);
		return view;
	}

	protected void showTextLine(float values) {
		int lin1x = (int) line1.getTop();
		int lin2x = (int) line2.getTop();
		int lin3x = (int) line3.getTop();
		int lin4x = (int) line4.getTop();
		Log.i(TAG, lin1x + "/" + lin2x + "/"+ lin3x + "/"+ lin4x + "/");
		int valuesInt = (int) values;
		if(!line1.isShown() && valuesInt > lin1x) {
			Log.i(TAG, "line1.isShown()");
			line1.setVisibility(View.VISIBLE);
		} else if(!line2.isShown() && valuesInt > lin2x) {
			Log.i(TAG, "line2.isShown()");
			line2.setVisibility(View.VISIBLE);
		}else if(!line3.isShown() && valuesInt > lin3x) {
			Log.i(TAG, "line3.isShown()");
			line3.setVisibility(View.VISIBLE);
		}else if(!line4.isShown() && valuesInt > lin4x) {
			Log.i(TAG, "line4.isShown()");
			line4.setVisibility(View.VISIBLE);
		}
	}


	private ObjectAnimator customLoadingAnim(final View loadingView) {
		ObjectAnimator anim = ObjectAnimator.ofFloat(loadingView, "rotation", 0f,
				360f);
		anim.setDuration(700);
		anim.setRepeatMode(ObjectAnimator.RESTART);
		anim.setStartDelay(500);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(5/* ObjectAnimator.INFINITE */);
		anim.start();
		anim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				HollowRoundView view = (HollowRoundView) ((ObjectAnimator) animation).getTarget();
				view.setChangeWidth(welText.getWidth());
				view.changeHollow();
			}
		});
		return anim;
	}

	private void customLoadingAnimUp(final View loadingView) {
		AnimatorSet set = new AnimatorSet();
		AnimatorSet set2 = new AnimatorSet();
		float y = 300;
		ObjectAnimator step1 = ObjectAnimator.ofFloat(loadingView, "y", loadingView.getTop(), loadingView.getTop() - y);
		ObjectAnimator step2 = ObjectAnimator.ofFloat(welText, "alpha", 0f, 1f);
		ObjectAnimator step3 = ObjectAnimator.ofFloat(welText, "y", welText.getTop(), welText.getTop() - y);
		ValueAnimator  step4 = ValueAnimator.ofFloat(loadingView.getWidth(), loadingView.getWidth() - 30);
		step4.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float val = (float)animation.getAnimatedValue();
				LayoutParams layoutParams = loadingView.getLayoutParams();
	            layoutParams.width = (int) val;
	            loadingView.setLayoutParams(layoutParams);
			}
		});
		step4.setRepeatCount(ValueAnimator.INFINITE);
		step4.setRepeatMode(ValueAnimator.REVERSE);
		step4.setDuration(2000);
		set.playTogether(step1, step2, step3);
		set.setDuration(2000);
		set.setStartDelay(300);
		set.setInterpolator(new DecelerateInterpolator());
		
		set2.playSequentially(set, step4);
		set2.start();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void checkIn(ObjectAnimator anim) {
		if (PersonalData.isLogin(activity)) {
			// 加载聊天信息
			EMGroupManager.getInstance().loadAllGroups();
			EMChatManager.getInstance().loadAllConversations();
			// 加载好友列表
			try {
				FriendsHelper instance = FriendsHelper.getInstance(activity);
				EMContactManager mec = EMContactManager.getInstance();
				List<String> usernames = mec.getContactUserNames();
				int i = 0;
				for (String string : usernames) {
					Log.i(TAG, instance + "username:" + string);
					// instance.replaceFriend(
					// new Friend(friend_id[i], friend_name[i], friend_remark,
					// string, friend_url[i], friend_gender,
					// friend_last_words));
					i++;
				}

			} catch (EaseMobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (listener != null) {
			listener.onCheckLogin(PersonalData.isLogin(activity));
		}
		anim.end();
	}

	public interface OnWelcomeAnimationEndListener {
		void onCheckLogin(boolean isLogin);

	}

	public void setOnWelcomeAnimationEndListener(
			OnWelcomeAnimationEndListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}
}
