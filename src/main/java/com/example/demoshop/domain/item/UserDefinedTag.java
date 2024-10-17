package com.example.demoshop.domain.item;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class UserDefinedTag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_tag_id")
    private Long id;

    private String name;

    private long tagFrequency; // 태그 사용 빈도수

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public UserDefinedTag(String name, long tagFrequency) {
        this.name = name;
        this.tagFrequency = tagFrequency;
    }

    public void assignToItem(Item item) {
        this.item = item;
    }

    public void updateTagFrequency(long count) {
        this.tagFrequency = count;
    }


}


