package com.example.demoshop.controller.dto;

import com.example.demoshop.domain.item.common.SanrioCharacters;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class SearchCondition {

    private SanrioCharacters sanrioCharacters; // 산리오 구분
    private SearchType searchType; // 타입> 태그/상품명
    private String keyword; // 검색어
}
