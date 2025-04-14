package com.example.demoshop.repository.item;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.service.item.ItemService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Random;

import static com.example.demoshop.domain.item.common.SanrioCharacters.CINNAMOROLL;


@Slf4j
@SpringBootTest
@Transactional
class ItemRepositoryTest {


    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Test
    void print() {
        List<Item> items = itemRepository.searchByItemName("1시");

        for (Item item : items) {
            log.info("item name ={}", item.getNameKor());
        }

        log.info("산리오 & 상품명");
        List<Item> items2 = itemRepository.searchByKeywordAndCharacter("1시", CINNAMOROLL);
        for (Item item : items2) {
            log.info("item name ={}", item.getNameKor());
        }
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

        getFile(multipartFileList);

        return multipartFileList;
    }

    private static void getFile(List<MultipartFile> multipartFileList) {
        String path = "C:/Users/thumper/Pictures/Screenshots/";
        String imageName1 = "pochaco.png";
        String imageName2 = "pompom.png";
        String imageName3 = "mymelody.png";

        MockMultipartFile multipartFile =
                new MockMultipartFile(path, imageName1, "image/png", new byte[]{1,2,3,4});
                new MockMultipartFile(path, imageName2, "image/png", new byte[]{1,2,3,4});
                new MockMultipartFile(path, imageName3, "image/png", new byte[]{1,2,3,4});
        multipartFileList.add(multipartFile);
    }
}