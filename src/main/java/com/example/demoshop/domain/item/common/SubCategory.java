package com.example.demoshop.domain.item.common;

import lombok.Getter;

@Getter
public enum SubCategory {


    // Popular Search
    DOLL("인형"),
    KEYRING_CHARM("키링/챰"),
    BROOCH_BADGE("브로치/뱃지"),
    ACRYLIC_STAND("아크릴 스탠드"),
    CAPSULE_TOY("캡슐토이/가챠"),
    FIGURE("피규어"),
    CARD_TYPE("카드류"),

    // Photocard Decor
    PHOTOCARD_HOLDER("포카카드 홀더"),
    TOPLOADER_SLEEVE("탑로더/슬리브"),

    // Desk Decor
    DESK_FIGURE("피규어"),
    HUMIDIFIER("가습기"),
    KEYBOARD("키보드"),
    KEYCAP("키캡"),
    MOUSE("마우스"),
    DESK_CLOCK("탁상시계"),
    HANDY_FAN("핸디팬"),
    BLUETOOTH("블루투스"),
    MOUSE_PAD("마우스패드"),
    AIR_FRESHENER("방향제"),
    USB_MULTI_HUB("USB 멀티허브"),

    // Journal & Stationery
    STICKER("스티커"),
    MASKING_TAPE("마스킹테이프"),
    MEMO_PAD("메모지"),
    LETTER_PAPER("편지지"),
    POSTCARD("엽서"),
    STAMP("스탬프"),
    DIARY("다이어리"),
    CALENDAR("달력"),
    NOTE("노트"),
    FILE_BINDER("파일/바인더"),
    PEN_HOLDER("펜꽂이"),
    OFFICE_SUPPLIES("사무용품"),
    PHONE_STRAP("휴대폰 스트랩"),

    // Writing Instruments
    BALLPOINT_PEN("볼펜"),
    MECHANICAL_PENCIL("샤프"),

    // Accessories
    HAIR_ITEM("헤어템"),
    MIRROR("거울"),
    WALLET("지갑"),
    BAG("가방"),
    STORAGE_ORGANIZATION("수납/정리"),
    CUSHION_BLANKET("쿠션/담요"),
    MAGNET("자석"),
    POUCH_CASE("파우치/케이스");

    private String nameKor;

    SubCategory(String nameKor) {
        this.nameKor = nameKor;
    }
}
