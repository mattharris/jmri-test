// CarsSetFrameAction.java
package jmri.jmrit.operations.rollingstock.cars;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;

/**
 * Swing action to create and register a CarsSetFrame object.
 *
 * @author Bob Jacobsen Copyright (C) 2001
 * @author Daniel Boudreau Copyright (C) 2010
 * @version $Revision$
 */
public class CarsSetFrameAction extends AbstractAction {

    /**
     *
     */
    private static final long serialVersionUID = -3263490494828511284L;
    CarsTableModel _carsTableModel;
    JTable _carsTable;

    public CarsSetFrameAction(String s) {
        super(s);
    }

    public CarsSetFrameAction(JTable carsTable) {
        this(Bundle.getMessage("TitleSetCars"));
        _carsTable = carsTable;
    }

    public void actionPerformed(ActionEvent e) {
        // create a car table frame
        CarsSetFrame csf = new CarsSetFrame();
        csf.initComponents(_carsTable);
    }
}

/* @(#)CarsSetFrameAction.java */
