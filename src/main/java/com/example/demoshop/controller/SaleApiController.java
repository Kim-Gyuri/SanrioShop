package com.example.demoshop.controller;

import com.example.demoshop.domain.transaction.Notification;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.repository.sale.NotificationRepository;
import com.example.demoshop.request.item.IdRequest;
import com.example.demoshop.response.item.SellerItemDto;
import com.example.demoshop.response.item.CustomerOrderDto;
import com.example.demoshop.response.sale.SaleItemResponse;
import com.example.demoshop.service.sale.SaleItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class SaleApiController {

    private final PagedResourcesAssembler<SellerItemDto> sellerItemAssembler;
    private final PagedResourcesAssembler<CustomerOrderDto> customerOrderAssembler;
    private final SaleItemService saleItemService;
    private final NotificationRepository notificationRepository;


    // 주문요청
    @PostMapping(value = "/orders")
    public void addWishList(@AuthenticationPrincipal User user, @RequestBody IdRequest idRequest) {
        saleItemService.contactTrade(user.getEmail(), idRequest.getId());
    }

    // 주문상품 조회 (by 구매 회원)
    @GetMapping("/orders")
    public PagedModel<EntityModel<CustomerOrderDto>> findOrderList(@AuthenticationPrincipal User user, Pageable pageable) {
        Page<CustomerOrderDto> order = saleItemService.findOrderList(user.getEmail(), pageable);

        return customerOrderAssembler.toModel(order);
    }

    // 판매 조회; 판매상품 + 주문 (by 판매 회원)
    @GetMapping("/sellers/sales")
    public PagedModel<EntityModel<SellerItemDto>> findSales(Pageable pageable, @AuthenticationPrincipal User user)  {
        Page<SellerItemDto> sale = saleItemService.findSale(user.getEmail(), pageable);

        return sellerItemAssembler.toModel(sale);
    }

    // 판매 조회> 판매상품 > 주문 상세조회 (주문자 정보 확인용도)
    @GetMapping("/sellers/orders/{itemId}")
    public SaleItemResponse findSaleItemDetail(@AuthenticationPrincipal User user, @PathVariable("itemId") Long id) {
        return saleItemService.findOrderDetail(id);
    }

    @GetMapping("/message")
    public List<Notification> findMessage(@AuthenticationPrincipal User user) {
        return notificationRepository.findByUserEmail(user.getEmail());
    }

    @DeleteMapping("/message/{id}")
    public void deleteMessage(@AuthenticationPrincipal User user, @PathVariable("id") Long messageId) {
        saleItemService.deleteMessage(messageId);
    }

}
