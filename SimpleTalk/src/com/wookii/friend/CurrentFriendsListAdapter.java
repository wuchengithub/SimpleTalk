package com.wookii.friend;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.wookii.simpletalk.BaseApplication;
import com.wookii.simpletalk.R;
import com.wookii.simpletalk.greenrobot.Friend;
import com.wookii.talktamplet.MessageHelper;
import com.wookii.widget.CircleImageView;

public class CurrentFriendsListAdapter extends RecyclerView.Adapter<CurrentFriendsListAdapter.ViewHolder> {

	public static SparseBooleanArray selectMap;
	public class ViewHolder extends RecyclerView.ViewHolder{

		public View mViewHolder;
		public CircleImageView pic;

		public ViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			mViewHolder = itemView;
			pic = (CircleImageView)itemView.findViewById(R.id.friend_pic);
		}

	}

	private static final String TAG = "CurrentFriendsListAdapter";

	private ArrayList<Friend> data;
	private int prePosition = 0 ;

	private int page = 1;

	private int offsetMoe;

	private int VISIBLE_COUNT = 5;

	private int fixPosition;

	private boolean isReverse;

	private Context context;

	private OnChildSelectedListener listener;

	private int currentSelectChildPosition;
	public CurrentFriendsListAdapter(Context context, ArrayList<Friend> friends) {
		// TODO Auto-generated constructor stub
		this.data = friends;
		this.context = context;
		
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int arg1) {
		excuAnima(vh, arg1);
		BaseApplication.imageLoader.displayImage(data.get(arg1).getFriend_url(), vh.pic, BaseApplication.options);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = View.inflate(arg0.getContext(), R.layout.current_talk_friends_item, null);
		view.setAlpha(0.5f);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}


	public synchronized void selectChild(RecyclerView recyclerView, boolean isLeft) {
			
		int targetIndex = setTarget(isLeft);
		if(targetIndex < 0 || targetIndex == prePosition) return;
		nextOrPre(recyclerView, targetIndex, prePosition, isLeft);
		
		// 求出最后一页有几条数据
		double realSeeCount = VISIBLE_COUNT;
		double itemCount = getItemCount();
		int yu = (int) (itemCount % realSeeCount);
		
		if(!isReverse) {//如果没有翻转，计算是否翻转
			// 还剩余几个item
			int itemIndex = getItemCount() - 1;
			int lastSize = itemIndex - targetIndex;
			if (lastSize < yu) {// 说明下一页的item数将不够VISIBLE_COUNT
				isReverse = true;
				if (!isLeft) {// next,如果继续王前进，置换起跳点
					fixPosition = -itemIndex;
				}
			}
		} else {
			
			if(prePosition <= yu) {
				isReverse = false;
				if (isLeft) {// next,如果继续王前进，置换起跳点
					fixPosition = 0;
				}
			}
		}
		

		prePosition = targetIndex;
		notifyItemRangeChanged(0, getItemCount());
	}


	private void nextOrPre(RecyclerView recyclerView, int targetIndex, int position, boolean isLeft) {
		//是否翻页
		if(needJump(targetIndex, isLeft)) {
			int direction = 1;
			if(isLeft) {
				direction = -1;
			}
			recyclerView.smoothScrollToPosition(position
					+ direction * VISIBLE_COUNT);
		}
	}

	private boolean needJump(int position, boolean isLeft) {
		// TODO Auto-generated method stub
		int j = Math.abs(position + fixPosition);
		if((!isReverse && isLeft) || (isReverse && !isLeft)) {
			j += 1;
		} 
		int i = j % VISIBLE_COUNT;
		return  j!= 0 && i == 0;
	}

	private int setTarget(boolean isLeft) {
		int target = prePosition;
		if(isLeft) {//后一个
			target -=1;
		}else {
			//前一个
			target +=1;
		}
		if(target < 0) {
			return target = 0;
		}
		
		if(target > getItemCount() - 1) {
			return target = getItemCount() - 1;
		}
		selectMap = new SparseBooleanArray();
		selectMap.put(target, true);
		return target;
	}
	private void excuAnima(ViewHolder vh, int position){
		if(selectMap == null) return;
		if(selectMap.get(position)) {
			MessageHelper.getInstance(context).setCurrentUserName(data.get(position).getFriend_user_name());
			Log.i(TAG, "current excuAnima:" + position);
			currentSelectChildPosition = position;
			if(listener!= null) {
				listener.onChildSelected(position);
			}
			ViewCompat.setScaleX(vh.itemView, 1.25f);
			ViewCompat.setScaleY(vh.itemView, 1.25f);
			ViewCompat.setAlpha(vh.itemView, 1f);
			ViewCompat.animate(vh.itemView).alpha(1f).scaleX(1.25f).scaleY(1.25f).setDuration(100).start();
		/*	ObjectAnimator a = ObjectAnimator.ofFloat(vh.itemView, "alpha", 0.5f, 1f);
			ObjectAnimator x = ObjectAnimator.ofFloat(vh.itemView, "scaleX", 1f, 1.25f);
			ObjectAnimator y = ObjectAnimator.ofFloat(vh.itemView, "scaleY", 1f, 1.25f);
			AnimatorSet set= new AnimatorSet();
			set.playTogether(a,x,y);
			set.setDuration(100);
			set.start();*/
		} else {
			vh.itemView.setAlpha(0.5f);
			vh.itemView.setScaleX(1f);
			vh.itemView.setScaleY(1f);
		}
	}

	public void setOnChildSelectedListener(OnChildSelectedListener listener){
		this.listener = listener;
	}
	
	public interface OnChildSelectedListener{
		void onChildSelected(int position);
		
	}
	public void setSelectedChild(int i) {
		// TODO Auto-generated method stub
		if(selectMap == null) selectMap = new SparseBooleanArray();
		selectMap.put(i, true);
	}
	
	public void customNotifyItemMoved() {
		if(currentSelectChildPosition == 0) return;
		notifyItemMoved(currentSelectChildPosition, 1);
	}
}
