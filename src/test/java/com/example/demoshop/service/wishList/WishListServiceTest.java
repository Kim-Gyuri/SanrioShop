package com.example.demoshop.service.wishList;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.domain.wishList.WishItem;
import com.example.demoshop.domain.wishList.WishList;
import com.example.demoshop.exception.users.UserNotFoundException;
import com.example.demoshop.exception.wishList.WishItemNotFoundException;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.exception.item.ItemNotFoundException;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.repository.wishList.WishItemRepository;
import com.example.demoshop.repository.wishList.WishListRepository;
import com.example.demoshop.request.users.SignupRequest;
import com.example.demoshop.response.wishList.WishItemResponse;
import com.example.demoshop.service.item.ItemService;
import com.example.demoshop.service.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
@Transactional
class WishListServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    WishListService wishListService;

    @Autowired
    UserService userService;


    @Test
    @DisplayName("찜하기 표시 - 성공 케이스")
    void markAsWished_success() throws IOException {
        // given
        User uploader = getUserDto("jinu4");
        User buyer = getUserDto("nana4");

        Item item = uploadItem(uploader);
        
        // when
        WishItemResponse wishItemResponse = wishListService.markAsWished(buyer, item.getId());

        // then
        assertEquals(1, wishItemResponse.getLikeCount());

        // clean
        userRepository.delete(buyer);
        userRepository.delete(uploader);

    }


    @Test
    @DisplayName("찜하기 취소 - 성공 케이스")
    void unmarkAsWished_success() throws IOException {
        // given
        User uploader = getUserDto("tay3");
        User buyer = getUserDto("mhamha3");

        Item item = uploadItem(uploader);

        WishItemResponse markResponse = wishListService.markAsWished(buyer, item.getId());

        // when
        WishItemResponse wishItemResponse = wishListService.unmarkAsWished(buyer, item.getId());

        // then
        assertEquals(0, wishItemResponse.getLikeCount());

        // clean
        userRepository.delete(buyer);
        userRepository.delete(uploader);
    }




    private Item uploadItem(User user) throws IOException {
        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .nameKor("산리오 한교동 가방고리 동전지갑")
                .price(18000)
                .description("14x10(cm) 크기.")
                .sanrioCharacters(SanrioCharacters.HANGYODON)
                .mainCategory(MainCategory.ACCESSORIES)
                .subCategory(SubCategory.WALLET)
                .build();

        List<String> userDefinedTagNames = List.of("한교동", "가방고리", "동전지갑", "정품");
        List<TagOption> recommendedTagOptions = List.of(TagOption.FLAT_CASE, TagOption.FACE_SHAPE_CASE, TagOption.PORTABLE_MIRROR);

        createItemRequest.setUserDefinedTagNames(userDefinedTagNames);
        createItemRequest.setRecommendedTagOptions(recommendedTagOptions);

        Long itemId = itemService.createItem(user, createItemRequest, generateMultipartFileList());

        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    private User getUserDto(String name) {
        SignupRequest request = SignupRequest.builder()
                .email(name + "@gmail.com")
                .password("12341234")
                .nickname(name)
                .build();

        userService.signup(request);

        return userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("가입되지 않은 회원"));
    }



    private static List<MultipartFile> generateMultipartFileList() {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0; i<2; i++){ // 상품 이미지 경로 + 이미지 이름 저장해서 add
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

}