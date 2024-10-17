package com.example.demoshop.response.wishList;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class WishItemResponse {

    private Long wishItemId;
    private int likeCount;

    @Builder
    public WishItemResponse(int likeCount, Long wishItemId) {
        this.likeCount = likeCount;
        this.wishItemId = wishItemId;
    }

    @Builder
    public WishItemResponse(int likeCount) {
        this.likeCount = likeCount;
    }
}
