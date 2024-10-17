package com.example.demoshop.auth.jwt.login;

import com.example.demoshop.auth.jwt.util.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.PrintWriter;


import static com.example.demoshop.utils.constants.JwtConstants.JWT_AUTH;


/**
 * JWT 토큰의 유효성을 검사하고, 인증한다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProperties jwtProperties;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Thread currentThread = Thread.currentThread();
		log.info("current running thread : " + currentThread.getName());

		// Take out the token from the request header.
		String accessToken = request.getHeader(JWT_AUTH);
		log.info("access token ={}", accessToken);
		// If there is no token, it passes to the next filter.
		if (accessToken == null) {
			log.info("Access token is missing");
			filterChain.doFilter(request, response);

			return;
		}

		// Check if token has expired, if expired do not pass to next filter
		try {
			jwtProperties.isExpired(accessToken);
		} catch (ExpiredJwtException ex) {
			log.info("Access token expired", ex);

			// response body
			PrintWriter writer = response.getWriter();
			writer.print("access token expired");

			// response status code
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// Check if the token is access (발급시 명시한다. in payload)
		String tokenType = jwtProperties.getType(accessToken);

		if (!tokenType.equals(JWT_AUTH)) {
			log.info("Invalid token type: {}", tokenType);

			// response body
			PrintWriter writer = response.getWriter();
			writer.print("invalid access token");

			// response status code
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// get username,role
		String username = jwtProperties.getUsername(accessToken);

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

}