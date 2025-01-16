package at.fhtw.bweng_ws24.exception;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ExceptionUnitTest {

    @TestFactory
    Stream<DynamicTest> testExceptionClasses() {
        return classProvider()
                .map(clazz -> dynamicTest("Test constructors for " + clazz.getSimpleName(), () -> {
                    // Test default constructor
                    assertDoesNotThrow(() -> clazz.getDeclaredConstructor().newInstance());
                    // Test constructor with String message
                    assertDoesNotThrow(() -> clazz.getDeclaredConstructor(String.class).newInstance("test message"));
                    // Test constructor with String message and Throwable cause
                    assertDoesNotThrow(() -> clazz.getDeclaredConstructor(String.class, Throwable.class)
                            .newInstance("test message", new RuntimeException("test cause")));
                }));
    }

    static Stream<Class<?>> classProvider() {
        return Stream.of(
                EmailExistsException.class,
                FileException.class,
                PasswordWrongException.class,
                StockNotEnoughException.class,
                UsernameExistsException.class
        );
    }
}