package com.example.demoshop.repository.item;

import com.example.demoshop.controller.dto.CategoryCondition;
import com.example.demoshop.controller.dto.SearchCondition;
import com.example.demoshop.response.item.*;
import com.example.demoshop.response.sale.SaleItemResponse;
import com.example.demoshop.response.sale.UserNotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchItemRepository {

    // 회원의 판매목록 조회
    Page<SellerItemDto> findAllBySeller(Pageable pageable, String userEmail);

    // 판매자 > 주문 상세조회
    SaleItemResponse findSaleItemDetail(Long itemId);

    // 특정 상품을 찜목록으로 등록한 회원 조회
    List<UserNotificationDto> findUsersByItemId(Long itemId);

    // 회원의 찜하기 목록 조회
    Page<WishlistItemDto> findWishListByUser(Pageable pageable, String userEmail);

    // 회원의 주문목록 조회
    Page<CustomerOrderDto> findOrderListByUser(Pageable pageable, String userEmail);

    // 회원이 특정 상품에 대해 찜하기를 등록했는지 확인합니다.
    boolean isExistInUserWishList(Long itemId, String userEmail);

    /** 메인 페이지
     * searchMainPageItems_tag : 태그 검색 조건을 사용하여 상품을 조회합니다.
     * searchMainPageItems_name : 상품명 검색 조건을 사용하여 상품을 조회합니다.
     */
    Page<ThumbnailItemDto> searchMainPageItems_tag(Pageable pageable, SearchCondition condition, String userEmail);
    Page<ThumbnailItemDto> searchMainPageItems_name(Pageable pageable, SearchCondition condition, String userEmail);

    /** 카테고리 페이지
     * searchByCategory : 주어진 카테고리 조건을 기준으로 상품을 조회합니다.
     * searchByCategory_tag : 선택된 카테고리 조건에 추가로 태그 검색 조건을 적용하여 상품을 조회합니다.
     */
    Page<ThumbnailItemDto> searchByCategory(Pageable pageable, CategoryCondition condition, String userEmail);
    Page<ThumbnailItemDto> searchByCategory_tag(Pageable pageable, CategoryCondition condition, String userEmail);

}
