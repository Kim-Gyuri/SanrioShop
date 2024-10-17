package com.example.demoshop.auth.jwt.util;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Component
public class JwtProperties  {
	private SecretKey secretKey;

	private static final String USERNAME = "username";
	private static final String ROLE = "role";
	private static final String TYPE = "type";
	public JwtProperties(@Value("${spring.jwt.secret}")String secret) {

		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String getUsername(String token) {

		return Jwts.parser()
				.verifyWith(secretKey).build()
				.parseSignedClaims(token)
				.getPayload()
				.get(USERNAME, String.class);
	}

	public String getRole(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get(ROLE, String.class);
	}

	public Boolean isExpired(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build().parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date());
	}


	public String createJwt(String type, String username, String role, Long expiredMs) {

		return Jwts.builder()
				.claim(TYPE, type)
				.claim(USERNAME, username)
				.claim(ROLE, role)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(secretKey)
				.compact();
	}

	public String getType(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get(TYPE, String.class);
	}


}