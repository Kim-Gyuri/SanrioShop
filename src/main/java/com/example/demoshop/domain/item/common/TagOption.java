package com.example.demoshop.domain.item.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum TagOption {

    // sticker
    CLEAR_STICKER("클리어 스티커"),
    CUTTING_DECO_STICKER("컷팅 데코 스티커"),
    PAPER_STICKER("페이퍼 스티커"),
    GOLD_FOIL_STICKER("금박 스티커"),
    THREE_D_STICKER("입체 스티커"),
    NAME_STICKER("네임 스티커"),
    HOLOGRAM_STICKER("홀로그램 스티커"),
    MATTE_STICKER("무광 스티커"),
    GLOSSY_STICKER("유광 스티커"),

    // Memo Pad Examples
    PAD_MEMO("떡메모지"),
    STICKY_MEMO("점착 메모지"),
    RING_MEMO("링메모지"),

    // Stamp Examples
    SINGLE_STAMP("단일 스탬프"),
    ROTATING_STAMP("회전 스탬프"),
    WOOD_STAMP("우드 스탬프"),
    CLEAR_STAMP("클리어 스탬프"),
    STAMP_SET("스탬프 세트"),
    STAMP_CASE("스탬프 케이스"),

    // Diary Examples
    FABRIC_BOOK_COVER("패브릭 북 커버"),
    HARDCOVER_DIARY("양장 다이어리"),

    A5_SIZE("A5"),
    A6_SIZE("A6"),
    A7_SIZE("A7"),
    A4_SIZE("A4"),
    B5_SIZE("B5"),
    B6_SIZE("B6"),
    B7_SIZE("B7"),

    RING_TYPE_DIARY("링 타입"),
    NOTE_TYPE_DIARY("노트 타입"),

    // Note Examples
    PLAIN_NOTE("무지"),
    LINED_NOTE("라인"),
    GRID_NOTE("그리드"),
    DOT_NOTE("도트"),

    // File/Binder Examples
    A5_FILE("A5 파일"),
    A4_FILE_L_HOLDER("A4 파일/L홀더"),
    INDEX_FILE("인덱스형 파일"),
    ZIPPER_FILE("지퍼형 파일"),

    // Pen holder
    ACRYLIC_PEN_HOLDER("아크릴 펜꽂이"),
    PLASTIC_PEN_HOLDER("플라스틱 펜꽂이"),

    // office supplies
    KNIFE_SCISSORS("칼/가위"),
    CORRECTION_TAPE("마스킹 테이프"),
    RULER("자"),
    CLIP_PIN("클립/집게"),
    STAPLER("스테이플러"),

    // phone
    CABLE_ORGANIZER("케이블 정리"),
    PHONE_CASE("폰케이스"),
    PHONE_GRIP("스마트톡"),

    // pen
    PEN_0_7MM("0.7mm"),
    PEN_0_6MM("0.6mm"),
    PEN_0_5MM("0.5mm"),
    PEN_0_4MM("0.4mm"),
    PEN_0_3MM("0.3mm"),
    PEN_2_COLOR("2색"),
    PEN_3_COLOR("3색"),
    PEN_4_COLOR("4색"),
    PEN_5_COLOR("5색"),
    CHARM_BALLPOINT_PEN("챰 볼펜"),
    MASCOT_BALLPOINT_PEN("마스코트 볼펜"),

    // sharp
    KNOCK_PENCIL("노크 연필"),
    CHARM_SHARP("챰 샤프"),
    MASCOT_SHARP("마스코트 샤프"),

    // Color Deco Pen Examples
    GLITTER_PEN("글리터펜"),
    SIGN_PEN("사인펜"),

    // Hair Item Examples
    HAIR_ACCESSORY("헤어 악세서리"),
    BRUSH("브러쉬"),
    FACIAL_BAND("세안밴드"),
    HEADBAND("머리띠"),

    // Mirror Examples
    MINI_STAND_MIRROR("미니 스탠드"),
    MIRROR_KEYRING("거울 키링"),
    COMB_MIRROR("빗&거울"),
    PORTABLE_MIRROR("휴대용"),
    STAND_MIRROR("스탠드 거울"),

    // Wallet Examples
    COIN_WALLET("동전지갑"),
    FOLD_WALLET("반지갑"),
    CLEAR_TYPE_WALLET("클리어 타입"),
    LONG_WALLET("장지갑"),


    // Bag Examples
    SHOULDER_BAG("숄더백"),
    TOTE_BAG("토트백"),
    CLEAR_TYPE_BAG("클리어 타입"),
    DOLL_BAG("인형가방"),
    FACE_SHAPE_BAG("얼굴 모양"),
    ECO_BAG("에코백"),
    MINI_SHOULDER_BAG("미니 숄더백"),
    MESH_TYPE_BAG("매쉬 타입"),
    POUCH_TYPE_BAG("파우치 타입"),
    MINI_BAG("미니"),
    CHECK_PATTERN_BAG("체크무늬"),
    BASKET_BAG("장바구니"),


    // Storage/Organization Examples
    COSMETIC_STORAGE("화장품 보관"),
    ZIPPER_BAG("지퍼백"),
    TRAY("트레이"),
    FOLDABLE("접이식"),
    PLASTIC_STORAGE("플라스틱"),
    STAIN_STORAGE("스테인"),
    BASKET("바구니"),
    COLLECTION_STORAGE_BOX("컬렉션 수납함"),
    ACRYLIC_TYPE("아크릴 타입"),


    // Cushion/Blanket Examples
    TOWEL("타올"),
    SUMMER_BLANKET("여름 담요"),
    WINTER_BLANKET("겨울 담요"),

    // Pouch/Case Examples
    PEN_CASE("펜"),
    FACE_SHAPE_CASE("얼굴 모양"),
    SEWING_CASE("복조리"),
    CLEAR_TYPE_CASE("클리어 타입"),
    MINI_CASE("미니"),
    SEWN_CASE("봉제"),
    FLAT_CASE("납작"),
    THREE_D_CASE("입체"),
    GLASSES_CASE("안경"),
    ZIPPER_BAG_CASE("지퍼백"),


    NONE("없음");


    private String nameKor;


    TagOption(String nameKor) {
        this.nameKor = nameKor;
    }


    public static List<TagOption> fromNameKor(String nameKor) {

        if (nameKor == null || nameKor.isEmpty()) {
            return new ArrayList<>();  // or throw an exception if that's more appropriate
        }

        String lowerCaseNameKor = nameKor.toLowerCase();

        return Stream.of(TagOption.values())
                .filter(option -> option.getNameKor().toLowerCase().contains(lowerCaseNameKor))
                .collect(Collectors.toList());
    }
}
