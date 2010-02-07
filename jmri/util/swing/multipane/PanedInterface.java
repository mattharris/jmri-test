// PanedInterface.java

package jmri.util.swing.multipane;

import javax.swing.*;
import java.util.*;

import jmri.util.JmriJFrame;
import jmri.util.swing.*;

/**
 * Display a JComponent in a specific paned window.
 *
 * @author Bob Jacobsen  Copyright 2010
 * @since 2.9.4
 * @version $Revision: 1.1 $
 */

public class PanedInterface implements jmri.util.swing.WindowInterface {

    public PanedInterface(MultiPaneWindow frame) {
        this.frame = frame;
    }

    MultiPaneWindow frame;
    
    public void show(jmri.util.swing.JmriPanel child, 
                        JmriAbstractAction act,
                        Hint hint) {

        JComponent destination;
        if (hint==Hint.EXTEND) 
            destination = frame.getLowerRight();
        else
            destination = frame.getUpperRight();
        
        destination.removeAll();
        destination.add(child);
        destination.revalidate();
        frame.resetRightToPreferredSizes();
        
        actions.add(act);
    }

    public void show(final jmri.util.swing.JmriPanel child, 
                        jmri.util.swing.JmriAbstractAction act) {
            
                show(child, act, Hint.DEFAULT);
            }

    HashSet<JmriAbstractAction> actions = new HashSet<JmriAbstractAction>();
    
    /**
     * Return the same instance for multiple requests
     */
    public boolean multipleInstances() { return false; }
    
    /**
     * Dispose when associated window is complete
     */
    public void dispose(){
        for (JmriAbstractAction a : actions) 
            a.dispose();
    }
}