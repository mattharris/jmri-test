// AbstractConfigFile.java

package apps;

import com.sun.java.util.collections.List;
import java.io.*;
import jmri.jmrit.XmlFile;
import org.jdom.*;
import org.jdom.output.*;

// try to limit the JDOM to this class, so that others can manipulate...

/**
 * Abstract base class to represent and manipulate the preferences information for an
 * application. Works with the AbstractConfigFrame
 *
 * @author			Bob Jacobsen   Copyright (C) 2001
 * @version		 	$Revision: 1.7 $
 * @see apps.AbstractConfigFrame
 */
abstract public class AbstractConfigFile extends XmlFile {

    public void readFile(String name) throws java.io.FileNotFoundException, org.jdom.JDOMException {
        Element root = rootFromName(name);
        _connection = root.getChild("connection");
        _gui = root.getChild("gui");
        _programmer = root.getChild("programmer");
    }

    // access to the three elements
    public Element getConnectionElement() {
        return _connection;
    }

    public Element getGuiElement() {
        return _gui;
    }

    public Element getProgrammerElement() {
        return _programmer;
    }

    Element _connection;
    Element _gui;
    Element _programmer;

    public void writeFile(String name, AbstractConfigFrame f) {
        try {
            // This is taken in large part from "Java and XML" page 368

            // create file Object
            XmlFile.ensurePrefsPresent(XmlFile.prefsDir());
            File file = new File(prefsDir()+name);

            // create root element
            Element root = new Element("preferences-config");
            Document doc = new Document(root);
            doc.setDocType(new DocType("preferences-config","preferences-config.dtd"));

            // add connection element
            Element values;
            root.addContent(f.getCommPane().getConnection());

            // add gui element
            root.addContent(f.getGUI());

            // add programmer element
            root.addContent(f.getProgrammer());

            // write the result to selected file
            java.io.FileOutputStream o = new java.io.FileOutputStream(file);
            XMLOutputter fmt = new XMLOutputter();
            fmt.setNewlines(true);   // pretty printing
            fmt.setIndent(true);
            fmt.output(doc, o);
            o.close();
        }
        catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    abstract protected String configFileName();

    public String defaultConfigFilename() { return configFileName();}

    // initialize logging
    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(AbstractConfigFile.class.getName());

}
