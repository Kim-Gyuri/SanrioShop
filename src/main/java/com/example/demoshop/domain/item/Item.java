package com.example.demoshop.domain.item;

import com.example.demoshop.domain.item.common.IsMainImg;
import com.example.demoshop.domain.item.common.MainCategory;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import com.example.demoshop.domain.item.common.SubCategory;
import com.example.demoshop.domain.transaction.SaleItem;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.request.item.UpdateItemRequest;
import com.example.demoshop.response.item.ItemDetailDto;
import com.example.demoshop.response.item.ProductDto;
import com.example.demoshop.response.item.TagDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demoshop.utils.constants.ImgConstants.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

/**
 * 거래되는 상품 정보
 */
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Item {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String nameKor; // 이름-한국어

    private LocalDateTime createAt; //  업로드 날짜

    private int price; // 가격

    private String description;  //상품 설명

    @Column(name = "like_count")
    private int likeCount; // favorites 지목된 횟수

    @Enumerated(EnumType.STRING)
    private SanrioCharacters sanrioCharacters; // 캐릭터 구분

    @Enumerated(EnumType.STRING)
    private MainCategory mainCategory; // 상위 카테고리

    @Enumerated(EnumType.STRING)
    private SubCategory subCategory; // 하위 카테고리

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<ItemImg> itemImgList = new ArrayList<>(); // 상품 이미지 (다중)


    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<UserDefinedTag> userDefinedTagList = new HashSet<>(); // 검색 태그 (다중)

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<RecommendedTag> recommendedTagList = new HashSet<>(); // 서버가 추천해준 태그 (다중)

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @Builder
    public Item(User uploader, String nameKor, int price, String description,
                SanrioCharacters sanrioCharacters, MainCategory mainCategory, SubCategory subCategory) {
        this.uploader = uploader;
        this.nameKor = nameKor;
        this.price = price;
        this.description = description;
        this.sanrioCharacters = sanrioCharacters;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.createAt = LocalDateTime.now();
        this.likeCount = 0;
    }


    /**
     *  연관관계 메소드
     */
    // 이미지 업로드
    public void uploadImg(ItemImg itemImg) {
        this.itemImgList.add(itemImg);
        itemImg.assignToItem(this);
    }

    // 이미지 삭제
    public void removeImg(ItemImg itemImg) {
        this.itemImgList.remove(itemImg);
    }

    // 찜 등록 요청을 받았을 때
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 찜 삭제 요청을 받았을 때
    public void decreaseLikeCount() {
        this.likeCount--;
    }


    // 상품 정보 업데이트
    public void update(UpdateItemRequest request) {
        this.nameKor = request.getNameKor();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.sanrioCharacters = request.getSanrioCharacters();
        this.mainCategory = request.getMainCategory();
        this.subCategory = request.getSubCategory();
    }

    // 유저가 등록한 태그 추가
    public void addUserDefinedTag(UserDefinedTag userDefinedTag) {
        this.userDefinedTagList.add(userDefinedTag);
        userDefinedTag.assignToItem(this);
    }

    // 추천 받은 태그를 추가
    public void addRecommendedTag(RecommendedTag recommendedTag) {
        this.recommendedTagList.add(recommendedTag);
        recommendedTag.assignToItem(this);
    }

    //  태그 삭제
    public void removeUserDefinedTag(UserDefinedTag userDefinedTag) {
        this.userDefinedTagList.remove(userDefinedTag);
    }

    public void removeRecommendedTag(RecommendedTag recommendedTag) {
        this.recommendedTagList.remove(recommendedTag);
    }

    // 판매자 정보 확인 > saleItem(판매상품 정보)를 만들 때 연관관계 매핑에 필요하다.
    public User checkSeller() {
        return this.uploader;
    }


    // 상품 상세 페이지 > 상품 상세 정보 조회
    public ProductDto toProductDto(boolean isWished, boolean isOrdered) {
        return ProductDto.builder()
                .id(this.id)
                .nameKor(this.nameKor)
                .price(this.price)
                .description(this.description)
                .createAt(this.createAt)
                .likeCount(this.likeCount)
                .sanrioCharacter(this.sanrioCharacters.getNameKor())
                .mainCategory(this.mainCategory.getNameKor())
                .subCategory(this.subCategory.getNameKor())
                .thumbnail(getThumbnail())
                .imgList(getFullImgList())
                .userDefinedTags(getUserTagNames())
                .recommendedTags(getRecommendTagNames())
                .isLikedByUser(isWished)
                .isOrderedByUser(isOrdered)
                .build();
    }

    // 상품 수정 페이지를 위한 상품 정보 불러오기
    public ItemDetailDto toItemDetailDto() {
        return ItemDetailDto.builder()
                .id(this.id)
                .nameKor(this.nameKor)
                .price(this.price)
                .description(this.description)
                .sanrioCharacters(this.sanrioCharacters)
                .mainCategory(this.mainCategory)
                .subCategory(this.subCategory)
                .imgList(getFullImgList())
                .userDefinedTags(findUserTagDto())
                .recommendedTags(findRecommendedTagDto())
                .build();
    }



    // 전체 이미지 조회
    public List<String> getFullImgList() {
        return this.itemImgList.stream()
                .map(ItemImg::getImgUrl)
                .collect(Collectors.toList());
    }

    // 메인 이미지 조회
    public String getThumbnail() {
        return this.itemImgList.stream()
                .filter(itemImg -> itemImg.getIsMainImg() == IsMainImg.Y)
                .map(ItemImg::getImgUrl)
                .findFirst()
                .orElse(null); // 메인 이미지가 없으면 빈 문자열 반환
    }

    // 새로운 메인 이미지 선정
    public void updateMainImg() {
        this.itemImgList.get(FIRST_FILE).updateThumbnail();
    }

    // 하나 이상의 이미지가 존재하는지 확인
    public boolean hasRequiredImageCount() {
        return !this.getItemImgList().isEmpty();
    }


    // 상품 > 태그 정보 조회
    public List<String> getUserTagNames() {
        return this.userDefinedTagList.stream()
                .map(UserDefinedTag::getName)
                .collect(Collectors.toList());
    }

    public List<String> getRecommendTagNames() {
        return this.recommendedTagList.stream()
                .map(RecommendedTag::getTagName)
                .collect(Collectors.toList());
    }


    // 상품 수정 페이지를 위한 상품 > 태그 정보 불러오기
    private List<TagDto> findUserTagDto() {
        return this.userDefinedTagList.stream()
                .map(userDefinedTag -> new TagDto(userDefinedTag.getId(), userDefinedTag.getName()))
                .collect(Collectors.toList());
    }

    private List<TagDto> findRecommendedTagDto() {
        return this.recommendedTagList.stream()
                .map(recommendedTag -> new TagDto(recommendedTag.getId(), recommendedTag.getTagName()))
                .collect(Collectors.toList());
    }

}
