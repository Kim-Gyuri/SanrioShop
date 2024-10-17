package com.example.demoshop.service.users;

import com.example.demoshop.domain.transaction.Notification;
import com.example.demoshop.domain.wishList.WishList;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.repository.sale.NotificationRepository;
import com.example.demoshop.request.users.SignupRequest;
import com.example.demoshop.exception.users.DuplicateEmailException;
import com.example.demoshop.exception.users.DuplicateNicknameException;
import com.example.demoshop.exception.users.NotAuthorizedException;
import com.example.demoshop.exception.users.UserNotFoundException;
import com.example.demoshop.repository.wishList.WishListRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.response.users.ProfileResponse;
import com.example.demoshop.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    private final WishListRepository wishListRepository;
    private final FileService fileService;

    private final NotificationRepository notificationRepository;

    /**
     * 회원가입
     */
    public void signup(SignupRequest request) {
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
