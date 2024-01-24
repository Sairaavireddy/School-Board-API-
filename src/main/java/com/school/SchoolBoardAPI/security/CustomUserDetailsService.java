package com.school.SchoolBoardAPI.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userrepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userrepository.findByusername(username).map(user->new CustomUserDetails(user)).orElseThrow(
				()-> new UsernameNotFoundException("Failed to authenticate the user"));
	}

}
