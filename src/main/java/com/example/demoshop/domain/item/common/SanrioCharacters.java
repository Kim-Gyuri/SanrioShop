package com.example.demoshop.domain.item.common;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SanrioCharacters {
    POCHACCO("포차코"),
    CINNAMOROLL("시나모롤"),
    HELLO_KITTY("헬로키티"),
    SHOW_BY_ROCK("쇼바이락"), //-> 나중에 삭제할 예정
    POMPOMPURIN("폼폼푸린"),
    KUROMI("쿠로미"),
    MY_MELODY("마이멜로디"),
    HANGYODON("한교동"),
    KEROKEROKEROPPI("케로케로케로피");


    private String nameKor;

    SanrioCharacters(String nameKor) {
        this.nameKor = nameKor;
    }

    public static SanrioCharacters enumOf(String nameKor) {
        return Arrays.stream(SanrioCharacters.values())
                .filter(t->t.getNameKor().equals(nameKor))
                .findAny().orElse(null);
    }

}
