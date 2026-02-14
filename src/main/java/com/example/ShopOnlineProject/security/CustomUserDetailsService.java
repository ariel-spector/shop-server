package com.example.ShopOnlineProject.security;

import com.example.ShopOnlineProject.model.CustomUser;
import com.example.ShopOnlineProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserService userService;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CustomUser user= userService .getUserByUsername ( username );
		if (user==null){
			throw new UsernameNotFoundException ( "User not found" );
		}
		System.out.println ("Retrieved user: " + user.getUsername () + " with roles: " + user.getRole ().name () );

		List<GrantedAuthority> authorities = new ArrayList<> ();
		authorities.add ( new SimpleGrantedAuthority ( user.getRole ().name () ) );

		return new User ( user.getUsername (), user.getPassword () , authorities );

	}
}
