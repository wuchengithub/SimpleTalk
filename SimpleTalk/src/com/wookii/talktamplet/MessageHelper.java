package com.wookii.talktamplet;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

public class MessageHelper implements EMCallBack{

	private static final String TAG = null;
	private static MessageHelper instance;
	private Context context;
	private String userName;
	private MessageHelper(Context context) {
		this.context = context;
	}
	public synchronized static MessageHelper getInstance(Context context) {
		if(instance == null) instance = new MessageHelper(context);
		return instance;
	}
	public void sendMessageForSimple(String content) {
		// TODO Auto-generated method stub
		//获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
		EMConversation conversation = EMChatManager.getInstance().getConversation(userName);
		//创建一条文本消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		//如果是群聊，设置chattype,默认是单聊
		//message.setChatType(ChatType.GroupChat);
		//设置消息body
		TextMessageBody txtBody = new TextMessageBody(content);
		message.addBody(txtBody);
		//设置接收人
		message.setReceipt(userName);
		//把消息加入到此会话对象中
		conversation.addMessage(message);
		//发送消息
		EMChatManager.getInstance().sendMessage(message, this);
	}

	public List<EMMessage> getHistoryMessage() {
		
		EMConversation conversation = EMChatManager.getInstance().getConversation(userName);
		//获取此会话的所有消息
		List<EMMessage> messages = new ArrayList<EMMessage>();
		int msgCount = conversation.getMsgCount();
		for(int i = 1; i <= 3; i++) {
			if(msgCount -i < 0) break;
			messages.add(conversation.getMessage(msgCount -i));
		}
		Log.i(TAG, "getHistoryMessage:" + messages.size());
		//sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
		//获取startMsgId之前的pagesize条消息，此方法获取的messages sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
		//List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
		//如果是群聊，调用下面此方法
		//List<EMMessage> messages = conversation.loadMoreGroupMsgFromDB(startMsgId, pagesize);
		return messages;
	}
	public void setCurrentUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public void onError(int arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onError:" + arg1);
	}
	@Override
	public void onProgress(int arg0, String arg1) {
		// TODO Auto-generated method stub
		//Log.i(TAG, "onProgress");
	}
	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSuccess");
	}
}
