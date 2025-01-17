package at.fhtw.bweng_ws24.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    void shouldInitializeOrderMapper() {
        assertThat(orderMapper).isNotNull();
    }
}