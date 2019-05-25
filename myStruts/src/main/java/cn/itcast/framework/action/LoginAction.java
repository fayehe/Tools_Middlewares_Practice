package cn.itcast.framework.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.entity.User;
import cn.itcast.service.UserService;

public class LoginAction {
	
	public Object execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return null;
	}

	public Object login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Object uri = null;

		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		User user = new User();
		user.setName(name);
		user.setPwd(pwd);

		UserService userService = new UserService();
		User userInfo = userService.login(user);
		if (userInfo == null) {
			uri = "loginFaild"; 
		} else {
			request.getSession().setAttribute("userInfo", userInfo);
			uri = "loginSuccess";  
		}
		return uri;
	}
}
