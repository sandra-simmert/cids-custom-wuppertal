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
package de.cismet.cids.custom.nas;

import Sirius.navigator.connection.SessionManager;

import com.vividsolutions.jts.geom.Geometry;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;

import de.cismet.cids.custom.utils.BusyLoggingTextPane;
import de.cismet.cids.custom.utils.BusyLoggingTextPane.Styles;
import de.cismet.cids.custom.utils.alkis.AlkisConstants;
import de.cismet.cids.custom.utils.nas.NASProductGenerator;
import de.cismet.cids.custom.utils.pointnumberreservation.PointNumberReservation;
import de.cismet.cids.custom.utils.pointnumberreservation.PointNumberReservationRequest;
import de.cismet.cids.custom.wunda_blau.search.actions.PointNumberReserverationServerAction;

import de.cismet.cids.server.actions.ServerActionParameter;

import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.interaction.CismapBroker;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class PointNumberReservationPanel extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(PointNumberReservationPanel.class);
    private static final String SEVER_ACTION = "pointNumberReservation";

    //~ Instance fields --------------------------------------------------------

    boolean showErrorLbl = false;
    private final PointNumberDialog pnrDialog;
    private BusyLoggingTextPane protokollPane;
    private ArrayList<String> nbz = new ArrayList<String>();
    private int maxNbz = 4;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnErstellen;
    private javax.swing.JButton btnPosMap;
    private javax.swing.JComboBox cbNbz;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JSpinner jspAnzahl;
    private javax.swing.JLabel lblAnzahl;
    private javax.swing.JLabel lblNbz;
    private javax.swing.JLabel lblNbzError;
    private javax.swing.JLabel lblStartwert;
    private javax.swing.JTextField tfStartWert;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PointNumberReservationPanel object.
     */
    public PointNumberReservationPanel() {
        this(null);
    }

    /**
     * Creates new form PointNumberReservationPanel.
     *
     * @param  pnrDialog  DOCUMENT ME!
     */
    public PointNumberReservationPanel(final PointNumberDialog pnrDialog) {
        this.pnrDialog = pnrDialog;
        final Properties props = new Properties();
        try {
            props.load(PointNumberReservationPanel.class.getResourceAsStream("pointNumberSettings.properties"));
            maxNbz = Integer.parseInt(props.getProperty("maxNbz")); // NOI18N
            if (!loadNummerierungsbezirke()) {
                showErrorLbl = true;
            }
        } catch (Exception e) {
            showErrorLbl = true;
        }
        initComponents();
        if (!showErrorLbl) {
            cbNbz.setModel(new javax.swing.DefaultComboBoxModel(nbz.toArray(new String[nbz.size()])));
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void showError() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (protokollPane != null) {
                        protokollPane.addMessage(
                            "Während der Bearbeitung des Auftrags trat ein Fehler auf!",
                            Styles.ERROR);
                        protokollPane.setBusy(false);
                    }
                }
            }, 50);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean loadNummerierungsbezirke() {
        nbz = new ArrayList<String>();
        final MappingComponent mapC = CismapBroker.getInstance().getMappingComponent();
        Geometry g = ((XBoundingBox)mapC.getCurrentBoundingBoxFromCamera()).getGeometry();
        if (g.getSRID() != 5) {
            g = CrsTransformer.transformToGivenCrs(g, AlkisConstants.COMMONS.SRS_SERVICE);
        }
        final XBoundingBox bb = new XBoundingBox(g);
        final int lowerX = ((Double)Math.floor(bb.getX1())).intValue() / 1000;
        final int upperX = ((Double)Math.floor(bb.getX2())).intValue() / 1000;
        final int lowerY = ((Double)Math.floor(bb.getY1())).intValue() / 1000;
        final int upperY = ((Double)Math.floor(bb.getY2())).intValue() / 1000;
        final int diffX = (((upperX - lowerX) + 1) == 0) ? 1 : ((upperX - lowerX) + 1);
        final int diffY = (((upperY - lowerY) + 1) == 0) ? 1 : ((upperY - lowerY) + 1);

        if ((diffX * diffY) > maxNbz) {
            return false;
        }
        for (int i = 0; i < diffX; i++) {
            final int x = lowerX + i;
            for (int j = 0; j < diffY; j++) {
                final int y = lowerY + j;
                nbz.add("32" + x + y);
            }
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblNbz = new javax.swing.JLabel();
        cbNbz = new javax.swing.JComboBox();
        btnPosMap = new javax.swing.JButton();
        lblAnzahl = new javax.swing.JLabel();
        jspAnzahl = new javax.swing.JSpinner();
        lblStartwert = new javax.swing.JLabel();
        tfStartWert = new javax.swing.JTextField();
        btnErstellen = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));
        if (showErrorLbl) {
            lblNbzError = new javax.swing.JLabel();
        }

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            lblNbz,
            org.openide.util.NbBundle.getMessage(
                PointNumberReservationPanel.class,
                "PointNumberReservationPanel.lblNbz.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(lblNbz, gridBagConstraints);

        cbNbz.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 10);
        add(cbNbz, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnPosMap,
            org.openide.util.NbBundle.getMessage(
                PointNumberReservationPanel.class,
                "PointNumberReservationPanel.btnPosMap.text")); // NOI18N
        btnPosMap.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnPosMapActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(btnPosMap, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblAnzahl,
            org.openide.util.NbBundle.getMessage(
                PointNumberReservationPanel.class,
                "PointNumberReservationPanel.lblAnzahl.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(lblAnzahl, gridBagConstraints);

        jspAnzahl.setModel(new javax.swing.SpinnerNumberModel(
                Integer.valueOf(0),
                Integer.valueOf(0),
                null,
                Integer.valueOf(1)));
        jspAnzahl.setMinimumSize(new java.awt.Dimension(100, 28));
        jspAnzahl.setPreferredSize(new java.awt.Dimension(100, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jspAnzahl, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblStartwert,
            org.openide.util.NbBundle.getMessage(
                PointNumberReservationPanel.class,
                "PointNumberReservationPanel.lblStartwert.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(lblStartwert, gridBagConstraints);

        tfStartWert.setText(org.openide.util.NbBundle.getMessage(
                PointNumberReservationPanel.class,
                "PointNumberReservationPanel.tfStartWert.text")); // NOI18N
        tfStartWert.setMinimumSize(new java.awt.Dimension(100, 27));
        tfStartWert.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(tfStartWert, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnErstellen,
            org.openide.util.NbBundle.getMessage(
                PointNumberReservationPanel.class,
                "PointNumberReservationPanel.btnErstellen.text")); // NOI18N
        btnErstellen.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnErstellenActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(btnErstellen, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        add(filler2, gridBagConstraints);

        if (showErrorLbl) {
            lblNbzError.setForeground(new java.awt.Color(255, 0, 0));
            org.openide.awt.Mnemonics.setLocalizedText(
                lblNbzError,
                org.openide.util.NbBundle.getMessage(
                    PointNumberReservationPanel.class,
                    "PointNumberReservationPanel.lblNbzError.text")); // NOI18N
        }
        if (showErrorLbl) {
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
            add(lblNbzError, gridBagConstraints);
        }
    }                                                                 // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnErstellenActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnErstellenActionPerformed
        // check anr
        protokollPane = pnrDialog.getProtokollPane();
        try {
            protokollPane.getDocument().remove(0, protokollPane.getDocument().getLength());
        } catch (BadLocationException ex) {
            LOG.error("Could not clear Protokoll Pane", ex);
        }
        protokollPane.setBusy(true);

        final String anr = pnrDialog.getAnr();
        if (!anr.matches("[a-zA-Z0-9_-]*")) {
            return;
        }

        final String anrPrefix = (pnrDialog.getAnrPrefix() == null) ? "3290" : pnrDialog.getAnrPrefix();

        protokollPane.addMessage("Prüfe ob Antragsnummer " + anrPrefix + "-" + anr + " schon exisitiert.", Styles.INFO);

        final SwingWorker<PointNumberReservationRequest, Void> reservationWorker =
            new SwingWorker<PointNumberReservationRequest, Void>() {

                @Override
                protected PointNumberReservationRequest doInBackground() throws Exception {
                    final String nummerierungsbezirk = (String)cbNbz.getSelectedItem();
                    final Integer anzahl = (Integer)jspAnzahl.getValue();
                    final Integer startwert;
                    final String swText = tfStartWert.getText();
                    if ((swText != null) && !swText.equals("") && swText.matches("[0-9]*")) {
                        startwert = Integer.parseInt(swText);
                    } else {
                        startwert = 0;
                    }

                    final ServerActionParameter action;
                    if (pnrDialog.isErgaenzenMode()) {
                        action = new ServerActionParameter(
                                PointNumberReserverationServerAction.PARAMETER_TYPE.ACTION.toString(),
                                PointNumberReserverationServerAction.ACTION_TYPE.EXTEND_RESERVATION);
                    } else {
                        action = new ServerActionParameter(
                                PointNumberReserverationServerAction.PARAMETER_TYPE.ACTION.toString(),
                                PointNumberReserverationServerAction.ACTION_TYPE.DO_RESERVATION);
                    }
                    final ServerActionParameter prefix = new ServerActionParameter(
                            PointNumberReserverationServerAction.PARAMETER_TYPE.PREFIX.toString(),
                            anrPrefix);
                    final ServerActionParameter aNummer = new ServerActionParameter(
                            PointNumberReserverationServerAction.PARAMETER_TYPE.AUFTRAG_NUMMER.toString(),
                            anr);
                    final ServerActionParameter nbz = new ServerActionParameter(
                            PointNumberReserverationServerAction.PARAMETER_TYPE.NBZ.toString(),
                            nummerierungsbezirk);
                    final ServerActionParameter amount = new ServerActionParameter(
                            PointNumberReserverationServerAction.PARAMETER_TYPE.ANZAHL.toString(),
                            anzahl);
                    final ServerActionParameter startVal = new ServerActionParameter(
                            PointNumberReserverationServerAction.PARAMETER_TYPE.STARTWERT.toString(),
                            startwert);

                    final PointNumberReservationRequest result = (PointNumberReservationRequest)SessionManager
                                .getProxy()
                                .executeTask(
                                        SEVER_ACTION,
                                        "WUNDA_BLAU",
                                        null,
                                        action,
                                        prefix,
                                        aNummer,
                                        nbz,
                                        amount,
                                        startVal);

                    return result;
                }

                @Override
                protected void done() {
                    final Timer t = new Timer();
                    t.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                try {
                                    final PointNumberReservationRequest result = get();
                                    if ((result == null) || (result.getAntragsnummer() == null)
                                                || (result.getPointNumbers() == null)) {
                                        protokollPane.addMessage("Fehler beim senden des Auftrags", Styles.ERROR);
                                    }
                                    protokollPane.addMessage("Ok.", Styles.SUCCESS);
                                    protokollPane.setBusy(false);
                                    protokollPane.addMessage(
                                        "Reservierung für Antragsnummer: "
                                                + result.getAntragsnummer()
                                                + ". Folgende Punktnummern reserviert:",
                                        Styles.SUCCESS);
                                    protokollPane.addMessage("", Styles.INFO);
                                    for (final PointNumberReservation pnr : result.getPointNumbers()) {
                                        protokollPane.addMessage("" + pnr.getPunktnummern(), Styles.INFO);
                                    }
                                } catch (InterruptedException ex) {
                                    LOG.error("Swing worker that executes the reservation was interrupted", ex);
                                    showError();
                                } catch (ExecutionException ex) {
                                    LOG.error("Error in execution of Swing Worker that executes the reservation", ex);
                                    showError();
                                }
                            }
                        }, 50);
                }
            };

        final SwingWorker<Boolean, Void> isAntragExistingWorker = new SwingWorker<Boolean, Void>() {

                @Override
                protected Boolean doInBackground() throws Exception {
                    final ServerActionParameter action = new ServerActionParameter(
                            PointNumberReserverationServerAction.PARAMETER_TYPE.ACTION.toString(),
                            PointNumberReserverationServerAction.ACTION_TYPE.IS_ANTRAG_EXISTING);
                    final ServerActionParameter prefix = new ServerActionParameter(
                            PointNumberReserverationServerAction.PARAMETER_TYPE.PREFIX.toString(),
                            anrPrefix);
                    final ServerActionParameter aNummer = new ServerActionParameter(
                            PointNumberReserverationServerAction.PARAMETER_TYPE.AUFTRAG_NUMMER.toString(),
                            anr);
                    final boolean isAntragExisting = (Boolean)SessionManager.getProxy()
                                .executeTask(
                                        SEVER_ACTION,
                                        "WUNDA_BLAU",
                                        null,
                                        action,
                                        prefix,
                                        aNummer);

                    return isAntragExisting;
                }

                @Override
                protected void done() {
                    final Timer t = new Timer();
                    t.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                final boolean startReservationWorker = false;
                                try {
                                    final Boolean anrExists = get();
                                    if ((anrExists && pnrDialog.isErgaenzenMode())
                                                || (!anrExists && !pnrDialog.isErgaenzenMode())) {
                                        protokollPane.addMessage("Ok.", Styles.SUCCESS);
                                        protokollPane.addMessage("Sende Reservierungsauftrag.", Styles.INFO);
//                                startReservationWorker = true;
                                        reservationWorker.run();
                                    } else {
                                        if (pnrDialog.isErgaenzenMode()) {
                                            protokollPane.addMessage(
                                                "Auftragsnummer existiert noch nicht!",
                                                Styles.ERROR);
                                            protokollPane.setBusy(false);
                                        } else {
                                            protokollPane.addMessage("Auftragsnummer existiert bereits", Styles.ERROR);
                                            protokollPane.setBusy(false);
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    LOG.error(
                                        "Swing worker that checks if antragsnummer is existing was interrupted",
                                        ex);
                                    showError();
                                } catch (ExecutionException ex) {
                                    LOG.error(
                                        "Error in execution of Swing Worker that checks if antragsnummer is existing",
                                        ex);
                                    showError();
                                }
//                                pnrDialog.invalidate();
//                                pnrDialog.revalidate();
//                                pnrDialog.repaint();
                                if (startReservationWorker) {
                                    reservationWorker.run();
                                }
                            }
                        }, 50);
                }
            };

        isAntragExistingWorker.execute();
    } //GEN-LAST:event_btnErstellenActionPerformed

    /**
     * DOCUMENT ME!
     */
    public void checkNummerierungsbezirke() {
        if (loadNummerierungsbezirke()) {
            if (showErrorLbl) {
                this.remove(lblNbzError);
                lblNbzError = null;
                showErrorLbl = false;
            }
            cbNbz.setModel(new javax.swing.DefaultComboBoxModel(nbz.toArray(new String[nbz.size()])));
        } else {
            showErrorLbl = true;
            if (lblNbzError == null) {
                lblNbzError = new javax.swing.JLabel();
                lblNbzError.setForeground(new java.awt.Color(255, 0, 0));
                org.openide.awt.Mnemonics.setLocalizedText(
                    lblNbzError,
                    org.openide.util.NbBundle.getMessage(
                        PointNumberReservationPanel.class,
                        "PointNumberReservationPanel.lblNbzError.text")); // NOI18N
                final java.awt.GridBagConstraints gridBagConstraints;
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
                add(lblNbzError, gridBagConstraints);
                pnrDialog.repaint();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnPosMapActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnPosMapActionPerformed
        checkNummerierungsbezirke();
    }                                                                             //GEN-LAST:event_btnPosMapActionPerformed
}