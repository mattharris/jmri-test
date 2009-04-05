// QsiMonAction.java

package jmri.jmrix.qsi.qsimon;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Swing action to create and register a
 *       			QsiMonFrame object
 *
 * @author			Bob Jacobsen    Copyright (C) 2007
 * @version			$Revision: 1.3 $
 */

public class QsiMonAction 			extends AbstractAction {

	public QsiMonAction(String s) { super(s);}

	public QsiMonAction() { 
	    this(java.util.ResourceBundle.getBundle("jmri.jmrix.JmrixSystemsBundle")
	            .getString("MenuItemCommandMonitor"));
	}
	
    public void actionPerformed(ActionEvent e) {
		// create a QsiMonFrame
		QsiMonFrame f = new QsiMonFrame();
		try {
			f.initComponents();
			}
		catch (Exception ex) {
			log.warn("QsiMonAction starting QsiMonFrame: Exception: "+ex.toString());
			}
		f.setVisible(true);
	}

	static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(QsiMonAction.class.getName());

}


/* @(#)QsiMonAction.java */
