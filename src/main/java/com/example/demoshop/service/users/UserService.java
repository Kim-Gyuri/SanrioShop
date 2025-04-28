package com.example.demoshop.service.users;

import com.example.demoshop.auth.jwt.util.JwtProperties;
import com.example.demoshop.auth.security.CustomUserDetailsService;
import com.example.demoshop.domain.transaction.Notification;
import com.example.demoshop.domain.wishList.WishList;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.repository.sale.NotificationRepository;
import com.example.demoshop.repository.token.RefreshTokenRepository;
import com.example.demoshop.request.token.CreateRefreshToken;
import com.example.demoshop.request.users.LoginRequest;
import com.example.demoshop.request.users.SignupRequest;
import com.example.demoshop.exception.users.DuplicateEmailException;
import com.example.demoshop.exception.users.DuplicateNicknameException;
import com.example.demoshop.exception.users.NotAuthorizedException;
import com.example.demoshop.exception.users.UserNotFoundException;
import com.example.demoshop.repository.wishList.WishListRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.response.users.ProfileResponse;
import com.example.demoshop.response.users.UserTokenDto;
import com.example.demoshop.service.FileService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.example.demoshop.utils.constants.JwtConstants.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    private final WishListRepository wishListRepository;
    private final FileService fileService;

    private final NotificationRepository notificationRepository;

    /**
     * 로그인
     */
    public UserTokenDto login(LoginRequest loginRequest, HttpServletResponse response) {
        // 인증된 회원정보인지 검증
        authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        // 패스워드 인코딩 검증
        checkEncodePassword(loginRequest.getPassword(), userDetails.getPassword());

        // get role
        String email = loginRequest.getEmail();
        String role = getRole(userDetails);

        String access = jwtProperties.createJwt(JWT_AUTH, email, role, ACCESS_TOKEN_EXPIRATION);
        String refresh = jwtProperties.createJwt(JWT_REFRESH, email, role, REFRESH_TOKEN_EXPIRATION);

        //Refresh 토큰 저장, cookie
        addRefreshEntity(new CreateRefreshToken(email, refresh, REFRESH_TOKEN_EXPIRATION));
        Cookie cookie = createCookie(JWT_REFRESH, refresh);

        //응답 설정
        response.setHeader(JWT_AUTH, access);
        response.addCookie(cookie);
        response.setStatus(HttpStatus.OK.value());


        return UserTokenDto.fromEntity(userDetails, access);
    }

    private void addRefreshEntity(CreateRefreshToken refreshRequest) {

        refreshTokenRepository.save(refreshRequest.ofEntity());
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(COOKIE_TIME);
        cookie.setHttpOnly(true);

        return cookie;
    }
    private static String getRole(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        return role;
    }




    /**
     * 사용자가 입력한 비번과 DB에 저장된 비번이 같은지 체크 : 인코딩 확인
     * @param rawPassword
     * @param encodedPassword
     */
    private void checkEncodePassword(String rawPassword, String encodedPassword) {
        if (!encoder.matches(rawPassword, encodedPassword)) {
            throw new UserNotFoundException("비밀번호 불일치");
        }
    }

    /**
     * 사용자 인증
     * @param email
     * @param pwd
     */
    private void authenticate(String email, String pwd) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pwd));
        } catch (DisabledException e) {
            throw new UserNotFoundException("인증되지 않은 아이디입니다.");
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("비밀번호가 일치하지 않습니다.");
        }
    }



    /**
     * 회원가입
     */
    public Long signup(SignupRequest request) {
        if (checkEmailDuplicate(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        if (checkNicknameDuplicate(request.getNickname())) {
            throw new DuplicateNicknameException();
        }

        // 패스워드 암호화
        request.encodePwd(encoder);

        // 찜바구니 생성
        User user = userRepository.save(request.ofEntity());
        createRequiredResources(user);

        return user.getId();
    }

    // 유저 회원가입 시 필요한 세팅 : 찜하기 객체
    private void createRequiredResources(User user) {
        user.createWishList(wishListRepository.save(new WishList()));
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
    }

    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }


    @Transactional(readOnly = true)
    public ProfileResponse currentUserProfile(User user) {
        User currentUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        return currentUser.toProfileDto();
    }


    // 프사 수정
    public void updateProfileImg(User user, MultipartFile file) throws IOException {

        User loginUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        String profileImg = uploadProfile(file);

        loginUser.updateProfileImg(profileImg);
    }

    // 닉네임 수정
    public void updateNickname(User user, String nickname) {
        User loginUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        // 닉네임 중복 체크
        if (checkNicknameDuplicate(nickname)) {
            throw new DuplicateNicknameException();
        }

        loginUser.updateNickname(nickname);
    }


    @Transactional(readOnly = true)
    public List<Notification> findMessage(String userEmail) {
        return notificationRepository.findByUserEmail(userEmail);
    }


   // 유저 프로필 사진 업로드
    private String uploadProfile(MultipartFile file) throws IOException {
        return fileService.storeFile(file);
    }


    private void banCheck(User user) {
        if (user.isBan()) {
            throw new NotAuthorizedException("관리자에 의해 이용이 정지된 사용자 입니다.");
        }
    }

}
