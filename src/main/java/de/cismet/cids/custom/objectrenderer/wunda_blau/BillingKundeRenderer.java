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

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import org.openide.util.Exceptions;

import java.awt.CardLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.custom.objectrenderer.utils.ObjectRendererUtils;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public class BillingKundeRenderer extends javax.swing.JPanel implements CidsBeanRenderer {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(BillingKundeRenderer.class);
    // column headers
    private static final String[] AGR_COMLUMN_NAMES = new String[] {
            "Geschäftsbuchnummer",
            "Projektbezeichnung",
            "Verwendung",
            "Produkt",
            "Preis",
            "MwSt-Satz",
            "Datum",
            "Benutzer"
        };

    // property names
    private static final String[] AGR_PROPERTY_NAMES = new String[] {
            "geschaeftsbuchnummer",
            "projektbezeichnung",
            "verwendungskey",
            "produktbezeichnung",
            "netto_summe",
            "mwst_satz",
            "ts",
            "angelegt_durch.login_name"
        };

    //~ Instance fields --------------------------------------------------------

    private boolean editable;
    private BillingTableModel tableModel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuchungsbeleg;
    private javax.swing.JButton btnRechnungsanlage;
    private javax.swing.ButtonGroup btngTimeFilters;
    private javax.swing.JCheckBox cboKostenfrei;
    private javax.swing.JCheckBox cboKostenpflichtig;
    private javax.swing.JComboBox cboMonth;
    private javax.swing.JComboBox cboQuarter;
    private javax.swing.JComboBox cboYear_Month;
    private javax.swing.JComboBox cboYear_Quarter;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    private javax.swing.JLabel lblFilterResult;
    private javax.swing.JPanel pnlDateRange;
    private javax.swing.JPanel pnlFilters;
    private javax.swing.JPanel pnlKostenart;
    private javax.swing.JPanel pnlMonth;
    private javax.swing.JPanel pnlQuarter;
    private javax.swing.JPanel pnlTimeFilters;
    private javax.swing.JPanel pnlToday;
    private javax.swing.JPanel pnlVerwendungszweck;
    private javax.swing.JTable tblBillings;
    private javax.swing.JToggleButton tbtnDateRange;
    private javax.swing.JToggleButton tbtnMonth;
    private javax.swing.JToggleButton tbtnQuarter;
    private javax.swing.JToggleButton tbtnToday;
    private javax.swing.JTextField txtGeschäftsbuchnummer;
    private javax.swing.JTextField txtProjekt;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form KundenRenderer.
     */
    public BillingKundeRenderer() {
        this(true);
    }

    /**
     * Creates a new KundeRenderer object.
     *
     * @param  editable  DOCUMENT ME!
     */
    public BillingKundeRenderer(final boolean editable) {
        this.editable = editable;
        initComponents();
        setTimeRelatedModels();
        changeVisibleTimeFilterPanel();
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

        btngTimeFilters = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlFilters = new javax.swing.JPanel();
        pnlTimeFilters = new javax.swing.JPanel();
        tbtnDateRange = new javax.swing.JToggleButton();
        tbtnQuarter = new javax.swing.JToggleButton();
        tbtnMonth = new javax.swing.JToggleButton();
        tbtnToday = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        pnlToday = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(1, 0),
                new java.awt.Dimension(1, 0),
                new java.awt.Dimension(1, 32767));
        pnlMonth = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cboMonth = new javax.swing.JComboBox();
        cboYear_Month = new javax.swing.JComboBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(1, 0),
                new java.awt.Dimension(1, 0),
                new java.awt.Dimension(1, 32767));
        pnlQuarter = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cboQuarter = new javax.swing.JComboBox();
        cboYear_Quarter = new javax.swing.JComboBox();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(1, 0),
                new java.awt.Dimension(1, 0),
                new java.awt.Dimension(1, 32767));
        pnlDateRange = new javax.swing.JPanel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtGeschäftsbuchnummer = new javax.swing.JTextField();
        txtProjekt = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        pnlVerwendungszweck = new javax.swing.JPanel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        pnlKostenart = new javax.swing.JPanel();
        cboKostenpflichtig = new javax.swing.JCheckBox();
        cboKostenfrei = new javax.swing.JCheckBox();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));
        lblFilterResult = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBillings = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnBuchungsbeleg = new javax.swing.JButton();
        btnRechnungsanlage = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(jLabel1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel2,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(jLabel2, gridBagConstraints);

        pnlFilters.setLayout(new java.awt.GridBagLayout());

        pnlTimeFilters.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 2));
        pnlTimeFilters.setLayout(new java.awt.GridBagLayout());

        btngTimeFilters.add(tbtnDateRange);
        org.openide.awt.Mnemonics.setLocalizedText(
            tbtnDateRange,
            org.openide.util.NbBundle.getMessage(
                BillingKundeRenderer.class,
                "BillingKundeRenderer.tbtnDateRange.text")); // NOI18N
        tbtnDateRange.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tbtnDateRangeActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTimeFilters.add(tbtnDateRange, gridBagConstraints);

        btngTimeFilters.add(tbtnQuarter);
        org.openide.awt.Mnemonics.setLocalizedText(
            tbtnQuarter,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.tbtnQuarter.text")); // NOI18N
        tbtnQuarter.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tbtnQuarterActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTimeFilters.add(tbtnQuarter, gridBagConstraints);

        btngTimeFilters.add(tbtnMonth);
        org.openide.awt.Mnemonics.setLocalizedText(
            tbtnMonth,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.tbtnMonth.text")); // NOI18N
        tbtnMonth.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tbtnMonthActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTimeFilters.add(tbtnMonth, gridBagConstraints);

        btngTimeFilters.add(tbtnToday);
        tbtnToday.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            tbtnToday,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.tbtnToday.text")); // NOI18N
        tbtnToday.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tbtnTodayActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTimeFilters.add(tbtnToday, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new java.awt.CardLayout());

        pnlToday.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel9,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel9.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnlToday.add(jLabel9, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlToday.add(filler2, gridBagConstraints);

        jPanel1.add(pnlToday, "pnlToday");

        pnlMonth.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel3,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 3, 3);
        pnlMonth.add(jLabel3, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel10,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel10.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 10, 3);
        pnlMonth.add(jLabel10, gridBagConstraints);

        cboMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Januar", "Februar", "März" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 3, 0);
        pnlMonth.add(cboMonth, gridBagConstraints);

        cboYear_Month.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2013" }));
        cboYear_Month.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cboYear_MonthActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 10, 0);
        pnlMonth.add(cboYear_Month, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlMonth.add(filler1, gridBagConstraints);

        jPanel1.add(pnlMonth, "pnlMonth");

        pnlQuarter.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel11,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel11.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 4, 3);
        pnlQuarter.add(jLabel11, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel12,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel12.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 10, 3);
        pnlQuarter.add(jLabel12, gridBagConstraints);

        cboQuarter.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { "Januar - März", "April - Juni", "Juli - September", "Oktober - Dezember" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 4, 0);
        pnlQuarter.add(cboQuarter, gridBagConstraints);

        cboYear_Quarter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2013" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 10, 0);
        pnlQuarter.add(cboYear_Quarter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlQuarter.add(filler4, gridBagConstraints);

        jPanel1.add(pnlQuarter, "pnlQuarter");

        pnlDateRange.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 10, 0);
        pnlDateRange.add(jXDatePicker1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel7,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel7.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 4, 3);
        pnlDateRange.add(jLabel7, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel8,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel8.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 10, 3);
        pnlDateRange.add(jLabel8, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 4, 0);
        pnlDateRange.add(jXDatePicker2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weightx = 1.0;
        pnlDateRange.add(filler3, gridBagConstraints);

        jPanel1.add(pnlDateRange, "pnlDateRange");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlTimeFilters.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlFilters.add(pnlTimeFilters, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel6,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel3.add(jLabel6, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel5,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel3.add(jLabel5, gridBagConstraints);

        txtGeschäftsbuchnummer.setText(org.openide.util.NbBundle.getMessage(
                BillingKundeRenderer.class,
                "BillingKundeRenderer.txtGeschäftsbuchnummer.text")); // NOI18N
        txtGeschäftsbuchnummer.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    txtGeschäftsbuchnummerActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(txtGeschäftsbuchnummer, gridBagConstraints);

        txtProjekt.setText(org.openide.util.NbBundle.getMessage(
                BillingKundeRenderer.class,
                "BillingKundeRenderer.txtProjekt.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(txtProjekt, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jComboBox1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel4,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel3.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlFilters.add(jPanel3, gridBagConstraints);

        pnlVerwendungszweck.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    BillingKundeRenderer.class,
                    "BillingKundeRenderer.pnlVerwendungszweck.border.title"))); // NOI18N
        pnlVerwendungszweck.setLayout(new javax.swing.BoxLayout(pnlVerwendungszweck, javax.swing.BoxLayout.Y_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(
            jCheckBox2,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jCheckBox2.text")); // NOI18N
        pnlVerwendungszweck.add(jCheckBox2);

        org.openide.awt.Mnemonics.setLocalizedText(
            jCheckBox3,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jCheckBox3.text")); // NOI18N
        pnlVerwendungszweck.add(jCheckBox3);

        org.openide.awt.Mnemonics.setLocalizedText(
            jCheckBox4,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jCheckBox4.text")); // NOI18N
        pnlVerwendungszweck.add(jCheckBox4);

        org.openide.awt.Mnemonics.setLocalizedText(
            jCheckBox1,
            org.openide.util.NbBundle.getMessage(BillingKundeRenderer.class, "BillingKundeRenderer.jCheckBox1.text")); // NOI18N
        pnlVerwendungszweck.add(jCheckBox1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlFilters.add(pnlVerwendungszweck, gridBagConstraints);

        pnlKostenart.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    BillingKundeRenderer.class,
                    "BillingKundeRenderer.pnlKostenart.border.title"))); // NOI18N
        pnlKostenart.setLayout(new javax.swing.BoxLayout(pnlKostenart, javax.swing.BoxLayout.Y_AXIS));

        cboKostenpflichtig.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            cboKostenpflichtig,
            org.openide.util.NbBundle.getMessage(
                BillingKundeRenderer.class,
                "BillingKundeRenderer.cboKostenpflichtig.text")); // NOI18N
        pnlKostenart.add(cboKostenpflichtig);

        cboKostenfrei.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            cboKostenfrei,
            org.openide.util.NbBundle.getMessage(
                BillingKundeRenderer.class,
                "BillingKundeRenderer.cboKostenfrei.text")); // NOI18N
        pnlKostenart.add(cboKostenfrei);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlFilters.add(pnlKostenart, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        pnlFilters.add(filler5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(pnlFilters, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblFilterResult,
            org.openide.util.NbBundle.getMessage(
                BillingKundeRenderer.class,
                "BillingKundeRenderer.lblFilterResult.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 2, 0);
        add(lblFilterResult, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(453, 275));

        tblBillings.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                    { null, null, null, null },
                    { null, null, null, null },
                    { null, null, null, null },
                    { null, null, null, null }
                },
                new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane1.setViewportView(tblBillings);
        tblBillings.getColumnModel()
                .getColumn(0)
                .setHeaderValue(org.openide.util.NbBundle.getMessage(
                        BillingKundeRenderer.class,
                        "BillingKundeRenderer.tblBillings.columnModel.title0")); // NOI18N
        tblBillings.getColumnModel()
                .getColumn(1)
                .setHeaderValue(org.openide.util.NbBundle.getMessage(
                        BillingKundeRenderer.class,
                        "BillingKundeRenderer.tblBillings.columnModel.title1")); // NOI18N
        tblBillings.getColumnModel()
                .getColumn(2)
                .setHeaderValue(org.openide.util.NbBundle.getMessage(
                        BillingKundeRenderer.class,
                        "BillingKundeRenderer.tblBillings.columnModel.title2")); // NOI18N
        tblBillings.getColumnModel()
                .getColumn(3)
                .setHeaderValue(org.openide.util.NbBundle.getMessage(
                        BillingKundeRenderer.class,
                        "BillingKundeRenderer.tblBillings.columnModel.title3")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 0);
        add(jScrollPane1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            btnBuchungsbeleg,
            org.openide.util.NbBundle.getMessage(
                BillingKundeRenderer.class,
                "BillingKundeRenderer.btnBuchungsbeleg.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanel2.add(btnBuchungsbeleg, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnRechnungsanlage,
            org.openide.util.NbBundle.getMessage(
                BillingKundeRenderer.class,
                "BillingKundeRenderer.btnRechnungsanlage.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        jPanel2.add(btnRechnungsanlage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        add(jPanel2, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cboYear_MonthActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboYear_MonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboYear_MonthActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tbtnTodayActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnTodayActionPerformed
        changeVisibleTimeFilterPanel();
    }//GEN-LAST:event_tbtnTodayActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tbtnMonthActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnMonthActionPerformed
        changeVisibleTimeFilterPanel();
    }//GEN-LAST:event_tbtnMonthActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tbtnQuarterActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnQuarterActionPerformed
        changeVisibleTimeFilterPanel();
    }//GEN-LAST:event_tbtnQuarterActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tbtnDateRangeActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnDateRangeActionPerformed
        changeVisibleTimeFilterPanel();
    }//GEN-LAST:event_tbtnDateRangeActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void txtGeschäftsbuchnummerActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGeschäftsbuchnummerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGeschäftsbuchnummerActionPerformed

    @Override
    public CidsBean getCidsBean() {
        return null;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCidsBean(final CidsBean kundeBean) {
        try {
            final List<CidsBean> benutzerBeans = kundeBean.getBeanCollectionProperty("benutzer");
            final String benutzerIDs = benutzerBeans.get(0).getProperty("id").toString();

            final MetaClass MB_MC = ClassCacheMultiple.getMetaClass("WUNDA_BLAU", "billing_billing");
            String query = "SELECT " + MB_MC.getID() + ", " + MB_MC.getPrimaryKey() + " ";
            query += "FROM " + MB_MC.getTableName();
            query += " WHERE angelegt_durch = " + benutzerIDs;
            final MetaObject[] metaObjects = SessionManager.getProxy().getMetaObjectByQuery(query, 0);

            final List<CidsBean> billingBeans = new ArrayList<CidsBean>(metaObjects.length);
            for (final MetaObject mo : metaObjects) {
                billingBeans.add(mo.getBean());
            }

            fillBillingTable(billingBeans);
        } catch (ConnectionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void dispose() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        return "Kunden Test";
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTitle(final String title) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * DOCUMENT ME!
     */
    private void setTimeRelatedModels() {
        final String[] months = getMonthStrings();
        cboMonth.setModel(new javax.swing.DefaultComboBoxModel(months));

        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final String[] years = new String[100];
        for (int i = 0; i < 100; i++) {
            years[i] = Integer.toString(currentYear - i);
        }
        cboYear_Month.setModel(new javax.swing.DefaultComboBoxModel(years));
        cboYear_Quarter.setModel(new javax.swing.DefaultComboBoxModel(years));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
//        final JFrame frame = new JFrame();
//        frame.add(new KundeRenderer());
//        frame.pack();
//        frame.setVisible(true);
        DevelopmentTools.createRendererInFrameFromRMIConnectionOnLocalhost(
            "WUNDA_BLAU",
            "Administratoren",
            "admin",
            "kif",
            "billing_kunde",
            1,
            "Foo",
            1280,
            1024);
    }

    /**
     * DOCUMENT ME!
     */
    private void changeVisibleTimeFilterPanel() {
        final CardLayout cardLayout = (CardLayout)jPanel1.getLayout();
        if (tbtnToday.isSelected()) {
            cardLayout.show(jPanel1, "pnlToday");
        } else if (tbtnMonth.isSelected()) {
            cardLayout.show(jPanel1, "pnlMonth");
        } else if (tbtnQuarter.isSelected()) {
            cardLayout.show(jPanel1, "pnlQuarter");
        } else if (tbtnDateRange.isSelected()) {
            cardLayout.show(jPanel1, "pnlDateRange");
        } else {
            LOG.warn("No toggle button, to show a time filter, is selected. This should never happen.");
        }
    }

    /**
     * DateFormatSymbols returns an extra, empty value at the end of the array of months. Remove it.
     *
     * @return  DOCUMENT ME!
     */
    private static String[] getMonthStrings() {
        final String[] months = new java.text.DateFormatSymbols().getMonths();
        final int lastIndex = months.length - 1;

        if ((months[lastIndex] == null)
                    || (months[lastIndex].length() <= 0)) { // last item empty
            final String[] monthStrings = new String[lastIndex];
            System.arraycopy(months, 0,
                monthStrings, 0, lastIndex);
            return monthStrings;
        } else {                                            // last item not empty
            return months;
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @param  billingBeans  DOCUMENT ME!
     */
    private void fillBillingTable(final Collection<CidsBean> billingBeans) {
        final List<Object[]> tableData = new ArrayList<Object[]>();

        for (final CidsBean punktBean : billingBeans) {
            tableData.add(cidsBean2Row(punktBean));
        }
        tableModel = new BillingTableModel(tableData.toArray(new Object[tableData.size()][]), AGR_COMLUMN_NAMES);
        tblBillings.setModel(tableModel);
    }

    /**
     * Extracts the date from a CidsBean into an Object[] -> table row. (Collection attributes are flatened to
     * comaseparated lists)
     *
     * @param   billingBean  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Object[] cidsBean2Row(final CidsBean billingBean) {
        if (billingBean != null) {
            final Object[] result = new Object[AGR_COMLUMN_NAMES.length];
            for (int i = 0; i < AGR_PROPERTY_NAMES.length; ++i) {
                final Object property = billingBean.getProperty(AGR_PROPERTY_NAMES[i]);
                final String propertyString;
                propertyString = ObjectRendererUtils.propertyPrettyPrint(property);
                result[i] = propertyString;
            }
            return result;
        }
        return new Object[0];
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static final class BillingTableModel extends DefaultTableModel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new PointTableModel object.
         *
         * @param  data    DOCUMENT ME!
         * @param  labels  DOCUMENT ME!
         */
        public BillingTableModel(final Object[][] data, final String[] labels) {
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
            return false;
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
            return super.getColumnClass(columnIndex);
        }
    }
}