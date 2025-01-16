package at.fhtw.bweng_ws24.dto;

import at.fhtw.bweng_ws24.testutil.ModelTester;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class DtoUnitTest {

    @ParameterizedTest
    @MethodSource("classProvider")
    void testModelClasses(Class<?> clazz) {
        ModelTester.forClass(clazz)
                .test();
    }

    static Stream<Class<?>> classProvider() {
        return Stream.of(
                OrderDto.class,
                OrderItemDto.class,
                PostProductDto.class,
                PostUserDto.class,
                PutProductDto.class,
                PutUserDto.class,
                PutUserPasswordDto.class,
                ResourceImageDto.class,
                TokenRequestDto.class,
                TokenResponseDto.class,
                UserResponseDto.class
        );
    }
}