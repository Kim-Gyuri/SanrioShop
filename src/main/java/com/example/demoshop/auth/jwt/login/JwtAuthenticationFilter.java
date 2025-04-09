package com.example.demoshop.auth.jwt.login;

import com.example.demoshop.auth.jwt.util.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;



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
		String username = null;


		// If there is no token, it passes to the next filter.
		if (accessToken != null) {
			log.info("access token ={}", accessToken);
			try {
				username = jwtProperties.getUsername(accessToken);
				log.info("username = {}", username);
			} catch (IllegalArgumentException ex) {
				log.info("fail get user id");
				ex.printStackTrace();
			} catch (ExpiredJwtException ex) {
				log.info("Token expired");
				ex.printStackTrace();
			} catch (MalformedJwtException ex) {
				log.info("Invalid JWT !!");
				ex.printStackTrace();
			} catch (Exception e) {
				log.info("Unable to get JWT Token !!");
				e.getStackTrace();
			}
		}

		if ((username != null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (this.jwtProperties.validateToken(accessToken, userDetails)) {

				// All things going well
				// Authentication stuff
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				authenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				log.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			} else {
				log.info("Invalid JWT Token !!");
			}
		} else {
			log.info("Username is null or context is not null !!");
		}
		filterChain.doFilter(request, response);
	}

}