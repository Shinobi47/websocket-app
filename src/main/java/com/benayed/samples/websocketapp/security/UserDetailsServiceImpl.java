package com.benayed.samples.websocketapp.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService{

//	private UserRepository userRepository;
	
	private List<UserDetails> users;
	
	PasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		initUsers();
		return this.users.stream()
				.filter(user -> user.getUsername().equals(username))
				.findFirst()
				.orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
	}
	
	
	private void initUsers() {
		UserDetails user1 = User.builder().username("Haytam").password(encoder.encode("foo")).authorities("ROOT").build();
		UserDetails user2 = User.builder().username("User1").password(encoder.encode("bar")).authorities("USER").build();
		users = Arrays.asList(user1, user2);
	}
}
