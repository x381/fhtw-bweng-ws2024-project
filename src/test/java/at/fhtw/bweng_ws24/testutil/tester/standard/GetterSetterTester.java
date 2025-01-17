package at.fhtw.bweng_ws24.testutil.tester.standard;

import at.fhtw.bweng_ws24.testutil.tester.base.AbstractClassTester;
import at.fhtw.bweng_ws24.testutil.tester.base.Tester;

import java.lang.reflect.Method;

/**
 * Tester for verifying getter and setter functionality.
 */
public class GetterSetterTester extends AbstractClassTester implements Tester {

    @Override
    public void test(Object instance) {
        Class<?> clazz = instance.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (isGetter(method)) {
                testGetter(method, clazz);
            } else if (isSetter(method)) {
                testSetter(method, clazz);
            }
        }
    }

    private boolean isGetter(Method method) {
        return method.getName().startsWith("get") && method.getParameterCount() == 0;
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set") && method.getParameterCount() == 1;
    }

    private void testGetter(Method getter, Class<?> clazz) {
        String propertyName = getter.getName().substring(3);
        try {
            Method setter = clazz.getDeclaredMethod("set" + propertyName, getter.getReturnType());
            Object instance = createInstance(clazz);
            Object sampleValue = createSampleValue(getter.getReturnType());

            setter.invoke(instance, sampleValue);
            Object actualValue = getter.invoke(instance);

            if (!sampleValue.equals(actualValue)) {
                throw new AssertionError("Getter/Setter failed for property " + propertyName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error testing getter/setter for " + propertyName, e);
        }
    }

    private void testSetter(Method setter, Class<?> clazz) {
        String propertyName = setter.getName().substring(3);
        try {
            Method getter = clazz.getDeclaredMethod("get" + propertyName);
            Object instance = createInstance(clazz);
            Object sampleValue = createSampleValue(setter.getParameterTypes()[0]);

            setter.invoke(instance, sampleValue);
            Object actualValue = getter.invoke(instance);

            if (!sampleValue.equals(actualValue)) {
                throw new AssertionError("Setter/Getter failed for property " + propertyName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error testing setter/getter for " + propertyName, e);
        }
    }

    private Object createSampleValue(Class<?> type) {
        if (type == int.class || type == Integer.class) return 1;
        if (type == String.class) return "test";
        if (type == boolean.class || type == Boolean.class) return true;
        // Add more type cases as needed.
        return null;
    }
}