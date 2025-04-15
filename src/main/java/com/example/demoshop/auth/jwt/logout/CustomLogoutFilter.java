package com.example.demoshop.auth.jwt.logout;

import com.example.demoshop.auth.jwt.util.JwtProperties;
import com.example.demoshop.repository.token.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.example.demoshop.utils.constants.JwtConstants.*;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);

    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

         /**
         *  path and method 검증하기
         */
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (!request.getMethod().equals(HttpMethod_POST)) {

            filterChain.doFilter(request, response);
            return;
        }

        /**
         *  refresh token을 꺼내기
         */
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            log.info("쿠키가 없어요!");
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JWT_REFRESH)) {
                refresh = cookie.getValue();
            }
        }

        /**
         *  refresh 토큰이 null인지 확인.
         */
        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        /**
         * refresh 토큰이 만료되었는지?
         */
        try {
            jwtProperties.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        /**
         * 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
         */

        String type = jwtProperties.getType(refresh);
        if (!type.equals(JWT_REFRESH)) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        /**
         * DB에 저장되어 있는지 확인
         */
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


        /**
         * 로그아웃 진행
         * Refresh 토큰 DB에서 제거
         */
        refreshRepository.deleteByRefresh(refresh);

        //Refresh 토큰 Cookie 값 0으로 초기화
        Cookie cookie = invalidateRefreshTokenCookie();

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // 쿠키 삭제
    private static Cookie invalidateRefreshTokenCookie() {
        Cookie cookie = new Cookie(JWT_REFRESH, null);
        cookie.setMaxAge(IMMEDIATE_EXPIRATION);
        cookie.setPath("/");
        return cookie;
    }


}