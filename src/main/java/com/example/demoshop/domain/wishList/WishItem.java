package com.example.demoshop.domain.wishList;

import com.example.demoshop.domain.item.Item;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class WishItem {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="wish_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wish_list_id")
    private WishList wishList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public WishItem(WishList wishList, Item item) {
        this.wishList = wishList;
        this.item = item;
    }






}
