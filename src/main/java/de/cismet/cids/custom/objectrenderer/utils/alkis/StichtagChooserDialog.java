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
package de.cismet.cids.custom.objectrenderer.utils.alkis;

import Sirius.navigator.connection.SessionManager;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import org.openide.util.NbBundle;

import java.awt.CardLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class StichtagChooserDialog extends javax.swing.JDialog {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private org.jdesktop.swingx.JXDatePicker datepicker;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private org.jdesktop.swingx.JXBusyLabel lblBusy;
    private javax.swing.JLabel lblCheckDate;
    private javax.swing.JLabel lblDateWarning;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JPanel pnlCheckDate;
    private javax.swing.JPanel pnlControls;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JPanel pnlWrongDate;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form StichtagChooserDialog2.
     *
     * @param  parent  DOCUMENT ME!
     */
    public StichtagChooserDialog(final java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        this.setTitle(NbBundle.getMessage(StichtagChooserDialog.class, "StichtagChooserDialog.title"));
        lblIcon.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        lblBusy.setBusy(false);
        final CardLayout cl = (CardLayout)(pnlStatus.getLayout());
        cl.show(pnlStatus, "empty");
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

        jLabel1 = new javax.swing.JLabel();
        datepicker = new org.jdesktop.swingx.JXDatePicker();
        pnlControls = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnlStatus = new javax.swing.JPanel();
        pnlCheckDate = new javax.swing.JPanel();
        lblBusy = new org.jdesktop.swingx.JXBusyLabel();
        lblCheckDate = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        pnlWrongDate = new javax.swing.JPanel();
        lblDateWarning = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        jPanel1 = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(520, 150));
        setPreferredSize(new java.awt.Dimension(520, 150));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(StichtagChooserDialog.class, "StichtagChooserDialog.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        datepicker = new JXDatePicker(cal.getTime());
        datepicker.getMonthView().setUpperBound(cal.getTime());
        // this enables direct switching of the year...
        datepicker.getMonthView().setZoomable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(datepicker, gridBagConstraints);

        pnlControls.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            btnOk,
            org.openide.util.NbBundle.getMessage(StichtagChooserDialog.class, "StichtagChooserDialog.btnOk.text")); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnOkActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlControls.add(btnOk, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnCancel,
            org.openide.util.NbBundle.getMessage(StichtagChooserDialog.class, "StichtagChooserDialog.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCancelActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        pnlControls.add(btnCancel, gridBagConstraints);

        pnlStatus.setLayout(new java.awt.CardLayout());

        pnlCheckDate.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            lblBusy,
            org.openide.util.NbBundle.getMessage(StichtagChooserDialog.class, "StichtagChooserDialog.lblBusy.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlCheckDate.add(lblBusy, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblCheckDate,
            org.openide.util.NbBundle.getMessage(
                StichtagChooserDialog.class,
                "StichtagChooserDialog.lblCheckDate.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlCheckDate.add(lblCheckDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        pnlCheckDate.add(filler1, gridBagConstraints);

        pnlStatus.add(pnlCheckDate, "checkDate");

        pnlWrongDate.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            lblDateWarning,
            org.openide.util.NbBundle.getMessage(
                StichtagChooserDialog.class,
                "StichtagChooserDialog.lblDateWarning.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        pnlWrongDate.add(lblDateWarning, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        pnlWrongDate.add(filler2, gridBagConstraints);

        pnlStatus.add(pnlWrongDate, "warning");
        pnlStatus.add(jPanel1, "empty");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlControls.add(pnlStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(pnlControls, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblIcon,
            org.openide.util.NbBundle.getMessage(StichtagChooserDialog.class, "StichtagChooserDialog.lblIcon.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(lblIcon, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnOkActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnOkActionPerformed

        final SwingWorker<Date, Void> worker = new SwingWorker<Date, Void>() {

                @Override
                protected Date doInBackground() throws Exception {
                    return (Date)SessionManager.getProxy().executeTask(
                            "getDate",
                            "WUNDA_BLAU",
                            null);
                }

                @Override
                protected void done() {
                    final Date d = datepicker.getDate();
                    try {
                        final Date serverDate = get();
                        if (d.before(serverDate)) {
                            StichtagChooserDialog.this.setVisible(false);
                            final CardLayout cl = (CardLayout)(pnlStatus.getLayout());
                            cl.show(pnlStatus, "empty");
                        } else {
                            final CardLayout cl = (CardLayout)(pnlStatus.getLayout());
                            cl.show(pnlStatus, "warning");
                        }
                    } catch (Exception ex) {
                        final ErrorInfo ei = new ErrorInfo(
                                "Fehler beim Validieren des eingegebenen Datums",
                                "Das eingegebene Datum konnte nicht auf dem Sever validiert werden",
                                null,
                                null,
                                ex,
                                Level.SEVERE,
                                null);
                        JXErrorPane.showDialog(StichtagChooserDialog.this, ei);
                        datepicker.setDate(null);
                        setVisible(false);
                    } finally {
                        lblBusy.setBusy(false);
                        StichtagChooserDialog.this.repaint();
                    }
                }
            };

        lblBusy.setBusy(true);
        final CardLayout cl = (CardLayout)(pnlStatus.getLayout());
        cl.show(pnlStatus, "checkDate");
        repaint();
        worker.execute();
        this.repaint();
    } //GEN-LAST:event_btnOkActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCancelActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCancelActionPerformed
        datepicker.setDate(null);
        setVisible(false);
    }                                                                             //GEN-LAST:event_btnCancelActionPerformed

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
            java.util.logging.Logger.getLogger(StichtagChooserDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StichtagChooserDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StichtagChooserDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StichtagChooserDialog.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final StichtagChooserDialog dialog = new StichtagChooserDialog(new javax.swing.JFrame());
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                            @Override
                            public void windowClosing(final java.awt.event.WindowEvent e) {
                                System.exit(0);
                            }
                        });
                    dialog.setVisible(true);
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Date getDate() {
        return datepicker.getDate();
    }
}