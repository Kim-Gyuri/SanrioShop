package com.example.demoshop.repository.item;

import com.example.demoshop.domain.item.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    Optional<ItemImg> findByImgUrl(String imgUrl);
}
