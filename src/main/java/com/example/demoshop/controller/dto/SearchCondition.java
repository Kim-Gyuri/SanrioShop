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

    private SanrioCharacters sanrioCharacters;
    private SearchType searchType;
    private String keyword;
}
