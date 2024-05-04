package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.dto.ShopOutputDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.manager.dto.CreateShopDto;
import com.zerobase.shopreservation.manager.dto.UpdateShopDto;
import com.zerobase.shopreservation.manager.exception.ShopManagerNotMatchException;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void create(CreateShopDto createShopDto) {
        Shop shop = createShopDto.toShop();
        shop.setManager(getManager());

        shopRepository.save(shop);
    }

    /**
     * 상점 정보 업데이트
     */
    @Transactional
    public void update(long id, UpdateShopDto dto) {
        // shop 불러오기
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isEmpty()) {
            throw new ShopNotExistException();
        }
        Shop shop = optionalShop.get();

        // shop의 manager인지 검사
        Member manager = getManager();
        if (shop.getManager().getId() != manager.getId()) {
            throw new ShopManagerNotMatchException();
        }

        if (dto.getAddress() != null) shop.setAddress(dto.getAddress());
        if (dto.getName() != null) shop.setName(dto.getName());
        if (dto.getDescription() != null)
            shop.setDescription(dto.getDescription());
        if (dto.getLatitude() != null) shop.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) shop.setLongitude(dto.getLongitude());
        if (dto.getReservationStartTimeWeekday() != null)
            shop.setReservationStartTimeWeekday(dto.getReservationStartTimeWeekday());
        if (dto.getReservationFinalTimeWeekday() != null)
            shop.setReservationFinalTimeWeekday(dto.getReservationFinalTimeWeekday());
        if (dto.getReservationStartTimeWeekend() != null)
            shop.setReservationStartTimeWeekend(dto.getReservationStartTimeWeekend());
        if (dto.getReservationFinalTimeWeekend() != null)
            shop.setReservationFinalTimeWeekend(dto.getReservationFinalTimeWeekend());
        if (dto.getReservationIntervalMinute() != null)
            shop.setReservationIntervalMinute(dto.getReservationIntervalMinute());

        shopRepository.save(shop);
    }

    /**
     * 상점 목록을 봄
     * manager가 자신의 상점 목록을 봄. 다른 manager의 상점 목록을 보여주진 않음
     */
    public List<ShopOutputDto> list() {
        List<Shop> list = shopRepository.findByManagerAndDeleteMarker(getManager(), false);
        List<ShopOutputDto> listDto = new ArrayList<>();
        for (Shop r : list) {
            listDto.add(ShopOutputDto.of(r));
        }
        return listDto;
    }

    /**
     * 상점을 제거함
     * manager가 자신의 상점을 제거함. 다른 manager의 상점을 제거할 순 없음
     * 실제 DB에서 삭제하진 않고, delete marker를 두어 제거된 표시를 함.
     * (상점과 연결된 review, reservation을 보존하는 것을 수월하게 하기 위해)
     */
    @Transactional
    public void delete(long id) {
        Optional<Shop> shop = shopRepository.findByIdAndManager(id, getManager());

        if (shop.isEmpty()) {
            throw new ShopNotExistException();
        }

        shop.get().setDeleteMarker(true);
        shopRepository.save(shop.get());
    }
}
