package com.example.demoshop.domain.item.common;

public enum IsMainImg {
    Y("Y", "대표이미지"),
    N("N", "세부 이미지");

    private String YN;
    private String kCode;

    IsMainImg(String YN, String kCode) {
        this.YN = YN;
        this.kCode = kCode;
    }


}