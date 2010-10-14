// UncaughtExceptionHandlerTest.java

package jmri.util.exceptionhandler;

import junit.framework.*;

import jmri.util.*;

/**
 * Tests for the jmri.util.UncaughtExceptionHandler class.
 * @author	Bob Jacobsen  Copyright 2010
 * @version	$Revision: 1.7 $
 */
public class UncaughtExceptionHandlerTest extends SwingTestCase {

    public void testThread() throws Exception {
        Thread t = new Thread(){
            public void run() {
                deref(null);
            }
            void deref(Object o) { o.toString(); }
        };
        t.start();
        flushAWT();
        awtSleep(100);
        JUnitAppender.assertErrorMessage("Unhandled Exception: java.lang.NullPointerException");
    }

    public void testSwing() throws Exception {
        boolean caught = false;
        Runnable r = new Runnable(){
            public void run() {
                deref(null);
            }
            void deref(Object o) { o.toString(); }
        };
        try {
            javax.swing.SwingUtilities.invokeAndWait(r);
        } catch (java.lang.reflect.InvocationTargetException e) { caught = true;}
        flushAWT();
        Assert.assertTrue("threw exception", caught);
    }
        
	// from here down is testing infrastructure

	public UncaughtExceptionHandlerTest(String s) {
		super(s);
	}

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {UncaughtExceptionHandlerTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

	// test suite from all defined tests
	public static Test suite() {
		TestSuite suite = new TestSuite(UncaughtExceptionHandlerTest.class);
		return suite;
	}

    // The minimal setup for log4J
    protected void setUp() throws Exception { 
        apps.tests.Log4JFixture.setUp(); 
        super.setUp();
    }
    protected void tearDown() throws Exception { 
        super.tearDown();
        apps.tests.Log4JFixture.tearDown(); 
    }

	static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UncaughtExceptionHandlerTest.class.getName());

}
