package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.dto.ShopOutputDto;
import com.zerobase.shopreservation.common.dto.ShopOutputPageDto;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.customer.dto.OneShopSearchDto;
import com.zerobase.shopreservation.customer.dto.ShopSearchDto;
import com.zerobase.shopreservation.customer.mapper.ShopMapper;
import com.zerobase.shopreservation.customer.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("customerShopService")
@RequiredArgsConstructor
public class ShopService extends BaseService {

    final private ShopMapper shopMapper;

    /**
     * 상점을 검색함
     * ShopSearchDto에 name, address 모두 없다면, 전체를 검색함
     */
    public ShopOutputPageDto search(ShopSearchDto shopSearchDto) {
        long totalCount = shopMapper.selectListCount(shopSearchDto);
        // 0row -> 1page / 10row -> 1page / 11row -> 2page
        long totalPage = Math.max(totalCount - 1, 0) / shopSearchDto.getPageSize() + 1;

        List<ShopOutputDto> shopOutputDtoList = shopMapper.selectList(shopSearchDto);
        return ShopOutputPageDto.builder()
                .shops(shopOutputDtoList)
                .totalCount(totalCount)
                .totalPage(totalPage)
                .currentPage(shopSearchDto.getPageIndex())
                .build();
    }

    /**
     * 상점 하나를 검색함
     */
    public ShopOutputDto detail(OneShopSearchDto dto) {
        ShopOutputDto shopOutputDto = shopMapper.selectOne(dto);
        if (shopOutputDto == null) {
            throw new ShopNotExistException();
        }
        return shopOutputDto;
    }
}
