package com.wookii.logister;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.wookii.simpletalk.MainActivity;
import com.wookii.simpletalk.PersonalData;
import com.wookii.simpletalk.R;

public class LauncherActivity extends FragmentActivity implements WelcomeFragment.OnWelcomeAnimationEndListener{


	private static final String TAG = "LauncherActivity";
	private LoginFragment loginFragment;
	private HashMap<String, String> loginValues;
	private boolean isFrist = true;
	private String name = null;
	private String password = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luncher);
		if(savedInstanceState == null || savedInstanceState.getBoolean("isFrist")){
			WelcomeFragment welcomeFragment = new WelcomeFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.main_container, welcomeFragment).commit();
			welcomeFragment.setOnWelcomeAnimationEndListener(this);
		} else {
			name = savedInstanceState.getString("name");
			password = savedInstanceState.getString("password");
			loginFragment = new LoginFragment();
			replaceLoginFragment();
		}
		Log.i(TAG, "onCreate");
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onSaveInstanceState");
		isFrist = false;
		outState.putBoolean("isFrist", isFrist);
		this.loginValues = loginFragment.getloginValues();
		outState.putString("name", this.loginValues.get("name"));
		outState.putString("password", this.loginValues.get("password"));
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onCheckLogin(boolean isLogin) {
		// TODO Auto-generated method stub
		/*if(isLogin) {
			getInHome();
		} else {
			
		}*/
		replaceLoginFragment();
	}
	
	private void replaceLoginFragment() {
		loginFragment = new LoginFragment();
		FragmentTransaction beginTransaction = getSupportFragmentManager()
				.beginTransaction();
		beginTransaction
				.setCustomAnimations(R.anim.fragment_slide_bottom_enter, 0)
				.replace(R.id.main_container, loginFragment).commit();
		if(name != null) {
			loginFragment.setName(name);
			
		}
		if(password != null) {
			loginFragment.setPassword(password);
		}
	}
	public void getInHome(){
		Intent home = new Intent(this, MainActivity.class);
		startActivity(home);
		this.finish();
	}
}
