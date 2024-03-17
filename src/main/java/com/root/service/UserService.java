package com.root.service;

import com.root.entity.User;

public interface UserService {

	public User saveUser(User user , String url);

	public void removeSessionMassage();

	public void sendEmail(User user, String path);

	public boolean verifyAccount(String varificationCode);
}
