package com.example.demo.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.UserList;
import com.example.demo.domain.repository.LoginUserRepository;

@Service
public class ItemListUserDetailsService implements UserDetailsService{
	
	@Autowired
	LoginUserRepository loginUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		UserList user = loginUserRepository.findById(username).orElse(null);
		UserDetails userDetails = new ItemListUserDetails(user);
		return userDetails;
	}

}
