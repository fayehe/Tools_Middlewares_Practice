package cn.itcast.service;

import cn.itcast.dao.UserDao;
import cn.itcast.entity.User;


public class UserService {
	
	private UserDao ud = new UserDao();

	public User login(User user){
		return ud.login(user);
	}
	
	public void register(User user) {
		ud.register(user);
	}
}
