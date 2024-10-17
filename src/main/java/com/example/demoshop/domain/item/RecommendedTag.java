package com.example.demoshop.domain.item;

import com.example.demoshop.domain.item.common.TagOption;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RecommendedTag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TagOption tagOption;

    private long tagFrequency; // 태그 사용 빈도수

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public RecommendedTag(TagOption tagOption, long tagFrequency) {
        this.tagOption = tagOption;
        this.tagFrequency = tagFrequency;
    }

    public void assignToItem(Item item) {
        this.item = item;
    }

    public void updateTagFrequency(long count) {
        this.tagFrequency = count;
    }


    public String getTagName() {
        return this.tagOption.getNameKor();
    }
}
