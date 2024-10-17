package com.example.demoshop.service.item;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.RecommendedTag;
import com.example.demoshop.domain.item.UserDefinedTag;
import com.example.demoshop.exception.item.TagNotFoundException;
import com.example.demoshop.repository.item.RecommendedTagRepository;
import com.example.demoshop.repository.item.UserDefinedTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final UserDefinedTagRepository userDefinedTagRepository;
    private final RecommendedTagRepository recommendedTagRepository;

    public void removeUserDefinedTag(Long userDefinedTagId) {

        UserDefinedTag userDefinedTag = userDefinedTagRepository.findById(userDefinedTagId).orElseThrow(TagNotFoundException::new);

        Item item = userDefinedTag.getItem();
        item.removeUserDefinedTag(userDefinedTag);

        userDefinedTagRepository.delete(userDefinedTag);
    }

    public void removeRecommendedTag(Long recommendedTagId) {

        RecommendedTag recommendedTag = recommendedTagRepository.findById(recommendedTagId).orElseThrow(TagNotFoundException::new);

        Item item = recommendedTag.getItem();
        item.removeRecommendedTag(recommendedTag);

        recommendedTagRepository.delete(recommendedTag);
    }
}
