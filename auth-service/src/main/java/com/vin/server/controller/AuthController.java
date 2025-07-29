package com.vin.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vin.server.dto.UserRegistrationReq;
import com.vin.server.dto.UserSignInReq;
import com.vin.server.entity.UserRegistration;
import com.vin.server.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

	private final UserService service;
	private final AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public ResponseEntity<?> addNewUser(@RequestBody UserRegistrationReq credential){
		System.out.println(credential.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(service.saveUserDetails(credential));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateAndGetTocken(@RequestBody UserSignInReq signInReq){
		System.out.println("in Signin "+ signInReq);
		Authentication authToken = new UsernamePasswordAuthenticationToken(signInReq.getEmail(), signInReq.getPassword());
		System.out.println("before authentication- "+authToken.isAuthenticated());
		
		Authentication validAuth = authenticationManager.authenticate(authToken);
		System.out.println("After authentication- "+authToken.isAuthenticated());
		System.out.println(validAuth);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.generateToken(validAuth));
	}
}
