package com.example.demoshop.service.token;


import com.example.demoshop.auth.jwt.util.JwtProperties;
import com.example.demoshop.request.token.CreateRefreshToken;
import com.example.demoshop.exception.token.InvalidRefreshTokenException;
import com.example.demoshop.exception.token.RefreshTokenNotFoundException;
import com.example.demoshop.repository.token.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demoshop.utils.constants.JwtConstants.*;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReissueService {

    private final JwtProperties jwtUtil;
    private final RefreshTokenRepository refreshRepository;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(JWT_REFRESH)) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new RefreshTokenNotFoundException("Refresh token not found in cookies");
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new InvalidRefreshTokenException("Refresh token expired");
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String tokenType = jwtUtil.getType(refresh);

        if (!tokenType.equals(JWT_REFRESH)) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt(JWT_AUTH, username, role, ACCESS_TOKEN_EXPIRATION);
        String newRefresh = jwtUtil.createJwt(JWT_REFRESH, username, role, REFRESH_TOKEN_EXPIRATION);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(new CreateRefreshToken(username, newRefresh, REFRESH_TOKEN_EXPIRATION));

        //response
        response.setHeader(JWT_AUTH, newAccess);
        response.addCookie(createCookie(JWT_REFRESH, newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addRefreshEntity(CreateRefreshToken refreshRequest) {

        refreshRepository.save(refreshRequest.ofEntity());
        log.info("Saved new refresh token for user: {}", refreshRequest.getUsername());
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(COOKIE_TIME);
        cookie.setHttpOnly(true);
        log.info("Created new cookie with key: {}", key);

        return cookie;
    }

}
