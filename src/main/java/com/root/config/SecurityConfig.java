package com.root.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	public CostomAuthSuccessHandler successHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService getDetailsService() {

		return new CostomUserDetailsService();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();

		dao.setUserDetailsService(getDetailsService());

		dao.setPasswordEncoder(passwordEncoder());

		return dao;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		/*
		 * http.csrf().disable().authorizeHttpRequests().requestMatchers("/",
		 * "/register", "/signin","/userCreated").permitAll()
		 * .requestMatchers("/user/**").authenticated().and().formLogin().loginPage(
		 * "/signin")
		 * .loginProcessingUrl("/userlogin").defaultSuccessUrl("/user/profile").
		 * permitAll();
		 * 
		 * return http.build();
		 */

		/* that is Relo based autherization */

		http.csrf().disable()
		.authorizeHttpRequests()
		  .requestMatchers("/user/**")
		  .hasRole("USER")
		  .requestMatchers("/admin/**")
		  .hasRole("ADMIN")
				.requestMatchers("/**").permitAll()
		.and()
		   .formLogin()
				.loginPage("/signin")
				.loginProcessingUrl("/userlogin")
				.successHandler(successHandler)
				.permitAll();

		return http.build();
	}

}
