package com.example.demoshop.repository.item;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.RecommendedTag;
import com.example.demoshop.domain.item.common.TagOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedTagRepository extends JpaRepository<RecommendedTag, Long> {

    List<RecommendedTag> findByTagOption(TagOption tagOption);
    long countByTagOption(TagOption tagOption);

    void deleteByItem(Item item);

    boolean existsByTagOption(TagOption tagOption);
}
