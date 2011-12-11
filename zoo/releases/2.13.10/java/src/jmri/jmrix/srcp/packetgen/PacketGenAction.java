// PacketGenAction.java

package jmri.jmrix.srcp.packetgen;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Swing action to create and register a
 *       			PacketGenFrame object
 * 
 * @author Bob Jacobsen    Copyright (C) 2008
 * @version $Revision$
 */
public class PacketGenAction 			extends AbstractAction {

	public PacketGenAction(String s) { super(s);}

    public PacketGenAction() {
        this("Generate SRCP message");
    }

    public void actionPerformed(ActionEvent e) {
		PacketGenFrame f = new PacketGenFrame();
		try {
			f.initComponents();
			}
		catch (Exception ex) {
			log.error("Exception: "+ex.toString());
			}
		f.setVisible(true);
	}
   static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PacketGenAction.class.getName());
}


/* @(#)PacketGenAction.java */
