package com.example.demoshop.domain.item;

import com.example.demoshop.domain.item.common.IsMainImg;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "item_img")
public class ItemImg {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_img_id")
    private Long id;

    private String imgUrl; // 이미지 경로

    @Enumerated(EnumType.STRING)
    private IsMainImg isMainImg;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemImg(String imgUrl, IsMainImg isMainImg) {
        this.imgUrl = imgUrl;
        this.isMainImg = isMainImg;
    }

    /**
     * 연관관계 메소드
     */
    public void assignToItem(Item item) {
        this.item = item;
    }

    public void updateThumbnail() {
        this.isMainImg = IsMainImg.Y;
    }

}
