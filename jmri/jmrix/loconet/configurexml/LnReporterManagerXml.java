package jmri.jmrix.loconet.configurexml;

import org.jdom.Element;

/**
 * Provides load and store functionality for
 * configuring LnReporterManagers.
 * <P>
 * Uses the store method from the abstract base class, but
 * provides a load method here.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2002
 * @version $Revision: 1.8 $
 */
public class LnReporterManagerXml extends jmri.managers.configurexml.AbstractReporterManagerConfigXML {

    public LnReporterManagerXml() {
        super();
    }

    public void setStoreElementClass(Element reporters) {
        reporters.setAttribute("class","jmri.jmrix.loconet.configurexml.LnReporterManagerXml");
    }

    public void load(Element element, Object o) {
        log.error("Invalid method called");
    }

    public boolean load(Element reporters) {

        // load individual Reporters
        return loadReporters(reporters);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LnReporterManagerXml.class.getName());

}