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
package de.cismet.cids.custom.mail;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.openide.util.Exceptions;

import java.io.IOException;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public class EMailTemplateChooserDialog extends javax.swing.JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            EMailTemplateChooserDialog.class);
    private static TemplateInfo templateInfo;
    private static ArrayList<Template> templates = new ArrayList<Template>();

    static {
        try {
            templateInfo =
                new ObjectMapper().readValue(TemplateInfo.class.getResourceAsStream(
                        "/de/cismet/cids/custom/billing/email_templates.json"),
                    TemplateInfo.class);

            templates = templateInfo.getTemplates();
        } catch (IOException ex) {
            LOG.error("Error while trying to read the email_templates.json", ex);
        }
    }

    //~ Instance fields --------------------------------------------------------

    private Template templateToReturn;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdoptTemplate;
    private javax.swing.JComboBox cobTemplates;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtSubject;
    private javax.swing.JTextField txtTo;
    private javax.swing.JTextArea txtaBody;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form EMailTemplateChooserDialog.
     */
    public EMailTemplateChooserDialog() {
        super();
        setModal(true);
        initComponents();

        final DefaultComboBoxModel<Template> model = new DefaultComboBoxModel<Template>(templates.toArray(
                    new Template[templates.size()]));
        cobTemplates.setModel(model);
        cobTemplates.setSelectedIndex(0);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Shows the dialog and when the dialog is closed then it returns the chosen template.
     *
     * @return  DOCUMENT ME!
     */
    public Template showDialogAndReturnBody() {
        StaticSwingTools.showDialog(this);
        return templateToReturn;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        cobTemplates = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtSubject = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtaBody = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        txtTo = new javax.swing.JTextField();
        btnAdoptTemplate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(
                EMailTemplateChooserDialog.class,
                "EMailTemplateChooserDialog.title")); // NOI18N
        setIconImage(null);
        setMinimumSize(new java.awt.Dimension(450, 321));
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        cobTemplates.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cobTemplates.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cobTemplatesActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 2);
        jPanel1.add(cobTemplates, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(
                EMailTemplateChooserDialog.class,
                "EMailTemplateChooserDialog.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(
                    org.openide.util.NbBundle.getMessage(
                        EMailTemplateChooserDialog.class,
                        "EMailTemplateChooserDialog.jPanel2.border.outsideBorder.title")),
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10))); // NOI18N
        jPanel2.setLayout(new java.awt.GridBagLayout());

        txtSubject.setEditable(false);
        txtSubject.setText(org.openide.util.NbBundle.getMessage(
                EMailTemplateChooserDialog.class,
                "EMailTemplateChooserDialog.txtSubject.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 1);
        jPanel2.add(txtSubject, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel2,
            org.openide.util.NbBundle.getMessage(
                EMailTemplateChooserDialog.class,
                "EMailTemplateChooserDialog.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 5, 5);
        jPanel2.add(jLabel2, gridBagConstraints);

        txtaBody.setEditable(false);
        txtaBody.setColumns(20);
        txtaBody.setLineWrap(true);
        txtaBody.setRows(5);
        txtaBody.setText(org.openide.util.NbBundle.getMessage(
                EMailTemplateChooserDialog.class,
                "EMailTemplateChooserDialog.txtaBody.text")); // NOI18N
        txtaBody.setMargin(new java.awt.Insets(3, 3, 3, 3));
        jScrollPane1.setViewportView(txtaBody);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel3,
            org.openide.util.NbBundle.getMessage(
                EMailTemplateChooserDialog.class,
                "EMailTemplateChooserDialog.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        txtTo.setEditable(false);
        txtTo.setText(org.openide.util.NbBundle.getMessage(
                EMailTemplateChooserDialog.class,
                "EMailTemplateChooserDialog.txtTo.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 1);
        jPanel2.add(txtTo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnAdoptTemplate,
            org.openide.util.NbBundle.getMessage(
                EMailTemplateChooserDialog.class,
                "EMailTemplateChooserDialog.btnAdoptTemplate.text")); // NOI18N
        btnAdoptTemplate.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnAdoptTemplateActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 2);
        jPanel1.add(btnAdoptTemplate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnAdoptTemplateActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnAdoptTemplateActionPerformed
        templateToReturn = (Template)cobTemplates.getSelectedItem();
        setVisible(false);
    }                                                                                    //GEN-LAST:event_btnAdoptTemplateActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cobTemplatesActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cobTemplatesActionPerformed
        final Template template = (Template)cobTemplates.getSelectedItem();
        txtSubject.setText(template.getSubject());
        txtaBody.setText(template.getBody());
        txtTo.setText(template.to + " und Kunden");
    }                                                                                //GEN-LAST:event_cobTemplatesActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EMailTemplateChooserDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EMailTemplateChooserDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EMailTemplateChooserDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EMailTemplateChooserDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        UIManager.installLookAndFeel("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                        final EMailTemplateChooserDialog dialog = new EMailTemplateChooserDialog();
                        dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                                @Override
                                public void windowClosing(final java.awt.event.WindowEvent e) {
                                    System.exit(0);
                                }
                            });
                        dialog.setVisible(true);
                    } catch (ClassNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (InstantiationException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IllegalAccessException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (UnsupportedLookAndFeelException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
    }
}