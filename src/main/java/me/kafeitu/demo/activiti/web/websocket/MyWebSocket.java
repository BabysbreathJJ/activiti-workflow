package me.kafeitu.demo.activiti.web.websocket;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

public class MyWebSocket implements OnTextMessage {

	private Connection conn;
	private String username = "匿名用户";

	public MyWebSocket(String name) {
		// TODO Auto-generated constructor stub
		if (name != null && name.length() > 0)
			this.username = name;
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	public void onClose(int arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("onClose==========================");
		MyWebSocketServlet.getSocketList().remove(this);
	}

	@Override
	public void onOpen(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
		System.out.println("onOpen==========================" + conn.getMaxIdleTime());
		MyWebSocketServlet.getSocketList().add(this);
	}

	@Override
	public void onMessage(String data) {
		// TODO Auto-generated method stub
		System.out.println("~~~~~~~~~~" + data);
		if (this.conn.isOpen()) {
			try {
				this.conn.sendMessage("你好 "+username+"!   " + data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Connection getConn() {
		return conn;
	}

	

}
