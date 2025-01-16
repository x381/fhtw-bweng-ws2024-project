package at.fhtw.bweng_ws24.testutil.tester.base;

/**
 * Functional interface for testing methods or classes.
 */
@FunctionalInterface
public interface Tester {
    void test(Object instance);
}