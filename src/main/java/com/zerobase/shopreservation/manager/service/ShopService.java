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
@Service("managerShopService")
@RequiredArgsConstructor
public class ShopService extends BaseService {

    private final ShopRepository shopRepository;

    /**
     * 상점 생성
     * manager가 상점을 만듦
     */
    public void create(CreateShopDto createShopDto) {
        Shop shop = createShopDto.toShop();
        shop.setManager(getManager());

        shopRepository.save(shop);
    }

    /**
     * 상점 목록을 봄
     * manager가 자신의 상점 목록을 봄. 다른 manager의 상점 목록을 보여주진 않음
     */
    public List<ShopDto> list() {
        List<Shop> list = shopRepository.findByManagerAndDeleteMarker(getManager(), false);
        List<ShopDto> listDto = new ArrayList<>();
        for (Shop r : list) {
            listDto.add(ShopDto.of(r));
        }
        return listDto;
    }

    /**
     * 상점을 제거함
     * manager가 자신의 상점을 제거함. 다른 manager의 상점을 제거할 순 없음
     * 실제 DB에서 삭제하진 않고, delete marker를 두어 제거된 표시를 함.
     * (상점과 연결된 review, reservation을 보존하는 것을 수월하게 하기 위해)
     */
    public void delete(long id) {
        Optional<Shop> shop = shopRepository.findByIdAndManager(id, getManager());

        if (shop.isEmpty()) {
            throw new ShopNotExistException();
        }

        shop.get().setDeleteMarker(true);
        shopRepository.save(shop.get());
    }
}
