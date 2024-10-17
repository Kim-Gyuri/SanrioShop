package com.example.demoshop.domain.item.common;

import lombok.Getter;

import static com.example.demoshop.domain.item.common.SubCategory.*;


@Getter
public enum MainCategory {

    POPULAR_SEARCH("인기 검색어", new SubCategory[] {
            DOLL,
            KEYRING_CHARM,
            BROOCH_BADGE,
            ACRYLIC_STAND,
            CAPSULE_TOY,
            FIGURE,
            CARD_TYPE
    }),
    PHOTOCARD_DECOR("포카 꾸미기", new SubCategory[] {
            PHOTOCARD_HOLDER,
            TOPLOADER_SLEEVE
    }),
    DESK_DECOR("책상 꾸미기", new SubCategory[] {
            DESK_FIGURE,
            HUMIDIFIER,
            KEYBOARD,
            KEYCAP,
            MOUSE,
            DESK_CLOCK,
            HANDY_FAN,
            BLUETOOTH,
            MOUSE_PAD,
            AIR_FRESHENER,
            USB_MULTI_HUB
    }),
    JOURNAL_STATIONERY("다꾸/문구", new SubCategory[] {
            STICKER,
            MASKING_TAPE,
            MEMO_PAD,
            LETTER_PAPER,
            POSTCARD,
            STAMP,
            DIARY,
            CALENDAR,
            NOTE,
            FILE_BINDER,
            PEN_HOLDER,
            OFFICE_SUPPLIES,
            PHONE_STRAP
    }),
    WRITING_INSTRUMENTS("필기류", new SubCategory[] {
            BALLPOINT_PEN,
            MECHANICAL_PENCIL
    }),
    ACCESSORIES("소품", new SubCategory[] {
            HAIR_ITEM,
            MIRROR,
            WALLET,
            BAG,
            STORAGE_ORGANIZATION,
            CUSHION_BLANKET,
            MAGNET,
            POUCH_CASE
    });


    private String nameKor;
    private SubCategory[] subCategories;

    MainCategory(String nameKor, SubCategory[] subCategories) {
        this.nameKor = nameKor;
        this.subCategories = subCategories;
    }

}
