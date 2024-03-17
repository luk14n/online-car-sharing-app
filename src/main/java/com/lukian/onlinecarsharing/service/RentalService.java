package com.lukian.onlinecarsharing.service;

import com.lukian.onlinecarsharing.dto.rental.ActualReturnDateSetRequestDto;
import com.lukian.onlinecarsharing.dto.rental.RentalCreateRequestDto;
import com.lukian.onlinecarsharing.dto.rental.RentalDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalDto save(Long userId, RentalCreateRequestDto requestDto);

    List<RentalDto> getActiveOrNot(Pageable pageable, Long userId, boolean isActive);

    List<RentalDto> findAll(Pageable pageable);

    RentalDto setActualReturnDate(Long userId,
                                  ActualReturnDateSetRequestDto requestDto);

    List<RentalDto> findAllPersonal(Long userId, Pageable pageable);
}
