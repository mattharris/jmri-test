// BuildReportOptionFrame.java
package jmri.jmrit.operations.setup;

import jmri.jmrit.operations.OperationsFrame;

/**
 * Frame for user edit of the build report options
 *
 * @author Dan Boudreau Copyright (C) 2008, 2010, 2011, 2012, 2013
 * @version $Revision: 21643 $
 */
public class BuildReportOptionFrame extends OperationsFrame {

    public BuildReportOptionFrame() {
        super(Bundle.getMessage("TitleBuildReportOptions"), new BuildReportOptionPanel()); // NOI18N
    }

    @Override
    public void initComponents() {
        super.initComponents();
        // build menu
        addHelpMenu("package.jmri.jmrit.operations.Operations_BuildReportDetails", true); // NOI18N
    }
}
