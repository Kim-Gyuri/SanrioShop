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
import org.springframework.data.domain.*;


import java.util.*;
import java.util.stream.Collectors;

import static com.example.demoshop.domain.users.user.QUser.user;
import static com.example.demoshop.domain.wishList.QWishItem.wishItem;
import static com.example.demoshop.domain.wishList.QWishList.*;
import static com.example.demoshop.response.item.ThumbnailItemDto.convertToThumbnailItemDto;


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
        QUser buyer = user; // SaleItem의 buyer를 나타내는 QUser
        QUser seller = user; // Item의 uploader를 나타내는 QUser

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
        QUser buyer = user;

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



    // 회원이 특정 상품에 대해 찜하기를 등록했는지 확인합니다.
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


    // 홈 > 태그 검색 포함한 경우
    @Override
    public Slice<ThumbnailItemDto> searchMainPageItems_tag_Cursor(
            Long lastItemId, int pageSize,
            SearchCondition condition, String userEmail) {

        List<Item> combinedItems = new ArrayList<>();

        // 키워드 null 체크
        String keyword = condition.getKeyword();
        if (keyword != null && !keyword.isBlank()) {
            combinedItems.addAll(fetchItemsByRecommendedTag(keyword, condition.getSanrioCharacters(), lastItemId));
            combinedItems.addAll(fetchItemsByUserDefinedTag(keyword, condition.getSanrioCharacters(), lastItemId));
        }

        // 정렬 및 페이징 처리
        combinedItems.sort(Comparator.comparing(Item::getId).reversed());

        boolean hasNext = combinedItems.size() > pageSize;
        if (hasNext) {
            combinedItems = combinedItems.subList(0, pageSize);
        }

        List<Long> itemIds = combinedItems.stream().map(Item::getId).collect(Collectors.toList());
        Map<Long, List<String>> likers = getLikerTuplesByItemIds(itemIds); // 상품 찜 등록한 회원조회

        List<ThumbnailItemDto> dtoList = combinedItems.stream()
                .map(item -> convertToThumbnailItemDto(item, likers, userEmail))
                .collect(Collectors.toList());

        return new SliceImpl<>(dtoList, PageRequest.of(0, pageSize), hasNext);
    }

    // item의 recoomendTag 필드를 검색조건으로
    private List<Item> fetchItemsByRecommendedTag(String keyword,SanrioCharacters sanrioCharacters, Long lastItemId) {
        QItem item = QItem.item;
        QRecommendedTag recommendedTag = QRecommendedTag.recommendedTag;

        List<TagOption> tagOptions = TagOption.fromNameKor(keyword);
        if (tagOptions.isEmpty()) return Collections.emptyList();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(recommendedTag.tagOption.in(tagOptions));
        if (sanrioCharacters != null) {
            builder.and(item.sanrioCharacters.eq(sanrioCharacters));
        }
        if (lastItemId != null) {
            builder.and(item.id.lt(lastItemId));
        }
        // BooleanBuilder에서 만들어진 whereClause를 쿼리로 변환하여 로그 출력
        log.info("Generated whereClause: " + builder.toString());

        return queryFactory
                .selectFrom(item)
                .leftJoin(item.recommendedTagList, recommendedTag)
                .where(builder)
                .orderBy(item.id.desc())
                .fetch();
    }

    // item의 UserDefinedTag 필드를 검색조건으로
    private List<Item> fetchItemsByUserDefinedTag(String keyword,SanrioCharacters sanrioCharacters, Long lastItemId) {
        QItem item = QItem.item;
        QUserDefinedTag userDefinedTag = QUserDefinedTag.userDefinedTag;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(userDefinedTag.name.eq(keyword));
        if (sanrioCharacters!= null) {
            builder.and(item.sanrioCharacters.eq(sanrioCharacters));
        }
        if (lastItemId != null) {
            builder.and(item.id.lt(lastItemId));
        }

        log.info("Generated whereClause: " + builder.toString());

        return queryFactory
                .selectFrom(item)
                .leftJoin(item.userDefinedTagList, userDefinedTag)
                .where(builder)
                .orderBy(item.id.desc())
                .fetch();
    }



    // 홈> 검색 ( 검색조건이 없는 경우 cusor 페이징만 처리한다.)
    @Override
    public Slice<ThumbnailItemDto> searchMainPageItems_Cursor(
            Long lastItemId, int pageSize,
            SearchCondition condition, String userEmail) {

        QItem item = QItem.item;
        QUserDefinedTag userDefinedTag = QUserDefinedTag.userDefinedTag;
        QRecommendedTag recommendedTag = QRecommendedTag.recommendedTag;

        BooleanBuilder whereClause = new BooleanBuilder();

        // lastItemId 조건을 whereClause에 추가
        if (lastItemId != null) {
            whereClause = whereClause.and(item.id.lt(lastItemId));
        }

        // BooleanBuilder에서 만들어진 whereClause를 쿼리로 변환하여 로그 출력
        log.info("Generated whereClause: " + whereClause.toString());


        List<Item> items = queryFactory
                .selectFrom(item)
                .distinct()
                .leftJoin(item.recommendedTagList, recommendedTag)
                .leftJoin(item.userDefinedTagList, userDefinedTag)
                .where(whereClause)
                .orderBy(item.id.desc())
                .limit(pageSize + 1) // +1로 다음 페이지 유무 판단
                .fetch();

        boolean hasNext = items.size() > pageSize;

        if (hasNext) {
            items.remove(pageSize); // 초과 아이템 제거
        }

        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());
        Map<Long, List<String>> likers = getLikerTuplesByItemIds(itemIds);

        List<ThumbnailItemDto> dtoList = items.stream()
                .map(itemTemp -> convertToThumbnailItemDto(itemTemp, likers, userEmail))
                .collect(Collectors.toList());

        return new SliceImpl<>(dtoList, PageRequest.of(0, pageSize), hasNext);
    }


    /**
     * 카테고리 페이지 > 태그 검색을 하는 경우
     * (선택된 카테고리에서) + 태그 검색
     */
    @Override
    public Slice<ThumbnailItemDto> search_category_with_tag(
            Long lastItemId, int pageSize,
            CategoryCondition condition, String userEmail) {

        List<Item> combinedItems = new ArrayList<>();

        // 키워드 null 체크
        String keyword = condition.getTag();
        if (keyword != null && !keyword.isBlank()) {
            combinedItems.addAll(fetchItemsByRecommendedTag(keyword, condition.getSanrioCharacters(), lastItemId));
            combinedItems.addAll(fetchItemsByUserDefinedTag(keyword, condition.getSanrioCharacters(), lastItemId));
        }

        // 정렬 및 페이징 처리
        combinedItems.sort(Comparator.comparing(Item::getId).reversed());

        boolean hasNext = combinedItems.size() > pageSize;
        if (hasNext) {
            combinedItems = combinedItems.subList(0, pageSize);
        }


        List<Long> itemIds = combinedItems.stream().map(Item::getId).collect(Collectors.toList());
        Map<Long, List<String>> likers = getLikerTuplesByItemIds(itemIds); // 찜 등록한 회원조회

        List<ThumbnailItemDto> dtoList = combinedItems.stream()
                .map(item -> convertToThumbnailItemDto(item, likers, userEmail))
                .collect(Collectors.toList());

        return new SliceImpl<>(dtoList, PageRequest.of(0, pageSize), hasNext);
    }


    /**
     * 카테고리 페이지 >
     * (선택된 카테고리로 커서 페이징)
     */

    @Override
    public Slice<ThumbnailItemDto> search_category_no_tag(Long lastItemId, int pageSize, CategoryCondition condition, String userEmail) {

        QItem item = QItem.item;
        QUserDefinedTag userDefinedTag = QUserDefinedTag.userDefinedTag;
        QRecommendedTag recommendedTag = QRecommendedTag.recommendedTag;

        BooleanBuilder whereClause = buildCategoryAndTagSearchCondition(condition, item);

        // lastItemId 조건을 whereClause에 추가
        if (lastItemId != null) {
            whereClause = whereClause.and(item.id.lt(lastItemId));
        }

        // BooleanBuilder에서 만들어진 whereClause를 쿼리로 변환하여 로그 출력
        log.info("Generated whereClause: " + whereClause.toString());


        List<Item> items = queryFactory
                .selectFrom(item)
                .distinct()
                .leftJoin(item.recommendedTagList, recommendedTag)
                .leftJoin(item.userDefinedTagList, userDefinedTag)
                .where(whereClause)
                .orderBy(item.id.desc())
                .limit(pageSize + 1) // +1로 다음 페이지 유무 판단
                .fetch();

        boolean hasNext = items.size() > pageSize;

        if (hasNext) {
            items.remove(pageSize); // 초과 아이템 제거
        }

        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());
        Map<Long, List<String>> likers = getLikerTuplesByItemIds(itemIds); // 찜 등록한 회원조회

        List<ThumbnailItemDto> dtoList = items.stream()
                .map(itemTemp -> convertToThumbnailItemDto(itemTemp, likers, userEmail))
                .collect(Collectors.toList());

        return new SliceImpl<>(dtoList, PageRequest.of(0, pageSize), hasNext);
    }


    private static BooleanBuilder buildCategoryAndTagSearchCondition(CategoryCondition condition, QItem item) {
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


    // 상품 ID별 찜 등록한 회원정보 조회
    public Map<Long, List<String>> getLikerTuplesByItemIds(List<Long> itemIds) {

        List<Tuple> likerTuples = queryFactory
                .select(wishItem.item.id, user.email)
                .from(wishItem)
                .join(wishItem.wishList, wishList)
                .join(wishList.user, user)
                .where(wishItem.item.id.in(itemIds))
                .fetch();

        return likerTuples.stream()
                .filter(tuple -> tuple.get(wishItem.item.id) != null && tuple.get(user.email) != null)
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(wishItem.item.id),
                        Collectors.mapping(tuple -> tuple.get(user.email), Collectors.toList())
                ));
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