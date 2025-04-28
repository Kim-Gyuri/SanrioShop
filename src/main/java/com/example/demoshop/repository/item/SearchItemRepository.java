package com.example.demoshop.repository.item;

import com.example.demoshop.controller.dto.CategoryCondition;
import com.example.demoshop.controller.dto.SearchCondition;
import com.example.demoshop.response.item.*;
import com.example.demoshop.response.sale.SaleItemResponse;
import com.example.demoshop.response.sale.UserNotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

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


    /** 카테고리 페이지
     * searchByCategory_tag : (테마검색 + 태그) + 산리오 구분 검색가능
     */
    Slice<ThumbnailItemDto> search_category_no_tag(Long lastItemId, int pageSize, CategoryCondition condition, String userEmail);
    Slice<ThumbnailItemDto> search_category_with_tag(Long lastItemId, int pageSize, CategoryCondition condition, String userEmail);


    /** 메인 페이지
     * searchMainPageItems_tag : (태그 + 산리오 구분) 검색가능
     */
    Slice<ThumbnailItemDto> searchMainPageItems_Cursor(Long lastItemId, int pageSize, SearchCondition condition, String userEmail);
    Slice<ThumbnailItemDto> searchMainPageItems_tag_Cursor(Long lastItemId, int pageSize, SearchCondition condition, String userEmail);

    /**
     * 상품마다 찜등록한 회원정보 조회  (상품 페이징 조회에서 상품에 대한 찜등록 정보를 찾기 위한 것)
     * 상품 ID별 찜 등록한 회원정보 조회
     */
    Map<Long, List<String>> getLikerTuplesByItemIds(List<Long> itemIds);
}
