package com.root.service;

import com.root.entity.User;

public interface UserService {

	public User saveUser(User user);

	public void removeSessionMassage();
}
