package com.zerobase.shopreservation.common.mapper;

import com.zerobase.shopreservation.common.dto.ReviewListInfoDto;
import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.common.dto.ReviewsOfShopDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    ReviewListInfoDto selectListInfo(ReviewsOfShopDto dto);

    List<ReviewOutputDto> selectList(ReviewsOfShopDto dto);
}