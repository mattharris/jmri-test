package jmri.jmrit.beantable;

import jmri.*;

public class SensorTableTabAction extends AbstractTableTabAction {
    
    public SensorTableTabAction(String s){
        super(s);
    }
    
    public SensorTableTabAction(){
        this("Multiple Tabbed");
    }
    
    protected Manager getManager() {
        return InstanceManager.sensorManagerInstance();
    }
       
    protected String getClassName() {
        return SensorTableAction.class.getName();
    }
    
    protected AbstractTableAction getNewTableAction (String choice){
        return new SensorTableAction(choice);
    }

    @Override
    protected String helpTarget() {
        return "package.jmri.jmrit.beantable.SensorTable";
    }
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SensorTableTabAction.class.getName());
}