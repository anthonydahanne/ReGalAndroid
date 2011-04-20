package net.dahanne.android.regalandroid;

import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class AllTests extends TestSuite {
    
    public static TestSuite suite() {
    	return new TestSuiteBuilder(AllTests.class)
        .includeAllPackagesUnderHere()
        .build();
    }
    
//    @Override
//    public ClassLoader getLoader() {
//        return CalculatorInstrumentationTestRunner.class.getClassLoader();
//    }
}

