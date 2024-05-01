package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.manager.dto.CreateShopDto;
import com.zerobase.shopreservation.manager.dto.ShopDto;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService extends BaseService {

    private final ShopRepository shopRepository;

    public void create(CreateShopDto createShopDto) {
        Shop shop = createShopDto.toShop();
        shop.setManager(getManager());

        shopRepository.save(shop);
    }

    public List<ShopDto> list() {
        List<Shop> list = shopRepository.findByManagerAndDeleteMarker(getManager(), false);
        List<ShopDto> listDto = new ArrayList<>();
        for (Shop r : list) {
            listDto.add(ShopDto.of(r));
        }
        return listDto;
    }

    public void delete(long id) {
        Optional<Shop> shop = shopRepository.findByIdAndManager(id, getManager());

        if (shop.isEmpty()) {
            throw new ShopNotExistException();
        }

        shop.get().setDeleteMarker(true);
        shopRepository.save(shop.get());
    }
}
