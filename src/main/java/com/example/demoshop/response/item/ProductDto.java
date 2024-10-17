package com.example.demoshop.response.item;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 썸네일에 배치되는 아이템 정렬용 -> 메인 페이지, 카테고리 페이지
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String nameKor;
    private int price;
    private String description;
    private LocalDateTime createAt;
    private int likeCount;
    private String sanrioCharacter;
    private String mainCategory;
    private String subCategory;
    private String thumbnail;
    private List<String> imgList;
    private List<String> userDefinedTags;
    private List<String> recommendedTags;
    private boolean isLikedByUser;  // 현재 로그인된 회원이 찜하기 눌렀는지
    private boolean isOrderedByUser; // 현재 로그인된 회원이 주문요청을 보냈는지


    @Builder
    public ProductDto(Long id, String nameKor, int price, String description, LocalDateTime createAt, int likeCount,
                      String sanrioCharacter, String mainCategory, String subCategory, String thumbnail,
                      List<String> imgList, List<String> userDefinedTags, List<String> recommendedTags,
                      boolean isLikedByUser, boolean isOrderedByUser) {
        this.id = id;
        this.nameKor = nameKor;
        this.price = price;
        this.description = description;
        this.createAt = createAt;
        this.likeCount = likeCount;
        this.sanrioCharacter = sanrioCharacter;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.thumbnail = thumbnail;
        this.imgList = imgList;
        this.userDefinedTags = userDefinedTags;
        this.recommendedTags = recommendedTags;
        this.isLikedByUser = isLikedByUser;
        this.isOrderedByUser = isOrderedByUser;
    }




}
