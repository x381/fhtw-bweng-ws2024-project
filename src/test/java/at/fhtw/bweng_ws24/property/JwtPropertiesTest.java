package at.fhtw.bweng_ws24.property;

import at.fhtw.bweng_ws24.testutil.ModelTester;
import org.junit.jupiter.api.Test;

class JwtPropertiesTest {
    @Test
    void testGetterSetter() {
        // Using ModelTester to validate getters and setters of JwtProperties
        ModelTester.forClass(JwtProperties.class).test();
    }
}