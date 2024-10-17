package com.example.demoshop.response.item;

import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemDetailDto {

    private Long id; // item pk
    private String nameKor;
    private int price;
    private String description;
    private SanrioCharacters sanrioCharacters;
    private MainCategory mainCategory;
    private SubCategory subCategory;

    private List<String> imgList;
    private List<TagDto> userDefinedTags;
    private List<TagDto> recommendedTags;

    @Builder
    public ItemDetailDto(Long id, String nameKor, int price, String description,
                         SanrioCharacters sanrioCharacters, MainCategory mainCategory, SubCategory subCategory,
                         List<String> imgList, List<TagDto> userDefinedTags, List<TagDto> recommendedTags) {
        this.id = id;
        this.nameKor = nameKor;
        this.price = price;
        this.description = description;
        this.sanrioCharacters = sanrioCharacters;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.imgList = imgList;
        this.userDefinedTags = userDefinedTags;
        this.recommendedTags = recommendedTags;
    }
}
