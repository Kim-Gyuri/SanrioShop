package com.example.demoshop.service.sale;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.transaction.Notification;
import com.example.demoshop.domain.transaction.SaleItem;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.exception.item.ItemNotFoundException;
import com.example.demoshop.exception.sale.SaleItemAlreadyExistsException;
import com.example.demoshop.exception.sale.SaleItemNotFoundException;
import com.example.demoshop.exception.users.UserNotFoundException;
import com.example.demoshop.repository.item.ItemRepository;
import com.example.demoshop.repository.sale.NotificationRepository;
import com.example.demoshop.repository.sale.SaleItemRepository;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.request.sale.CreateNotificationRequest;
import com.example.demoshop.response.item.CustomerOrderDto;
import com.example.demoshop.response.item.SellerItemDto;
import com.example.demoshop.response.sale.SaleItemResponse;
import com.example.demoshop.response.sale.UserNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SaleItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final SaleItemRepository saleItemRepository;
    private final NotificationRepository notificationRepository;


    /**
     판매자 조회 - 판매상품 & 주문
     */
    @Transactional(readOnly = true)
    public Page<SellerItemDto> findSale(String uploaderEmail, Pageable pageable) {
        return itemRepository.findAllBySeller(pageable, uploaderEmail);
    }

    /**
     * 판매자 조회 > 주문 상세조회
     */
    @Transactional(readOnly = true)
    public SaleItemResponse findOrderDetail(Long itemId) {

        return itemRepository.findSaleItemDetail(itemId);
    }


    /**
     * 주문 조회
     */
    @Transactional(readOnly = true)
    public Page<CustomerOrderDto> findOrderList(String buyerEmail, Pageable pageable) {
        return itemRepository.findOrderListByUser(pageable, buyerEmail);
    }

    /**
        주문요청 처리
     */
    public Long contactTrade(String buyerEmail, Long itemId) {

        // 동시 거래 경우를 고려하여 거래요청 로직을 작성.
        try {
            Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
            log.info("Found item with ID: {}", itemId);

            log.info("Attempting to contact trade for buyer: {}, itemId: {}", buyerEmail, itemId);

            User buyer = userRepository.findByEmail(buyerEmail)
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));
            log.info("Found buyer: {}", buyer.getEmail());

            // sale check
            saleItemRepository.findByItemWithLock(item).ifPresent(saleItem -> {
                throw new SaleItemAlreadyExistsException();
            });

            Long saleId = createSaleItem(buyer, item);
            log.info("Created sale item with ID: {}", saleId);

            // 주문관련 알림 메시지 생성
            createOrderNotification(buyerEmail, item, buyer);

            return saleId;
        } catch (Exception e) {
            log.error("Error during contactTrade for buyer: {}, itemId: {}", buyerEmail, itemId, e);
            throw e;
        }
    }

    private void createOrderNotification(String buyerEmail, Item item, User buyer) {
        // 구매자에게 보내는 알림 메시지
        CreateNotificationRequest buyerRequest = CreateNotificationRequest.builder()
                .itemImg(item.getThumbnail())
                .itemName(item.getNameKor())
                .buyerNick(buyer.getNickname())
                .message(requestOrder(buyer.getNickname(), item.getNameKor()))
                .userEmail(buyerEmail)
                .build();
        notificationRepository.save(buyerRequest.toEntity());


        // 판매자에게 보내는 알림 메시지
        CreateNotificationRequest sellerRequest = CreateNotificationRequest.builder()
                .itemImg(item.getThumbnail())
                .itemName(item.getNameKor())
                .buyerNick(buyer.getNickname())
                .message(receiveOrder(buyer.getNickname(), item.getNameKor()))
                .userEmail(item.checkSeller().getEmail())
                .build();
        notificationRepository.save(sellerRequest.toEntity());
    }


    /**
     * 알림 메시지 ->
     */
    // 판매자 받는 것> 주문을 받다.
    private String receiveOrder(String buyerNick, String itemName) {
        return buyerNick +"님이 " + itemName + " 상품을 주문하였습니다.";
    }

    // 주문자가 받는 것> 주문요청을 보냈습니다.
    private String requestOrder(String buyerNick, String itemName) {
        return buyerNick + "님, " + itemName + "의 주문이 완료되었습니다. 감사합니다!";
    }

    /**
     * 알림 조회
     */
    @Transactional(readOnly = true)
    public List<Notification> findMessage(String userEmail) {
        return notificationRepository.findByUserEmail(userEmail);
    }

    public void deleteMessage(Long messageId) {
        notificationRepository.deleteById(messageId);
    }


    /**
     * 주문요청 처리
     * - 취소 / 주문완료 처리/ 주문 대기(거래 대기중)
     */
    public void changeToScheduled(Long saleItemId) {
        SaleItem saleItem = saleItemRepository.findById(saleItemId).orElseThrow(SaleItemNotFoundException::new);
        saleItem.changeToScheduled();
    }

    public void changeToCompleted(Long saleItemId) {
        SaleItem saleItem = saleItemRepository.findById(saleItemId).orElseThrow(SaleItemNotFoundException::new);
        saleItem.changeToCompleted();
    }

    public void changeToContacting(Long saleItemId) {
        SaleItem saleItem = saleItemRepository.findById(saleItemId).orElseThrow(SaleItemNotFoundException::new);
        saleItem.changeToContacting();

        saleItemRepository.delete(saleItem);
    }


    private Long createSaleItem(User buyer, Item item) {
        SaleItem saleItem = SaleItem.builder()
                .seller(item.checkSeller())
                .buyer(buyer)
                .item(item)
                .price(item.getPrice())
                .build();

        SaleItem savedSaleItem = saleItemRepository.save(saleItem);

        return savedSaleItem.getId();
    }
}
