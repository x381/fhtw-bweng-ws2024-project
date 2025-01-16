package at.fhtw.bweng_ws24.property;

import at.fhtw.bweng_ws24.testutil.ModelTester;
import org.junit.jupiter.api.Test;

class MinioPropertiesTest {
    @Test
    void testGetterSetter() {
        // Using ModelTester to validate getters and setters of MinioProperties
        ModelTester.forClass(MinioProperties.class).test();
    }
}