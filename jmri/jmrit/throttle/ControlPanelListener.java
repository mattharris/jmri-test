package jmri.jmrit.throttle;

/**
 *
 * @author     glen  Copyright (C) 2002
 * @version    $Revision: 1.2 $
 */
public interface ControlPanelListener extends java.util.EventListener
{
        public void notifySpeedChanged(int speed);
        public void notifyDirectionChanged(boolean isForward);

}