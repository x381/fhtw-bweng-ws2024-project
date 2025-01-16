package at.fhtw.bweng_ws24.model;

import at.fhtw.bweng_ws24.testutil.ModelTester;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ModelUnitTest {

    @ParameterizedTest
    @MethodSource("classProvider")
    void testModelClasses(Class<?> clazz) {
        // Using the ModelTester utility to test the provided class
        ModelTester.forClass(clazz).test();
    }

    // Supplies all model classes for testing
    static Stream<Class<?>> classProvider() {
        return Stream.of(
                Order.class,
                OrderItem.class,
                OrderStatus.class,
                Product.class,
                ProductCategory.class,
                ResourceImage.class,
                User.class,
                UserGender.class
        );
    }
}