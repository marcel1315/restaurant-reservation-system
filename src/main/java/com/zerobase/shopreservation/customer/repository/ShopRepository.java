package com.zerobase.shopreservation.customer.repository;

import com.zerobase.shopreservation.common.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customerShopRepository")
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByDeleteMarker(boolean deleteMarker);

    List<Shop> findByNameContainsAndDeleteMarker(String name, boolean deleteMarker);

    List<Shop> findByAddressContainsAndDeleteMarker(String address, boolean deleteMarker);

    List<Shop> findByNameContainsAndAddressContainsAndDeleteMarker(String name, String address, boolean deleteMarker);
}
