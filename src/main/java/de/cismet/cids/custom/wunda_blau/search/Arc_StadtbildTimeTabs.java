/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.custom.wunda_blau.search;

import java.util.Calendar;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public class Arc_StadtbildTimeTabs extends javax.swing.JPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboYear;
    private org.jdesktop.swingx.JXDatePicker dpDay;
    private org.jdesktop.swingx.JXDatePicker dpFTFrom;
    private org.jdesktop.swingx.JXDatePicker dpFTTill;
    private org.jdesktop.swingx.JXDatePicker dpFrom;
    private org.jdesktop.swingx.JXDatePicker dpTo;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel tabDay;
    private javax.swing.JPanel tabFrom;
    private javax.swing.JPanel tabFromTill;
    private javax.swing.JPanel tabTo;
    private javax.swing.JPanel tabYear;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form Arc_StadtbildTimeTabs.
     */
    public Arc_StadtbildTimeTabs() {
        initComponents();
        setTimeRelatedModels();

        // set Zoomable has to be set to activate the SpinningCalendarHeaderHandler. for more information see
        // JXDatePickerHeaderTakeoff or http://stackoverflow.com/questions/16111943/java-swing-jxdatepicker
        dpDay.getMonthView().setZoomable(true);
        dpFTFrom.getMonthView().setZoomable(true);
        dpFTTill.getMonthView().setZoomable(true);
        dpFrom.getMonthView().setZoomable(true);
        dpTo.getMonthView().setZoomable(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void setTimeRelatedModels() {
        // put the last 100 year numbers into the comboboxes
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final Integer[] years = new Integer[100];
        for (int i = 0; i < 100; i++) {
            years[i] = new Integer(currentYear - i);
        }
        cboYear.setModel(new javax.swing.DefaultComboBoxModel<Integer>(years));
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabDay = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dpDay = new org.jdesktop.swingx.JXDatePicker();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        tabYear = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        cboYear = new javax.swing.JComboBox();
        tabFrom = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        dpFrom = new org.jdesktop.swingx.JXDatePicker();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        tabTo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        dpTo = new org.jdesktop.swingx.JXDatePicker();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        tabFromTill = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dpFTFrom = new org.jdesktop.swingx.JXDatePicker();
        jLabel6 = new javax.swing.JLabel();
        dpFTTill = new org.jdesktop.swingx.JXDatePicker();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));

        setLayout(new java.awt.BorderLayout());

        tabDay.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(Arc_StadtbildTimeTabs.class, "Arc_StadtbildTimeTabs.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 5, 5);
        tabDay.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 5, 5);
        tabDay.add(dpDay, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        tabDay.add(filler1, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                Arc_StadtbildTimeTabs.class,
                "Arc_StadtbildTimeTabs.tabDay.TabConstraints.tabTitle"),
            tabDay); // NOI18N

        tabYear.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel2,
            org.openide.util.NbBundle.getMessage(Arc_StadtbildTimeTabs.class, "Arc_StadtbildTimeTabs.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 5, 5);
        tabYear.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        tabYear.add(filler2, gridBagConstraints);

        cboYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2013" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 5, 5);
        tabYear.add(cboYear, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                Arc_StadtbildTimeTabs.class,
                "Arc_StadtbildTimeTabs.tabYear.TabConstraints.tabTitle"),
            tabYear); // NOI18N

        tabFrom.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel3,
            org.openide.util.NbBundle.getMessage(Arc_StadtbildTimeTabs.class, "Arc_StadtbildTimeTabs.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 5, 5);
        tabFrom.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 5, 5);
        tabFrom.add(dpFrom, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        tabFrom.add(filler3, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                Arc_StadtbildTimeTabs.class,
                "Arc_StadtbildTimeTabs.tabFrom.TabConstraints.tabTitle"),
            tabFrom); // NOI18N

        tabTo.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel4,
            org.openide.util.NbBundle.getMessage(Arc_StadtbildTimeTabs.class, "Arc_StadtbildTimeTabs.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 5, 5);
        tabTo.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 5, 5);
        tabTo.add(dpTo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        tabTo.add(filler4, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                Arc_StadtbildTimeTabs.class,
                "Arc_StadtbildTimeTabs.tabTo.TabConstraints.tabTitle"),
            tabTo); // NOI18N

        tabFromTill.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel5,
            org.openide.util.NbBundle.getMessage(Arc_StadtbildTimeTabs.class, "Arc_StadtbildTimeTabs.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 5, 5);
        tabFromTill.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 5, 5);
        tabFromTill.add(dpFTFrom, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel6,
            org.openide.util.NbBundle.getMessage(Arc_StadtbildTimeTabs.class, "Arc_StadtbildTimeTabs.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 5, 5);
        tabFromTill.add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 5, 5);
        tabFromTill.add(dpFTTill, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        tabFromTill.add(filler5, gridBagConstraints);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(
                Arc_StadtbildTimeTabs.class,
                "Arc_StadtbildTimeTabs.tabFromTill.TabConstraints.tabTitle"),
            tabFromTill); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents
}