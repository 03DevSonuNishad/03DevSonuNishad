package com.root.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.root.entity.User;
import com.root.repository.UserRepo;

@Component
public class CostomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepo.findByEmail(username);

		if (user == null) {

			throw new UsernameNotFoundException("Invailed User Name");
		} else {

			return new CostomUser(user);
		}

	}

}
