package com.example.demoshop.repository.item;

import com.example.demoshop.domain.item.Item;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, SearchItemRepository {

    // 락이 필요한 상황에서 사용하는 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Optional<Item> findByIdWithLock(@Param("id") Long id);

    // nameKor에 해당하는 Item이 존재하는지 확인하는 메서드
    // boolean existsByNameKor(String nameKor);
    // boolean existsBySanrioCharacters(SanrioCharacters sanrioCharacters);

    // for test
    Optional<Item> findByNameKor(String itemName);



    // 상품명(FT 검색) + 캐릭터 선택 (Cursor 페이징)
    @Query(value =
            "SELECT * " +
                    "FROM item " +
                    "WHERE MATCH(name_kor) AGAINST(:keyword IN BOOLEAN MODE) " +
                    "AND sanrio_characters = :character " +
                    "AND (:lastItemId IS NULL OR item_id < :lastItemId) " +  // lastItemId가 null일 경우 조건 생략
                    "ORDER BY item_id DESC " +
                    "LIMIT :limit", nativeQuery = true)
    List<Item> searchByKeywordAndCharacterWithCursor(@Param("keyword") String keyword, @Param("character") String character,
                                                     @Param("lastItemId") Long lastItemId, @Param("limit") int limit);


    // 상품명(FT 검색) (Cursor 페이징)
    @Query(value = "SELECT * FROM item " +
            "WHERE MATCH(name_kor) AGAINST(:keyword IN NATURAL LANGUAGE MODE) " +
            "AND (:lastItemId IS NULL OR item_id > :lastItemId) " + // lastItemId가 null일 경우 조건 생략
            "ORDER BY item_id DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Item> searchByItemNameWithCursor(@Param("keyword") String keyword, @Param("lastItemId") Long lastItemId, @Param("limit") int limit);


}
