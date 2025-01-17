package at.fhtw.bweng_ws24.testutil.tester.base;

import java.lang.reflect.Method;

/**
 * Abstract class providing base functionality for testers.
 */
public abstract class AbstractClassTester {

    protected Object createInstance(Class<?> clazz) {
        try {
            if (clazz.isEnum()) {
                return clazz.getEnumConstants()[0];
            }
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error creating instance of class " + clazz.getSimpleName(), e);
        }
    }

    protected Method getMethod(Class<?> clazz, String methodName) {
        try {
            return clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}