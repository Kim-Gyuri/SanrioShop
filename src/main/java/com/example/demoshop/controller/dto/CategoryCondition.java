package com.example.demoshop.controller.dto;

import com.example.demoshop.domain.item.RecommendedTag;
import com.example.demoshop.domain.item.UserDefinedTag;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryCondition {

    private SanrioCharacters sanrioCharacters;
    private MainCategory mainCategory;
    private SubCategory subCategory;
    private String tag;


}
