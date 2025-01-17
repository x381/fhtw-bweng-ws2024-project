package at.fhtw.bweng_ws24.testutil.tester.standard;

import at.fhtw.bweng_ws24.testutil.tester.base.AbstractClassTester;
import at.fhtw.bweng_ws24.testutil.tester.base.Tester;

import java.lang.reflect.Method;

/**
 * Tester for verifying the `toString()` implementation.
 */
public class ToStringTester extends AbstractClassTester implements Tester {

    @Override
    public void test(Object instance) {
        Class<?> clazz = instance.getClass();
        try {
            Method toStringMethod = getMethod(clazz, "toString");
            if (toStringMethod == null) return;

            Object instanceTest = createInstance(clazz);
            String expected = clazz.getSimpleName() + "(";
            String actual = (String) toStringMethod.invoke(instanceTest);

            if (!actual.startsWith(expected)) {
                throw new AssertionError("toString() failed for class " + clazz.getSimpleName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error testing toString() for class " + clazz.getSimpleName(), e);
        }
    }
}