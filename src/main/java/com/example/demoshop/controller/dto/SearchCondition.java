package com.example.demoshop.controller.dto;

import com.example.demoshop.domain.item.common.SanrioCharacters;
import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchCondition {

    private SanrioCharacters sanrioCharacters;
    private String tag;
    private String itemName;



}
