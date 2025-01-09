package at.fhtw.bweng_ws24.mapper;

import at.fhtw.bweng_ws24.dto.OrderDto;
import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    // Mapping Order to OrderDto
    @Mappings({
            @Mapping(source = "orderItems", target = "orderItems")
    })
    OrderDto orderToOrderDto(Order order);

    // Mapping OrderDto to Order
    @Mappings({
            @Mapping(source = "orderItems", target = "orderItems")
    })
    Order orderDtoToOrder(OrderDto orderDto);

    // Convert List<String> (product IDs) to List<OrderItem>
    default List<OrderItem> mapProductIdsToOrderItems(List<String> productIds) {
        return productIds.stream()
                .map(productId -> new OrderItem(null, productId, 1)) // Default quantity 1
                .toList();
    }

    // Convert List<OrderItem> to List<String> (extracting product IDs)
    default List<String> mapOrderItemsToProductIds(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getProductId)
                .toList();
    }
}