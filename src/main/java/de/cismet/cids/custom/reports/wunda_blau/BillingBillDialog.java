/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.custom.reports.wunda_blau;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class BillingBillDialog extends javax.swing.JDialog {

    //~ Instance fields --------------------------------------------------------

    private Boolean billing;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form BillingBillDialog.
     *
     * @param  parent  DOCUMENT ME!
     */
    public BillingBillDialog(final java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(BillingBillDialog.class, "BillingBillDialog.title")); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(jCheckBox1);
        org.openide.awt.Mnemonics.setLocalizedText(
            jCheckBox1,
            org.openide.util.NbBundle.getMessage(BillingBillDialog.class, "BillingBillDialog.jCheckBox1.text")); // NOI18N
        jCheckBox1.setFocusPainted(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jCheckBox1, gridBagConstraints);

        buttonGroup1.add(jCheckBox2);
        org.openide.awt.Mnemonics.setLocalizedText(
            jCheckBox2,
            org.openide.util.NbBundle.getMessage(BillingBillDialog.class, "BillingBillDialog.jCheckBox2.text")); // NOI18N
        jCheckBox2.setFocusPainted(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jCheckBox2, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jButton1,
            org.openide.util.NbBundle.getMessage(BillingBillDialog.class, "BillingBillDialog.jButton1.text")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                jCheckBox1,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                jButton1,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel2.add(jButton1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(BillingBillDialog.class, "BillingBillDialog.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jLabel1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jButton2,
            org.openide.util.NbBundle.getMessage(BillingBillDialog.class, "BillingBillDialog.jButton2.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                jCheckBox2,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                jButton2,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel2.add(jButton2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jPanel1, gridBagConstraints);

        bindingGroup.bind();

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        billing = true;
        dispose();
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        billing = false;
        dispose();
    }                                                                            //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean isBilling() {
        return billing;
    }
}