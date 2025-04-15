package com.example.demoshop.controller;

import com.example.demoshop.controller.dto.CategoryCondition;
import com.example.demoshop.controller.dto.SearchCondition;
import com.example.demoshop.controller.dto.SearchType;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.request.item.UpdateItemRequest;
import com.example.demoshop.response.item.*;
import com.example.demoshop.service.item.ItemImgService;
import com.example.demoshop.service.item.ItemService;
import com.example.demoshop.service.item.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;
    private final ItemImgService itemImgService;
    private final TagService tagService;
    private final PagedResourcesAssembler<ThumbnailItemDto> pagedResourcesAssembler;


    /**
     * 홈 > (산리오 구분/태그/상품명)조건으로 검색
     */
    @GetMapping("/items/list")
    public PagedModel<EntityModel<ThumbnailItemDto>> itemList(@AuthenticationPrincipal User user, Pageable pageable,
                                                              @RequestParam(required = false, name= "sanrio") SanrioCharacters sanrioCharacters,
                                                              @RequestParam(required = false, name = "keyword") String keyword,
                                                              @RequestParam(required = false, name = "searchType") SearchType searchType) {
        SearchCondition condition = new SearchCondition(sanrioCharacters, searchType, keyword);

        Page<ThumbnailItemDto> response = itemService.searchItems(pageable, condition, user.getEmail());

        return pagedResourcesAssembler.toModel(response);
    }


    /**
     * 홈 > 카테고리 메뉴 선택
     * 태그 검색도 가능
     */
    @GetMapping("/items/category")
    public PagedModel<EntityModel<ThumbnailItemDto>> testPage_search_category(@AuthenticationPrincipal User user, Pageable pageable,
                                                                              @RequestParam(required = false, name= "sanrio") SanrioCharacters sanrioCharacters,
                                                                              @RequestParam(required = false, name = "mainCategory") MainCategory mainCategory,
                                                                              @RequestParam(required = false, name = "subCategory") SubCategory subCategory,
                                                                              @RequestParam(required = false, name = "tag") String tag) {

        CategoryCondition condition = new CategoryCondition(sanrioCharacters, mainCategory, subCategory, tag);

        Page<ThumbnailItemDto> response = itemService.search_fetch_category(pageable, condition, user.getEmail());

       return pagedResourcesAssembler.toModel(response);
    }


    /**
     * 홈 > 상품 선택했을 때, 상품 상세 페이지
     */
    @GetMapping("/items/product/{itemId}")
    public ProductDto findProduct(@AuthenticationPrincipal User user, @PathVariable("itemId") Long id) {
        return itemService.findProduct(user, id);
    }

    /**
     * 판매자: 판매 상품 등록
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/items", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void write(@AuthenticationPrincipal User user,
                      @Validated @RequestPart(name = "itemCreate") CreateItemRequest itemRequest,
                      @RequestParam(name = "productImage") List<MultipartFile> multipartFiles) throws IOException {

        itemService.createItem(user, itemRequest, multipartFiles);

    }

    /**
     * 판매자: 판매 상품 삭제
     */
    @DeleteMapping("/items/{id}")
    public void deleteItem(@AuthenticationPrincipal User user, @PathVariable("id") Long itemId) {
        itemService.deleteItem(itemId);
    }

    /**
     * 판매자: 판매 상품 수정 > 상품정보 수정
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/items/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable("id") Long itemId, @RequestPart(name = "itemUpdate", required = false) UpdateItemRequest itemRequest,
                       @RequestParam(name = "productImage", required = false) List<MultipartFile> multipartFiles) throws IOException {

        itemService.updateItem(itemId, itemRequest, multipartFiles);
    }


    /**
     * 상품 수정 페이지를 위한 상품 정보 불러오기
     */
    @GetMapping("/items/product/{id}/edit")
    public ItemDetailDto findItemForUpdate(@PathVariable("id") Long itemId) {
        return itemService.findItemDetail(itemId);
    }



    /**
     * 판매자: 판매 상품 - 이미지 삭제
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/items/img")
    public void deleteImg(@AuthenticationPrincipal User user, @RequestParam ("img") String imgUrl) {
        itemImgService.deleteImg(imgUrl);
    }

    /**
     *  판매자: 판매 상품  - 태그 삭제
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/items/userTag/{id}")
    public void deleteUserTag(@AuthenticationPrincipal User user, @PathVariable("id") Long userDefinedTagId) {
        tagService.removeUserDefinedTag(userDefinedTagId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/items/recommendedTag/{id}")
    public void deleteRecommendedTag(@AuthenticationPrincipal User user,  @PathVariable("id") Long recommendedTagId) {
        tagService.removeRecommendedTag(recommendedTagId);
    }

}
