package com.example.demoshop.request.item;

import com.example.demoshop.domain.item.common.IsMainImg;
import lombok.Data;

@Data
public class CreateImgRequest {

    private IsMainImg YN;


    public CreateImgRequest() {
        YN = IsMainImg.N;
    }

}
