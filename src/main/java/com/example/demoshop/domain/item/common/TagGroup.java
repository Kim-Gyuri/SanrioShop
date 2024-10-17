package com.example.demoshop.domain.item.common;

import lombok.Getter;
import static com.example.demoshop.domain.item.common.TagOption.*;

@Getter
public enum TagGroup {

    STICKER("스티커", new TagOption[] {
            CLEAR_STICKER,
            CUTTING_DECO_STICKER,
            PAPER_STICKER,
            GOLD_FOIL_STICKER,
            THREE_D_STICKER,
            NAME_STICKER,
            HOLOGRAM_STICKER,
            MATTE_STICKER,
            GLOSSY_STICKER
    }),

    MEMO_PAD("메모지", new TagOption[] {
            PAD_MEMO,
            STICKY_MEMO,
            RING_MEMO
    }),

    STAMP("스탬프", new TagOption[] {
            SINGLE_STAMP,
            ROTATING_STAMP,
            WOOD_STAMP,
            CLEAR_STAMP,
            STAMP_SET,
            STAMP_CASE
    }),

    DIARY("다이어리", new TagOption[] {
            FABRIC_BOOK_COVER,
            HARDCOVER_DIARY,
            A5_SIZE,
            A6_SIZE,
            A7_SIZE,
            RING_TYPE_DIARY,
            NOTE_TYPE_DIARY
    }),

    NOTE("노트", new TagOption[] {
            PLAIN_NOTE,
            LINED_NOTE,
            GRID_NOTE,
            DOT_NOTE,
            A4_SIZE,
            A5_SIZE,
            B5_SIZE,
            B6_SIZE,
            B7_SIZE,
    }),

    FILE_BINDER("파일/바인더", new TagOption[] {
            A5_FILE,
            A4_FILE_L_HOLDER,
            INDEX_FILE,
            ZIPPER_FILE,
    }),

    PEN_HOLDER("펜꽂이", new TagOption[] {
            ACRYLIC_PEN_HOLDER,
            PLASTIC_PEN_HOLDER
    }),

    OFFICE_SUPPLIES("사무용품", new TagOption[] {
            KNIFE_SCISSORS,
            CORRECTION_TAPE,
            RULER,
            CLIP_PIN,
            STAPLER,
    }),

    PHONE("휴대폰", new TagOption[] {
            CABLE_ORGANIZER,
            PHONE_CASE,
            PHONE_GRIP
    }),


    BALLPOINT_PEN("볼펜", new TagOption[] {
            PEN_0_5MM,
            PEN_0_4MM,
            PEN_0_7MM,
            PEN_0_3MM,
            PEN_2_COLOR,
            PEN_3_COLOR,
            PEN_4_COLOR,
            PEN_5_COLOR,
            CHARM_BALLPOINT_PEN,
            MASCOT_BALLPOINT_PEN
    }),

    MECHANICAL_PENCIL("샤프", new TagOption[] {
            PEN_0_3MM,
            PEN_0_5MM,
            NOTE_TYPE_DIARY,
            CHARM_SHARP,
            MASCOT_SHARP
    }),

    COLOR_DECO_PEN("컬러 데코펜", new TagOption[] {
            GLITTER_PEN,
            SIGN_PEN
    }),

    HAIR_ITEM("헤어템", new TagOption[] {
            HAIR_ACCESSORY,
            BRUSH,
            FACIAL_BAND,
            HEADBAND
    }),

    MIRROR("거울", new TagOption[] {
            MINI_STAND_MIRROR,
            MIRROR_KEYRING,
            COMB_MIRROR,
            PORTABLE_MIRROR,
            STAND_MIRROR,
    }),
    WALLET("지갑", new TagOption[] {
            COIN_WALLET,
            FOLD_WALLET,
            CLEAR_TYPE_WALLET,
            LONG_WALLET
    }),
    BAG("가방", new TagOption[] {
            SHOULDER_BAG,
            TOTE_BAG,
            CLEAR_TYPE_BAG,
            DOLL_BAG,
            FACE_SHAPE_BAG,
            ECO_BAG,
            MINI_SHOULDER_BAG,
            MESH_TYPE_BAG,
            POUCH_TYPE_BAG,
            MINI_BAG,
            CHECK_PATTERN_BAG,
            BASKET_BAG
    }),
    STORAGE_ORGANIZATION("수납/정리/보관템", new TagOption[] {
            COSMETIC_STORAGE,
            ZIPPER_BAG,
            TRAY,
            FOLDABLE,
            PLASTIC_STORAGE,
            STAIN_STORAGE,
            BASKET,
            COLLECTION_STORAGE_BOX,
            ACRYLIC_TYPE
    }),
    CUSHION_BLANKET("쿠션/담요", new TagOption[] {
            TOWEL,
            SUMMER_BLANKET,
            WINTER_BLANKET
    }),
    POUCH_CASE("파우치/케이스", new TagOption[] {
            PEN_CASE,
            FACE_SHAPE_CASE,
            CLEAR_TYPE_CASE,
            MINI_CASE,
            SEWING_CASE,
            FLAT_CASE,
            THREE_D_CASE,
            GLASSES_CASE,
            ZIPPER_BAG_CASE
    });

    private String nameKor;
    private TagOption[] tagOptions;

    TagGroup(String nameKor, TagOption[] tagOptions) {
        this.nameKor = nameKor;
        this.tagOptions = tagOptions;
    }

}