package com.example.demoshop.repository.wishList;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.wishList.WishItem;
import com.example.demoshop.domain.wishList.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {


    Optional<WishItem> findByWishListAndItem(WishList wishList, Item item);

    List<WishItem> findAllByItem(Item item);
}
