// pricom.pockettester.DataSourceAction.java

package jmri.jmrix.pricom.pockettester;

/**
 * JmriJFrameAction to create and register a
 * pricom.pockettester.DataSource object
 *
 * @see jmri.jmrix.pricom.pockettester.DataSource
 *
 * @author			Bob Jacobsen    Copyright (C) 2002,2005
 * @version			$Revision: 1.4 $
 */
public class DataSourceAction extends jmri.util.JmriJFrameAction  {

    public DataSourceAction(String s) { super(s);}        
    public DataSourceAction() {
        super(""); // have to invoke a ctor that exists
        java.util.ResourceBundle rb 
            = java.util.ResourceBundle.getBundle("jmri.jmrix.pricom.pockettester.TesterBundle");
        putValue(javax.swing.Action.NAME, rb.getString("ActionSource"));
    }

     /**
      * Method to be overridden to make this work.
      * Provide a completely qualified class name,
      * must be castable to JmriJFrame
      */
    public String getName() { return "jmri.jmrix.pricom.pockettester.DataSource"; }

    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(DataSourceAction.class.getName());

}

/* @(#)DataSourceAction.java */
