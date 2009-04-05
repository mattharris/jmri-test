package jmri.jmrix.secsi.configurexml;

import org.jdom.Element;
import jmri.jmrix.secsi.*;

/**
 * Provides load and store functionality for
 * configuring SerialSensorManagers.
 * <P>
 * Uses the store method from the abstract base class, but
 * provides a load method here.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2003, 2006, 2007, 2008
 * @version $Revision: 1.2 $
 */
public class SerialSensorManagerXml extends jmri.configurexml.AbstractSensorManagerConfigXML {

    public SerialSensorManagerXml() {
        super();
    }

    public void setStoreElementClass(Element sensors) {
        sensors.setAttribute("class","jmri.jmrix.secsi.configurexml.SerialSensorManagerXml");
    }

    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    public void load(Element sensors) {
        // create the master object
        SerialSensorManager mgr = SerialSensorManager.instance();
        // load individual sensors
        loadSensors(sensors);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SerialSensorManagerXml.class.getName());
}