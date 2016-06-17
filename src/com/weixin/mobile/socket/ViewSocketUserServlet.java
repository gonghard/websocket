package com.weixin.mobile.socket;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.weixin.mobile.socket.util.SocketSession;

/**
 * Servlet implementation class ViewSocketUserServlet
 */
@WebServlet(urlPatterns = "/users")
public class ViewSocketUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(JsonUtil.serialize(SocketSession.getSingleton().getUsers()));
		out.flush();
	}

}
