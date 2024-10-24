package com.example.demoshop.controller;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.request.item.UpdateItemRequest;
import com.example.demoshop.request.users.SignupRequest;
import com.example.demoshop.service.item.ItemService;
import com.example.demoshop.service.users.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ItemApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ResourceLoader loader;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void clean() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void cleanAfter() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("상품 등록")
    void upload_item_success() throws Exception {
        //given
        User user = getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        CreateItemRequest itemRequest = getItemRequest();
        List<MockMultipartFile> multipartFiles = getMockMultipartFiles();

        MockMultipartFile request = new MockMultipartFile("itemCreate", null, "application/json", objectMapper.writeValueAsString(itemRequest).getBytes(StandardCharsets.UTF_8));


        // when
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/api/items")
                .file(request)
                .file(multipartFiles.get(0))
                .file(multipartFiles.get(1))
                .file(multipartFiles.get(2))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
                .andExpect(status().isCreated())
                .andDo(print());


        // then
        Item item = itemRepository.findAll().get(0);
        assertEquals("산리오 한교동 가방고리 동전지갑", item.getNameKor());
        assertEquals(SanrioCharacters.HANGYODON, item.getSanrioCharacters());
    }


    @Test
    @DisplayName("상품 수정")
    void update_item_success() throws Exception {

        // given
        User user = getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        CreateItemRequest itemRequest = getItemRequest();
        List<MultipartFile> files = generateMultipartFileList();

        Long itemId = itemService.createItem(user, itemRequest, files);

        UpdateItemRequest updateItemRequest = updateItemRequest();
        List<MockMultipartFile> multipartFiles = getMockMultipartFiles();

        MockMultipartFile request = new MockMultipartFile("itemUpdate", null, "application/json", objectMapper.writeValueAsString(updateItemRequest).getBytes(StandardCharsets.UTF_8));

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, "/api/items/" + itemId)
                        .file(request)
                        .file(multipartFiles.get(0))
                        .file(multipartFiles.get(1))
                        .file(multipartFiles.get(2))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                )
                .andExpect(status().isOk())
                .andDo(print());


        // then
        Item item = itemRepository.findAll().get(0);
        assertEquals(10000, item.getPrice());
        assertEquals("10x10(cm) 크기.", item.getDescription());
    }

    @Test
    @DisplayName("상품 삭제")
    void delete_item_success() throws Exception {
        // given
        User user = getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        CreateItemRequest itemRequest = getItemRequest();
        List<MultipartFile> files = generateMultipartFileList();

        Long itemId = itemService.createItem(user, itemRequest, files);

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.DELETE, "/api/items/" + itemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertThat(itemRepository.findById(itemId)).isEmpty();

    }


    private static UpdateItemRequest updateItemRequest() {
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .nameKor("산리오 한교동 가방고리 동전지갑")
                .price(10000)
                .description("10x10(cm) 크기.")
                .sanrioCharacters(SanrioCharacters.HANGYODON)
                .mainCategory(MainCategory.ACCESSORIES)
                .subCategory(SubCategory.WALLET)
                .build();

        List<String> userDefinedTagNames_update = List.of("부들부들한 촉감");
        updateItemRequest.setUserDefinedTagNames(userDefinedTagNames_update);

        return updateItemRequest;
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

    private List<MockMultipartFile> getMockMultipartFiles() throws IOException {
        List<MockMultipartFile> multipartFiles = new ArrayList<>();
        Resource res1 = loader.getResource("classpath:/static/images/pochaco.png");
        Resource res2 = loader.getResource("classpath:/static/images/my_melody.png");
        Resource res3 = loader.getResource("classpath:/static/images/kuromi.png");
        MockMultipartFile files1 = new MockMultipartFile("productImage", "pochaco.png", "multipart/form-data", res1.getInputStream());
        MockMultipartFile files2 = new MockMultipartFile("productImage", "my_melody.png", "multipart/form-data", res2.getInputStream());
        MockMultipartFile files3 = new MockMultipartFile("productImage", "kuromi.png", "multipart/form-data", res3.getInputStream());
        multipartFiles.add(files1);
        multipartFiles.add(files2);
        multipartFiles.add(files3);

        return multipartFiles;
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