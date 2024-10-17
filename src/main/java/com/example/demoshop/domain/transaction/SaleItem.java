package com.example.demoshop.domain.transaction;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.transaction.common.SaleStatus;
import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.response.sale.SaleItemResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/**
 *  거래상품으로 올린,
 *  실제로 판매되거나 거래 과정에서 사용되는 상품.
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "sale_item")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "sale_item_id")
    private Long id;

    private LocalDateTime created_date; // 판매 등록한 날짜
    private Integer price; // 금액

    @Enumerated(EnumType.STRING)
    private SaleStatus status; // 거래 상태

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public SaleItem(User seller, User buyer, Item item, Integer price) {
        this.status = SaleStatus.CONTACT_PENDING;
        this.seller = seller;
        this.buyer = buyer;
        this.item = item;
        this.price = price;
        this.created_date = LocalDateTime.now();
    }

   public void changeToScheduled() {
        this.status = SaleStatus.MEETING_SCHEDULED;
   }

   public void changeToCompleted() {
        this.status = SaleStatus.TRADE_COMPLETE;
   }

   public void changeToContacting() {
        this.status = SaleStatus.CONTACT_PENDING;
   }



}
