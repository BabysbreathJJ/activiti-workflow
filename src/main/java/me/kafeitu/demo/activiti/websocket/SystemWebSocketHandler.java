package me.kafeitu.demo.activiti.websocket;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.web.socket.*;

import me.kafeitu.demo.activiti.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemWebSocketHandler implements WebSocketHandler {

	private static final Logger logger;

	private static final ArrayList<WebSocketSession> users;

	static {
		users = new ArrayList<>();
		logger = LoggerFactory.getLogger(SystemWebSocketHandler.class);

	}

	

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.debug("connect to the websocket success......");
		users.add(session);
		// String userName = (String)
		// session.getAttributes().get(Constants.WEBSOCKET_USERNAME);
		// if (userName != null) {
		// // ��ѯδ����Ϣ
		// int count = webSocketService
		// .getUnReadNews((String)
		// session.getAttributes().get(Constants.WEBSOCKET_USERNAME));
		//
		// session.sendMessage(new TextMessage(count + ""));
		// }
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

		// sendMessageToUsers();
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		logger.debug("websocket connection closed......");
		users.remove(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		logger.debug("websocket connection closed......");
		users.remove(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * �����������û�������Ϣ
	 *
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message) {
		for (WebSocketSession user : users) {
			try {
				if (user.isOpen()) {
					user.sendMessage(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ĳ���û�������Ϣ
	 *
	 * @param userName
	 * @param message
	 */
	public void sendMessageToUser(String userName, TextMessage message) {
		for (WebSocketSession user : users) {
			if (user.getAttributes().get(Constants.WEBSOCKET_USERNAME).equals(userName)) {
				try {
					if (user.isOpen()) {
						user.sendMessage(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	

}
