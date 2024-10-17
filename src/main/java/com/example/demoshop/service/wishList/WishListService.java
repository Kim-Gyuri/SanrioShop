package com.example.demoshop.service.wishList;

import com.example.demoshop.domain.wishList.WishItem;
import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.domain.wishList.WishList;
import com.example.demoshop.exception.wishList.DuplicateWishItemException;
import com.example.demoshop.exception.wishList.WishItemNotFoundException;
import com.example.demoshop.exception.item.ItemNotFoundException;
import com.example.demoshop.exception.users.UserNotFoundException;
import com.example.demoshop.repository.wishList.WishItemRepository;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.response.item.WishlistItemDto;

import com.example.demoshop.response.wishList.WishItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class WishListService {

    private final UserRepository userRepository;
    private final WishItemRepository wishItemRepository;
    private final ItemRepository itemRepository;

    public WishItemResponse markAsWished(User user, Long itemId) {
        User loginUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        return mark(itemId, loginUser);
    }


    public WishItemResponse unmarkAsWished(User user, Long itemId) {

        User loginUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        return unmark(loginUser, itemId);
    }

    /**
     * 찜목록 조회
     */
    @Transactional(readOnly = true)
    public Page<WishlistItemDto> findWishList(String buyerEmail, Pageable pageable) {
        return itemRepository.findWishListByUser(pageable, buyerEmail);
    }

    private WishItemResponse mark(Long itemId, User user) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        if (user.checkWishItemDuplicate(item)) {
            throw new DuplicateWishItemException("이미 찜한 상품입니다.");
        }

        WishItem wishItem = wishItemRepository.save(new WishItem(user.getWishList(), item));

        user.markAsWished(wishItem);
        item.increaseLikeCount();

        return new WishItemResponse(item.getLikeCount(), wishItem.getId());
    }

    private WishItemResponse unmark(User user, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        WishList wishList = user.getWishList();

        WishItem wishItem = wishItemRepository.findByWishListAndItem(wishList, item)
                .orElseThrow(() -> new WishItemNotFoundException("찜한 상품이 아닙니다."));

        user.unmarkAsWished(wishItem);

        item.decreaseLikeCount();

        // 연관된 데이터 처리 필요 시 추가 로직 삽입
        wishItemRepository.delete(wishItem);

        return new WishItemResponse(item.getLikeCount());
    }

}
