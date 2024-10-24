package com.example.demoshop.exception;

import com.example.demoshop.exception.item.*;
import com.example.demoshop.exception.sale.SaleItemAlreadyExistsException;
import com.example.demoshop.exception.sale.SaleItemNotFoundException;
import com.example.demoshop.exception.token.InvalidRefreshTokenException;
import com.example.demoshop.exception.token.RefreshTokenNotFoundException;
import com.example.demoshop.exception.token.TokenNotFoundException;
import com.example.demoshop.exception.users.DuplicateEmailException;
import com.example.demoshop.exception.token.InvalidTokenException;
import com.example.demoshop.exception.users.DuplicateNicknameException;
import com.example.demoshop.exception.users.NotAuthorizedException;
import com.example.demoshop.exception.users.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static com.example.demoshop.utils.constants.ResponseConstants.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public final ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException exception) {
        log.info("중복된 이메일입니다.", exception);
        return DUPLICATION_EMAIL;
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public final ResponseEntity<String> handleDuplicateNicknameException(DuplicateNicknameException exception) {
        log.info("중복된 닉네임입니다.", exception);
        return DUPLICATION_NICK;
    }

    @ExceptionHandler(SaleItemAlreadyExistsException.class)
    public final ResponseEntity<String> handleSaleItemAlreadyExistsException(SaleItemAlreadyExistsException exception) {
        log.info("이미 주문요청을 받은 상품입니다.", exception);
        return TRADE_ALREADY_EXISTS_EXCEPTION;
    }

    @ExceptionHandler(SaleItemNotFoundException.class)
    public final ResponseEntity<String> handleSaleItemNotFoundException(SaleItemNotFoundException saleItemNotFoundException) {
        log.info("주문요청을 받지 않은 상품입니다.");
        return TRADE_NOT_FOUND_EXCEPTION;
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public final ResponseEntity<String> handleItemNotFoundException(ItemNotFoundException exception) {
        log.info("존재하지 않는 상품입니다.", exception);
        return ITEM_NOT_FOUND;
    }

    @ExceptionHandler(ItemDeletionNotAllowedException.class)
    public final ResponseEntity<String> handleItemDeletionNotAllowedException(ItemDeletionNotAllowedException exception) {
        log.info("주문요청을 받은 상품은 삭제할 수 없습니다.", exception);
        return DELETION_NOT_ALLOWED;
    }

    @ExceptionHandler(TagNotFoundException.class)
    public final ResponseEntity<String> handleTagNotFoundException(TagNotFoundException exception) {
        log.info("주문요청을 받은 상품은 삭제할 수 없습니다.", exception);
        return TAG_NOT_FOUND;
    }

    @ExceptionHandler(ItemImgNotFoundException.class)
    public final ResponseEntity<String> handleItemImgNotFoundException(ItemImgNotFoundException exception) {
        log.info("존재하지 않는 상품 이미지입니다.", exception);
        return ITEM_IMG_NOT_FOUND;
    }

    @ExceptionHandler(MinimumImageRequiredException.class)
    public final ResponseEntity<String> handleMinimumImageRequiredException(MinimumImageRequiredException exception) {
        log.info("상품에 대한 최소한의 이미지가 필요합니다, 최소 1개");
        return ITEM_IMG_MIN_REQUIRED;
    }


    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<String> handleUserNotFoundException(
            UserNotFoundException exception) {
        log.debug("존재하지 않는 Email 또는 password 불일치", exception);
        return USER_NOT_FOUND;
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public final ResponseEntity handleNotAuthorizedException(NotAuthorizedException exception,
                                                             WebRequest webRequest) {
        log.debug("Not Authorized :: {}, detection time ={}", webRequest.getDescription(false),
                LocalDateTime.now(), exception);
        return NOT_AUTHORIZED;
    }
    @ExceptionHandler(InvalidTokenException.class)
    public final ResponseEntity handleInvalidTokenException(InvalidTokenException exception) {
        log.debug("인증 헤더가 잘못되었거나 누락되었습니다.", exception);
        return INVALID_AUTHORIZED;
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public final ResponseEntity<String> handleTokenNotFoundException(TokenNotFoundException exception) {
        log.debug("존재하지 않는 token 입니다.", exception);
        return TOKEN_NOT_FOUND;
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public final ResponseEntity<String> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        return REFRESH_TOKEN_NULL;
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public final ResponseEntity<String> handleInvalidRefreshTokenException(InvalidRefreshTokenException exception) {
        log.debug(exception.getMessage());
        return INVALID_REFRESH_TOKEN;
    }

}


