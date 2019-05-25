package cn.itcast.dao;

import cn.itcast.entity.User;


public class UserDao {

	public User login(User user){
		if ("tom".equals(user.getName()) && "888".equals(user.getPwd()) ){
			return user;
		}
		return null;
	}
	
	public void register(User user) {
		System.out.println("欢迎你：" + user.getName());
	}
}
