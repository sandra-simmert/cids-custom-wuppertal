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
package de.cismet.cids.custom.objectrenderer.wunda_blau;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.ui.RequestsFullSizeComponent;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.custom.objectrenderer.utils.ObjectRendererUtils;
import de.cismet.cids.custom.objectrenderer.utils.billing.PrintBillingReportForCustomer;
import de.cismet.cids.custom.reports.wunda_blau.PrintStatisticsReport;
import de.cismet.cids.custom.wunda_blau.search.server.CidsBillingSearchStatement;
import de.cismet.cids.custom.wunda_blau.search.server.CidsBillingSearchStatement.Kostentyp;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanAggregationRenderer;

import de.cismet.tools.gui.TitleComponentProvider;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public class BillingKundeAggregationRenderer extends javax.swing.JPanel implements RequestsFullSizeComponent,
    CidsBeanAggregationRenderer,
    TitleComponentProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(BillingKundeAggregationRenderer.class);
    // column headers
    private static final String[] AGR_COMLUMN_NAMES = new String[] {
            "Auswahl für Berichte",
            "Kundenname",
            "aggregierter Preis",
            "kostenpflichtige Downloads",
        };
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    //~ Instance fields --------------------------------------------------------

    private Collection<CidsBean> cidsBeans = null;
    private Collection<Object[]> tableData;
    private List<CidsBean> filteredBillingBeans;
    private Date[] fromDate_tillDate;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXBusyLabel blblBusy;
    private javax.swing.JButton btnBuchungsbeleg;
    private javax.swing.JButton btnGeschaeftsstatistik;
    private javax.swing.JButton btnRechnungsanlage;
    private javax.swing.JButton btnShowResults;
    private javax.swing.JComboBox cboAbrechnungsturnus;
    private javax.swing.JCheckBox cboBillDownloads;
    private javax.swing.JCheckBox cboHideFreeDownloads;
    private javax.swing.JCheckBox cboHideFreeDownloadsBuchungsbeleg;
    private javax.swing.JCheckBox cboHideFreeDownloadsRechnungsanlage;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAgrTitle;
    private javax.swing.JLabel lblFilterResult;
    private javax.swing.JLabel lblResultHeader;
    private javax.swing.JPanel panTitle;
    private javax.swing.JPanel panTitleString;
    private javax.swing.JPanel pnlBusyLable;
    private javax.swing.JPanel pnlFilterResults;
    private javax.swing.JPanel pnlFilters;
    private javax.swing.JPanel pnlTable;
    private de.cismet.cids.custom.objectrenderer.utils.billing.TimeFilterPanel pnlTimeFilters;
    private de.cismet.cids.custom.objectrenderer.utils.billing.VerwendungszweckPanel pnlVerwendungszweck;
    private de.cismet.tools.gui.SemiRoundedPanel smiplFilter;
    private de.cismet.tools.gui.SemiRoundedPanel smiplTable;
    private javax.swing.JTable tblCustomers;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form BillingKundeAggregationRenderer.
     */
    public BillingKundeAggregationRenderer() {
        initComponents();
        setFilterActionInExternalPanels();
        setAbrechnungsturnusIntoComboBox();
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

        panTitle = new javax.swing.JPanel();
        panTitleString = new javax.swing.JPanel();
        lblAgrTitle = new javax.swing.JLabel();
        smiplFilter = new de.cismet.tools.gui.SemiRoundedPanel();
        jLabel2 = new javax.swing.JLabel();
        smiplTable = new de.cismet.tools.gui.SemiRoundedPanel();
        lblResultHeader = new javax.swing.JLabel();
        pnlTable = new javax.swing.JPanel();
        pnlFilterResults = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCustomers = new javax.swing.JTable();
        lblFilterResult = new javax.swing.JLabel();
        pnlBusyLable = new javax.swing.JPanel();
        blblBusy = new org.jdesktop.swingx.JXBusyLabel();
        pnlFilters = new javax.swing.JPanel();
        btnShowResults = new javax.swing.JButton();
        pnlTimeFilters = new de.cismet.cids.custom.objectrenderer.utils.billing.TimeFilterPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cboAbrechnungsturnus = new javax.swing.JComboBox();
        cboHideFreeDownloads = new javax.swing.JCheckBox();
        pnlVerwendungszweck = new de.cismet.cids.custom.objectrenderer.utils.billing.VerwendungszweckPanel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        jPanel5 = new javax.swing.JPanel();
        cboBillDownloads = new javax.swing.JCheckBox();
        btnBuchungsbeleg = new javax.swing.JButton();
        btnGeschaeftsstatistik = new javax.swing.JButton();
        btnRechnungsanlage = new javax.swing.JButton();
        cboHideFreeDownloadsRechnungsanlage = new javax.swing.JCheckBox();
        cboHideFreeDownloadsBuchungsbeleg = new javax.swing.JCheckBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));

        panTitle.setOpaque(false);
        panTitle.setLayout(new java.awt.BorderLayout());

        panTitleString.setOpaque(false);
        panTitleString.setLayout(new java.awt.GridBagLayout());

        lblAgrTitle.setFont(new java.awt.Font("Tahoma", 1, 18));      // NOI18N
        lblAgrTitle.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(
            lblAgrTitle,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.lblAgrTitle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panTitleString.add(lblAgrTitle, gridBagConstraints);

        panTitle.add(panTitleString, java.awt.BorderLayout.CENTER);

        setLayout(new java.awt.GridBagLayout());

        smiplFilter.setBackground(new java.awt.Color(51, 51, 51));
        smiplFilter.setLayout(new java.awt.FlowLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel2,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.jLabel2.text")); // NOI18N
        smiplFilter.add(jLabel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(smiplFilter, gridBagConstraints);

        smiplTable.setBackground(new java.awt.Color(51, 51, 51));
        smiplTable.setLayout(new java.awt.FlowLayout());

        lblResultHeader.setBackground(new java.awt.Color(51, 51, 51));
        lblResultHeader.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(
            lblResultHeader,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.lblResultHeader.text")); // NOI18N
        smiplTable.add(lblResultHeader);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(smiplTable, gridBagConstraints);

        pnlTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlTable.setLayout(new java.awt.CardLayout());

        pnlFilterResults.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(453, 275));

        tblCustomers.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                    { null, null, null, null },
                    { null, null, null, null },
                    { null, null, null, null },
                    { null, null, null, null }
                },
                new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        tblCustomers.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    tblCustomersMouseClicked(evt);
                }
                @Override
                public void mouseExited(final java.awt.event.MouseEvent evt) {
                    tblCustomersMouseExited(evt);
                }
            });
        tblCustomers.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

                @Override
                public void mouseMoved(final java.awt.event.MouseEvent evt) {
                    tblCustomersMouseMoved(evt);
                }
            });
        jScrollPane1.setViewportView(tblCustomers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 0);
        pnlFilterResults.add(jScrollPane1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblFilterResult,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.lblFilterResult.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlFilterResults.add(lblFilterResult, gridBagConstraints);

        pnlTable.add(pnlFilterResults, "table");

        pnlBusyLable.setLayout(new java.awt.BorderLayout());

        blblBusy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(
            blblBusy,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.blblBusy.text")); // NOI18N
        pnlBusyLable.add(blblBusy, java.awt.BorderLayout.CENTER);

        pnlTable.add(pnlBusyLable, "busy");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(pnlTable, gridBagConstraints);

        pnlFilters.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlFilters.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            btnShowResults,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.btnShowResults.text")); // NOI18N
        btnShowResults.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnShowResultsActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 2);
        pnlFilters.add(btnShowResults, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        pnlFilters.add(pnlTimeFilters, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(1, 3, 2, 4),
                javax.swing.BorderFactory.createTitledBorder(
                    org.openide.util.NbBundle.getMessage(
                        BillingKundeAggregationRenderer.class,
                        "BillingKundeAggregationRenderer.jPanel3.border.insideBorder.title")))); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel4,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 3);
        jPanel3.add(jLabel4, gridBagConstraints);

        cboAbrechnungsturnus.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cboAbrechnungsturnusActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 8);
        jPanel3.add(cboAbrechnungsturnus, gridBagConstraints);

        cboHideFreeDownloads.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            cboHideFreeDownloads,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.cboHideFreeDownloads.text")); // NOI18N
        cboHideFreeDownloads.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cboHideFreeDownloadsActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 10, 0);
        jPanel3.add(cboHideFreeDownloads, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlFilters.add(jPanel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlFilters.add(pnlVerwendungszweck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        pnlFilters.add(filler4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(pnlFilters, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 10, 10));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        cboBillDownloads.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            cboBillDownloads,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.cboBillDownloads.text")); // NOI18N
        cboBillDownloads.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cboBillDownloadsActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        jPanel5.add(cboBillDownloads, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnBuchungsbeleg,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.btnBuchungsbeleg.text")); // NOI18N
        btnBuchungsbeleg.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnBuchungsbelegActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 5);
        jPanel5.add(btnBuchungsbeleg, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnGeschaeftsstatistik,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.btnGeschaeftsstatistik.text")); // NOI18N
        btnGeschaeftsstatistik.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnGeschaeftsstatistikActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 5);
        jPanel5.add(btnGeschaeftsstatistik, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnRechnungsanlage,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.btnRechnungsanlage.text")); // NOI18N
        btnRechnungsanlage.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnRechnungsanlageActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel5.add(btnRechnungsanlage, gridBagConstraints);

        cboHideFreeDownloadsRechnungsanlage.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            cboHideFreeDownloadsRechnungsanlage,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.cboHideFreeDownloadsRechnungsanlage.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel5.add(cboHideFreeDownloadsRechnungsanlage, gridBagConstraints);

        cboHideFreeDownloadsBuchungsbeleg.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            cboHideFreeDownloadsBuchungsbeleg,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.cboHideFreeDownloadsBuchungsbeleg.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        jPanel5.add(cboHideFreeDownloadsBuchungsbeleg, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(filler1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        add(filler3, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tblCustomersMouseClicked(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_tblCustomersMouseClicked
    }                                                                            //GEN-LAST:event_tblCustomersMouseClicked

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tblCustomersMouseExited(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_tblCustomersMouseExited
    }                                                                           //GEN-LAST:event_tblCustomersMouseExited

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tblCustomersMouseMoved(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_tblCustomersMouseMoved
    }                                                                          //GEN-LAST:event_tblCustomersMouseMoved

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cboAbrechnungsturnusActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cboAbrechnungsturnusActionPerformed
        filterSettingsChanged();
    }                                                                                        //GEN-LAST:event_cboAbrechnungsturnusActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnShowResultsActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnShowResultsActionPerformed
        filterBuchungen();
    }                                                                                  //GEN-LAST:event_btnShowResultsActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cboBillDownloadsActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cboBillDownloadsActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_cboBillDownloadsActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnBuchungsbelegActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnBuchungsbelegActionPerformed
        final HashMap<CidsBean, Collection<CidsBean>> billingsOfCustomers = createBillingsOfCostumersForReports(evt);
        for (final CidsBean kundeBean : billingsOfCustomers.keySet()) {
            new PrintBillingReportForCustomer(
                kundeBean,
                billingsOfCustomers.get(kundeBean),
                fromDate_tillDate,
                false,
                cboBillDownloads.isSelected()).print();
        }
    }                                                                                    //GEN-LAST:event_btnBuchungsbelegActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnRechnungsanlageActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnRechnungsanlageActionPerformed
        final HashMap<CidsBean, Collection<CidsBean>> billingsOfCustomers = createBillingsOfCostumersForReports(evt);
        for (final CidsBean kundeBean : billingsOfCustomers.keySet()) {
            new PrintBillingReportForCustomer(
                kundeBean,
                billingsOfCustomers.get(kundeBean),
                fromDate_tillDate,
                true,
                cboBillDownloads.isSelected()).print();
        }
        if (cboBillDownloads.isSelected()) {
            filterBuchungen();
        }
    }                                                                                      //GEN-LAST:event_btnRechnungsanlageActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnGeschaeftsstatistikActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnGeschaeftsstatistikActionPerformed
        final Collection<CidsBean> billings = createBillingsForStatisticsReport();
        new PrintStatisticsReport(fromDate_tillDate, billings).print();
    }                                                                                          //GEN-LAST:event_btnGeschaeftsstatistikActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cboHideFreeDownloadsActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cboHideFreeDownloadsActionPerformed
        filterSettingsChanged();
    }                                                                                        //GEN-LAST:event_cboHideFreeDownloadsActionPerformed

    /**
     * DOCUMENT ME!
     */
    private void setFilterActionInExternalPanels() {
        final Action filterAction = new AbstractAction() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    filterSettingsChanged();
                }
            };
        pnlTimeFilters.setFilterSettingChangedAction(filterAction);
        pnlVerwendungszweck.setFilterSettingChangedAction(filterAction);
    }

    /**
     * DOCUMENT ME!
     */
    private void filterSettingsChanged() {
        org.openide.awt.Mnemonics.setLocalizedText(
            lblResultHeader,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.lblResultHeader.text.filterSettingsChanged")); // NOI18N
    }

    @Override
    public Collection<CidsBean> getCidsBeans() {
        return cidsBeans;
    }

    @Override
    public void setCidsBeans(final Collection<CidsBean> cidsBeans) {
        this.cidsBeans = cidsBeans;
        setTitle(null);
        if (!cidsBeans.isEmpty()) {
            filterBuchungen();
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void filterBuchungen() {
        org.openide.awt.Mnemonics.setLocalizedText(
            lblResultHeader,
            org.openide.util.NbBundle.getMessage(
                BillingKundeAggregationRenderer.class,
                "BillingKundeAggregationRenderer.lblResultHeader.text"));

        final ArrayList<MetaObject> kundenMetaObjects = new ArrayList<MetaObject>();
        for (final CidsBean kundeBean : cidsBeans) {
            kundenMetaObjects.add(kundeBean.getMetaObject());
        }
        final CidsBillingSearchStatement cidsBillingSearchStatement = new CidsBillingSearchStatement(
                SessionManager.getSession().getUser(),
                kundenMetaObjects);

        // set filters
        cidsBillingSearchStatement.setVerwendungszweckKeys(
            pnlVerwendungszweck.createSelectedVerwendungszweckKeysStringArray());
        fromDate_tillDate = pnlTimeFilters.chooseDates();
        cidsBillingSearchStatement.setFrom(fromDate_tillDate[0]);
        cidsBillingSearchStatement.setTill(fromDate_tillDate[1]);

        final Object abrechnungsturnus = cboAbrechnungsturnus.getSelectedItem();
        String abrechnungsturnusID = "";
        if (abrechnungsturnus instanceof CidsBean) {
            abrechnungsturnusID = ((CidsBean)abrechnungsturnus).getProperty("id").toString();
        }
        cidsBillingSearchStatement.setAbrechnungsturnusID(abrechnungsturnusID);

        if (cboHideFreeDownloads.isSelected()) {
            cidsBillingSearchStatement.setKostentyp(Kostentyp.KOSTENPFLICHTIG);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Query to get the billings: " + cidsBillingSearchStatement.generateQuery());
        }

        blblBusy.setBusy(true);
        ((CardLayout)pnlTable.getLayout()).show(pnlTable, "busy");
        btnBuchungsbeleg.setEnabled(false);
        btnRechnungsanlage.setEnabled(false);
        btnShowResults.setEnabled(false);
        btnGeschaeftsstatistik.setEnabled(false);
        final SwingWorker<Collection<MetaObject>, Void> swingWorker = new SwingWorker<Collection<MetaObject>, Void>() {

                @Override
                protected Collection<MetaObject> doInBackground() throws Exception {
                    // return cidsBillingSearchStatement.performServerSearch();
                    return SessionManager.getProxy()
                                .customServerSearch(SessionManager.getSession().getUser(),
                                    cidsBillingSearchStatement);
                }

                @Override
                protected void done() {
                    try {
                        final Collection<MetaObject> metaObjects = get();

                        if (metaObjects == null) {
                            LOG.error("Billing metaobjects was null.");
                        } else if (metaObjects.isEmpty()) {
                            LOG.info("No Billing metaobjects found.");
                            filteredBillingBeans = new ArrayList<CidsBean>();
                            fillCustomerTable(filteredBillingBeans);
                            lblFilterResult.setText(generateFilterResultText(new ArrayList<CidsBean>()));
                        } else {
                            final List<CidsBean> billingBeans = new ArrayList<CidsBean>(metaObjects.size());
                            for (final MetaObject mo : metaObjects) {
                                billingBeans.add(mo.getBean());
                            }
                            fillCustomerTable(billingBeans);
                            filteredBillingBeans = billingBeans;
                            lblFilterResult.setText(generateFilterResultText(billingBeans));
                        }
                    } catch (InterruptedException ex) {
                        LOG.error("Error while filtering the billings.", ex);
                    } catch (ExecutionException ex) {
                        LOG.error("Error while filtering the billings.", ex);
                    } finally {
                        ((CardLayout)pnlTable.getLayout()).show(pnlTable, "table");
                        btnBuchungsbeleg.setEnabled(true);
                        btnRechnungsanlage.setEnabled(true);
                        btnShowResults.setEnabled(true);
                        btnGeschaeftsstatistik.setEnabled(true);
                        blblBusy.setBusy(false);
                    }
                }
            };
        swingWorker.execute();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   billingBeans  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String generateFilterResultText(final Collection<CidsBean> billingBeans) {
        if (billingBeans.isEmpty()) {
            return NbBundle.getMessage(
                    BillingKundeRenderer.class,
                    "BillingKundeRenderer.generateFilterResultText().noBillings");
        } else {
            fromDate_tillDate = pnlTimeFilters.chooseDates();
            final Date from = fromDate_tillDate[0];
            final Date till = fromDate_tillDate[1];

            final StringBuilder text = new StringBuilder(NbBundle.getMessage(
                        BillingKundeRenderer.class,
                        "BillingKundeAggregationRenderer.generateFilterResultText().billings1"));
            if (from == null) {
                text.append(".");
            } else if ((till == null) || from.equals(till)) {
                text.append(NbBundle.getMessage(
                        BillingKundeRenderer.class,
                        "BillingKundeAggregationRenderer.generateFilterResultText().billings2.oneDate"));
                text.append(DATE_FORMAT.format(from));
                text.append(".");
            } else {
                text.append(NbBundle.getMessage(
                        BillingKundeRenderer.class,
                        "BillingKundeAggregationRenderer.generateFilterResultText().billings2.twoDates1"));
                text.append(DATE_FORMAT.format(from));
                text.append(NbBundle.getMessage(
                        BillingKundeRenderer.class,
                        "BillingKundeAggregationRenderer.generateFilterResultText().billings2.twoDates2"));
                text.append(DATE_FORMAT.format(till));
                text.append(".");
            }
            return text.toString();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  billingBeans  DOCUMENT ME!
     */
    private void fillCustomerTable(final Collection<CidsBean> billingBeans) {
        final HashMap<CidsBean, Object[]> aggregatedData = aggregateData(billingBeans);
        tableData = aggregatedData.values();

        final AggregatedBillingTableModel tableModel = new AggregatedBillingTableModel(
                tableData.toArray(new Object[tableData.size()][]),
                AGR_COMLUMN_NAMES);
        tblCustomers.setModel(tableModel);
        tblCustomers.getColumnModel().getColumn(2).setCellRenderer(new EuroFormatterRenderer());
        if (!tableData.isEmpty()) {
            final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tblCustomers.getModel());
            tblCustomers.setRowSorter(sorter);
        } else {
            tblCustomers.setRowSorter(null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   billingBeans  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private HashMap<CidsBean, Object[]> aggregateData(final Collection<CidsBean> billingBeans) {
        final HashMap<CidsBean, Object[]> aggregatedData = new HashMap<CidsBean, Object[]>();
        for (final CidsBean billingBean : billingBeans) {
            final CidsBean kundeBean = (CidsBean)billingBean.getProperty("angelegt_durch.kunde");
            final double netto_sum = (Double)billingBean.getProperty("netto_summe");
            if (aggregatedData.containsKey(kundeBean)) {
                final Object[] dataForCustomer = aggregatedData.get(kundeBean);
                if (netto_sum > 0) {
                    final double subtotal = (Double)dataForCustomer[2];
                    dataForCustomer[2] = subtotal + netto_sum;
                    final int amountOfBillingsWithCosts = (Integer)dataForCustomer[3];
                    dataForCustomer[3] = amountOfBillingsWithCosts + 1;
                }
            } else {
                final Boolean useInReports = Boolean.TRUE;
                int amountOfBillingsWithCosts = 0;
                if (netto_sum > 0) {
                    amountOfBillingsWithCosts = 1;
                }
                final Object[] dataForCustomer = { useInReports, kundeBean, netto_sum, amountOfBillingsWithCosts };
                aggregatedData.put(kundeBean, dataForCustomer);
            }
        }
        return aggregatedData;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   evt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private HashMap<CidsBean, Collection<CidsBean>> createBillingsOfCostumersForReports(final ActionEvent evt) {
        final HashMap<CidsBean, Collection<CidsBean>> billingsOfCustomers =
            new HashMap<CidsBean, Collection<CidsBean>>();
        final boolean showBillingWithoutCostInReport = retrieveShowBillingInReport(evt);
        for (final CidsBean billingBean : filteredBillingBeans) {
            final Double nettoSum = (Double)billingBean.getProperty("netto_summe");
            if (showBillingWithoutCostInReport || (nettoSum > 0)) {
                final CidsBean kundeBean = (CidsBean)billingBean.getProperty("angelegt_durch.kunde");
                if (isCustomerSelectedToBeIncludedIntoReport(kundeBean)) {
                    if (billingsOfCustomers.containsKey(kundeBean)) {
                        billingsOfCustomers.get(kundeBean).add(billingBean);
                    } else {
                        final ArrayList<CidsBean> billings = new ArrayList<CidsBean>();
                        billings.add(billingBean);
                        billingsOfCustomers.put(kundeBean, billings);
                    }
                }
            }
        }
        return billingsOfCustomers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   evt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean retrieveShowBillingInReport(final ActionEvent evt) {
        final JButton source = (JButton)evt.getSource();
        if (source.equals(btnBuchungsbeleg)) {
            return !cboHideFreeDownloadsBuchungsbeleg.isSelected();
        } else if (source.equals(btnRechnungsanlage)) {
            return !cboHideFreeDownloadsRechnungsanlage.isSelected();
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Collection<CidsBean> createBillingsForStatisticsReport() {
        final Collection<CidsBean> billlings = new ArrayList<CidsBean>();
        for (final CidsBean billingBean : filteredBillingBeans) {
            final CidsBean kundeBean = (CidsBean)billingBean.getProperty("angelegt_durch.kunde");
            if (isCustomerSelectedToBeIncludedIntoReport(kundeBean)) {
                billlings.add(billingBean);
            }
        }
        return billlings;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   kundeBean  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isCustomerSelectedToBeIncludedIntoReport(final CidsBean kundeBean) {
        final AggregatedBillingTableModel model = (AggregatedBillingTableModel)tblCustomers.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            final CidsBean kundeFromTable = (CidsBean)model.getValueAt(i, 1);
            if (kundeFromTable.equals(kundeBean)) {
                return (Boolean)model.getValueAt(i, 0);
            }
        }
        return false;
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getTitle() {
        return lblAgrTitle.getText();
    }

    @Override
    public void setTitle(final String title) {
        String desc = "Kundenaggregationsrenderer: ";
        final Collection<CidsBean> beans = cidsBeans;
        if ((beans != null) && (beans.size() > 0)) {
            desc += beans.size() + " Kunden ausgewählt";
        }
        lblAgrTitle.setText(desc);
    }

    @Override
    public JComponent getTitleComponent() {
        return panTitle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JLabel getTitleLabel() {
        return lblAgrTitle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        try {
            final CidsBean[] beans = DevelopmentTools.createCidsBeansFromRMIConnectionOnLocalhost(
                    "WUNDA_BLAU",
                    "Administratoren",
                    "admin",
                    "kif",
                    "billing_kunde",
                    60);

            DevelopmentTools.createAggregationRendererInFrameFromRMIConnectionOnLocalhost(Arrays.asList(beans),
                "Ausgewählte Kunden",
                1024,
                800);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setAbrechnungsturnusIntoComboBox() {
        try {
            final MetaClass MB_MC = ClassCacheMultiple.getMetaClass("WUNDA_BLAU", "billing_abrechnungsturnus");
            String query = "SELECT " + MB_MC.getID() + ", " + MB_MC.getPrimaryKey() + " \n";
            query += "FROM " + MB_MC.getTableName();
            final MetaObject[] metaObjects = SessionManager.getProxy().getMetaObjectByQuery(query.toString(), 0);
            for (final MetaObject abrechnungsturnus : metaObjects) {
                cboAbrechnungsturnus.addItem(abrechnungsturnus.getBean());
            }
        } catch (ConnectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        ((DefaultComboBoxModel)cboAbrechnungsturnus.getModel()).insertElementAt(" ", 0);
        cboAbrechnungsturnus.setSelectedIndex(0);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static final class AggregatedBillingTableModel extends DefaultTableModel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new PointTableModel object.
         *
         * @param  data    DOCUMENT ME!
         * @param  labels  DOCUMENT ME!
         */
        public AggregatedBillingTableModel(final Object[][] data, final String[] labels) {
            super(data, labels);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   row     DOCUMENT ME!
         * @param   column  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public boolean isCellEditable(final int row, final int column) {
            if (column == 0) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param   columnIndex  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public Class<?> getColumnClass(final int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class EuroFormatterRenderer extends DefaultTableCellRenderer {

        //~ Instance fields ----------------------------------------------------

        private NumberFormat euroFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new EuroFormatterRenderer object.
         */
        public EuroFormatterRenderer() {
            this.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  value  DOCUMENT ME!
         */
        @Override
        protected void setValue(final Object value) {
            if ((value == null) || !(value instanceof Number)) {
                setText(ObjectRendererUtils.propertyPrettyPrint(value));
            } else {
                setText(euroFormatter.format(value));
            }
        }
    }
}