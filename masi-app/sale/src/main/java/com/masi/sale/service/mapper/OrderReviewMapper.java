package com.masi.sale.service.mapper;

import com.masi.sale.domain.Order;
import com.masi.sale.domain.OrderReview;
import com.masi.sale.service.dto.OrderDTO;
import com.masi.sale.service.dto.OrderReviewDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderReview} and its DTO {@link OrderReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderReviewMapper extends EntityMapper<OrderReviewDTO, OrderReview> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderReviewDTO toDto(OrderReview s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
