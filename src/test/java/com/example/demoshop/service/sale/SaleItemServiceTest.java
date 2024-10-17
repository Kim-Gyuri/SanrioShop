package com.example.demoshop.service.sale;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.transaction.SaleItem;

import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.exception.item.ItemNotFoundException;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.sale.SaleItemRepository;
import com.example.demoshop.repository.users.UserRepository;

import com.example.demoshop.service.item.ItemService;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SaleItemServiceTest {


    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    SaleItemService saleItemService;

    @Autowired
    SaleItemRepository saleItemRepository;



    @Test
    @DisplayName("동시 거래 요청 - 성공 케이스")
    void concurrentContactTrade_success_V2() throws IOException, InterruptedException {

        // given
        User uploader = getUserDto("bdd6");

        Long id = uploadItem(uploader);
        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        // 두 명의 구매자 설정
        User buyer1 = getUserDto("cocoa6");
        User buyer2 = getUserDto("butter6");

        AtomicInteger failCount = new AtomicInteger(0); // 실패한 주문의 개수를 셀 카운터
        ExecutorService executorService = Executors.newFixedThreadPool(2); // 두 개의 스레드를 사용할 스레드풀

        // 시작 래치와 종료 래치
        var startLatch = new CountDownLatch(1);
        var endLatch = new CountDownLatch(2);


        // 구매 작업을 수행하는 Runnable 생성
        Runnable task1 = () -> {
            try {
                startLatch.await(); // 모든 스레드가 시작 지점을 기다림
                saleItemService.contactTrade(buyer1.getEmail(), item.getId());// 첫 번째 구매자가 주문 시도
            } catch (Exception e) {
                failCount.incrementAndGet(); // 실패 시 카운터 증가
            } finally {
                endLatch.countDown(); // 작업이 끝났음을 알림
            }
        };

        Runnable task2 = () -> {
            try {
                startLatch.await(); // 모든 스레드가 시작 지점을 기다림
                saleItemService.contactTrade(buyer2.getEmail(), item.getId()); // 두 번째 구매자가 주문 시도
            } catch (Exception e) {
                failCount.incrementAndGet(); // 실패 시 카운터 증가
            } finally {
                endLatch.countDown(); // 작업이 끝났음을 알림
            }
        };

        // 두 개의 스레드를 실행
        executorService.submit(task1);
        executorService.submit(task2);

        startLatch.countDown(); // 모든 스레드가 동시에 시작하도록 래치를 낮춤
        endLatch.await(); // 모든 스레드의 작업이 끝날 때까지 대기

        // Then
        // 한 개의 주문만 성공했는지 확인
        assertEquals(1, failCount.get()); // 실패한 주문은 하나여야 함

        // 삭제 전 잠시 대기
        Thread.sleep(1000);

        SaleItem saleItem = saleItemRepository.findByItem(item).orElseThrow(ItemNotFoundException::new);
        saleItemRepository.delete(saleItem);
        userRepository.delete(buyer1);
        userRepository.delete(buyer2);
        itemRepository.delete(item);
        userRepository.delete(uploader);


        // ExecutorService 종료
        executorService.shutdown();
    }



    private Long uploadItem(User user) throws IOException {
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
        return itemId;
    }

    private User getUserDto(String name) {
        User user = User.builder()
                .email(name + "@gmail.com")
                .password("12341234")
                .nickname(name)
                .build();

        return userRepository.save(user);
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