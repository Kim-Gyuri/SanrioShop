package com.example.demoshop.utils.constants;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

public class ResponseConstants {
    public static final ResponseEntity<Void> OK = ResponseEntity.ok().build();

    public static final ResponseEntity<Void> CREATED = ResponseEntity.status(HttpStatus.CREATED).build();

    public static final ResponseEntity<Void> BAD_REQUEST = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    public static final ResponseEntity<String> DUPLICATION_EMAIL = new ResponseEntity<>("중복된 이메일입니다.", HttpStatus.CONFLICT);
    public static final ResponseEntity<String> DUPLICATION_NICK = new ResponseEntity<>("중복된 닉네임입니다.", HttpStatus.CONFLICT);
    public static final ResponseEntity<String> ITEM_NOT_FOUND = new ResponseEntity<>("존재하지 않는 상품입니다.", HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<String> TAG_NOT_FOUND = new ResponseEntity<>("존재하지 않는 태그 정보입니다.", HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<String> TRADE_ALREADY_EXISTS_EXCEPTION = new ResponseEntity<>("이미 거래 중인 상품입니다.", HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<String> TRADE_NOT_FOUND_EXCEPTION = new ResponseEntity<>("주문내역이 없습니다.", HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<String> ITEM_IMG_NOT_FOUND = new ResponseEntity<>("존재하지 않는 상품 이미지입니다.", HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<String> ITEM_IMG_MIN_REQUIRED = new ResponseEntity<>("최소 이미지 수를 충족하지 못했습니다.", HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<String> USER_NOT_FOUND = new ResponseEntity<>("가입하지 않은 Email 이거나, 잘못된 password 입니다.", HttpStatus.NOT_FOUND);

    public static final ResponseEntity<String> NOT_AUTHORIZED = new ResponseEntity<>("해당 리소스에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    public static final ResponseEntity<String> INVALID_AUTHORIZED = new ResponseEntity<>("올바르지 않는 token 양식입니다.", HttpStatus.UNAUTHORIZED);

    public static final ResponseEntity<String> TOKEN_NOT_FOUND = new ResponseEntity<>("토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    public static final ResponseEntity<String> REFRESH_TOKEN_NULL = new ResponseEntity<>("refresh token이 없습니다.", HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<String> INVALID_REFRESH_TOKEN = new ResponseEntity<>("Invalid refresh token type", HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<String> DELETION_NOT_ALLOWED = new ResponseEntity<>("주문요청을 받은 상품은 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);
}
