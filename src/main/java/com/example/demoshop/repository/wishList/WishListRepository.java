package com.example.demoshop.repository.wishList;

import com.example.demoshop.domain.wishList.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
}
