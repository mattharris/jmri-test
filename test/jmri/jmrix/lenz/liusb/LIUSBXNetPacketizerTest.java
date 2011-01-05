package jmri.jmrix.lenz.liusb;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * <p>Title: LIUSBXNetPacketizerTest </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * @author Paul Bender 
 * @version $Revision: 1.4 $
 */
public class LIUSBXNetPacketizerTest extends TestCase {

        public void testCtor() {
          LIUSBXNetPacketizer f = new LIUSBXNetPacketizer(new jmri.jmrix.lenz.LenzCommandStation());
          Assert.assertNotNull(f);
        }

        // from here down is testing infrastructure

    public LIUSBXNetPacketizerTest(String s) {
        super(s);
    }

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {"-noloading", LIUSBXNetPacketizerTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

    // The minimal setup for log4J
    protected void setUp() { apps.tests.Log4JFixture.setUp(); }
    protected void tearDown() { apps.tests.Log4JFixture.tearDown(); }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LIUSBXNetPacketizerTest.class.getName());

}
