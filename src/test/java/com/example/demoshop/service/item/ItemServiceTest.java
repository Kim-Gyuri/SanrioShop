package com.example.demoshop.service.item;

import com.example.demoshop.controller.dto.CategoryCondition;
import com.example.demoshop.controller.dto.SearchCondition;
import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.exception.item.ItemNotFoundException;
import com.example.demoshop.repository.sale.SaleItemRepository;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.request.item.UpdateItemRequest;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.response.item.ThumbnailItemDto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    SaleItemRepository saleItemRepository;



    @AfterEach
    void cleanAfter() {
        saleItemRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }




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

    }


    @Test
    @DisplayName("상품명으로 검색했을 때 - 성능 테스트")
    void searchByName() throws IOException {
        // given
        User user = getUploaderDto();
        dummyItemData(user);

        String targetName = "포차코 복조리";
        Pageable pageable = PageRequest.of(0, 20);
        SearchCondition condition = new SearchCondition();
        condition.setKeyword(targetName);

        log.info("더미 데이터 총 상품 개수: {}", itemRepository.count());

        long startTime = System.currentTimeMillis();
        Page<ThumbnailItemDto> result = itemService.search_fetch_mainPage(pageable, condition, user.getEmail());
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        log.info("상품명으로 검색했을 때 걸린 시간: {}", duration);

        // 검색된 총 아이템 수와 페이지 수를 확인
        log.info("검색된 총 아이템 수: {}", result.getTotalElements());
        log.info("검색된 페이지 수: {}", result.getTotalPages());

        List<ThumbnailItemDto> content = result.getContent();

        for (int i = 0; i < content.size(); i++) {
            ThumbnailItemDto thumbnailItemDto = content.get(i);
            log.info("검색결과 {}번 = {}", i + 1, thumbnailItemDto.getNameKor());
        }

    }

    @Test
    @DisplayName("테마으로 검색했을 때 - 성능 테스트")
    void search_with_tag() throws IOException {
        // given
        User user = getUploaderDto();
        dummyItemData(user);

        Pageable pageable = PageRequest.of(0, 10);
        CategoryCondition condition = new CategoryCondition();
        condition.setSanrioCharacters(SanrioCharacters.POCHACCO);
        condition.setMainCategory(MainCategory.ACCESSORIES);
        condition.setSubCategory(SubCategory.POUCH_CASE);
        condition.setTag("복조리");

        long startTime = System.currentTimeMillis();
        Page<ThumbnailItemDto> result = itemService.search_fetch_category(pageable, condition, user.getEmail());
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        log.info("테마로 검색했을 때 걸린 시간: {}", duration);

        // 검색된 총 아이템 수와 페이지 수를 확인
        log.info("검색된 총 아이템 수: {}", result.getTotalElements());
        log.info("검색된 페이지 수: {}", result.getTotalPages());

        List<ThumbnailItemDto> content = result.getContent();

        for (int i = 0; i < content.size(); i++) {
            ThumbnailItemDto thumbnailItemDto = content.get(i);
            log.info("검색결과 {}번 = {}", i + 1, thumbnailItemDto.getNameKor());
        }
    }



    private void dummyItemData(User user) throws IOException {
        createItemDummy(user, "포차코 가챠 후와후와 페이스 파우치", List.of("가챠", "페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 펜 파우치", List.of("페이스파우치", "산리오", "포차코", "필통","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 투명 아크릴 스탠드 파우치", List.of("투명파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.CLEAR_TYPE_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 티타임 파우치", List.of("가챠","페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.DOLL_BAG),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 갸루 콜렉션 복조리 파우치", List.of("가챠","페이스파우치", "갸루", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 글리터 파우치", List.of("글리터", "글리터가방", "가챠","페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.CLEAR_TYPE_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 이타백 투명 가챠 파우치", List.of("이타백", "투명가방", "클리어", "가챠", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.CLEAR_TYPE_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 클리어 멀티 케이스", List.of( "산리오", "포차코", "멀티케이스", "미니가방","여자아이용", "귀여운파우치"),
                   List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE),
                   SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 클리어 보스턴백 모양 파우치", List.of("보스턴백 스타일", "클리어", "페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                   List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.CLEAR_TYPE_CASE),
                   SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 스쿨백 펜 파우치", List.of("필통", "스쿨백", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                   List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE),
                   SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 플랫 파우치", List.of("플랫백","납작한", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                    List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.FLAT_CASE),
                    SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 플랫 실리콘 파우치", List.of("실리콘","플랫파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                    List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.FLAT_CASE),
                    SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 파우치모양 키링", List.of("파우치스타일", "키링", "페이스파우치", "산리오", "챰", "여자아이용", "귀여운파우치"),
                    List.of(TagOption.FACE_SHAPE_BAG),
                    SanrioCharacters.POCHACCO, MainCategory.POPULAR_SEARCH, SubCategory.KEYRING_CHARM);

        createItemDummy(user, "포차코 구름모양 파우치", List.of("가챠","구름", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                    List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.THREE_D_CASE),
                    SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 스쿨백모양 키링", List.of("가챠","스쿨백모양", "챰", "키링", "귀여운 키링", "산리오", "포차코", "미니가방","여자아이용"),
                List.of(TagOption.FLAT_CASE),
                SanrioCharacters.POCHACCO, MainCategory.POPULAR_SEARCH, SubCategory.KEYRING_CHARM);

        createItemDummy(user, "포차코 구름가방 키링", List.of("가챠","구름모양", "챰", "키링", "귀여운 키링", "산리오", "포차코", "미니가방","여자아이용"),
                List.of(TagOption.FLAT_CASE, TagOption.CLEAR_TYPE_CASE),
                SanrioCharacters.POCHACCO, MainCategory.POPULAR_SEARCH, SubCategory.KEYRING_CHARM);

        createItemDummy(user, "포차코 복조리 스티커", List.of("가챠","페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.CLEAR_TYPE_CASE, TagOption.CORRECTION_TAPE),
                SanrioCharacters.POCHACCO, MainCategory.JOURNAL_STATIONERY, SubCategory.STICKER);

        createItemDummy(user, "포차코 크리스마스 복조리 스티커", List.of("가챠","페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.CLEAR_TYPE_CASE, TagOption.CORRECTION_TAPE),
                SanrioCharacters.POCHACCO, MainCategory.JOURNAL_STATIONERY, SubCategory.STICKER);

        createItemDummy(user, "포차코 솜사탕 구름모양 파우치", List.of("가챠","구름", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
            List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE, TagOption.THREE_D_CASE),
            SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 수영가방", List.of("여름가방","수영", "비치가방", "페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.ZIPPER_BAG_CASE, TagOption.MESH_TYPE_BAG),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.BAG);

        createItemDummy(user, "포차코 하트모양 파우치", List.of("하트모양","페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 물놀이백", List.of("여름가방","수영", "비치가방", "페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.ZIPPER_BAG_CASE, TagOption.MESH_TYPE_BAG),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.BAG);

        createItemDummy(user, "포차코 인형케리어", List.of("인형케리어", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.PLASTIC_STORAGE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.STORAGE_ORGANIZATION);

        createItemDummy(user, "포차코 파우치", List.of("가챠","페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 매쉬가방", List.of("매쉬가방", "매쉬타입", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.ZIPPER_BAG_CASE, TagOption.MESH_TYPE_BAG),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.BAG);

        createItemDummy(user, "포차코 대형 파우치", List.of("산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.MESH_TYPE_BAG),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 휴대폰 클리어 숄더백", List.of("클리어", "휴대폰가방", "산리오", "포차코", "미니가방", "여자아이용", "귀여운케이스"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.MINI_SHOULDER_BAG, TagOption.CLEAR_TYPE_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 가방챰 키링", List.of("가챠", "파우치 키링", "챰", "키링", "산리오", "포차코", "미니가방", "여자아이용", "귀여운파우치"),
                List.of(TagOption.ACRYLIC_TYPE, TagOption.MIRROR_KEYRING),
                SanrioCharacters.POCHACCO, MainCategory.POPULAR_SEARCH, SubCategory.KEYRING_CHARM);

        createItemDummy(user, "포차코 실내화가방", List.of("실내화 가방", "산리오", "포차코", "미니가방","여자아이용", "귀여운 가방"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.BAG);

        createItemDummy(user, "포차코 포켓 A5 클리어파일", List.of("산리오", "포차코", "클리어파일", "포켓주머니"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.CLEAR_TYPE_CASE, TagOption.A5_SIZE),
                SanrioCharacters.POCHACCO, MainCategory.JOURNAL_STATIONERY, SubCategory.FILE_BINDER);

        createItemDummy(user, "포차코 포켓 A4 L홀더", List.of("산리오", "포차코", "클리어파일", "포켓주머니"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.CLEAR_TYPE_CASE, TagOption.A4_SIZE, TagOption.A4_FILE_L_HOLDER),
                SanrioCharacters.POCHACCO, MainCategory.JOURNAL_STATIONERY, SubCategory.FILE_BINDER);

        createItemDummy(user, "포차코 포켓 파우치", List.of("가챠", "복조리 파우치", "산리오", "포차코", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 포켓 스토리 시리즈 백팩 모양 미니 파우치", List.of("가챠","페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.FACE_SHAPE_BAG, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.POUCH_CASE);

        createItemDummy(user, "포차코 포켓이 있는 B6 링 노트", List.of("링노트", "노트", "산리오", "포차코", "B6", "포켓 노트"),
                List.of(TagOption.B6_SIZE, TagOption.RING_TYPE_DIARY),
                SanrioCharacters.POCHACCO, MainCategory.JOURNAL_STATIONERY, SubCategory.NOTE);

        createItemDummy(user, "포차코 쇼핑백모양 지퍼백 세트", List.of("쇼핑백모양", "산리오", "포차코", "지퍼백"),
                List.of(TagOption.ZIPPER_FILE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.STORAGE_ORGANIZATION);

        createItemDummy(user, "포차코 에코백", List.of("에코백", "산리오", "포차코", "미니가방","여자아이용", "귀여운 가방"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.SEWING_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.BAG);

        createItemDummy(user, "포차코 에코백이 들어있는 마스코트 홀더 키링", List.of("가챠", "키링", "챰", "산리오", "포차코"),
                List.of(TagOption.FLAT_CASE),
                SanrioCharacters.POCHACCO, MainCategory.POPULAR_SEARCH, SubCategory.KEYRING_CHARM);

        createItemDummy(user, "포차코 휴대폰 숄더백", List.of("휴대폰 가방", "숄더백", "산리오", "포차코", "미니가방", "귀여운가방"),
                List.of(TagOption.PORTABLE_MIRROR, TagOption.SHOULDER_BAG, TagOption.CLEAR_TYPE_BAG),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.BAG);

        createItemDummy(user, "포차코 휴대폰 클리어숄더백", List.of("가챠","페이스파우치", "산리오", "포차코", "미니가방","여자아이용", "귀여운파우치"),
                List.of(TagOption.SHOULDER_BAG, TagOption.CLEAR_TYPE_CASE),
                SanrioCharacters.POCHACCO, MainCategory.ACCESSORIES, SubCategory.BAG);
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

    private void createItemDummy(User user,String nameKor, List<String> tags, List<TagOption> tagOptions, SanrioCharacters sanrio, MainCategory main, SubCategory sub) throws IOException {
        CreateItemRequest request = CreateItemRequest.builder()
                .nameKor(nameKor)
                .price(18000)
                .description("상품 설명입니다.")
                .sanrioCharacters(sanrio)
                .mainCategory(main)
                .subCategory(sub)
                .build();

        request.setUserDefinedTagNames(tags);
        request.setRecommendedTagOptions(tagOptions);

        itemService.createItem(user, request, generateMultipartFileList());
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