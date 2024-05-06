package com.zerobase.shopreservation.common.mapper;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.common.dto.ReservationsOfShopDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    long selectListCount(ReservationsOfShopDto dto);

    List<ReservationOutputDto> selectList(ReservationsOfShopDto dto);
}