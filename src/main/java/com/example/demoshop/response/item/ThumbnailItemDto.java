package com.example.demoshop.response.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 썸네일에 배치되는 아이템 정렬용 -> 메인 페이지, 카테고리 페이지
 */
@Getter
@Setter
@NoArgsConstructor
public class ThumbnailItemDto {
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
    private List<String> userDefinedTags;
    private List<String> recommendedTags;
    private List<String> likers;
    private boolean isLikedByUser;  // 새로운 필드 추가

    // 기존 생성자에 새로운 필드 추가
    @Builder
    public ThumbnailItemDto(Long id, String nameKor, int price, String description, LocalDateTime createAt, int likeCount, String sanrioCharacter, String mainCategory, String subCategory, String thumbnail, List<String> userDefinedTags, List<String> recommendedTags, List<String> likers, boolean isLikedByUser) {
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
        this.userDefinedTags = userDefinedTags;
        this.recommendedTags = recommendedTags;
        this.likers = likers;
        this.isLikedByUser = isLikedByUser;
    }
}
