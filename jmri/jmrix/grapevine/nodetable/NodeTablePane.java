// NodeTableFrame.java

package jmri.jmrix.grapevine.nodetable;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.Border;

import jmri.jmrix.grapevine.SerialTrafficController;
import jmri.jmrix.grapevine.SerialNode;
import jmri.jmrix.grapevine.SerialReply;
import jmri.jmrix.grapevine.SerialMessage;

import jmri.jmrix.grapevine.nodeconfig.NodeConfigFrame;

import jmri.util.table.ButtonEditor;
import jmri.util.table.ButtonRenderer;

/**
 * Pane for user management of serial nodes. Contains a table that 
 * does the real work.
 * <p>
 * Nodes can be in three states:
 * <OL>
 * <LI>Configured
 * <LI>Present, not configured
 * <LI>Not present
 * </OL>
 
 * @author	Bob Jacobsen   Copyright (C) 2004, 2007
 * @author	Dave Duchamp   Copyright (C) 2004, 2006
 * @version	$Revision: 1.2 $
 */
public class NodeTablePane extends javax.swing.JPanel implements jmri.jmrix.grapevine.SerialListener {

    ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrix.grapevine.nodetable.NodeTableBundle");
		
    /**
     * Constructor method
     */
    public NodeTablePane() {
    	super();
    }

    NodesModel nodesModel = null;
    JLabel status;
    
    /** 
     *  Initialize the window
     */
    public void initComponents() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        nodesModel = new NodesModel();

        JTable nodesTable = jmri.util.JTableUtil.sortableDataModel(nodesModel);

        // install a button renderer & editor
        ButtonRenderer buttonRenderer = new ButtonRenderer();
                nodesTable.setDefaultRenderer(JButton.class,buttonRenderer);
        TableCellEditor buttonEditor = new ButtonEditor(new JButton());
                nodesTable.setDefaultEditor(JButton.class,buttonEditor);
        
        try {
            jmri.util.com.sun.TableSorter tmodel = ((jmri.util.com.sun.TableSorter)nodesTable.getModel());
            tmodel.setSortingStatus(nodesModel.STATUSCOL, jmri.util.com.sun.TableSorter.DESCENDING);
        } catch (ClassCastException e3) {}  // if not a sortable table model
        nodesTable.setRowSelectionAllowed(false);
        nodesTable.setPreferredScrollableViewportSize(new java.awt.Dimension(580,80));

        JScrollPane scrollPane = new JScrollPane(nodesTable);
        add(scrollPane);
        
        // status info on bottom
        JPanel p = new JPanel() {
            public Dimension getMaximumSize() { 
                int height = getPreferredSize().height;
                int width = super.getMaximumSize().width;
                return new Dimension(width, height); }
        };
        p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
        JButton b = new JButton(rb.getString("ButtonCheck"));
        b.setToolTipText(rb.getString("TipCheck"));
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                startPoll();
            }
        });
        p.add(b);
        status = new JLabel("");
        p.add(status);
        
        p.add(Box.createHorizontalGlue());
        
        // renumber button
        b = new JButton(rb.getString("ButtonRenumber"));
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                renumber();
            }
        });
        p.add(b);

        add(p);
        // start the search for nodes
        startPoll();
    }
    
    /**
     * Open a renumber frame
     */
    void renumber() {
        RenumberFrame f = new RenumberFrame();
        f.initComponents();
        f.setVisible(true);
    }
    
    javax.swing.Timer timer;
    /**
     * Start the check of the actual hardware
     */
    public void startPoll() {
        // mark as none seen
        for (int i = 0; i<128; i++) scanSeen[i] = false;

        status.setText(rb.getString("StatusStart"));
        
        // create a timer to send messages
        timer = new javax.swing.Timer(50, new java.awt.event.ActionListener() {
                int node = 1;
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // send message to node
                    SerialTrafficController.instance().sendSerialMessage(SerialMessage.getPoll(node), null);
                    // done?
                    node++;
                    if (node>=128) {
                        // yes, stop
                        timer.stop();
                        timer = null;
                        // if nothing seen yet, this is a failure
                        if (status.getText().equals(rb.getString("StatusStart"))) 
                            status.setText(rb.getString("StatusFail"));
                        else
                            status.setText(rb.getString("StatusOK"));
                    }
                }
            });
        timer.setInitialDelay(50);
        timer.setRepeats(true);
        timer.start();
               
        // redisplay the table
        nodesModel.fireTableDataChanged();
    }

    // indicate whether node has been seen
    boolean scanSeen[] = new boolean[128];
    
    /**
     * Ignore messages being sent
     */
    public void message(SerialMessage m) {}
    
    /**
     * Listen for software version messages to know
     * a node is present
     */
    public void reply(SerialReply m) {
        // set the status as having seen something
        if (status.getText().equals(rb.getString("StatusStart"))) 
            status.setText(rb.getString("StatusRunning"));
        // is this a software version reply?
        if (m.getNumDataElements() != 2) return;
        // yes, extract node number
        int num = m.getElement(0)&0x7F;
        // mark as seen
        scanSeen[num] = true;
        // force redisplay of that line
        nodesModel.fireTableRowsUpdated(num, num);
    }

    /**
     * Set up table for selecting showing nodes.
     *<ol>
     *<li>Address
     *<li>Present Y/N
     *<li>Edit button
     *</ol>
     */
    public class NodesModel extends AbstractTableModel {
        static private final int ADDRCOL = 0;
        static private final int STATUSCOL = 1;
        static private final int EDITCOL = 2;
        
        static private final int LAST = 2;
        
        public int getColumnCount () {return LAST+1;}

        public int getRowCount () {
            return 127;
        }

        public String getColumnName(int c) {
            switch (c) {
            case ADDRCOL:
                return rb.getString("TitleAddress");
            case STATUSCOL:
                return rb.getString("TitleStatus");
            case EDITCOL:
                return rb.getString("TitleConfigure");
            default:
                return "";
            }
        }

        public Class getColumnClass(int c) {
            if (c == EDITCOL)
                return JButton.class;
            else if (c == ADDRCOL)
                return Integer.class;
            else 
                return String.class;
        }

        public boolean isCellEditable(int r,int c) {
            return (c==EDITCOL);
        }

        public Object getValueAt (int r,int c) {
            // r is row number, from 1, and also therefore node number
            switch (c) {
            case ADDRCOL:
                return new Integer(r);
            case STATUSCOL:
                // see if node exists
                if (SerialTrafficController.instance().getNodeFromAddress(r)!=null)
                    return rb.getString("StatusConfig");
                else {
                    // see if seen in scan
                    if (scanSeen[r])
                        return rb.getString("StatusPresent");
                    else
                        return rb.getString("StatusAbsent");
                }
            case EDITCOL:
                // see if node exists
                if (SerialTrafficController.instance().getNodeFromAddress(r)!=null)
                    return rb.getString("ButtonEdit");
                else
                    return rb.getString("ButtonAdd");
            default:
                return null;
            }
        }

        public void setValueAt(Object type,int r,int c) {
            switch (c) {
            case EDITCOL:
                NodeConfigFrame f = new NodeConfigFrame();
                f.initComponents();
                f.setNodeAddress(r+1);
                f.setVisible(true);
                return;
            default:
                return;
            }
        }
    }

    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(NodeTableFrame.class.getName());

}
