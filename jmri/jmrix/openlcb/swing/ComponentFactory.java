// ComponentFactory.java

package jmri.jmrix.openlcb.swing;

import jmri.jmrix.openlcb.SystemConnectionMemo;
import jmri.jmrix.openlcb.OpenLcbMenu;

/**
 * Provide access to Swing components for the LocoNet subsystem.
 *
 * @author		Bob Jacobsen  Copyright (C) 2010
 * @version             $Revision: 1.1 $
 * @since 2.9.4
 */
public class ComponentFactory extends jmri.jmrix.swing.ComponentFactory {

    public ComponentFactory(SystemConnectionMemo memo) {
        this.memo = memo;
    }
    
    SystemConnectionMemo memo;
    
    /**
     * Provide a menu with all items attached to this system connection
     */
    public javax.swing.JMenu getMenu() {
        return new OpenLcbMenu(memo);
    }
}


/* @(#)ComponentFactory.java */
