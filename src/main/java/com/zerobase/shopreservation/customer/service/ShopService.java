package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.customer.dto.ShopDto;
import com.zerobase.shopreservation.customer.dto.ShopSearchDto;
import com.zerobase.shopreservation.customer.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("customerShopService")
@RequiredArgsConstructor
public class ShopService extends BaseService {

    final private ShopRepository shopRepository;

    /**
     * 상점을 검색함
     * ShopSearchDto에 name, address 모두 없다면, 전체를 검색함
     */
    public List<ShopDto> search(ShopSearchDto shopSearchDto) {
        List<Shop> list;
        String name = shopSearchDto.getName();
        String address = shopSearchDto.getAddress();

        if (name != null && address != null) {
            list = shopRepository.findByNameContainsAndAddressContainsAndDeleteMarker(name, address, false);
        } else if (name != null) {
            list = shopRepository.findByNameContainsAndDeleteMarker(name, false);
        } else if (address != null) {
            list = shopRepository.findByAddressContainsAndDeleteMarker(address, false);
        } else {
            list = shopRepository.findByDeleteMarker(false);
        }

        List<ShopDto> listDto = new ArrayList<>();
        for (Shop r : list) {
            listDto.add(ShopDto.of(r));
        }
        return listDto;
    }

    /**
     * 상점 하나를 검색함
     */
    public ShopDto detail(long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        if (shop.isEmpty()) {
            throw new ShopNotExistException();
        }
        return ShopDto.of(shop.get());
    }
}
