package com.example.ShopOnlineProject.controller;

import com.example.ShopOnlineProject.model.AuthenticationRequest;
import com.example.ShopOnlineProject.model.AuthenticationResponse;
import com.example.ShopOnlineProject.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationService authenticationService;

	@PostMapping("/api/authenticate")
	public ResponseEntity <?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
		try {
			AuthenticationResponse authResponse = authenticationService.createAuthenticationToken ( authenticationRequest );
			return ResponseEntity.ok ()
					.body ( authResponse );
		}catch (Exception exception){
			return ResponseEntity.status ( HttpStatus.FORBIDDEN ).body ( "Incorrect Username or Password" );
		}

		}


}
