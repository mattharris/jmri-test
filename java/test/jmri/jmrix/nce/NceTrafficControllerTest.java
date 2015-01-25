// NceTrafficControllerTest.java

package jmri.jmrix.nce;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

/**
 * JUnit tests for the NceTrafficController class
 * @author			Bob Jacobsen Copyright 2003, 2007
 * @version $Revision$
 */
public class NceTrafficControllerTest extends TestCase {

	public void testCreate() {
		NceTrafficController m = new NceTrafficController();
		Assert.assertNotNull("exists", m );
	}

    /**
     * Test disabled until threading can be resolved
     */
	public void xtestSendAscii() throws Exception {
		NceTrafficController c = new NceTrafficController(){
		    // skip timeout message
		    protected void handleTimeout(jmri.jmrix.AbstractMRMessage msg,jmri.jmrix.AbstractMRListener l) {}
            public void receiveLoop() {}
            protected void portWarn(Exception e) {}
        };

		// connect to iostream via port controller
		NcePortControllerScaffold p = new NcePortControllerScaffold();
		c.connectPort(p);

		// send a message
		NceMessage m = new NceMessage(3);
		m.setBinary(false);
		m.setOpCode('0');
		m.setElement(1, '1');
		m.setElement(2, '2');
		c.sendNceMessage(m, new NceListenerScaffold());
		Assert.assertEquals("total length ", 4, tostream.available());
		Assert.assertEquals("Char 0", '0', tostream.readByte());
		Assert.assertEquals("Char 1", '1', tostream.readByte());
		Assert.assertEquals("Char 2", '2', tostream.readByte());
		Assert.assertEquals("EOM", 0x0d, tostream.readByte());
		Assert.assertEquals("remaining ", 0, tostream.available());
	}

    /**
     * Test disabled until threading can be resolved
     */
	public void xtestSendBinary() throws Exception {
		NceTrafficController c = new NceTrafficController(){
		    // skip timeout message
		    protected void handleTimeout(jmri.jmrix.AbstractMRMessage msg,jmri.jmrix.AbstractMRListener l) {}
            public void receiveLoop() {}
            protected void portWarn(Exception e) {}
        };

		// connect to iostream via port controller
		NcePortControllerScaffold p = new NcePortControllerScaffold();
		c.connectPort(p);

		// send a message
		NceMessage m = new NceMessage(3);
		m.setBinary(true);
		m.setOpCode(0x81);
		m.setElement(1, 0x12);
		m.setElement(2, 0x34);
		c.sendNceMessage(m, new NceListenerScaffold());
		Assert.assertEquals("total length ", 3, tostream.available());
		Assert.assertEquals("Char 0", 0x81, 0xFF & tostream.readByte());
		Assert.assertEquals("Char 1", 0x12, tostream.readByte());
		Assert.assertEquals("Char 2", 0x34, tostream.readByte());
		Assert.assertEquals("remaining ", 0, tostream.available());
	}

    /**
     * Test disabled until threading can be resolved
     */
	public void xtestMonitor() throws Exception {
		NceTrafficController c = new NceTrafficController(){
		    // skip timeout message
		    protected void handleTimeout(jmri.jmrix.AbstractMRMessage msg,jmri.jmrix.AbstractMRListener l) {}
            public void receiveLoop() {}
            protected void portWarn(Exception e) {}
        };

		// connect to iostream via port controller
		NcePortControllerScaffold p = new NcePortControllerScaffold();
		c.connectPort(p);

		// start monitor
		rcvdMsg = null;
		NceListenerScaffold s = new NceListenerScaffold();
		c.addNceListener(s);

		// send a message
		NceMessage m = new NceMessage(3);
		m.setOpCode('0');
		m.setElement(1, '1');
		m.setElement(2, '2');
		c.sendNceMessage(m, new NceListenerScaffold());

		// check it arrived at monitor
		Assert.assertEquals("total length ", 4, tostream.available());
		Assert.assertEquals("Char 0", '0', tostream.readByte());
		Assert.assertEquals("Char 1", '1', tostream.readByte());
		Assert.assertEquals("Char 2", '2', tostream.readByte());
		Assert.assertEquals("EOM", 0x0d, tostream.readByte());
		Assert.assertEquals("remaining ", 0, tostream.available());
	}

    /**
     * Test disabled until threading can be resolved
     */
	public void xtestRcvReply() throws Exception {
		NceTrafficController c = new NceTrafficController(){
		    // skip timeout message
		    protected void handleTimeout(jmri.jmrix.AbstractMRMessage msg,jmri.jmrix.AbstractMRListener l) {}
            public void receiveLoop() {}
            protected void portWarn(Exception e) {}
        };

		// connect to iostream via port controller
		NcePortControllerScaffold p = new NcePortControllerScaffold();
		c.connectPort(p);

		// object to receive reply
		NceListener l = new NceListenerScaffold();
		c.addNceListener(l);

		// send a message
		NceMessage m = new NceMessage(3);
		m.setOpCode('0');
		m.setElement(1, '1');
		m.setElement(2, '2');
		c.sendNceMessage(m, l);
		// that's already tested, so don't do here.

		// now send reply
		tistream.write('R');
		tistream.write(0x0d);
		tistream.write('C');
		tistream.write('O');
		tistream.write('M');
		tistream.write('M');
		tistream.write('A');
		tistream.write('N');
		tistream.write('D');
		tistream.write(':');
		tistream.write(' ');

		// drive the mechanism
		c.handleOneIncomingReply();
		Assert.assertTrue("reply received ", waitForReply());
		Assert.assertEquals("first char of reply ", 'R', rcvdReply.getOpCode());
	}


	private boolean waitForReply() {
		// wait for reply (normally, done by callback; will check that later)
		int i = 0;
		while ( rcvdReply == null && i++ < 100  )  {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		}
		if (log.isDebugEnabled()) log.debug("past loop, i="+i
									+" reply="+rcvdReply);
		return i<100;
	}

	// internal class to simulate a NceListener
	class NceListenerScaffold implements jmri.jmrix.nce.NceListener {
		public NceListenerScaffold() {
			rcvdReply = null;
			rcvdMsg = null;
		}
		public void message(NceMessage m) {rcvdMsg = m;}
		public void reply(NceReply r) {rcvdReply = r;}
	}
	NceReply rcvdReply;
	NceMessage rcvdMsg;

	// internal class to simulate a NcePortController
	class NcePortControllerScaffold extends NcePortController {
            public java.util.Vector<String> getPortNames() { return null; }
	    public String openPort(String portName, String appName) { return null; }
	    public void configure() {}
	    public String[] validBaudRates() { return null; }

            protected NcePortControllerScaffold() throws Exception {
                super(null);
			PipedInputStream tempPipe;
			tempPipe = new PipedInputStream();
			tostream = new DataInputStream(tempPipe);
			ostream = new DataOutputStream(new PipedOutputStream(tempPipe));
			tempPipe = new PipedInputStream();
			istream = new DataInputStream(tempPipe);
			tistream = new DataOutputStream(new PipedOutputStream(tempPipe));
		}

		// returns the InputStream from the port
		public DataInputStream getInputStream() { return istream; }

		// returns the outputStream to the port
		public DataOutputStream getOutputStream() { return ostream; }

		// check that this object is ready to operate
		public boolean status() { return true; }
	}
	static DataOutputStream ostream;  // Traffic controller writes to this
	static DataInputStream  tostream; // so we can read it from this

	static DataOutputStream tistream; // tests write to this
	static DataInputStream  istream;  // so the traffic controller can read from this

	// from here down is testing infrastructure

	public NceTrafficControllerTest(String s) {
		super(s);
	}

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {NceTrafficControllerTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

	// test suite from all defined tests
	public static Test suite() {
		TestSuite suite = new TestSuite(NceTrafficControllerTest.class);
		return suite;
	}

	 static Logger log = Logger.getLogger(NceTrafficControllerTest.class.getName());

}
