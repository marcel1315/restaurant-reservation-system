package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.dto.ShopOutputDto;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.customer.dto.ShopSearchDto;
import com.zerobase.shopreservation.customer.repository.ShopRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer ShopService Test")
class ShopServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    ShopRepository shopRepository;

    @InjectMocks
    ShopService shopService;

    @Test
    @DisplayName("상점 검색 - 성공(이름 검색)")
    void list_shops_name() {
        //given
        ShopSearchDto dto = ShopSearchDto.builder().name("칼국수").build();

        //when
        shopService.search(dto);

        //then
        verify(shopRepository, times(1))
                .findByNameContainsAndDeleteMarker(eq("칼국수"), anyBoolean());
    }

    @Test
    @DisplayName("상점 검색 - 성공(주소 검색)")
    void list_shops_address() {
        //given
        ShopSearchDto dto = ShopSearchDto.builder().address("강남").build();

        //when
        shopService.search(dto);

        //then
        verify(shopRepository, times(1))
                .findByAddressContainsAndDeleteMarker(eq("강남"), anyBoolean());
    }

    @Test
    @DisplayName("상점 검색 - 성공(이름과 주소 검색)")
    void list_shops_name_and_address() {
        //given
        ShopSearchDto dto = ShopSearchDto.builder()
                .name("칼국수")
                .address("강남").build();

        //when
        shopService.search(dto);

        //then
        verify(shopRepository, times(1))
                .findByNameContainsAndAddressContainsAndDeleteMarker(eq("칼국수"), eq("강남"), anyBoolean());
    }

    @Test
    @DisplayName("상점 검색 - 성공(검색어 없음)")
    void list_shops_no_criteria() {
        //given
        ShopSearchDto dto = ShopSearchDto.builder().build();

        //when
        shopService.search(dto);

        //then
        verify(shopRepository, times(1))
                .findByDeleteMarker(anyBoolean());
    }

    @Test
    @DisplayName("상점 상세 - 성공")
    void detail_shop() {
        //given
        given(shopRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        Shop.builder()
                                .id(1L)
                                .build()
                ));

        //when
        ShopOutputDto detail = shopService.detail(1L);

        //then
        assertEquals(1L, detail.getId());
    }

    @Test
    @DisplayName("상점 상세 - 실패(상점 아이디에 대응하는 상점이 없음)")
    void detail_shop_fail() {
        //given
        given(shopRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(ShopNotExistException.class,
                () -> shopService.detail(1L)
        );
    }
}