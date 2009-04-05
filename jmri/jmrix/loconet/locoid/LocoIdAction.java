package jmri.jmrix.loconet.locoid;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * LocoIdAction.java
 *
 * Swing action to create and register a
 *       			LocoidFrame object
 *
 * @author			Bob Jacobsen    Copyright (C) 2006
 * @version         $Revision: 1.4 $
 */
public class LocoIdAction 			extends AbstractAction {

    public LocoIdAction(String s) { super(s);}

    public LocoIdAction() { this("Send LocoNet message");}

    public void actionPerformed(ActionEvent e) {
        // create a LocoIdFrame
        LocoIdFrame f = new LocoIdFrame();
        try {
            f.initComponents();
        }
        catch (Exception ex) {
            log.error("Exception: "+ex.toString());
        }
        f.setVisible(true);
        
    }
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LocoIdAction.class.getName());
}


/* @(#)LocoidAction.java */
