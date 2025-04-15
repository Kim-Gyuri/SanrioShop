package com.example.demoshop.auth.jwt.util;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 토큰의 생성 및 검증
 */

@Component
public class JwtProperties  {
	private SecretKey secretKey;

	private static final String USERNAME = "username";
	private static final String ROLE = "role";
	private static final String TYPE = "type";

	public JwtProperties(@Value("${spring.jwt.secret}")String secret) {

		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	// user 확인 >email 확인
	public String getUsername(String token) {

		return Jwts.parser()
				.verifyWith(secretKey).build()
				.parseSignedClaims(token)
				.getPayload()
				.get(USERNAME, String.class);
	}

	// user 확인 (UNAUTH(미인증), AUTH(인증), ADMIN(관리자))
	public String getRole(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get(ROLE, String.class);
	}

	// 토큰 만료되었는지?
	public Boolean isExpired(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build().parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date());
	}


	// 토큰 생성
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

	// 토큰 타입확인 (access, refresh)
	public String getType(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get(TYPE, String.class);
	}

	//validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsername(token);
		return (username.equals(userDetails.getUsername()) && !isExpired(token));
	}


}