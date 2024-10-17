package com.example.demoshop.response.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 상품 수정 ->
 *  저장된 상품 태그 정보를 불러온다.
 */
@Getter
@Setter
@NoArgsConstructor
public class TagDto {
    private Long id; // tag pk
    private String name; // tag name

    @Builder
    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
