package com.example.demoshop.repository.item;


import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.UserDefinedTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDefinedTagRepository extends JpaRepository<UserDefinedTag, Long> {

    List<UserDefinedTag> findByName(String name);
    long countByName(String name);

    void deleteByItem(Item item);

    boolean existsByName(String name);
}
