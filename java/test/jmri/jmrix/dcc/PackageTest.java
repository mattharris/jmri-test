package jmri.jmrix.dcc;

import org.apache.log4j.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the jmri.jmrix.dcc package
 * @author			Bob Jacobsen
 */
public class PackageTest extends TestCase {

    // from here down is testing infrastructure

    public PackageTest(String s) {
        super(s);
    }

    // Main entry point
    static public void main(String[] args) {
        String[] testCaseName = {PackageTest.class.getName()};
        junit.swingui.TestRunner.main(testCaseName);
    }

    // test suite from all defined tests
    public static Test suite() {
        TestSuite suite = new TestSuite("jmri.jmrix.dcc.PackageTest");  // no tests in this class itself
//        suite.addTest(new TestSuite(FooTest.class));
        return suite;
    }

    static Logger log = Logger.getLogger(PackageTest.class.getName());
}
