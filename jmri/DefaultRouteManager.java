// DefaultRouteManager.java

package jmri;

/**
 * Basic Implementation of a RouteManager.
 * <P>
 * Note that this does not enforce any particular system naming convention
 *
 * @author      Dave Duchamp Copyright (C) 2004
 * @version	$Revision: 1.6 $
 */
public class DefaultRouteManager extends AbstractManager
    implements RouteManager, java.beans.PropertyChangeListener {

    public DefaultRouteManager() {
        super();
    }

    public char systemLetter() { return 'I'; }
    public char typeLetter() { return 'R'; }
    
    /**
     * Method to provide a  Route 
     * whether or not is already exists.
     */
    public Route provideRoute(String systemName, String userName) {
        Route r;
        r = getByUserName(systemName);
        if (r!=null) return r;
        r = getBySystemName(systemName);
        if (r!=null) return r;
        // Route does not exist, create a new route
		String sName = systemName.toUpperCase();
        r = new DefaultRoute(sName,userName);
        if (r!=null) {
            // save in the maps
            register(r);
        }
        return r;
    }

    /**
     * @deprecated Since 2.5.1
     */
    public Route createNewRoute(String systemName, String userName) {
        return provideRoute(systemName, userName);
    }

    /**
     * Remove an existing route. Route must have been deactivated
     * before invoking this.
     */
    public void deleteRoute(Route r) {
        deregister(r);
    }
    
    /** 
     * Method to get an existing Route.  First looks up assuming that
     *      name is a User Name.  If this fails looks up assuming
     *      that name is a System Name.  If both fail, returns null.
     */
    public Route getRoute(String name) {
        Route r = getByUserName(name);
        if (r!=null) return r;
        return getBySystemName(name);
    }

    public Route getBySystemName(String name) {
		String key = name.toUpperCase();
        return (Route)_tsys.get(key);
    }

    public Route getByUserName(String key) {
        return (Route)_tuser.get(key);
    }
    
    static DefaultRouteManager _instance = null;
    static public DefaultRouteManager instance() {
        if (_instance == null) {
            _instance = new DefaultRouteManager();
        }
        return (_instance);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DefaultRouteManager.class.getName());
}

/* @(#)DefaultRouteManager.java */
