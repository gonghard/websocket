package com.weixin.mobile.socket;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.mobile.socket.util.SocketSession;

@WebServlet(urlPatterns = { "/orderWeiXin" })
public class OrderServlet extends HttpServlet {
	private static transient Logger log = LoggerFactory.getLogger(OrderServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		String msg = req.getParameter("msg");
		log.debug("接收到 " + msg);
		if (msg != null && msg.trim().length() != 0) {
			msg = "{\"_commond\": \"map-liyang-update\", \"data\": {\"city\": \"" + msg + "\", \"value\": \"1\"}}";
			String result = SocketSession.getSingleton().sendText(msg, "map");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(result);
			out.flush();
		}
	}

}
