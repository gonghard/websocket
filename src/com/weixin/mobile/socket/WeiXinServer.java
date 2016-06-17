package com.weixin.mobile.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.mobile.socket.util.SocketSession;

@ServerEndpoint(value = "/weixin")
public class WeiXinServer {
	private static transient Logger log = LoggerFactory.getLogger(WeiXinServer.class);

	@OnOpen
	public void onOpen(Session session) throws IOException {
		OpUser opUser = SocketSession.getSingleton().convertParams(session);
		if (opUser == null) {
			sendText(session, getMsg(false, "id和aim不能为空,不能建立连接"));
			session.close();
			return;
		}
		if (isEmpty(opUser.id)) {
			sendText(session, getMsg(false, "id不能为空，不能建立连接"));
			session.close();
			return;
		}
		if (isEmpty(opUser.aim)) {
			sendText(session, getMsg(false, "aim不能为空，不能建立连接"));
			session.close();
			return;
		}
		SocketSession.getSingleton().addSession(session);
		log.debug(opUser.id + "建立连接");
	}

	private boolean isEmpty(String val) {
		if (val == null || val.trim().length() == 0) {
			return true;
		}
		return false;
	}

	@OnClose
	public void onClose(Session session) {
		SocketSession.getSingleton().removeSession(session);

	}

	@OnMessage
	public void onMessage(String message, Session session) {
		session.getAsyncRemote().sendText(getMsg(true, "接收成功"));
		OpUser opUser = SocketSession.getSingleton().convertParams(session);
		log.debug(opUser.toString() + "发送消息" + message);
		SocketSession.getSingleton().sendText(message, opUser.aim);
	}

	private void sendText(Session session, String msg) {
		session.getAsyncRemote().sendText(msg);
	}

	private String getMsg(boolean code, String msg) {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("code", code);
		msgMap.put("msg", msg);
		return JsonUtil.serialize(msgMap);
	}

}
