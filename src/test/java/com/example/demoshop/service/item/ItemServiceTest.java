package com.example.demoshop.service.item;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.exception.item.ItemNotFoundException;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.request.item.UpdateItemRequest;
import com.example.demoshop.repository.item.ItemImgRepository;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.users.UserRepository;
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
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;




    @Test
    @DisplayName("상품 등록 - 성공케이스")
    void create_item_success() throws IOException {
        // given
        User user = getUploaderDto();

        // when
        Long itemId = createItem(user);

        // then
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        assertEquals(SanrioCharacters.HANGYODON, item.getSanrioCharacters());
        assertEquals(4, item.getUserDefinedTagList().size());

        // clean
        itemRepository.delete(item);
        userRepository.delete(user);
    }




    @Test
    @DisplayName("상품 수정 - 성공테스트")
    void updateItem_success() throws IOException {
        // given
        User user = getUploaderDto();

        Long itemId = createItem(user);

        // when
        updateItem(itemId);

        // then
        Item findItem = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        assertEquals(10000, findItem.getPrice());
        assertEquals(5, findItem.getUserDefinedTagList().size());


        // clean
        itemRepository.deleteById(itemId);
        userRepository.delete(user);

    }


    @Test
    @DisplayName("삭제")
    void deleteItem() throws IOException {
        // given
        User user = getUploaderDto();

        Long id = createItem(user);

        // when
        itemService.deleteItem(id);

        // then
        assertThrows(ItemNotFoundException.class, () -> {
            itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        });

        // clean
        userRepository.delete(user);
    }


    private void updateItem(Long itemId) throws IOException {
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
        List<MultipartFile> multipartFileList = new ArrayList<>(); // 추가 이미지가 없는 경우


        itemService.updateItem(itemId, updateItemRequest, multipartFileList);
    }

    private Long createItem(User user) throws IOException {
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

        Long id = itemService.createItem(user, createItemRequest, generateMultipartFileList());
        return id;
    }

    private User getUploaderDto() {
        User userDto = User.builder()
                .id(1L)
                .email("none1234567@gmail.com")
                .password("1234")
                .nickname("none")
                .build();

        User user = userRepository.save(userDto);
        return user;
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