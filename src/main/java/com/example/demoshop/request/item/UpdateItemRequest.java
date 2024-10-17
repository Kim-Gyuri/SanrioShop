package com.example.demoshop.request.item;

import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateItemRequest {

    private String nameKor;
    private int price;
    private String description;
    private SanrioCharacters sanrioCharacters;
    private MainCategory mainCategory;
    private SubCategory subCategory;
    private List<String> userDefinedTagNames;
    private List<TagOption> recommendedTagOptions;

    @Builder
    public UpdateItemRequest(String nameKor, int price, String description,
                             SanrioCharacters sanrioCharacters, MainCategory mainCategory, SubCategory subCategory) {
        this.nameKor = nameKor;
        this.price = price;
        this.description = description;
        this.sanrioCharacters = sanrioCharacters;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
    }

}
