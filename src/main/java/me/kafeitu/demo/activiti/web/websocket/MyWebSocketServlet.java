package me.kafeitu.demo.activiti.web.websocket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

@WebServlet("/ws/my")
public class MyWebSocketServlet extends WebSocketServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static List<MyWebSocket> socketList = new ArrayList<MyWebSocket>();
	
	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		// TODO Auto-generated method stub
		String name = request.getParameter("username");
		 System.out.println(name + "is connected protocol=" + protocol);
		 return new MyWebSocket(name);
	}

	
	
	public static synchronized List<MyWebSocket> getSocketList() {
		return socketList;
	}

	public static void setSocketList(List<MyWebSocket> socketList) {
		MyWebSocketServlet.socketList = socketList;
	}
	
	

}
