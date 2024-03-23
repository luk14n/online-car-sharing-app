package com.lukian.onlinecarsharing.mapper;

import com.lukian.onlinecarsharing.config.MapperConfig;
import com.lukian.onlinecarsharing.dto.payment.PaymentDto;
import com.lukian.onlinecarsharing.model.Payment;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
}
