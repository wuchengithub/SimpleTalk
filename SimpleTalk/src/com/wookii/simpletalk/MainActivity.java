package com.wookii.simpletalk;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.easemob.chat.EMMessage;
import com.kale.activityoptions.transition.TransitionCompat;
import com.kale.activityoptions.transition.TransitionCompat.ViewAnimationListener;
import com.wookii.friend.CurrentFriendsListAdapter;
import com.wookii.friend.CurrentFriendsListAdapter.OnChildSelectedListener;
import com.wookii.friend.FriendsHelper;
import com.wookii.friend.MyLinearLayoutManager;
import com.wookii.simpletalk.greenrobot.Friend;
import com.wookii.talktamplet.MessageHelper;
import com.wookii.talktamplet.TalkFaceLayout2;
import com.wookii.talktamplet.TalkFaceLayout2.OnFlingListener;

public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	private CurrentFriendsListAdapter mAdapter;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x123:
				ArrayList<Friend> friends = (ArrayList<Friend>)msg.obj;
				mAdapter = new CurrentFriendsListAdapter(MainActivity.this, friends);
				mAdapter.setSelectedChild(0);
				mAdapter.setOnChildSelectedListener(new OnChildSelectedListener() {
					
					@Override
					public void onChildSelected(int position) {
						// TODO Auto-generated method stub
								
						new Thread(){
							public void run() {
								List<EMMessage> historyMessage 
								= MessageHelper.getInstance(MainActivity.this).getHistoryMessage();
								Message obtainMessage = handler.obtainMessage();
								obtainMessage.obj = historyMessage;
								obtainMessage.what = 0x124;
								handler.sendMessage(obtainMessage);
							};
						}.start();
						
					}
				});
				mRecyclerView.setAdapter(mAdapter);
				//leftMenu.setAdapter(mAdapter);
				break;
			case 0x124:
				List<EMMessage> data = (List<EMMessage>)msg.obj;
				tfl.updateConversationLayout(data);
				break;

			default:
				break;
			}
		};
	};
	
	
	protected ArrayList<Friend> friends;
	private TalkFaceLayout2 tfl;
	private RecyclerView leftMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tfl = (TalkFaceLayout2)findViewById(R.id.talk_face_layout);
		tfl.setOnFlingListener(new OnFlingListener() {
			
			@Override
			public void onFlingLeft(boolean isLeft) {
				if(mAdapter != null) {
					mAdapter.selectChild(mRecyclerView, isLeft);
				}
			}

			@Override
			public void onFlingUpSendData() {
				/*if(mRecyclerView != null) {
					mAdapter.customNotifyItemMoved();
				}*/
			}
		});
		mRecyclerView = (RecyclerView) findViewById(R.id.current_talk_recycler_view);
		leftMenu = (RecyclerView) findViewById(R.id.left_drawer);
		leftMenu.setHasFixedSize(true);
		MyLinearLayoutManager leftMenuLayoutManager = new MyLinearLayoutManager(this);
		leftMenuLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		leftMenu.setLayoutManager(leftMenuLayoutManager);
		
		mRecyclerView.setHasFixedSize(true);
		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mLayoutManager.setSmoothScrollbarEnabled(true);
		mRecyclerView.setLayoutManager(mLayoutManager);
		//mRecyclerView.setItemAnimator(new CustomItemMovingAnim());
		getFriends();
		
		transitionCompat();
	}

	private void transitionCompat() {
		
		TransitionCompat.addViewAnimListener(new ViewAnimationListener() {
			
			@Override
			public void onViewAnimationUpdate(View view, ValueAnimator valueAnimator,
					float progress) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onViewAnimationStart(View view, Animator animator) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onViewAnimationEnd(View view, Animator animator) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onViewAnimationCancel(View view, Animator animator) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onInitAnimationViews(View view, int id) {
				// TODO Auto-generated method stub
				
			}
		});
		TransitionCompat.setAnimDuration(2000);
		TransitionCompat.startTransition(this, R.layout.activity_main);
	}

	private void getFriends() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				friends = FriendsHelper.getInstance(MainActivity.this).getFriends();
				if(friends != null && friends.size() == 0) {
					//new Friend("1", "哈哈米", "", friend_user_name, friend_url, friend_gender, friend_last_words)
				}
				Message obtainMessage = handler.obtainMessage();
				obtainMessage.obj = friends;
				obtainMessage.what = 0x123;
				handler.sendMessage(obtainMessage);
			};
		}.start();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		TransitionCompat.finishAfterTransition(this);
	}
}
