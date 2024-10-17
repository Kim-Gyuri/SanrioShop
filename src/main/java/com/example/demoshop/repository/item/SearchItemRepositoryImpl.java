package com.example.demoshop.repository.item;

import com.example.demoshop.controller.dto.CategoryCondition;
import com.example.demoshop.controller.dto.SearchCondition;
import com.example.demoshop.domain.item.*;
import com.example.demoshop.domain.item.common.*;

import com.example.demoshop.domain.transaction.QSaleItem;
import com.example.demoshop.domain.users.user.QUser;
import com.example.demoshop.domain.wishList.QWishItem;
import com.example.demoshop.domain.wishList.QWishList;


import com.example.demoshop.response.item.*;

import com.example.demoshop.response.sale.QSaleItemResponse;
import com.example.demoshop.response.sale.QUserNotificationDto;
import com.example.demoshop.response.sale.SaleItemResponse;
import com.example.demoshop.response.sale.UserNotificationDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.*;
import java.util.stream.Collectors;

import static com.example.demoshop.domain.users.common.QUserBase.userBase;


@Slf4j
public class SearchItemRepositoryImpl implements SearchItemRepository {
    private final JPAQueryFactory queryFactory;

    public SearchItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);

    }


    @Override
    public Page<SellerItemDto> findAllBySeller(Pageable pageable, String userEmail) {

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QSaleItem saleItem = QSaleItem.saleItem;
        QUser buyer = QUser.user; // SaleItem의 buyer를 나타내는 QUser
        QUser seller = QUser.user; // Item의 uploader를 나타내는 QUser

        // Fetch results
        List<SellerItemDto> items = queryFactory
                .select(new QSellerItemDto(
                        item.id,
                        saleItem.id.as("saleItemId"),
                        item.nameKor,
                        item.price,
                        item.createAt,
                        item.likeCount,
                        itemImg.imgUrl.as("thumbnail"),
                        buyer.email.as("buyerEmail"),  // userBase의 email 필드를 가져옴
                        saleItem.status
                ))
                .from(item)
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))
                .leftJoin(saleItem).on(saleItem.item.eq(item))
                .leftJoin(buyer).on(buyer.id.eq(saleItem.buyer.id)) // SaleItem의 buyer와 User를 조인
                .leftJoin(seller).on(seller.id.eq(item.uploader.id)) // Item의 uploader와 User를 조인
                .where(item.uploader.email.eq(userEmail))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(item)
                .where(item.uploader.email.eq(userEmail))
                .fetchCount();

        return new PageImpl<>(items, pageable, total);
    }

    @Override
    public SaleItemResponse findSaleItemDetail(Long itemId) {
        QSaleItem saleItem = QSaleItem.saleItem;
        QItem item = QItem.item;
        QUser buyer =QUser.user;

        return queryFactory
                .select(new QSaleItemResponse(buyer.email, saleItem.item.nameKor, saleItem.price))
                .from(saleItem)
                .innerJoin(saleItem.buyer, buyer)
                .innerJoin(saleItem.item, item)
                .where(saleItem.item.id.eq(itemId))
                .fetchOne();
    }

    @Override
    public Page<WishlistItemDto> findWishListByUser(Pageable pageable, String userEmail) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QUser user = QUser.user;
        QWishItem wishItem = QWishItem.wishItem;

        // Fetch results
        List<WishlistItemDto> items = queryFactory
                .select(new QWishlistItemDto(
                        item.id,
                        wishItem.id,
                        item.nameKor,
                        item.price,
                        itemImg.imgUrl.as("thumbnail")
                ))
                .from(item)
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))  // 메인 이미지 조인
                .leftJoin(wishItem).on(wishItem.item.eq(item))  // wishItem 조인
                .join(wishItem.wishList.user, user)  // 이 부분을 inner join으로 변경
                .where(user.email.eq(userEmail))  // 특정 사용자의 이메일 조건
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(item)
                .leftJoin(wishItem).on(wishItem.item.eq(item))  // wishItem 조인
                .join(wishItem.wishList.user, user)  // 이 부분을 inner join으로 변경
                .where(user.email.eq(userEmail))
                .fetchCount();

        return new PageImpl<>(items, pageable, total);
    }

    @Override
    public Page<CustomerOrderDto> findOrderListByUser(Pageable pageable, String userEmail) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QSaleItem saleItem = QSaleItem.saleItem;

        // Fetch results
        List<CustomerOrderDto> items = queryFactory
                .select(new QCustomerOrderDto(
                        item.id,
                        saleItem.id,
                        item.nameKor,
                        item.price,
                        itemImg.imgUrl.as("thumbnail")
                ))
                .from(saleItem)
                .join(saleItem.item, item)  // SaleItem과 Item 간의 inner join
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))
                .where(saleItem.buyer.email.eq(userEmail))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(saleItem)
                .join(saleItem.item, item)  // SaleItem과 Item 간의 inner join
                .where(saleItem.buyer.email.eq(userEmail))
                .fetchCount();

        return new PageImpl<>(items, pageable, total);
    }


    @Override
    public boolean isExistInUserWishList(Long itemId, String userEmail) {
        QUser user = QUser.user;
        QWishItem wishItem = QWishItem.wishItem;
        QWishList wishList = QWishList.wishList;

        Integer fetchOne = queryFactory
                .selectOne()
                .from(wishItem)
                .leftJoin(wishItem.wishList, wishList) // Left Join 사용
                .leftJoin(wishList.user, user) // Left Join 사용
                .where(
                        wishItem.item.id.eq(itemId),
                        user.email.eq(userEmail)
                )
                .fetchOne();

        return fetchOne != null;
    }

    @Override
    public Page<ThumbnailItemDto> searchMainPageItems_tag(Pageable pageable, SearchCondition condition, String userEmail) {

        QItem item = QItem.item;
        QUserDefinedTag userDefinedTag = QUserDefinedTag.userDefinedTag;
        QRecommendedTag recommendedTag = QRecommendedTag.recommendedTag;

        BooleanBuilder whereClause = buildTagSearchConditionForMainPage(condition, item, recommendedTag, userDefinedTag);

        List<Item> items = queryFactory.selectFrom(item)
                .distinct()
                .leftJoin(item.recommendedTagList, recommendedTag)
                .leftJoin(item.userDefinedTagList, userDefinedTag)
                .where(whereClause)
                .offset(pageable.getOffset())   // Set the starting point of the results
                .limit(pageable.getPageSize())  // Set the number of results to return
                .fetch();

        long total = queryFactory.select(item.id.countDistinct())
                .from(item)
                .leftJoin(item.recommendedTagList, recommendedTag)
                .leftJoin(item.userDefinedTagList, userDefinedTag)
                .where(whereClause)
                .fetchOne();

        // 상품에 찜하기를 누른 유저 정보를 찾음.
        Map<Long, List<String>> likers = getLikers();

        // Map ItemTemp to ItemDto
        List<ThumbnailItemDto> finalItems = items.stream()
                .map(itemTemp -> convertToThumbnailItemDto(itemTemp, likers, userEmail))
                .collect(Collectors.toList());

        return new PageImpl<>(finalItems, pageable, total);
    }

    private static BooleanBuilder buildTagSearchConditionForMainPage(SearchCondition condition, QItem item, QRecommendedTag recommendedTag, QUserDefinedTag userDefinedTag) {
        BooleanBuilder whereClause = new BooleanBuilder();
        if (condition.getSanrioCharacters() != null) {
            whereClause.and(item.sanrioCharacters.eq(condition.getSanrioCharacters()));
        }
        String searchTerm = "%" + condition.getTag().toLowerCase() + "%";
        // 태그 검색 조건을 가져옵니다
        List<TagOption> tagOptions = TagOption.fromNameKor(condition.getTag());
        for (TagOption tagOption : tagOptions) {
            log.info("tag name= {}",  tagOption.getNameKor());
        }

        // 태그 검색 조건이 비어 있지 않은 경우
        if (!tagOptions.isEmpty()) {
            whereClause.and(
                    recommendedTag.tagOption.in(tagOptions)
                            .or(userDefinedTag.name.toLowerCase().like(searchTerm))
            );
        } else {
            // 태그 검색 조건이 비어 있는 경우
            whereClause.and(
                    userDefinedTag.name.toLowerCase().like(searchTerm)
            );
        }
        return whereClause;
    }


    @Override
    public Page<ThumbnailItemDto> searchMainPageItems_name(Pageable pageable, SearchCondition condition, String userEmail) {

        QItem item = QItem.item;

        BooleanBuilder whereClause = buildNameSearchConditionForMainPage(condition, item);

        List<Item> items = queryFactory.selectFrom(item)
                .where(whereClause)
                .offset(pageable.getOffset())   // Set the starting point of the results
                .limit(pageable.getPageSize())  // Set the number of results to return
                .fetch();

        long total = queryFactory.select(item.count())
                .from(item)
                .where(whereClause)
                .fetchOne();

        // 상품에 찜하기를 누른 유저 정보를 찾음.
        Map<Long, List<String>> likers = getLikers();

        // Map ItemTemp to ItemDto
        List<ThumbnailItemDto> finalItems = items.stream()
                .map(itemTemp -> convertToThumbnailItemDto(itemTemp, likers, userEmail))
                .collect(Collectors.toList());

        return new PageImpl<>(finalItems, pageable, total);
    }

    private static BooleanBuilder buildNameSearchConditionForMainPage(SearchCondition condition, QItem item) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if (condition.getSanrioCharacters() != null) {
            whereClause.and(item.sanrioCharacters.eq(condition.getSanrioCharacters()));
        }

        if (condition.getItemName() != null && !condition.getItemName().isEmpty()) {
            String searchTerm = "%" + condition.getItemName().toLowerCase() + "%";
            whereClause.and(item.nameKor.toLowerCase().like(searchTerm));
        }
        return whereClause;
    }

    @Override
    public Page<ThumbnailItemDto> searchByCategory(Pageable pageable, CategoryCondition condition, String userEmail) {
        QItem item = QItem.item;

        BooleanBuilder whereClause = buildCategorySearchCondition(condition, item);

        List<Item> items = queryFactory.selectFrom(item)
                .where(whereClause)
                .offset(pageable.getOffset())   // Set the starting point of the results
                .limit(pageable.getPageSize())  // Set the number of results to return
                .fetch();

        long total = queryFactory.select(item.count())
                .from(item)
                .where(whereClause)
                .fetchOne();

        // 상품에 찜하기를 누른 유저 정보를 찾음.
        Map<Long, List<String>> likers = getLikers();

        // Map ItemTemp to ItemDto
        List<ThumbnailItemDto> finalItems = items.stream()
                .map(itemTemp -> convertToThumbnailItemDto(itemTemp, likers, userEmail))
                .collect(Collectors.toList());

        return new PageImpl<>(finalItems, pageable, total);
    }

    private static BooleanBuilder buildCategorySearchCondition(CategoryCondition condition, QItem item) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if (condition.getMainCategory() != null) {
            whereClause.and(item.mainCategory.eq(condition.getMainCategory()));
        }

        if (condition.getSubCategory() != null) {
            whereClause.and(item.subCategory.eq(condition.getSubCategory()));
        }

        if (condition.getSanrioCharacters() != null) {
            whereClause.and(item.sanrioCharacters.eq(condition.getSanrioCharacters()));
        }
        return whereClause;
    }


    @Override
    public Page<ThumbnailItemDto> searchByCategory_tag(Pageable pageable, CategoryCondition condition, String userEmail) {
        QItem item = QItem.item;
        QUserDefinedTag userDefinedTag = QUserDefinedTag.userDefinedTag;
        QRecommendedTag recommendedTag = QRecommendedTag.recommendedTag;

        // 카테고리 페이지에서 태그 검색 조건을 포함하는 경우
        BooleanBuilder whereClause = buildCategoryAndTagSearchCondition(condition, item, recommendedTag, userDefinedTag);

        List<Item> items = queryFactory.selectFrom(item)
                .distinct()
                .leftJoin(item.recommendedTagList, recommendedTag)
                .leftJoin(item.userDefinedTagList, userDefinedTag)
                .where(whereClause)
                .offset(pageable.getOffset())   // Set the starting point of the results
                .limit(pageable.getPageSize())  // Set the number of results to return
                .fetch();

        long total = queryFactory.select(item.id.countDistinct())
                .from(item)
                .leftJoin(item.recommendedTagList, recommendedTag)
                .leftJoin(item.userDefinedTagList, userDefinedTag)
                .where(whereClause)
                .fetchOne();

        // 상품에 찜하기를 누른 유저 정보를 찾음.
        Map<Long, List<String>> likers = getLikers();

        // Map ItemTemp to ItemDto
        List<ThumbnailItemDto> finalItems = items.stream()
                .map(itemTemp -> convertToThumbnailItemDto(itemTemp, likers, userEmail))
                .collect(Collectors.toList());

        return new PageImpl<>(finalItems, pageable, total);
    }

    private static BooleanBuilder buildCategoryAndTagSearchCondition(CategoryCondition condition, QItem item, QRecommendedTag recommendedTag, QUserDefinedTag userDefinedTag) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if (condition.getMainCategory() != null) {
            whereClause.and(item.mainCategory.eq(condition.getMainCategory()));
        }

        if (condition.getSubCategory() != null) {
            whereClause.and(item.subCategory.eq(condition.getSubCategory()));
        }

        if (condition.getSanrioCharacters() != null) {
            whereClause.and(item.sanrioCharacters.eq(condition.getSanrioCharacters()));
        }

        String searchTerm = "%" + condition.getTag().toLowerCase() + "%";
        // 태그 검색 조건을 가져옵니다
        List<TagOption> tagOptions = TagOption.fromNameKor(condition.getTag());

        // 태그 검색 조건이 비어 있지 않은 경우
        if (!tagOptions.isEmpty()) {
            whereClause.and(
                    recommendedTag.tagOption.in(tagOptions)
                            .or(userDefinedTag.name.toLowerCase().like(searchTerm))
            );
        } else {
            // 태그 검색 조건이 비어 있는 경우
            whereClause.and(
                    userDefinedTag.name.toLowerCase().like(searchTerm)
            );
        }
        return whereClause;
    }


    private ThumbnailItemDto convertToThumbnailItemDto(Item item, Map<Long, List<String>> likers, String userEmail) {
        return new ThumbnailItemDto(
                item.getId(),
                item.getNameKor(),
                item.getPrice(),
                item.getDescription(),
                item.getCreateAt(),
                item.getLikeCount(),
                item.getSanrioCharacters().getNameKor(),
                item.getMainCategory().getNameKor(),
                item.getSubCategory().getNameKor(),
                item.getThumbnail(),
                item.getUserTagNames(),
                item.getRecommendTagNames(),
                likers.getOrDefault(item.getId(), List.of()),
                likers.getOrDefault(item.getId(), List.of()).contains(userEmail)
        );
    }

    private Map<Long, List<String>> getLikers() {

        QUser user = QUser.user;
        QWishItem wishItem = QWishItem.wishItem;
        QWishList wishList = QWishList.wishList;

        List<Tuple> likerTuples = queryFactory
                .select(wishItem.item.id, user.email)
                .from(wishItem)
                .join(wishItem.wishList, wishList)
                .join(wishList.user, user)
                .fetch();

        Map<Long, List<String>> likers = likerTuples.stream()
                .filter(tuple -> tuple.get(wishItem.item.id) != null && tuple.get(user.email) != null)
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(wishItem.item.id),
                        Collectors.mapping(tuple -> tuple.get(user.email), Collectors.toList())
                ));

        return likers;
    }

    @Override
    public List<UserNotificationDto> findUsersByItemId(Long itemId) {
        QUser user = QUser.user;
        QWishList wishList = QWishList.wishList;
        QWishItem wishItem = QWishItem.wishItem;

        return queryFactory
                .select(new QUserNotificationDto(
                        user.nickname,
                        user.email))
                .from(wishItem)
                .join(wishItem.wishList, wishList)
                .join(wishList.user, user)
                .where(wishItem.item.id.eq(itemId))
                .fetch();

    }
}