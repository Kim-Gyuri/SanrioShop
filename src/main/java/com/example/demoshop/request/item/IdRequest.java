package com.example.demoshop.request.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class IdRequest {

    private Long id; // item pk


    @Builder
    public IdRequest(Long id) {
        this.id = id;
    }
}
