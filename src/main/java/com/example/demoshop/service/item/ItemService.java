package com.example.demoshop.service.item;

import com.example.demoshop.controller.dto.CategoryCondition;
import com.example.demoshop.controller.dto.SearchCondition;
import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.ItemImg;
import com.example.demoshop.domain.item.RecommendedTag;
import com.example.demoshop.domain.item.UserDefinedTag;
import com.example.demoshop.domain.item.common.IsMainImg;
import com.example.demoshop.domain.item.common.TagOption;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.domain.wishList.WishItem;
import com.example.demoshop.domain.wishList.WishList;
import com.example.demoshop.exception.item.ItemDeletionNotAllowedException;
import com.example.demoshop.exception.item.MinimumImageRequiredException;
import com.example.demoshop.repository.sale.NotificationRepository;
import com.example.demoshop.repository.sale.SaleItemRepository;
import com.example.demoshop.repository.wishList.WishItemRepository;
import com.example.demoshop.request.item.CreateImgRequest;
import com.example.demoshop.request.item.CreateItemRequest;
import com.example.demoshop.request.item.UpdateItemRequest;
import com.example.demoshop.exception.item.ItemNotFoundException;
import com.example.demoshop.exception.users.UserNotFoundException;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.item.RecommendedTagRepository;
import com.example.demoshop.repository.item.UserDefinedTagRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.request.sale.CreateNotificationRequest;
import com.example.demoshop.response.item.*;
import com.example.demoshop.response.sale.UserNotificationDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demoshop.utils.constants.ImgConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserDefinedTagRepository userDefinedTagRepository;
    private final RecommendedTagRepository recommendedTagRepository;
    private final SaleItemRepository saleItemRepository;
    private final ItemImgService imgService;
    private final WishItemRepository wishItemRepository;
    private final NotificationRepository notificationRepository;


    /**
     * 상품 등록
     */
    public Long createItem(User user, CreateItemRequest requestDto, List<MultipartFile> imgFileList) throws IOException {
        User uploader = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        Item savedItem = itemRepository.save(requestDto.toEntity(uploader));

        createItemTag(requestDto.getUserDefinedTagNames(), savedItem, requestDto.getRecommendedTagOptions());

        validateImgFiles(imgFileList); // 필수적으로 이미지 1개는 필수

        // 이미지 업로드
        uploadImg(imgFileList, savedItem);

        return savedItem.getId();
    }


    /**
     * 상품 수정
     */
    public void updateItem(Long id, UpdateItemRequest requestDto, List<MultipartFile> multipartFiles) throws IOException {

        Item savedItem = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        // 상품 상세 정보 수정
        if (requestDto != null) {
            savedItem.update(requestDto);
        }

        // 태그 정보 추가
        createItemTag(requestDto.getUserDefinedTagNames(), savedItem, requestDto.getRecommendedTagOptions());

        // 이미지 파일이 존재한다면, 이미지 새로 추가
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            uploadImg_extra(multipartFiles, savedItem);
        }
    }

    /**
     * 상품 수정 > 이미지 추가로 업로드
     */
    private void uploadImg_extra(List<MultipartFile> imgFileList, Item savedItem) throws IOException {

        for (MultipartFile file : imgFileList) {
            ItemImg itemImg = imgService.saveExtraImg(file);
            savedItem.uploadImg(itemImg);
        }
    }


    /**
     * 상품 삭제
     */
    public void deleteItem(Long id) {
        
        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        // 규칙: 주문 요청을 받은 상품은 삭제할 수 없다.
        if (isOrdered(item)) {
            throw new ItemDeletionNotAllowedException();
        }

        // 삭제 알림 생성
        List<UserNotificationDto> users = itemRepository.findUsersByItemId(id);
        for (UserNotificationDto user : users) {
            CreateNotificationRequest request = createOutOfStockNotificationRequest(item, user);
            notificationRepository.save(request.toEntity());
        }

        // 찜 목록에서 상품 삭제
        List<WishItem> wishItemList = wishItemRepository.findAllByItem(item);
        for (WishItem wishItem : wishItemList) {
            removeWishItem(wishItem);
        }

        // 상품 삭제
        itemRepository.delete(item);
    }

    private boolean isOrdered(Item item) {
        return saleItemRepository.existsByItem(item);
    }

    // 찜 목록에서 삭제하기
    private void removeWishItem(WishItem wishItem) {
        WishList wishList = wishItem.getWishList();
        wishList.removeWishThing(wishItem);
    }

    private CreateNotificationRequest createOutOfStockNotificationRequest(Item item, UserNotificationDto user) {
        return CreateNotificationRequest.builder()
                .itemImg(item.getThumbnail())
                .itemName(item.getNameKor())
                .buyerNick(user.getNickname())
                .message(notifyOutOfStock(item.getNameKor()))
                .userEmail(user.getEmail())
                .build();
    }

    // 찜 등록한 유저가 받는 것> 품절 안내문
    private String notifyOutOfStock(String itemName) {
        return itemName + "이 품절되어 찜목록에서 삭제되었습니다.";
    }


    /**
     * 홈 > 카테고리 메뉴 선택했을 때, 상품 정렬
     */
    @Transactional(readOnly = true)
    public Page<ThumbnailItemDto> search_fetch_category(Pageable pageable, CategoryCondition condition, String userEmail) {

        if (hasTagSearchCondition(condition.getTag())) {
            return itemRepository.searchByCategory_tag(pageable, condition, userEmail);
        }

        return itemRepository.searchByCategory(pageable, condition, userEmail);
    }

    /**
     * 홈 > 메인 페이지 - 검색
     */
    @Transactional(readOnly = true)
    public Page<ThumbnailItemDto> search_fetch_mainPage(Pageable pageable, SearchCondition condition, String userEmail) {

        if (hasTagSearchCondition(condition.getTag())) {
            return itemRepository.searchMainPageItems_tag(pageable, condition, userEmail);
        }

        // 조건이 비어있는 경우
        return itemRepository.searchMainPageItems_name(pageable, condition, userEmail);
    }

    private static boolean hasTagSearchCondition(String tag) {
        return !(tag == null || tag.isEmpty());
    }

    /**
     * 홈 > 상품 선택했을 때, 상품 상세 페이지
     */
    @Transactional(readOnly = true)
    public ProductDto findProduct(User user, Long itemId) {
        userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        boolean isWished = itemRepository.isExistInUserWishList(itemId, user.getEmail());

        boolean isOrderedByUser = saleItemRepository.existsByItem(item);

        return item.toProductDto(isWished, isOrderedByUser);
    }

    /**
     * 상품 수정 페이지> 상품 정보 불러오기
     */
    @Transactional(readOnly = true)
    public ItemDetailDto findItemDetail(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        return item.toItemDetailDto();
    }



    // 상품 등록 > 이미지 업로드
    private void uploadImg(List<MultipartFile> imgFileList, Item savedItem) throws IOException {
        for (int i = 0; i < imgFileList.size(); i++) {
            CreateImgRequest imgRequest = new CreateImgRequest();

            // 첫 번째 이미지를 메인 이미지로 설정
            imgRequest.setYN(i == THUMBNAIL_INDEX ? IsMainImg.Y : IsMainImg.N);

            // 이미지를 저장하고 게시물에 업로드
            ItemImg itemImage = imgService.saveImg(imgRequest, imgFileList.get(i));
            savedItem.uploadImg(itemImage);
        }
    }

    // 최소 상품 이미지 1개 이상 업로드 했는지 체크한다.
    private static void validateImgFiles(List<MultipartFile> multipartFileList) {
        if (multipartFileList == null || multipartFileList.isEmpty()) {
            throw new MinimumImageRequiredException();
        }
    }


    /**
     * 상품 정보 > 태그 추가
     */
    private void createItemTag(List<String> userDefinedTagNames, Item savedItem, List<TagOption> recommendedTagOptions) {
        if (userDefinedTagNames != null && !userDefinedTagNames.isEmpty()) {
            saveUserDefinedTags(userDefinedTagNames, savedItem);
        }

        if (recommendedTagOptions != null && !recommendedTagOptions.isEmpty()) {
            saveRecommendedTags(recommendedTagOptions, savedItem);
        }
    }



    private void saveUserDefinedTags(List<String> tagNames, Item savedItem) {
        List<UserDefinedTag> tags = tagNames.stream()
                .map(name -> UserDefinedTag.builder()
                        .name(name)
                        .tagFrequency(countByName(name))
                        .build())
                .map(userDefinedTagRepository::save)
                .collect(Collectors.toList());

        tags.forEach(savedItem::addUserDefinedTag);
    }

    private void saveRecommendedTags(List<TagOption> tagOptions, Item savedItem) {
        List<RecommendedTag> tags = tagOptions.stream()
                .map(option -> RecommendedTag.builder()
                        .tagOption(option)
                        .tagFrequency(countByTagOption(option))
                        .build())
                .map(recommendedTagRepository::save)
                .collect(Collectors.toList());

        tags.forEach(savedItem::addRecommendedTag);
    }

    // 해당 태그를 얼마나 많이 사용되었는지 빈도수 체크 -> 나중에 기능 구현에 활용해볼 예정.
    private Long countByName(String tagName) {
        long currentFrequency = userDefinedTagRepository.countByName(tagName);
        ++currentFrequency;
        
        updateUserDefinedTagsFrequency(tagName, currentFrequency);
        
        return currentFrequency;
    }

    private long countByTagOption(TagOption tagOption) {
        long currentFrequency = recommendedTagRepository.countByTagOption(tagOption);
        ++currentFrequency; // currentFrequency 값을 1 증가

        updateRecommendedTagsFrequency(tagOption, currentFrequency); // 업데이트된 빈도수로 RecommendedTags 업데이트

        return currentFrequency; // 증가된 빈도수 반환
    }

    private void updateRecommendedTagsFrequency(TagOption tagOption, long updatedFreq) {
        List<RecommendedTag> recommendedTags = recommendedTagRepository.findByTagOption(tagOption);

        recommendedTags.stream()
                .forEach(recommendedTag -> recommendedTag.updateTagFrequency(updatedFreq));
    }

    
    private void updateUserDefinedTagsFrequency(String tagName, long updatedFreq) {
        List<UserDefinedTag> userDefinedTags = userDefinedTagRepository.findByName(tagName);
        
        userDefinedTags.stream()
                .forEach(userDefinedTag -> userDefinedTag.updateTagFrequency(updatedFreq));
    }

}
