package jmri.jmrit.operations.rollingstock.cars;

import java.beans.PropertyChangeEvent;
import java.util.List;

import jmri.jmrit.operations.locations.Location;
import jmri.jmrit.operations.locations.LocationManager;
import jmri.jmrit.operations.locations.Track;
import jmri.jmrit.operations.trains.Train;
import jmri.jmrit.operations.trains.TrainManager;
import jmri.jmrit.operations.rollingstock.RollingStock;
import jmri.jmrit.operations.routes.RouteLocation;
import jmri.jmrit.operations.setup.Setup;

import org.jdom.Element;

/**
 * Represents a car on the layout
 * 
 * @author Daniel Boudreau
 * @version             $Revision: 1.2 $
 */
public class Car extends RollingStock implements java.beans.PropertyChangeListener{

	protected boolean _hazardous = false;
	protected boolean _caboose = false;
	protected boolean _fred = false;
	protected Kernel _kernel = null;
	
	LocationManager locationManager = LocationManager.instance();
	
	public Car(){
		
	}
	
	public Car(String road, String number) {
		super(road, number);
		log.debug("New car " + road + " " + number);
	}

	public void setHazardous(boolean hazardous){
		boolean old = _hazardous;
		_hazardous = hazardous;
		if (!old == hazardous)
			firePropertyChange("car hazardous", old?"true":"false", hazardous?"true":"false");
	}
	
	public boolean isHazardous(){
		return _hazardous;
	}
	
	public void setFred(boolean fred){
		boolean old = _fred;
		_fred = fred;
		if (!old == fred)
			firePropertyChange("car hazardous", old?"true":"false", fred?"true":"false");
	}
	
	public boolean hasFred(){
		return _fred;
	}
	
	public void setCaboose(boolean caboose){
		boolean old = _caboose;
		_caboose = caboose;
		if (!old == caboose)
			firePropertyChange("car hazardous", old?"true":"false", caboose?"true":"false");
	}
	
	public boolean isCaboose(){
		return _caboose;
	}
	
	/**
	 * A kernel is a group of cars that are switched as
	 * a unit. 
	 * @param kernel
	 */
	public void setKernel(Kernel kernel) {
		if (_kernel == kernel)
			return;
		String old ="";
		if (_kernel != null){
			old = _kernel.getName();
			_kernel.deleteCar(this);
		}
		_kernel = kernel;
		String newName ="";
		if (_kernel != null){
			_kernel.addCar(this);
			newName = _kernel.getName();
		}
		if (!old.equals(newName))
			firePropertyChange("kernel", old, newName);
	}

	public Kernel getKernel() {
		return _kernel;
	}
	
	public String getKernelName() {
		if (_kernel != null)
			return _kernel.getName();
		else
			return "";
	}
	
	public String testDestination(Location destination, Track track) {
		String status = super.testDestination(destination, track);
		if (!status.equals(OKAY))
			return status;
		// now check to see if car is in a kernel and can fit 
		if (getKernel() != null && track != null &&
				track.getUsedLength() + track.getReserved()+ getKernel().getLength() > track.getLength()){
			log.debug("Can't set car (" + getId() + ") at track destination ("+ destination.getName() + ", " + track.getName() + ") no room!");
			return LENGTH;	
		}
		return OKAY;
	}

	/**
	 * Construct this Entry from XML. This member has to remain synchronized
	 * with the detailed DTD in operations-cars.dtd
	 * 
	 * @param e  Car XML element
	 */
	public Car(org.jdom.Element e) {
		super.rollingStock(e);
		org.jdom.Attribute a;
		if ((a = e.getAttribute("hazardous")) != null)
			_hazardous = a.getValue().equals("true");
		if ((a = e.getAttribute("caboose")) != null)
			_caboose = a.getValue().equals("true");
		if ((a = e.getAttribute("fred")) != null)
			_fred = a.getValue().equals("true");
		if ((a = e.getAttribute("kernel")) != null){
			setKernel(CarManager.instance().getKernelByName(a.getValue()));
			if ((a = e.getAttribute("leadKernel")) != null){
				_kernel.setLeadCar(this);
			}
		}
	}
	
	boolean verboseStore = false;

	/**
	 * Create an XML element to represent this Entry. This member has to remain
	 * synchronized with the detailed DTD in operations-cars.dtd.
	 * 
	 * @return Contents in a JDOM Element
	 */
	public org.jdom.Element store() {
		org.jdom.Element e = new org.jdom.Element("car");
		super.store(e);
		if (isHazardous())
			e.setAttribute("hazardous", isHazardous()?"true":"false");
		if (isCaboose())
			e.setAttribute("caboose", isCaboose()?"true":"false");
		if (hasFred())
			e.setAttribute("fred", hasFred()?"true":"false");
		if (getKernel() != null){
			e.setAttribute("kernel", getKernelName());
			if (getKernel().isLeadCar(this))
				e.setAttribute("leadKernel", "true");
		}
		return e;
	}
	
	// car listens for changes in a location name or if a location is deleted
    public void propertyChange(PropertyChangeEvent e) {
    	// if (log.isDebugEnabled()) log.debug("Property change for car: " + getId()+ " property name: " +e.getPropertyName()+ " old: "+e.getOldValue()+ " new: "+e.getNewValue());
    	// notify if track location name changes
    	if (e.getPropertyName().equals("name")){
        	if (log.isDebugEnabled()) log.debug("Property change for car: " + getId()+ " property name: " +e.getPropertyName()+ " old: "+e.getOldValue()+ " new: "+e.getNewValue());
    		firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
    	}
    	if (e.getPropertyName().equals(Location.DISPOSE)){
    	    if (e.getSource() == _location){
        	   	if (log.isDebugEnabled()) log.debug("delete location for car: " + getId());
    	    	setLocation(null, null);
    	    }
    	    if (e.getSource() == _destination){
        	   	if (log.isDebugEnabled()) log.debug("delete destination for car: " + getId());
    	    	setDestination(null, null);
    	    }
     	}
    	if (e.getPropertyName().equals(Track.DISPOSE)){
    	    if (e.getSource() == _trackLocation){
        	   	if (log.isDebugEnabled()) log.debug("delete location for car: " + getId());
    	    	setLocation(_location, null);
    	    }
    	    if (e.getSource() == _trackDestination){
        	   	if (log.isDebugEnabled()) log.debug("delete destination for car: " + getId());
    	    	setDestination(_destination, null);
    	    }
    	    	
    	}
    	if (e.getPropertyName().equals(Train.DISPOSE)){
    		if (e.getSource() == _train){
        	   	if (log.isDebugEnabled()) log.debug("delete train for car: " + getId());
        	   	setTrain(null);
    		}
    	}
    }

	java.beans.PropertyChangeSupport pcs = new java.beans.PropertyChangeSupport(
			this);

	public synchronized void addPropertyChangeListener(
			java.beans.PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public synchronized void removePropertyChangeListener(
			java.beans.PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	protected void firePropertyChange(String p, Object old, Object n) {
		pcs.firePropertyChange(p, old, n);
	}

	static org.apache.log4j.Category log = org.apache.log4j.Category
			.getInstance(Car.class.getName());

}
