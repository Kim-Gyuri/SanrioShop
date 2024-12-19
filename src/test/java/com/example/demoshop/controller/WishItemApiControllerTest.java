package com.example.demoshop.controller;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.domain.wishList.WishItem;
import com.example.demoshop.exception.item.ItemNotFoundException;
import com.example.demoshop.exception.wishList.WishItemNotFoundException;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.sale.SaleItemRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.repository.wishList.WishItemRepository;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.request.item.IdRequest;
import com.example.demoshop.request.users.SignupRequest;
import com.example.demoshop.service.item.ItemService;
import com.example.demoshop.service.users.UserService;
import com.example.demoshop.service.wishList.WishListService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishItemApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WishItemRepository wishItemRepository;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SaleItemRepository saleItemRepository;


    @BeforeEach
    void clean() {
        saleItemRepository.deleteAll();
        itemRepository.deleteAll();
        wishItemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void cleanAfter() {
        saleItemRepository.deleteAll();
        itemRepository.deleteAll();
        wishItemRepository.deleteAll();
        userRepository.deleteAll();
    }



    @Test
    @DisplayName("찜 등록")
    void add_wish_success() throws Exception {
        //given
        User user = getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        CreateItemRequest itemRequest = getItemRequest();
        List<MultipartFile> files = generateMultipartFileList();

        Long itemId = itemService.createItem(user, itemRequest, files);
        IdRequest request = getRequest(itemId);



        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/wish")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isCreated())
                .andDo(print());


        // then
        Item findItem = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        WishItem wishItem = wishItemRepository.findByWishListAndItem(user.getWishList(), findItem)
                .orElseThrow(() -> new WishItemNotFoundException("유효하지 않는 찜상품입니다."));

        Item item = wishItem.getItem();

        assertEquals("산리오 한교동 가방고리 동전지갑", item.getNameKor());
        assertEquals(1, item.getLikeCount());

    }

    @Test
    @DisplayName("찜 취소")
    void remove_wish_success() throws Exception {
        //given
        User user = getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        CreateItemRequest itemRequest = getItemRequest();
        List<MultipartFile> files = generateMultipartFileList();

        Long itemId = itemService.createItem(user, itemRequest, files);
        IdRequest request = getRequest(itemId);

        wishListService.markAsWished(user, itemId);



        // When
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/wish/wishItem/" + itemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk())
                .andDo(print());


        // then
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        assertEquals(0, item.getLikeCount());

    }

    private IdRequest getRequest(Long itemId) {
        return IdRequest.builder()
                .id(itemId)
                .build();
    }

    private User getUser() {
        SignupRequest req = getSignupRequest();

        Long userId = userService.signup(req);

        return userService.findById(userId);
    }

    private static SignupRequest getSignupRequest() {
        return SignupRequest.builder()
                .email("none1234@gmail.com")
                .password("1234")
                .nickname("none")
                .build();
    }

    private CreateItemRequest getItemRequest() {
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

        return createItemRequest;
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