package at.fhtw.bweng_ws24.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void shouldInitializeProductMapper() {
        assertThat(productMapper).isNotNull();
    }
}