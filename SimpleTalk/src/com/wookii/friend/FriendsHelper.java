package com.wookii.friend;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

import com.wookii.simpletalk.greenrobot.DaoMaster;
import com.wookii.simpletalk.greenrobot.DaoSession;
import com.wookii.simpletalk.greenrobot.Friend;
import com.wookii.simpletalk.greenrobot.FriendDao;

public class FriendsHelper {

	private Context context;
	private FriendDao dao;
	private static FriendsHelper instance = null;
	
	public FriendsHelper(Context context) {
		this.context = context;
		DaoMaster daoMaster = DBManager.getDaoMaster(context);
		DaoSession daoSession = DBManager.getDaoSession(daoMaster, context);
		dao = daoSession.getFriendDao();
	}
	
	
	public synchronized static FriendsHelper getInstance(Context context){
		if(instance == null) {
			instance = new FriendsHelper(context);
		}
		return instance;
	}
	
	public ArrayList<Friend> getFriends(){
		return (ArrayList<Friend>) dao.loadAll();
	}
	
	public void replaceFriend(Friend friend) {
		
		dao.insertOrReplace(friend);
		
	}
}
