package com.example.demoshop.service.item;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.ItemImg;
import com.example.demoshop.domain.item.common.IsMainImg;
import com.example.demoshop.exception.item.MinimumImageRequiredException;
import com.example.demoshop.request.item.CreateImgRequest;
import com.example.demoshop.exception.item.ItemImgNotFoundException;
import com.example.demoshop.repository.item.ItemImgRepository;
import com.example.demoshop.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    private final FileService fileService;
    private final ItemImgRepository imgRepository;


    // 이미지 삭제
    public void deleteImg(String imgUrl) {
        ItemImg itemImg = imgRepository.findByImgUrl(imgUrl).orElseThrow(ItemImgNotFoundException::new);

        Item item = itemImg.getItem();

        // 상품에는 이미지가 최소 1개는 있어야 한다.
        validateItemImg(item);

        // 부모 엔티티 컬렉션에서 해당 이미지 제거
        item.removeImg(itemImg);

        // 삭제하려는 이미지가 '대표 이미지'인 경우 새로운 대표 이미지를 설정해야 합니다.
        if (itemImg.getIsMainImg() == IsMainImg.Y) {
            // 새로운 대표 이미지 설정
            item.updateMainImg();
        }

        imgRepository.delete(itemImg);
    }

    // 상품 등록 - 이미지 업로드
    public ItemImg saveImg(CreateImgRequest requestDto, MultipartFile multipartFile) throws IOException {
        String imgUrl = fileService.storeFile(multipartFile);

        return saveItemImg(requestDto.getYN(), imgUrl);
    }

    // 상품 수정 - 추가 이미지 업로드
    public ItemImg saveExtraImg(MultipartFile multipartFile) throws IOException {
        String imgUrl = fileService.storeFile(multipartFile);

        return saveItemImg(IsMainImg.N, imgUrl);
    }

    // 상품 이미지 저장
    private ItemImg saveItemImg(IsMainImg isMainImg, String imgUrl) {
        ItemImg imgEntity = ItemImg.builder()
                .imgUrl(imgUrl)
                .isMainImg(isMainImg)
                .build();

        return imgRepository.save(imgEntity);
    }

    // 상품 업데이트 -> 상품에 대한 이미지가 하나도 없다면, 오류 발생!
    private static void validateItemImg(Item item) {
        if (item.getItemImgList().isEmpty()) {
            throw new MinimumImageRequiredException();
        }
    }
}
