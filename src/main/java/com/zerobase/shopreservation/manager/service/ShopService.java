package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.type.MemberRole;
import com.zerobase.shopreservation.manager.dto.CreateShopDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.MemberNotExistException;
import com.zerobase.shopreservation.manager.dto.ShopDto;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final MemberRepository memberRepository;

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

    private Member getManager() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = "";
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
            break;
        }

        Optional<Member> manager = memberRepository.findByEmailAndRole(username, MemberRole.valueOf(role));
        if (manager.isEmpty()) {
            throw new MemberNotExistException();
        }
        return manager.get();
    }
}
