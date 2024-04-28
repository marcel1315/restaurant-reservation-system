package com.zerobase.restaurantreservationsystem.manager.service;

import com.zerobase.restaurantreservationsystem.common.exception.RestaurantNotExistException;
import com.zerobase.restaurantreservationsystem.common.type.MemberRole;
import com.zerobase.restaurantreservationsystem.manager.dto.CreateRestaurantDto;
import com.zerobase.restaurantreservationsystem.common.entity.Member;
import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import com.zerobase.restaurantreservationsystem.common.exception.MemberNotExistException;
import com.zerobase.restaurantreservationsystem.manager.dto.RestaurantDto;
import com.zerobase.restaurantreservationsystem.manager.repository.RestaurantRepository;
import com.zerobase.restaurantreservationsystem.common.repository.MemberRepository;
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
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    public void create(CreateRestaurantDto createRestaurantDto) {
        Restaurant restaurant = createRestaurantDto.toRestaurant();
        restaurant.setManager(getManager());

        restaurantRepository.save(restaurant);
    }

    public List<RestaurantDto> list() {
        List<Restaurant> list = restaurantRepository.findByManagerAndDeleteMarker(getManager(), false);
        List<RestaurantDto> listDto = new ArrayList<>();
        for (Restaurant r : list) {
            listDto.add(RestaurantDto.of(r));
        }
        return listDto;
    }

    public void delete(long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findByIdAndManager(id, getManager());

        if (restaurant.isEmpty()) {
            throw new RestaurantNotExistException();
        }

        restaurant.get().setDeleteMarker(true);
        restaurantRepository.save(restaurant.get());
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
