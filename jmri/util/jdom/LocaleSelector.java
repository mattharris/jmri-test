// LocaleSelector.java

package jmri.util.jdom;

import org.jdom.*;
import java.util.Locale;

/**
 * Select XML content based on current Locale.
 *
 * Try: jp_JP, then jp, then nothing.
 * 
 *
 * @author Bob Jacobsen  Copyright 2010
 * @since 2.9.3
 * @version $Revision: 1.1 $
 */

public class LocaleSelector {
    static String[] suffixes 
            = new String[] {
                Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry(),
                Locale.getDefault().getLanguage()                              
            };
    static public String getAttribute(Element el, String name) {
        String retval;
        // look for each suffix first
        for (String suffix : suffixes) {
            retval = checkElement(el, name, suffix);
            if (retval != null) return retval;
        }
        
        // failed, go back to original attribute
        Attribute a = el.getAttribute(name);
        if (a == null) return null;
        return a.getValue();
    }
    
    static String checkElement(Element el, String name, String suffix) {
        for (Object obj : el.getChildren(name)) {
            Element e = (Element)obj;
            Attribute a = e.getAttribute("lang", Namespace.XML_NAMESPACE);
            if (a != null) {
                if (a.getValue().equals(suffix)) {
                    return e.getText();
                }
            }
        }
        return null;        
    }

}