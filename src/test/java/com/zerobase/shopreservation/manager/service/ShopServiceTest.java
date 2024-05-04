package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.MemberRole;
import com.zerobase.shopreservation.manager.dto.CreateShopDto;
import com.zerobase.shopreservation.manager.dto.ShopOutputDto;
import com.zerobase.shopreservation.manager.dto.UpdateShopDto;
import com.zerobase.shopreservation.manager.exception.ShopManagerNotMatchException;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Manager ShopService Test")
class ShopServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ShopRepository shopRepository;

    @InjectMocks
    private ShopService shopService;

    @BeforeEach
    public void setupMember() {
        shopService.setMemberRepository(memberRepository);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_MANAGER");
        Collection authorities = Collections.singleton(authority); // Use raw type here

        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getAuthorities()).thenReturn(authorities);

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        lenient().when(memberRepository.findByEmailAndRole(any(), any()))
                .thenReturn(Optional.of(member1));
    }

    private final Member member1 = Member.builder()
            .id(1)
            .email("abc@gmail.com")
            .role(MemberRole.ROLE_MANAGER)
            .password("somehashedvalue")
            .phone("010-3333-2222")
            .build();

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("상점 만들기 - 성공")
    void create_shop() {
        //given
        CreateShopDto dto = CreateShopDto.builder()
                .name("뱅뱅막국수")
                .build();
        //when
        ArgumentCaptor<Shop> captor = ArgumentCaptor.forClass(Shop.class);
        shopService.create(dto);

        //then
        verify(shopRepository).save(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals("뱅뱅막국수", captor.getValue().getName()); // dto가 entity로 잘 변환되었는지 검사
        assertNotNull(captor.getValue().getManager());
        assertEquals("abc@gmail.com", captor.getValue().getManager().getEmail()); // manager 연결이 잘 되었는지 검사
    }

    @Test
    @DisplayName("상점 정보 수정 - 성공")
    void update_shop() {
        //given
        when(shopRepository.findById(1L))
                .thenReturn(Optional.of(Shop.builder()
                        .name("뱅뱅막국수")
                        .description("뱅뱅사거리 근처")
                        .address("서울특별시 강남구 양재대로 3")
                        .manager(member1)
                        .build()
                ));

        UpdateShopDto dto = UpdateShopDto.builder()
                .name("뱅뱅막국수 본점")
                .description("뱅뱅막국수는 뱅뱅사거리 근처 오래된 막국수집입니다.")
                .build();

        //when
        ArgumentCaptor<Shop> captor = ArgumentCaptor.forClass(Shop.class);
        shopService.update(1L, dto);

        //then
        verify(shopRepository).save(captor.capture());
        assertEquals("뱅뱅막국수 본점", captor.getValue().getName()); // 이름이 변경되었는지 확인
        assertEquals("뱅뱅막국수는 뱅뱅사거리 근처 오래된 막국수집입니다.", captor.getValue().getDescription()); // 설명이 변경되었는지 확인
        assertEquals("서울특별시 강남구 양재대로 3", captor.getValue().getAddress()); // 주소가 그대로인지 확인
    }

    @Test
    @DisplayName("상점 정보 수정 - 실패(shop이 없음)")
    void update_shop_fail_no_shop() {
        //given
        when(shopRepository.findById(1L))
                .thenReturn(Optional.empty());

        UpdateShopDto dto = UpdateShopDto.builder()
                .name("뱅뱅막국수 본점")
                .description("뱅뱅막국수는 뱅뱅사거리 근처 오래된 막국수집입니다.")
                .build();

        //when
        //then
        assertThrows(ShopNotExistException.class,
                () -> shopService.update(1L, dto)
        );
    }

    @Test
    @DisplayName("상점 정보 수정 - 실패(해당 상점의 manager가 아님)")
    void update_shop_fail_not_manager_of_the_shop() {
        //given
        Member member2 = Member.builder() // 현재 로그인한 manager는 id가 1임
                .id(2)
                .email("aaa@gmail.com")
                .role(MemberRole.ROLE_MANAGER)
                .password("somehashedvalue")
                .phone("010-1212-1313")
                .build();

        when(shopRepository.findById(1L))
                .thenReturn(Optional.of(Shop.builder()
                        .name("뱅뱅막국수")
                        .description("뱅뱅사거리 근처")
                        .address("서울특별시 강남구 양재대로 3")
                        .manager(member2)
                        .build()
                ));

        UpdateShopDto dto = UpdateShopDto.builder()
                .name("뱅뱅막국수 본점")
                .description("뱅뱅막국수는 뱅뱅사거리 근처 오래된 막국수집입니다.")
                .build();

        //when
        //then
        assertThrows(ShopManagerNotMatchException.class,
                () -> shopService.update(1L, dto)
        );
    }

    @Test
    @DisplayName("상점 목록 나열 - 성공")
    void list_shops() {
        //given
        List<Shop> shops = new ArrayList<>();
        shops.add(Shop.builder()
                .name("본죽")
                .build()
        );
        given(shopRepository.findByManagerAndDeleteMarker(any(), anyBoolean()))
                .willReturn(shops);

        //when
        List<ShopOutputDto> listDtos = shopService.list();

        //then
        assertEquals("본죽", listDtos.stream().findFirst().get().getName());
    }

    @Test
    @DisplayName("상점 제거 - 성공")
    void delete_shop() {
        //given
        given(shopRepository.findByIdAndManager(anyLong(), any()))
                .willReturn(Optional.of(Shop.builder()
                        .id(1)
                        .deleteMarker(false)
                        .build())
                );

        //when
        shopService.delete(1);
        ArgumentCaptor<Shop> captor = ArgumentCaptor.forClass(Shop.class);

        //then
        verify(shopRepository).save(captor.capture());
        assertTrue(captor.getValue().isDeleteMarker());
    }

    @Test
    @DisplayName("상점 제거 - 실패(상점이 존재하지 않음)")
    void delete_shop_fail() {
        //given
        given(shopRepository.findByIdAndManager(anyLong(), any()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(
                ShopNotExistException.class,
                () -> shopService.delete(1)
        );
    }
}