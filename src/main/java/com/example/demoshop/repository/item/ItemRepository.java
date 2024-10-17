package com.example.demoshop.repository.item;

import com.example.demoshop.domain.item.Item;
import com.example.demoshop.domain.item.common.SanrioCharacters;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, SearchItemRepository {

    // 락이 필요한 상황에서 사용하는 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Optional<Item> findByIdWithLock(@Param("id") Long id);

    // nameKor에 해당하는 Item이 존재하는지 확인하는 메서드
    boolean existsByNameKor(String nameKor);
    boolean existsBySanrioCharacters(SanrioCharacters sanrioCharacters);

}
