package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.ShopSort;
import com.zerobase.shopreservation.customer.dto.OneShopSearchDto;
import com.zerobase.shopreservation.customer.dto.ShopOutputDto;
import com.zerobase.shopreservation.customer.dto.ShopOutputPageDto;
import com.zerobase.shopreservation.customer.dto.ShopSearchDto;
import com.zerobase.shopreservation.customer.mapper.ShopMapper;
import com.zerobase.shopreservation.customer.repository.ShopRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer ShopService Test")
class ShopServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    ShopRepository shopRepository;

    @Mock
    ShopMapper shopMapper;

    @InjectMocks
    ShopService shopService;

    @Test
    @DisplayName("상점 검색 - 성공")
    void list_shops_name() {
        //given
        ShopSearchDto dto = ShopSearchDto.builder()
                .pageSize(3)
                .pageIndex(1)
                .name("상점")
                .sortBy(ShopSort.NAME)
                .build();
        when(shopMapper.selectListCount(dto))
                .thenReturn(55L);
        when(shopMapper.selectList(dto))
                .thenReturn(List.of(
                        ShopOutputDto.builder()
                                .name("상점1")
                                .build(),
                        ShopOutputDto.builder()
                                .name("상점2")
                                .build(),
                        ShopOutputDto.builder()
                                .name("상점3")
                                .build()
                ));

        //when
        ShopOutputPageDto page = shopService.search(dto);

        //then
        assertEquals(1, page.getCurrentPage());
        assertEquals(55, page.getTotalCount());
        assertEquals(19, page.getTotalPage()); // 55 / 3 = 18.xx
        assertEquals(3, page.getShops().size());
    }

    @Test
    @DisplayName("상점 검색 - 성공(결과가 없을 때)")
    void list_shops_name_empty_list() {
        //given
        ShopSearchDto dto = ShopSearchDto.builder()
                .pageSize(3)
                .pageIndex(1)
                .name("상점")
                .sortBy(ShopSort.NAME)
                .build();
        when(shopMapper.selectListCount(dto))
                .thenReturn(0L);
        when(shopMapper.selectList(dto))
                .thenReturn(List.of());

        //when
        ShopOutputPageDto page = shopService.search(dto);

        //then
        assertEquals(1, page.getCurrentPage());
        assertEquals(0, page.getTotalCount());
        assertEquals(1, page.getTotalPage()); // 55 / 3 = 18.xx
        assertEquals(0, page.getShops().size());
    }

    @Test
    @DisplayName("상점 상세 - 성공")
    void detail_shop() {
        //given
        when(shopMapper.selectOne(any()))
                .thenReturn(
                        ShopOutputDto.builder()
                                .id(1L)
                                .build()
                );
        OneShopSearchDto dto = OneShopSearchDto.builder()
                .id(1L)
                .currentLongitude(37.10)
                .currentLongitude(127.20)
                .build();
        //when
        ShopOutputDto detail = shopService.detail(dto);

        //then
        assertEquals(1L, detail.getId());
    }

    @Test
    @DisplayName("상점 상세 - 실패(상점 아이디에 대응하는 상점이 없음)")
    void detail_shop_fail() {
        //given
        when(shopMapper.selectOne(any()))
                .thenReturn(null);

        //when
        //then
        assertThrows(ShopNotExistException.class,
                () -> shopService.detail(any())
        );
    }
}