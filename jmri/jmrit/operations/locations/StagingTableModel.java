// StagingTableModel.java

package jmri.jmrit.operations.locations;

import java.beans.*;
import javax.swing.*;
import jmri.jmrit.operations.setup.Control;

/**
 * Table Model for edit of staging tracks used by operations
 *
 * @author Daniel Boudreau Copyright (C) 2008
 * @version   $Revision: 1.12 $
 */
public class StagingTableModel extends TrackTableModel {

	public StagingTableModel() {
		super();
	}

	public void initTable(JTable table, Location location) {
		super.initTable(table, location, Track.STAGING);
	}

	public String getColumnName(int col) {
		switch (col) {
		case NAMECOLUMN: return rb.getString("StagingName");
		}
		return super.getColumnName(col);
	}

	protected void editTrack (int row){
		log.debug("Edit staging");
		if (tef != null){
			tef.dispose();
		}
		tef = new StagingEditFrame();
		String stagingId = tracksList.get(row);
		Track staging = _location.getTrackById(stagingId);
		tef.initComponents(_location, staging);
		tef.setTitle(rb.getString("EditStaging"));
		focusEditFrame = true;
	}

    // this table listens for changes to a location and it's stagings
    public void propertyChange(PropertyChangeEvent e) {
    	if (Control.showProperty && log.isDebugEnabled()) 
    		log.debug("Property change " +e.getPropertyName()+ " old: "+e.getOldValue()+ " new: "+e.getNewValue());
    	if (e.getPropertyName().equals(Location.TRACK_LISTLENGTH_CHANGED_PROPERTY)) {
    		updateList();
    		fireTableDataChanged();
    	}

    	if (e.getSource().getClass().equals(Track.class)){
    		String type = ((Track) e.getSource()).getLocType();
    		if (type.equals(Track.STAGING)){
    			String stagingId = ((Track) e.getSource()).getId();
    			int row = tracksList.indexOf(stagingId);
    			if (Control.showProperty && log.isDebugEnabled()) 
    				log.debug("Update staging table row: "+ row + " id: " + stagingId);
    			if (row >= 0)
    				fireTableRowsUpdated(row, row);
    		}
    	}
    }
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StagingTableModel.class.getName());
}

