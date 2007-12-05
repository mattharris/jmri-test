// SimpleProgFrame.java

package jmri.jmrit.simpleprog;

import java.awt.*;

import javax.swing.*;

import jmri.Programmer;
import jmri.ProgListener;

/**
 * Frame providing a simple command station programmer
 *
 * @author	Bob Jacobsen   Copyright (C) 2001, 2007
 * @author  Giorgio Terdina Copyright (C) 2007
 * @version			$Revision: 1.12 $
 */
public class SimpleProgFrame extends jmri.util.JmriJFrame implements jmri.ProgListener {

    // GUI member declarations
    javax.swing.JToggleButton readButton 	= new javax.swing.JToggleButton();
    javax.swing.JToggleButton writeButton 	= new javax.swing.JToggleButton();
    javax.swing.JTextField  addrField       = new javax.swing.JTextField(4);
    javax.swing.JTextField  valField        = new javax.swing.JTextField(4);

    jmri.ProgModePane       modePane        = new jmri.ProgModePane(BoxLayout.Y_AXIS);

    javax.swing.ButtonGroup radixGroup 		= new javax.swing.ButtonGroup();
    javax.swing.JRadioButton hexButton    	= new javax.swing.JRadioButton();
    javax.swing.JRadioButton decButton   	= new javax.swing.JRadioButton();

    javax.swing.JLabel       resultsField   = new javax.swing.JLabel(" ");

    public SimpleProgFrame() {

        // configure items for GUI
        readButton.setText("Read CV");
        readButton.setToolTipText("Read the value from the selected CV");

        writeButton.setText("Write CV");
        writeButton.setToolTipText("Write the value to the selected CV");

        hexButton.setText("Hexadecimal");
        decButton.setText("Decimal");
        decButton.setSelected(true);

        // add the actions to the buttons
        readButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                readPushed(e);
            }
        });
        writeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                writePushed(e);
            }
        });
        decButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                decHexButtonChanged(e);
            }
        });
        hexButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                decHexButtonChanged(e);
            }
        });
        

        resultsField.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // general GUI config
        setTitle("Simple Programmer");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // install items in GUI
        javax.swing.JPanel tPane;  // temporary pane for layout
        javax.swing.JPanel tPane2;

        tPane = new JPanel();
        tPane.setLayout(new BoxLayout(tPane, BoxLayout.X_AXIS));
        tPane.add(readButton);
        tPane.add(writeButton);
        getContentPane().add(tPane);

        tPane = new JPanel();
        tPane.setLayout(new GridLayout(2,2));
        tPane.add(new JLabel("CV Number:"));
        tPane.add(addrField);
        tPane.add(new JLabel("Value:"));
        tPane.add(valField);
        getContentPane().add(tPane);

        getContentPane().add(new JSeparator());

        tPane = new JPanel();
        tPane.setLayout(new BoxLayout(tPane, BoxLayout.X_AXIS));

        tPane.add(modePane);

        tPane.add(new JSeparator(javax.swing.SwingConstants.VERTICAL));

        tPane2 = new JPanel();
        tPane2.setLayout(new BoxLayout(tPane2, BoxLayout.Y_AXIS));
        radixGroup.add(decButton);
        radixGroup.add(hexButton);
        tPane2.add(new JLabel("Value is:"));
        tPane2.add(decButton);
        tPane2.add(hexButton);
        tPane.add(tPane2);

        getContentPane().add(tPane);

        getContentPane().add(new JSeparator());

        getContentPane().add(resultsField);

       if (modePane.getProgrammer()== null){
    	   readButton.setEnabled (false);
    	   // boudreau let system Programmer determine default mode
//         modePane.setDefaultMode();
       }
		// disable read button if non-functional
        if (modePane.getProgrammer()!= null)
			readButton.setEnabled(modePane.getProgrammer().getCanRead());

        pack();
    }

    // utility function to get value, handling radix
    private int getNewVal() {
        try {
            if (decButton.isSelected())
                return Integer.valueOf(valField.getText()).intValue();
            else
                return Integer.valueOf(valField.getText(),16).intValue();
        } catch (java.lang.NumberFormatException e) {
            valField.setText("");
            return 0;
        }
    }
    private int getNewAddr() {
        try {
            return Integer.valueOf(addrField.getText()).intValue();
        } catch (java.lang.NumberFormatException e) {
            addrField.setText("");
            return 0;
        }
    }

    private String statusCode(int status) {
        Programmer p = modePane.getProgrammer();
        if (status == ProgListener.OK) return "OK";
        if (p == null) {
            return "No programmer connected";
        } else {
            return p.decodeErrorCode(status);
        }
    }

    // listen for messages from the Programmer object
    public void programmingOpReply(int value, int status) {
        resultsField.setText(statusCode(status));

        //operation over, raise the buttons
        readButton.setSelected(false);
        writeButton.setSelected(false);

        // capture the read value
        if (value !=-1)  // -1 implies nothing being returned
            if (decButton.isSelected())
                valField.setText(""+value);
            else
                valField.setText(Integer.toHexString(value));
    }

    // handle the buttons being pushed
    public void readPushed(java.awt.event.ActionEvent e) {
        Programmer p = modePane.getProgrammer();
        if (p == null) {
            resultsField.setText("No programmer connected");
            readButton.setSelected(false);
        } else {
        	if (p.getCanRead()) {
				try {
					resultsField.setText("programming...");
					p.readCV(getNewAddr(), this);
				} catch (jmri.ProgrammerException ex) {
					resultsField.setText("" + ex);
					readButton.setSelected(false);
				}
			}else{
				resultsField.setText("can't read in this Mode");
				readButton.setSelected(false);
			}
        }
    }

    public void writePushed(java.awt.event.ActionEvent e) {
        Programmer p = modePane.getProgrammer();
        if (p == null) {
            resultsField.setText("No programmer connected");
            writeButton.setSelected(false);
        } else {
            try {
                resultsField.setText("programming...");
                p.writeCV(getNewAddr(),getNewVal(), this);
            } catch (jmri.ProgrammerException ex) {
                resultsField.setText(""+ex);
                writeButton.setSelected(false);
            }
        }
    }
    
    // provide simple data conversion if dec or hex button changed
    public void decHexButtonChanged(java.awt.event.ActionEvent e) {
    	resultsField.setText("OK");
    	if (valField.getText().equals(""))
			return;
    	int value = 0;
		try {
			if (decButton.isSelected())
				// convert from hex to dec
				value = Integer.valueOf(valField.getText(), 16).intValue();
			else
				// convert from dec to hex
				value = Integer.valueOf(valField.getText()).intValue();
		} catch (java.lang.NumberFormatException ee) {
			resultsField.setText("error");
		}
		if (value != 0) {
			if (decButton.isSelected())
				valField.setText(Integer.toString(value));
			else
				valField.setText(Integer.toHexString(value));
		}
	}

    public void dispose() {
        modePane.dispose();
        super.dispose();
    }

}
