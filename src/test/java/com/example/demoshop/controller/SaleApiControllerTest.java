package com.example.demoshop.controller;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.transaction.SaleItem;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.sale.SaleItemRepository;
import com.example.demoshop.repository.users.UserRepository;

import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.request.item.IdRequest;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class SaleApiControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserDetailsService userDetailsService;


    @BeforeEach
    void clean() {
        saleItemRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void cleanAfter() {
        saleItemRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }



    @Test
    @Transactional
    @DisplayName("주문 요청")
    void order_success() throws Exception {

        //given
        User buyer = getUser("amy");
        User seller = getUser("thumper");
        UserDetails userDetails = userDetailsService.loadUserByUsername(buyer.getEmail());

        CreateItemRequest itemRequest = getItemRequest();
        List<MultipartFile> files = generateMultipartFileList();

        Long itemId = itemService.createItem(seller, itemRequest, files);
        IdRequest request = getRequest(itemId);


        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isCreated())
                .andDo(print());


        // then
        SaleItem saleItem = saleItemRepository.findAll().get(0);
        Item item = saleItem.getItem();

        assertEquals("산리오 한교동 가방고리 동전지갑", item.getNameKor());
        assertEquals("amy", saleItem.getBuyer().getNickname());
    }

    @Test
    @DisplayName("동시 주문요청")
    void order_concurrency_fail() throws Exception {
        // given
        User buyer_a = getUser("amy");
        User buyer_b = getUser("woody");
        User seller = getUser("thumper");
        UserDetails userDetailsA = userDetailsService.loadUserByUsername(buyer_a.getEmail());
        UserDetails userDetailsB = userDetailsService.loadUserByUsername(buyer_b.getEmail());

        CreateItemRequest itemRequest = getItemRequest();
        List<MultipartFile> files = generateMultipartFileList();

        Long itemId = itemService.createItem(seller, itemRequest, files);
        IdRequest request = getRequest(itemId);

        // 동시성 테스트를 위한 CountDownLatch 설정
        AtomicInteger failCount = new AtomicInteger(0); // 실패한 주문의 개수를 셀 카운터
        ExecutorService executorService = Executors.newFixedThreadPool(2); // 두 개의 스레드를 사용할 스레드풀

        var startLatch = new CountDownLatch(1); // 동시 시작을 위한 래치
        var endLatch = new CountDownLatch(2); // 두 요청의 종료를 기다리는 래치

        // Runnable 생성하여 각각의 요청 작업 정의
        Runnable task1 = () -> {
            try {
                startLatch.await(); // 시작 신호 대기
                mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetailsA)))
                        .andExpect(status().isCreated())
                        .andDo(print());
            } catch (Exception e) {
                e.printStackTrace();
                failCount.incrementAndGet(); // 실패 시 카운터 증가
            } finally {
                endLatch.countDown(); // 작업 종료 알림
            }
        };

        Runnable task2 = () -> {
            try {
                startLatch.await(); // 시작 신호 대기
                mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(SecurityMockMvcRequestPostProcessors.user(userDetailsB)))
                        .andExpect(status().isCreated())
                        .andDo(print());
            } catch (Exception e) {
                e.printStackTrace();
                failCount.incrementAndGet(); // 실패 시 카운터 증가
            } finally {
                endLatch.countDown(); // 작업 종료 알림
            }
        };

        // 두 개의 스레드를 실행
        executorService.submit(task1);
        executorService.submit(task2);

        startLatch.countDown(); // 두 스레드 동시에 시작
        endLatch.await(); // 두 작업 종료까지 대기

        // Then
        // 한 개의 주문만 성공했는지 확인
        SaleItem saleItem = saleItemRepository.findAll().get(0);

        assertEquals(1, failCount.get()); // 실패한 주문은 하나여야 함
        assertNotNull(saleItem.getBuyer()); // 성공한 구매자 존재 확인


        // 삭제 전 잠시 대기
        Thread.sleep(2000);

        // ExecutorService 종료
        executorService.shutdown();
    }


    private User getUser(String name) {
        SignupRequest req = getSignupRequest(name);

        Long userId = userService.signup(req);

        return userService.findById(userId);
    }

    private static SignupRequest getSignupRequest(String name) {
        return SignupRequest.builder()
                .email(name + "@gmail.com")
                .password("1234")
                .nickname(name)
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

    private IdRequest getRequest(Long itemId) {
        return IdRequest.builder()
                .id(itemId)
                .build();
    }

}