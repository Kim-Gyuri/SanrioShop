package com.example.demoshop.controller;

import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.request.item.IdRequest;
import com.example.demoshop.response.item.WishlistItemDto;
import com.example.demoshop.response.wishList.WishItemResponse;
import com.example.demoshop.service.wishList.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class WishItemApiController {

    private final WishListService wishListService;
    private final PagedResourcesAssembler<WishlistItemDto> pagedResourcesAssembler;

    @PostMapping(value = "/wish")
    public WishItemResponse addWishList(@AuthenticationPrincipal User user, @RequestBody IdRequest idRequest) {
        return wishListService.markAsWished(user, idRequest.getId());
    }

    @GetMapping(value = "/wish")
    public PagedModel<EntityModel<WishlistItemDto>> getWishListByUser(@AuthenticationPrincipal User user, Pageable pageable) {
        Page<WishlistItemDto> response = wishListService.findWishList(user.getEmail(), pageable);

        return pagedResourcesAssembler.toModel(response);
    }

    @DeleteMapping(value = "/wish/wishItem/{itemId}")
    public WishItemResponse deleteWish(@AuthenticationPrincipal User user, @PathVariable("itemId") Long itemId) {
        return wishListService.unmarkAsWished(user, itemId);
    }
}
