package com.example.demoshop.auth.jwt.login;

import com.example.demoshop.auth.jwt.util.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;


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

		// request header에서 Token 꺼내기
		String accessToken = request.getHeader(JWT_AUTH);
		String username = null;

		// 토큰이 있을 경우 유효성 검사 및 인증 처리
		if (accessToken != null) {
			log.info("access token ={}", accessToken);
			try {
				username = jwtProperties.getUsername(accessToken);
				log.info("username = {}", username);
			} catch (IllegalArgumentException e) {
				log.info("fail get user id");
				errorResponse(response, "JWT Token 값이 없습니다.");
				return;
			} catch (ExpiredJwtException e) {
				log.info("Token expired");
				errorResponse(response, "JWT Token 만료되었습니다.");
				return;
			} catch (MalformedJwtException e) {
				log.info("Invalid JWT !!");
				errorResponse(response, "JWT Token 값이 올바르게 구성되지 않았습니다.");
				return;
			} catch (Exception e) {
				log.info("Unable to get JWT Token !!");
				errorResponse(response, "JWT Token을 가져올 수 없습니다.");
				return;
			}
		}

		// 사용자 인증 처리
		if ((username != null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (this.jwtProperties.validateToken(accessToken, userDetails)) {

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				log.info("Authenticated user [{}] - security context has been set", username);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			} else {
				log.info("Invalid JWT Token !!");
				errorResponse(response, "JWT Token 값이 올바르게 구성되지 않았습니다.");
				return;
			}
		} else {
			log.info("이미 인증된 사용자입니다. 필터를 통과합니다.");
			// 인증은 이미 되어 있으므로 다음 필터로 넘긴다.
		}

		filterChain.doFilter(request, response);
	}

	// 예외 메시지 작성
	private void errorResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String json = new ObjectMapper().writeValueAsString(Map.of("message", message));
		response.getWriter().write(json);
	}

}