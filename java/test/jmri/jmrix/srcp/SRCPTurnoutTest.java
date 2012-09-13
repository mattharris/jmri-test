package jmri.jmrix.srcp;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import jmri.jmrix.srcp.parser.SRCPClientParser;
import jmri.jmrix.srcp.parser.ParseException;

import java.io.StringReader;

/**
 * SRCPTurnoutTest.java
 *
 * Description:	tests for the jmri.jmrix.srcp.SRCPTurnout class
 *
 * @author	Bob Jacobsen
 * @version $Revision$
 */
public class SRCPTurnoutTest extends TestCase {

    public void testCtor() {
        SRCPTurnout m = new SRCPTurnout(1);
        Assert.assertNotNull(m);
    }

    // from here down is testing infrastructure
    public SRCPTurnoutTest(String s) {
        super(s);
    }

    // Main entry point
    static public void main(String[] args) {
        String[] testCaseName = {"-noloading", SRCPTurnoutTest.class.getName()};
        junit.swingui.TestRunner.main(testCaseName);
    }

    // test suite from all defined tests
    public static Test suite() {
        TestSuite suite = new TestSuite(SRCPTurnoutTest.class);
        return suite;
    }

    // The minimal setup for log4J
    @Override
    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    @Override
    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SRCPTurnoutTest.class.getName());
}
