// EditorTableDataModel.java

package jmri.jmrix.loconet.soundloader;

import jmri.Manager;
import jmri.NamedBean;
import jmri.util.davidflanagan.HardcopyWriter;
import jmri.util.table.ButtonEditor;
import jmri.util.table.ButtonRenderer;

import jmri.jmrix.loconet.spjfile.SpjFile;

import java.beans.PropertyChangeListener;
import java.io.IOException;

import java.util.ResourceBundle;

import java.awt.Font;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;


/**
 * Table data model for display of Digitrax SPJ files
 * @author		Bob Jacobsen   Copyright (C) 2003, 2006
 * @author      Dennis Miller   Copyright (C) 2006
 * @version		$Revision: 1.2 $
 */
public class EditorTableDataModel extends javax.swing.table.AbstractTableModel {

    static public final int HEADERCOL = 0;
    static public final int TYPECOL = 1;
    static public final int MAPCOL = 2;
    static public final int HANDLECOL = 3;
    static public final int FILENAMECOL  = 4;
    static public final int LENGTHCOL = 5;
    static public final int PLAYBUTTONCOL = 6;
    static public final int REPLACEBUTTONCOL = 7;

    static public final int NUMCOLUMN = 8;

    static ResourceBundle res;

    SpjFile file;
    
    public EditorTableDataModel(SpjFile file) {
        super();
        res = ResourceBundle.getBundle("jmri.jmrix.loconet.soundloader.Editor");
        this.file = file;
    }

    public int getRowCount() {
        // The 0th header is not displayed
        return file.numHeaders()-1;
    }

    public int getColumnCount( ){ return NUMCOLUMN;}

    public String getColumnName(int col) {
        switch (col) {
        case HEADERCOL: return res.getString("HeaderHEADERCOL");
        case TYPECOL: return res.getString("HeaderTYPECOL");
        case HANDLECOL: return res.getString("HeaderHANDLECOL");
        case MAPCOL: return res.getString("HeaderMAPCOL");
        case FILENAMECOL: return res.getString("HeaderFILENAMECOL");
        case LENGTHCOL: return res.getString("HeaderLENGTHCOL");
        case PLAYBUTTONCOL: return res.getString("HeaderPLAYBUTTONCOL");
        case REPLACEBUTTONCOL: return res.getString("HeaderREPLACEBUTTONCOL");

        default: return "unknown";
        }
    }

    public Class getColumnClass(int col) {
        switch (col) {
        case HEADERCOL:
        case HANDLECOL:
            return Integer.class;
        case LENGTHCOL:
            return Float.class;
        case MAPCOL:
        case TYPECOL:
        case FILENAMECOL:
            return String.class;
        case REPLACEBUTTONCOL:
        case PLAYBUTTONCOL:
            return JButton.class;
        default:
            return null;
        }
    }

    public boolean isCellEditable(int row, int col) {
        switch (col) {
        case REPLACEBUTTONCOL:
        case PLAYBUTTONCOL:
            return true;
        default:
            return false;
        }
    }

    public Object getValueAt(int row, int col) {
        switch (col) {
        case HEADERCOL:
            return new Integer(row);
        case HANDLECOL:
            return new Integer(file.getHeader(row+1).getHandle());
        case MAPCOL: 
            return file.getMapEntry(file.getHeader(row+1).getHandle());
        case FILENAMECOL: 
            return ""+file.getHeader(row+1).getName();
        case TYPECOL: 
            return file.getHeader(row+1).typeAsString();
        case LENGTHCOL: 
            if (!file.getHeader(row+1).isWAV()) return null;
            float rate = (new jmri.jmrit.sound.WavBuffer(file.getHeader(row+1).getByteArray())).getSampleRate();
            if (rate == 0.f) {
                log.error("Rate should not be zero");
                return null;
            }
            float time = file.getHeader(row+1).getDataLength()/rate;
            return new Float(time);
        case PLAYBUTTONCOL:
            if (file.getHeader(row+1).isWAV())
                return res.getString("ButtonPlay");
            else if (file.getHeader(row+1).isTxt())
                return res.getString("ButtonView");
            else return null;
        case REPLACEBUTTONCOL:
            return res.getString("ButtonReplace");
        default:
            log.error("internal state inconsistent with table requst for "+row+" "+col);
            return null;
        }
    };

    public int getPreferredWidth(int col) {
        JTextField b;
        switch (col) {
        case TYPECOL: 
            return new JTextField(8).getPreferredSize().width;
        case MAPCOL: 
            return new JTextField(12).getPreferredSize().width;
        case HEADERCOL:
        case HANDLECOL:
            return new JTextField(3).getPreferredSize().width;
        case FILENAMECOL: 
            return new JTextField(12).getPreferredSize().width;
        case LENGTHCOL :
            return new JTextField(5).getPreferredSize().width;
        case PLAYBUTTONCOL: 
            b = new JTextField((String)getValueAt(1, PLAYBUTTONCOL));
            return b.getPreferredSize().width+30;
        case REPLACEBUTTONCOL: 
            b = new JTextField((String)getValueAt(1, REPLACEBUTTONCOL));
            return b.getPreferredSize().width+30;
        default:
        	log.warn("Unexpected column in getPreferredWidth: "+col);
            return new JTextField(8).getPreferredSize().width;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        if (col==PLAYBUTTONCOL) { 
            // button fired, handle
            if (file.getHeader(row+1).isWAV()) {
                playButtonPressed(value, row, col);
                return;
            } else if (file.getHeader(row+1).isTxt()) {
                viewButtonPressed(value, row, col);
                return;
            } else return;            
        }
    }

    // should probably be abstract and put in invoking GUI
    void playButtonPressed(Object value, int row, int col) {
        new jmri.jmrit.sound.WavBuffer(file.getHeader(row+1).getByteArray());
        jmri.jmrit.sound.SoundUtil.playSoundBuffer(file.getHeader(row+1).getByteArray());
    }

    // should probably be abstract and put in invoking GUI
    void viewButtonPressed(Object value, int row, int col) {
        String content = new String(file.getHeader(row+1).getByteArray());
        JFrame frame = new JFrame();
        JTextArea text = new JTextArea(content);
        text.setEditable(false);
        text.setFont(new Font("Monospaced", Font.PLAIN, text.getFont().getSize()));
        frame.getContentPane().add(new JScrollPane(text));
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Configure a table to have our standard rows and columns.
     * This is optional, in that other table formats can use this table model.
     * But we put it here to help keep it consistent.
     * @param table
     */
    public void configureTable(JTable table) {
        // allow reordering of the columns
        table.getTableHeader().setReorderingAllowed(true);

        // have to shut off autoResizeMode to get horizontal scroll to work (JavaSwing p 541)
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // resize columns as requested
        for (int i=0; i<table.getColumnCount(); i++) {
            int width = getPreferredWidth(i);
            table.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        table.sizeColumnsToFit(-1);

        // have the value column hold a button
        setColumnToHoldButton(table, PLAYBUTTONCOL, configureButton(PLAYBUTTONCOL));
        setColumnToHoldButton(table, REPLACEBUTTONCOL, configureButton(REPLACEBUTTONCOL));
    }

    public JButton configureButton(int col) {
        JButton b = new JButton((String)getValueAt(1, col));
        b.validate();
        return b;
    }

    /**
     * Service method to setup a column so that it will hold a
     * button for it's values
     * @param table
     * @param column
     * @param sample Typical button, used for size
     */
    void setColumnToHoldButton(JTable table, int column, JButton sample) {
        TableColumnModel tcm = table.getColumnModel();
        // install a button renderer & editor
        ButtonRenderer buttonRenderer = new ButtonRenderer();
		table.setDefaultRenderer(JButton.class,buttonRenderer);
        TableCellEditor buttonEditor = new ButtonEditor(new JButton());
		table.setDefaultEditor(JButton.class,buttonEditor);
        // ensure the table rows, columns have enough room for buttons
        table.setRowHeight(sample.getPreferredSize().height);
        // table.getColumnModel().getColumn(column)
		//	.setPreferredWidth(sample.getPreferredSize().width);
    }

    synchronized public void dispose() {
    }
    
    /**
     * Method to self print or print preview the table.
     * Printed in equally sized columns across the page with headings and
     * vertical lines between each column. Data is word wrapped within a column.
     * Can handle data as strings, comboboxes or booleans
     */
    public void printTable(HardcopyWriter w) {
        // determine the column size - evenly sized, with space between for lines
        int columnSize = (w.getCharactersPerLine()- this.getColumnCount() - 1)/this.getColumnCount();
        
        // Draw horizontal dividing line
        w.write(w.getCurrentLineNumber(), 0, w.getCurrentLineNumber(),
              (columnSize+1)*this.getColumnCount());
        
        // print the column header labels
        String[] columnStrings = new String[this.getColumnCount()];
        // Put each column header in the array
        for (int i = 0; i < this.getColumnCount(); i++){
            columnStrings[i] = this.getColumnName(i);
        }
        w.setFontStyle(Font.BOLD);
        printColumns(w, columnStrings, columnSize);
        w.setFontStyle(0);
        w.write(w.getCurrentLineNumber(), 0, w.getCurrentLineNumber(),
                (columnSize+1)*this.getColumnCount());
  
        // now print each row of data
        // create a base string the width of the column
        String spaces = "";
        for (int i = 0; i < columnSize; i++) {
            spaces = spaces + " ";
        }
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                //check for special, non string contents
                if (this.getValueAt(i, j) == null) {
                    columnStrings[j] = spaces;
                } else if (this.getValueAt(i, j)instanceof JComboBox){
                        columnStrings[j] = (String)((JComboBox) this.getValueAt(i, j)).getSelectedItem();
                    } else if (this.getValueAt(i, j)instanceof Boolean){
                            columnStrings[j] = ( this.getValueAt(i, j)).toString();
                        }else columnStrings[j] = (String) this.getValueAt(i, j);
            }
        printColumns(w, columnStrings, columnSize);
        w.write(w.getCurrentLineNumber(), 0, w.getCurrentLineNumber(),
                (columnSize+1)*this.getColumnCount());
        }            
        w.close();
    }
    
    protected void printColumns(HardcopyWriter w, String columnStrings[], int columnSize) {
        String columnString = "";
        String lineString = "";
        // create a base string the width of the column
        String spaces = "";
        for (int i = 0; i < columnSize; i++) {
            spaces = spaces + " ";
        }
        // loop through each column
        boolean complete = false;
        while (!complete){
            complete = true;
            for (int i = 0; i < columnStrings.length; i++) {
                // if the column string is too wide cut it at word boundary (valid delimiters are space, - and _)
                // use the intial part of the text,pad it with spaces and place the remainder back in the array
                // for further processing on next line
                // if column string isn't too wide, pad it to column width with spaces if needed
                if (columnStrings[i].length() > columnSize) {
                    boolean noWord = true;
                    for (int k = columnSize; k >= 1 ; k--) {
                        if (columnStrings[i].substring(k-1,k).equals(" ") 
                            || columnStrings[i].substring(k-1,k).equals("-")
                            || columnStrings[i].substring(k-1,k).equals("_")) {
                            columnString = columnStrings[i].substring(0,k) 
                                + spaces.substring(columnStrings[i].substring(0,k).length());
                            columnStrings[i] = columnStrings[i].substring(k);
                            noWord = false;
                            complete = false;
                            break;
                        }
                    }
                    if (noWord) {
                        columnString = columnStrings[i].substring(0,columnSize);
                        columnStrings[i] = columnStrings[i].substring(columnSize);
                        complete = false;
                    }
                    
                } else {
                    columnString = columnStrings[i] + spaces.substring(columnStrings[i].length());
                    columnStrings[i] = "";
                }
                lineString = lineString + columnString + " ";
            }
            try {
                w.write(lineString);
                //write vertical dividing lines
                for (int i = 0; i < w.getCharactersPerLine(); i = i+columnSize+1) {
                    w.write(w.getCurrentLineNumber(), i, w.getCurrentLineNumber() + 1, i);
                }
                lineString = "\n";
                w.write(lineString);
                lineString = "";
            } catch (IOException e) { log.warn("error during printing: "+e);}
        }
    }

    static final org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(EditorTableDataModel.class.getName());

}