package com.root.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.root.entity.User;
import com.root.repository.UserRepo;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	public BCryptPasswordEncoder passwordEncoder;

	@Override
	public User saveUser(User user, String url) {

		String password = passwordEncoder.encode(user.getPassword());

		user.setPassword(password);
		user.setRole("ROLE_USER");

		user.setEnable(false);

		user.setVerificationCode(UUID.randomUUID().toString());

		User newUser = userRepo.save(user);

		if (newUser != null) {
			sendEmail(newUser, url);
		}

		return newUser;
	}

	@Override
	public void removeSessionMassage() {

		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");
	}

	@Override
	public void sendEmail(User user, String url) {

		String from = "sonu20684827@gmail.com";
		String to = user.getEmail();
		String subject = "Account Varification";
		String content = "Dear [[Name]], <br>" + "Please click the link below to varify your registration :<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VARIFY</a></h3> " + "Thank You,<br>" + "Sonu Nishad";

		try {

			MimeMessage massage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(massage);

			helper.setFrom(from, "Sonu Nishad");
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[name]]", user.getFullName());

			String siteUrl = url + "/verify?code=" + user.getVerificationCode();

			System.out.println(siteUrl);

			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailSender.send(massage);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@Override
	public boolean verifyAccount(String varificationCode) {

		User user = userRepo.findByVerificationCode(varificationCode);

		if (user == null) {
			return false;
		} else {

			user.setEnable(true);
			user.setVerificationCode(null);

			userRepo.save(user);

			return true;
		}

	}

}
