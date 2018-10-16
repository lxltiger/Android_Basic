// ISecondary.aidl
package shine.com.advance;

// Declare any non-default types here with import statements

interface ISecondary {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getPid();

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
