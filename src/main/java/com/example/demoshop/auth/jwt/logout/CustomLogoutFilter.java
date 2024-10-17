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
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.example.demoshop.utils.constants.JwtConstants.*;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshRepository;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);

    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, ServletException {

        // path and method verify
        if (verifyPathAndMethod(request, response, filterChain)) return;

        //get refresh token
        String refresh = getRefreshTokenFromCookies(request);

        //refresh null check
        if (checkRefreshTokenNotNull(response, refresh)) return;

        //expired check
        if (checkIfTokenExpired(response, refresh)) return;

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        if (isRefreshToken(response, refresh)) return;

        //DB에 저장되어 있는지 확인
        if (validateTokenExistenceInDb(response, refresh)) return;

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        refreshRepository.deleteByRefresh(refresh);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = invalidateRefreshTokenCookie();

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean checkIfTokenExpired(HttpServletResponse response, String refresh) {
        try {
            jwtProperties.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return true;
        }
        return false;
    }

    private static boolean checkRefreshTokenNotNull(HttpServletResponse response, String refresh) {
        if (refresh == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return true;
        }
        return false;
    }

    private static String getRefreshTokenFromCookies(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(JWT_REFRESH)) {

                refresh = cookie.getValue();
            }
        }
        return refresh;
    }

    private static boolean verifyPathAndMethod(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/api/user/logout$")) {  //"^\\/logout$"

            filterChain.doFilter(request, response);
            return true;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals(HttpMethod_POST)) {

            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private boolean isRefreshToken(HttpServletResponse response, String refresh) {
        String type = jwtProperties.getType(refresh);
        if (!type.equals(JWT_REFRESH)) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return true;
        }
        return false;
    }

    private static Cookie invalidateRefreshTokenCookie() {
        Cookie cookie = new Cookie(JWT_REFRESH, null);
        cookie.setMaxAge(IMMEDIATE_EXPIRATION);
        cookie.setPath("/");
        return cookie;
    }

    private boolean validateTokenExistenceInDb(HttpServletResponse response, String refresh) {
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return true;
        }
        return false;
    }
}