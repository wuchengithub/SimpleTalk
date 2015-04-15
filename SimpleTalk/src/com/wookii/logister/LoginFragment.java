package com.wookii.logister;

import java.util.HashMap;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.kale.activityoptions.ActivityCompatICS;
import com.kale.activityoptions.ActivityOptionsCompatICS;
import com.wookii.simpletalk.MainActivity;
import com.wookii.simpletalk.PersonalData;
import com.wookii.simpletalk.R;

public class LoginFragment extends Fragment {

	
	private static final String TAG = "LoginFragment";
	private EditText edit_name;
	private EditText edit_password;
	private Button btn_login;
	private Activity activity;
	private String name;
	private String password;
	private View userIcon;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.fragment_login, null);
		edit_name = (EditText)root.findViewById(R.id.login_person_name);
		name = PersonalData.getUserName(activity);
		edit_password = (EditText)root.findViewById(R.id.login_password);
		password = PersonalData.getPassWord(activity);
		btn_login = (Button)root.findViewById(R.id.login_button);
		btn_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});
		userIcon = root.findViewById(R.id.login_user_icon);
		return root;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		edit_name.setText(name);
		edit_password.setText(password);
		super.onStart();
	}
	/**
	 * 执行登陆
	 */
	public void login(){
		boolean b = true;
		EditText[] views = new EditText[]{edit_name, edit_password};
		for (EditText view : views) {
			if(isIllegal(view)){
				view.setError("输入错误！", getResources().getDrawable(android.R.drawable.ic_dialog_alert));
				view.requestFocus();
				b = false;
				break;
			}
		}
		if(b){
			final HashMap<String, String> values = getloginValues();
			EMChatManager.getInstance().login(values.get("name"), values.get("password"),
					new EMCallBack() {//回调
						@Override
						public void onSuccess() {
							activity.runOnUiThread(new Runnable() {
								public void run() {
									String accessToken = EMChatManager.getInstance().getAccessToken();
									Log.d("main", "登陆聊天服务器成功！" + accessToken);
									PersonalData.setToken(activity, accessToken);
									PersonalData.setUserNmae(activity, values.get("name"));
									PersonalData.setPassword(activity, values.get("password"));
									getInHome();
								}
							});
						}

						@Override
						public void onProgress(int progress, String status) {

						}

						@Override
						public void onError(int code, String message) {
							Log.d("main", "登陆聊天服务器失败！" + message);
						}
					});
		}
	}
	/**
	 * 校验输入合法性
	 * @param view
	 * @return
	 */
	private boolean isIllegal(EditText view){
		return TextUtils.isEmpty(view.getText().toString());
	}
	
	public void getInHome(){
		//activity.finish();
		ActivityOptionsCompatICS options = ActivityOptionsCompatICS.
				makeSceneTransitionAnimation(activity, userIcon, R.id.main_navication);
		Intent home = new Intent(activity, MainActivity.class);
		ActivityCompatICS.startActivity(activity, home, options.toBundle());
	}
	
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	public void setPassword(String password) {
		// TODO Auto-generated method stub
		this.password = password;
	}

	public HashMap<String, String> getloginValues() {
		// TODO Auto-generated method stub
		HashMap<String,String> data = new HashMap<String,String>();
		data.put("name", TextUtils.isEmpty(edit_name.getText().toString()) ? null : edit_name.getText().toString());
		data.put("password", TextUtils.isEmpty(edit_password.getText().toString()) ? null : edit_password.getText().toString());
		return data;
	}
}
