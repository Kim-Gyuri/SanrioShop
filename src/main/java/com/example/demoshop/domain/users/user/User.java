package com.example.demoshop.domain.users.user;

import com.example.demoshop.domain.wishList.WishItem;
import com.example.demoshop.domain.wishList.WishList;
import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.users.common.UserBase;
import com.example.demoshop.domain.users.common.UserLevel;
import com.example.demoshop.domain.users.common.UserStatus;

import com.example.demoshop.response.users.ProfileResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


import static com.example.demoshop.utils.constants.ImgConstants.DEFAULT_PROFILE;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Table(name = "users")
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("USER")
@Entity
public class User extends UserBase implements UserDetails {

    private String nickname; // 닉네임1
    private String profileImg; // 프로필 이미지 경로

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus; // 관리자에 의해 정의된 상태 (블랙회원 관리)


    @OneToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "wish_list_id")
    private WishList wishList; //찜하기 리스트


    @OneToMany(mappedBy = "uploader", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Item> itemList = new ArrayList<>(); // 판매 상품 리스트


    @Builder
    public User(Long id, String email, String password, String nickname) {
        super(id, email, password, UserLevel.USER);
        this.nickname = nickname;
        this.userStatus = UserStatus.NORMAL;
        // 이미지 기본 세팅
        this.profileImg = DEFAULT_PROFILE;
    }

    /**
     * 연관관계 메소드
     */

    public void createWishList(WishList wishList) {
        this.wishList = wishList;
        wishList.assignToUser(this);
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void markAsWished(WishItem wishItem) {
        this.wishList.addWishThing(wishItem);
    }

    public void unmarkAsWished(WishItem wishItem) {
        this.wishList.removeWishThing(wishItem);
    }

    public boolean checkWishItemDuplicate(Item item) {

        if (wishList.getWishItemList().isEmpty()) {
            return false;
        }

        return wishList.getWishItemList().stream()
                .anyMatch(wishItem -> wishItem.getItem().equals(item));
    }

    public ProfileResponse toProfileDto() {
        return ProfileResponse.builder()
                .nickname(this.nickname)
                .profileImg(this.profileImg)
                .build();
    }


    public boolean isBan() {
        return this.userStatus == UserStatus.BAN;
    }


    //========== UserDetails implements ==========//
    /**
     * Token을 고유한 Email 값으로 생성합니다
     * @return email;
     */

    private String roles;
    public void settingRoles() {
        this.roles = UserLevel.USER.getCode();
    }

    public List<String> getRoleList() {
        if (this.roles == null) {
            return Collections.emptyList(); // 빈 목록 반환 또는 다른 처리 수행
        }
        return Arrays.asList(this.roles.split(","));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add( new SimpleGrantedAuthority("ROLE_" + this.userLevel.name()));
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
