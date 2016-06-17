package com.weixin.mobile.socket.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.mobile.socket.OpUser;

public class SocketSession {
	private static transient Logger log = LoggerFactory.getLogger(SocketSession.class);
	private static SocketSession socketSession;
	private static Map<String, Session> sessions = Collections.synchronizedMap(new HashMap<String, Session>());
	/**
	 * 发送者标识
	 */
	public static final String ID = "id";
	/**
	 * 接收者标识
	 */
	public static final String AIM = "aim";

	private SocketSession() {
	}

	public static SocketSession getSingleton() {
		if (socketSession == null) {
			synchronized (SocketSession.class) {
				if (socketSession == null) {
					socketSession = new SocketSession();
				}
			}
		}
		return socketSession;
	}

	public OpUser convertParams(Session session) {
		OpUser opUser = null;
		Map<String, List<String>> paramsMap = session.getRequestParameterMap();
		if (paramsMap == null) {
			return opUser;
		}
		opUser = new OpUser();
		List<String> val_ = paramsMap.get(ID);
		if (val_ != null && val_.get(0) != null && val_.get(0).trim().length() != 0) {
			opUser.id = val_.get(0);
		}
		val_ = paramsMap.get(AIM);
		if (val_ != null && val_.get(0) != null && val_.get(0).trim().length() != 0) {
			opUser.aim = val_.get(0);
		}
		return opUser;
	}

	public void addSession(Session session) {
		OpUser opUser = convertParams(session);
		log.debug(opUser.id + "建立连接");
		sessions.put(opUser.id, session);
	}

	public void removeSession(Session session) {
		OpUser opUser = convertParams(session);
		sessions.remove(opUser.id);
		log.debug(opUser.id + "断开连接");
	}

	public String sendText(String msg, String aimId) {
		Session aimSession = SocketSession.getSingleton().get(aimId);
		if (aimSession == null || !aimSession.isOpen()) {
			return "不在线";
		}
		if (aimSession.isOpen()) {
			log.debug("给" + aimId + "发送消息" + msg);
			aimSession.getAsyncRemote().sendText(msg);
		}
		return "成功";
	}

	public Session get(String aim) {
		return sessions.get(aim);
	}

	public Set<String> getUsers() {
		return sessions.keySet();
	}
}
