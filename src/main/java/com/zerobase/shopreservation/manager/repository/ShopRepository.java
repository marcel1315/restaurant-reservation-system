package com.zerobase.shopreservation.manager.repository;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("managerShopRepository")
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByManagerAndDeleteMarker(Member manager, boolean deleteMarker);

    Optional<Shop> findByIdAndManager(long shopId, Member manager);

    boolean existsByIdAndManager(long shopId, Member manager);
}
