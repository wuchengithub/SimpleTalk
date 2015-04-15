package com.wookii.friend;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.util.Log;
import android.view.View;

public class MyLinearLayoutManager extends LinearLayoutManager {

	private static final String TAG = "MyLinearLayoutManager";

	public MyLinearLayoutManager(Context context) {
		super(context);
	}

	@Override
	public void onItemsChanged(RecyclerView recyclerView) {
		// TODO Auto-generated method stub
		super.onItemsChanged(recyclerView);
		Log.d(TAG, "onItemsChanged");
	}
	/*@Override
	public void onLayoutChildren(Recycler arg0, State arg1) {
		// TODO Auto-generated method stub
		super.onLayoutChildren(arg0, arg1);
		Log.d(TAG, "onLayoutChildren:" + arg1.toString());
	
	}
	
	@Override
	public void onMeasure(Recycler recycler, State state, int widthSpec,
			int heightSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(recycler, state, widthSpec, heightSpec);
		Log.d(TAG, "onMeasure:" + state.toString());
	}
	@Override
	public boolean onRequestChildFocus(RecyclerView parent, State state,
			View child, View focused) {
		Log.d(TAG, "onRequestChildFocus:" + state.toString());
		return super.onRequestChildFocus(parent, state, child, focused);
		
	}*/
	@Override
	public void onItemsUpdated(RecyclerView recyclerView, int positionStart,
			int itemCount) {
		// TODO Auto-generated method stub
		super.onItemsUpdated(recyclerView, positionStart, itemCount);
		Log.d(TAG, "onItemsChanged");
	}
	public void ddsdsd(int position) {
		//get
	}
}
