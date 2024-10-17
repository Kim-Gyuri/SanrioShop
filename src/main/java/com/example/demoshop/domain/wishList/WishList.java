package com.example.demoshop.domain.wishList;


import com.example.demoshop.domain.users.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "wish_list")
public class WishList {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="wish_list_id")
    private Long id;

    @OneToMany(mappedBy = "wishList", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<WishItem> wishItemList = new HashSet<>();


    // wishList에서 wishItem 삭제해도 user.getWishList에 반영되도록 -> cascade 설정
    @OneToOne(mappedBy = "wishList", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private User user;


    /**
     * 연관관계 메소드
     */
    public void assignToUser(User user) {
        this.user = user;
    }

    public void addWishThing(WishItem wishItem) {
        this.wishItemList.add(wishItem);
    }

    public void removeWishThing(WishItem wishItem) {
        this.wishItemList.remove(wishItem);
    }


}
