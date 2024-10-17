package com.example.demoshop.repository.sale;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.transaction.SaleItem;
import com.example.demoshop.domain.users.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    // 락이 필요한 상황에서 사용하는 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SaleItem s WHERE s.item = :item")
    Optional<SaleItem> findByItemWithLock(@Param("item") Item item);

    Optional<SaleItem> findByItem(Item item);

    boolean existsByItem(Item item);


}
