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

import Sirius.navigator.actiontag.ActionTagProtected;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.search.CidsSearchExecutor;
import Sirius.navigator.search.dynamic.SearchControlListener;
import Sirius.navigator.search.dynamic.SearchControlPanel;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import com.vividsolutions.jts.geom.Geometry;

import org.apache.commons.lang.StringUtils;

import org.jdesktop.swingx.JXList;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

import java.awt.Dimension;
import java.awt.Frame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.lang.reflect.InvocationTargetException;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.custom.objecteditors.wunda_blau.Sb_stadtbildserieEditor;
import de.cismet.cids.custom.objecteditors.wunda_blau.Sb_stadtbildserieEditorAddSuchwortDialog;
import de.cismet.cids.custom.objectrenderer.utils.CidsBeanSupport;
import de.cismet.cids.custom.objectrenderer.utils.ObjectRendererUtils;
import de.cismet.cids.custom.utils.Sb_stadtbildUtils;
import de.cismet.cids.custom.wunda_blau.search.server.MetaObjectNodesStadtbildSerieSearchStatement;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.FastBindableReferenceCombo;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.server.search.MetaObjectNodeServerSearch;

import de.cismet.cids.tools.search.clientstuff.CidsWindowSearch;

import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.cismap.navigatorplugin.GeoSearchButton;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsWindowSearch.class)
public class Sb_StadtbildWindowSearch extends javax.swing.JPanel implements CidsWindowSearch,
    ActionTagProtected,
    SearchControlListener,
    PropertyChangeListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            Sb_StadtbildWindowSearch.class);
    private static final String ACTION_TAG = "custom.stadtbilder.search@WUNDA_BLAU";
    private static final Pattern SIX_DIGIT_INTEGER_PATTERN = Pattern.compile("\\d{6}");

    //~ Instance fields --------------------------------------------------------

    private MetaClass metaClass;
    private GeoSearchButton btnGeoSearch;
    private MappingComponent mappingComponent;
    private ImageIcon icon;
    private boolean geoSearchEnabled;
    private CidsBean cidsBean;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddSuchwort;
    private javax.swing.JButton btnRemoveSuchwort;
    private javax.swing.JCheckBox cbMapSearch;
    private javax.swing.JComboBox cboImageNrFrom;
    private javax.swing.JComboBox cboImageNrTo;
    private javax.swing.JComboBox cboOrt;
    private javax.swing.JComboBox cboStreet;
    private javax.swing.JCheckBox chboBodennaheAufnahme;
    private javax.swing.JCheckBox chboLuftbildschraegaufnahme;
    private javax.swing.JCheckBox chboLuftbildsenkrechtaufnahme;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblHausnummer;
    private javax.swing.JLabel lblOrtsname;
    private javax.swing.JLabel lblStrasse;
    private javax.swing.JList lstSuchworte;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlCtrlButtons1;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlImageNumber;
    private javax.swing.JPanel pnlKindOfImage;
    private javax.swing.JPanel pnlScrollPane;
    private javax.swing.JPanel pnlSearchWords;
    private javax.swing.JPanel pnlStrassenzuordnung;
    private de.cismet.cids.custom.wunda_blau.search.Sb_StadtbildTimeTabs sb_StadtbilderTimeTabs;
    private javax.swing.JTextField txtHausnummer;
    private javax.swing.JTextField txtImageNrFrom;
    private javax.swing.JTextField txtImageNrTo;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form Sb_StadtbildWindowSearch.
     */
    public Sb_StadtbildWindowSearch() {
        initComponents();

        final JPanel pnlSearchCancel = new CountSearchResultsSearchControlPanel(this);
        final Dimension max = pnlSearchCancel.getMaximumSize();
        final Dimension min = pnlSearchCancel.getMinimumSize();
        final Dimension pre = pnlSearchCancel.getPreferredSize();
        pnlSearchCancel.setMaximumSize(new java.awt.Dimension(
                new Double(max.getWidth()).intValue(),
                new Double(max.getHeight() + 5).intValue()));
        pnlSearchCancel.setMinimumSize(new java.awt.Dimension(
                new Double(min.getWidth()).intValue(),
                new Double(min.getHeight() + 5).intValue()));
        pnlSearchCancel.setPreferredSize(new java.awt.Dimension(
                new Double(pre.getWidth() + 6).intValue(),
                new Double(pre.getHeight() + 5).intValue()));
        pnlButtons.add(pnlSearchCancel);

        metaClass = ClassCacheMultiple.getMetaClass(CidsBeanSupport.DOMAIN_NAME, "sb_stadtbildserie"); // NOI18N

        byte[] iconDataFromMetaclass = new byte[] {};

        if (metaClass != null) {
            iconDataFromMetaclass = metaClass.getIconData();
        }

        if (iconDataFromMetaclass.length > 0) {
            LOG.info("Using icon from metaclass.");                                                              // NOI18N
            icon = new ImageIcon(metaClass.getIconData());
        } else {
            LOG.warn("Metaclass icon is not set. Trying to load default icon.");                                 // NOI18N
            final URL urlToIcon = getClass().getResource("/de/cismet/cids/custom/wunda_blau/search/search.png"); // NOI18N

            if (urlToIcon != null) {
                icon = new ImageIcon(urlToIcon);
            } else {
                icon = new ImageIcon(new byte[] {});
            }
        }

        pnlButtons.add(Box.createHorizontalStrut(5));

        mappingComponent = CismapBroker.getInstance().getMappingComponent();
        geoSearchEnabled = mappingComponent != null;
        if (geoSearchEnabled) {
            final Sb_StadtbildserieCreateSearchGeometryListener stadtbildserieCreateSearchGeometryListener =
                new Sb_StadtbildserieCreateSearchGeometryListener(
                    mappingComponent,
                    new Sb_StadtbildSearchTooltip(icon));
            stadtbildserieCreateSearchGeometryListener.addPropertyChangeListener(this);
            btnGeoSearch = new GeoSearchButton(
                    Sb_StadtbildserieCreateSearchGeometryListener.STADTBILDSERIE_CREATE_SEARCH_GEOMETRY,
                    mappingComponent,
                    null,
                    org.openide.util.NbBundle.getMessage(
                        Sb_StadtbildWindowSearch.class,
                        "Sb_StadtbildWindowSearch.btnGeoSearch.toolTipText")); // NOI18N
            pnlButtons.add(btnGeoSearch);
        }

        bindingGroup.unbind();

        try {
            cidsBean = CidsBeanSupport.createNewCidsBeanFromTableName("sb_stadtbildserie");
            DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                bindingGroup,
                cidsBean);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
        bindingGroup.bind();

        cboOrt.setSelectedItem(Sb_stadtbildUtils.getWUPPERTAL());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        pnlScrollPane = new javax.swing.JPanel();
        pnlKindOfImage = new javax.swing.JPanel();
        chboLuftbildschraegaufnahme = new javax.swing.JCheckBox();
        chboLuftbildsenkrechtaufnahme = new javax.swing.JCheckBox();
        chboBodennaheAufnahme = new javax.swing.JCheckBox();
        pnlImageNumber = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboImageNrFrom = new FastBindableReferenceCombo("%1$2s", new String[] { "BILDNUMMER" });
        jLabel2 = new javax.swing.JLabel();
        cboImageNrTo = new FastBindableReferenceCombo("%1$2s", new String[] { "BILDNUMMER" });
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        txtImageNrTo = new javax.swing.JTextField();
        txtImageNrFrom = new javax.swing.JTextField();
        pnlSearchWords = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSuchworte = new Sb_stadtbildserieEditor.JXListBugFixes();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        pnlCtrlButtons1 = new javax.swing.JPanel();
        btnAddSuchwort = new javax.swing.JButton();
        btnRemoveSuchwort = new javax.swing.JButton();
        sb_StadtbilderTimeTabs = new de.cismet.cids.custom.wunda_blau.search.Sb_StadtbildTimeTabs();
        pnlStrassenzuordnung = new javax.swing.JPanel();
        lblStrasse = new javax.swing.JLabel();
        cboStreet = new FastBindableReferenceCombo();
        lblOrtsname = new javax.swing.JLabel();
        cboOrt = new FastBindableReferenceCombo();
        lblHausnummer = new javax.swing.JLabel();
        txtHausnummer = new javax.swing.JTextField();
        pnlFooter = new javax.swing.JPanel();
        cbMapSearch = new javax.swing.JCheckBox();
        pnlButtons = new javax.swing.JPanel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));

        setPreferredSize(new java.awt.Dimension(70, 20));
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        pnlScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnlScrollPane.setLayout(new java.awt.GridBagLayout());

        pnlKindOfImage.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    Sb_StadtbildWindowSearch.class,
                    "Sb_StadtbildWindowSearch.pnlKindOfImage.border.title"))); // NOI18N
        pnlKindOfImage.setLayout(new javax.swing.BoxLayout(pnlKindOfImage, javax.swing.BoxLayout.PAGE_AXIS));

        chboLuftbildschraegaufnahme.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            chboLuftbildschraegaufnahme,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.chboLuftbildschraegaufnahme.text")); // NOI18N
        pnlKindOfImage.add(chboLuftbildschraegaufnahme);

        chboLuftbildsenkrechtaufnahme.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            chboLuftbildsenkrechtaufnahme,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.chboLuftbildsenkrechtaufnahme.text")); // NOI18N
        pnlKindOfImage.add(chboLuftbildsenkrechtaufnahme);

        chboBodennaheAufnahme.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(
            chboBodennaheAufnahme,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.chboBodennaheAufnahme.text")); // NOI18N
        pnlKindOfImage.add(chboBodennaheAufnahme);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 20);
        pnlScrollPane.add(pnlKindOfImage, gridBagConstraints);

        pnlImageNumber.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    Sb_StadtbildWindowSearch.class,
                    "Sb_StadtbildWindowSearch.pnlImageNumber.border.title"))); // NOI18N
        pnlImageNumber.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlImageNumber.add(jLabel1, gridBagConstraints);

        ((FastBindableReferenceCombo)cboImageNrFrom).setSorted(true);
        cboImageNrFrom.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 20);
        pnlImageNumber.add(cboImageNrFrom, gridBagConstraints);
        StaticSwingTools.decorateWithFixedAutoCompleteDecorator(cboImageNrFrom);
        cboImageNrFrom.setVisible(false);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel2,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        pnlImageNumber.add(jLabel2, gridBagConstraints);

        ((FastBindableReferenceCombo)cboImageNrTo).setSorted(true);
        cboImageNrTo.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        pnlImageNumber.add(cboImageNrTo, gridBagConstraints);
        StaticSwingTools.decorateWithFixedAutoCompleteDecorator(cboImageNrTo);
        cboImageNrTo.setVisible(false);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlImageNumber.add(filler2, gridBagConstraints);

        txtImageNrTo.setText(org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.txtImageNrTo.text"));        // NOI18N
        txtImageNrTo.setToolTipText(org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.txtImageNrTo.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 20);
        pnlImageNumber.add(txtImageNrTo, gridBagConstraints);

        txtImageNrFrom.setText(org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.txtImageNrFrom.text"));        // NOI18N
        txtImageNrFrom.setToolTipText(org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.txtImageNrFrom.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 20);
        pnlImageNumber.add(txtImageNrFrom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 20);
        pnlScrollPane.add(pnlImageNumber, gridBagConstraints);

        pnlSearchWords.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    Sb_StadtbildWindowSearch.class,
                    "Sb_StadtbildWindowSearch.pnlSearchWords.border.title"))); // NOI18N
        pnlSearchWords.setLayout(new java.awt.GridBagLayout());

        lstSuchworte.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(lstSuchworte);
        ((JXList)lstSuchworte).setAutoCreateRowSorter(true);
        ((JXList)lstSuchworte).setSortOrder(SortOrder.ASCENDING);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 20);
        pnlSearchWords.add(jScrollPane2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlSearchWords.add(filler3, gridBagConstraints);

        pnlCtrlButtons1.setOpaque(false);
        pnlCtrlButtons1.setLayout(new java.awt.GridBagLayout());

        btnAddSuchwort.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            btnAddSuchwort,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.btnAddSuchwort.text"));                                              // NOI18N
        btnAddSuchwort.setPreferredSize(new java.awt.Dimension(46, 21));
        btnAddSuchwort.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnAddSuchwortActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 10);
        pnlCtrlButtons1.add(btnAddSuchwort, gridBagConstraints);

        btnRemoveSuchwort.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_remove_mini.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            btnRemoveSuchwort,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.btnRemoveSuchwort.text"));                                              // NOI18N
        btnRemoveSuchwort.setPreferredSize(new java.awt.Dimension(46, 21));
        btnRemoveSuchwort.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnRemoveSuchwortActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 5, 10);
        pnlCtrlButtons1.add(btnRemoveSuchwort, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlSearchWords.add(pnlCtrlButtons1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 20);
        pnlScrollPane.add(pnlSearchWords, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 17, 5, 22);
        pnlScrollPane.add(sb_StadtbilderTimeTabs, gridBagConstraints);

        pnlStrassenzuordnung.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    Sb_StadtbildWindowSearch.class,
                    "Sb_StadtbildWindowSearch.pnlStrassenzuordnung.border.title"))); // NOI18N
        pnlStrassenzuordnung.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            lblStrasse,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.lblStrasse.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlStrassenzuordnung.add(lblStrasse, gridBagConstraints);

        ((FastBindableReferenceCombo)cboStreet).setSorted(true);
        cboStreet.setEditable(true);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_ONCE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.strasse}"),
                cboStreet,
                org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        pnlStrassenzuordnung.add(cboStreet, gridBagConstraints);
        StaticSwingTools.decorateWithFixedAutoCompleteDecorator(cboStreet);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblOrtsname,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.lblOrtsname.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlStrassenzuordnung.add(lblOrtsname, gridBagConstraints);

        ((FastBindableReferenceCombo)cboOrt).setSorted(true);
        cboOrt.setEditable(true);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_ONCE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.ort}"),
                cboOrt,
                org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        cboOrt.addItemListener(new java.awt.event.ItemListener() {

                @Override
                public void itemStateChanged(final java.awt.event.ItemEvent evt) {
                    cboOrtItemStateChanged(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        pnlStrassenzuordnung.add(cboOrt, gridBagConstraints);
        StaticSwingTools.decorateWithFixedAutoCompleteDecorator(cboOrt);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblHausnummer,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.lblHausnummer.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlStrassenzuordnung.add(lblHausnummer, gridBagConstraints);

        txtHausnummer.setText(org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.txtHausnummer.text"));        // NOI18N
        txtHausnummer.setToolTipText(org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.txtHausnummer.toolTipText")); // NOI18N
        txtHausnummer.setPreferredSize(new java.awt.Dimension(56, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        pnlStrassenzuordnung.add(txtHausnummer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 20);
        pnlScrollPane.add(pnlStrassenzuordnung, gridBagConstraints);

        pnlFooter.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            cbMapSearch,
            org.openide.util.NbBundle.getMessage(
                Sb_StadtbildWindowSearch.class,
                "Sb_StadtbildWindowSearch.cbMapSearch.text")); // NOI18N
        cbMapSearch.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cbMapSearchActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 5, 0);
        pnlFooter.add(cbMapSearch, gridBagConstraints);

        pnlButtons.setLayout(new javax.swing.BoxLayout(pnlButtons, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlFooter.add(pnlButtons, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlFooter.add(filler4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 20);
        pnlScrollPane.add(pnlFooter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        pnlScrollPane.add(filler1, gridBagConstraints);

        jScrollPane1.setViewportView(pnlScrollPane);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        bindingGroup.bind();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cbMapSearchActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cbMapSearchActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_cbMapSearchActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnAddSuchwortActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnAddSuchwortActionPerformed
        final Sb_stadtbildserieEditorAddSuchwortDialog dialog = new Sb_stadtbildserieEditorAddSuchwortDialog((Frame)
                SwingUtilities.getWindowAncestor(this),
                true);
        final CidsBean newSuchwort = dialog.showDialog();
        if (newSuchwort != null) {
            final DefaultListModel dlm = (DefaultListModel)lstSuchworte.getModel();
            dlm.addElement(newSuchwort);
        }
    }                                                                                  //GEN-LAST:event_btnAddSuchwortActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnRemoveSuchwortActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnRemoveSuchwortActionPerformed
        final DefaultListModel dlm = (DefaultListModel)lstSuchworte.getModel();

        if (this.lstSuchworte.getSelectedIndices().length > 0) {
            final int[] selectedIndices = lstSuchworte.getSelectedIndices();
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                dlm.removeElementAt(((JXList)lstSuchworte).convertIndexToModel(selectedIndices[i]));
            }
        }
    } //GEN-LAST:event_btnRemoveSuchwortActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cboOrtItemStateChanged(final java.awt.event.ItemEvent evt) { //GEN-FIRST:event_cboOrtItemStateChanged
        final Object selectedItem = cboOrt.getSelectedItem();
        if ((selectedItem != null) && selectedItem.equals(Sb_stadtbildUtils.getWUPPERTAL())) {
            // inside of Wuppertal
            cboStreet.setEnabled(true);
            lblStrasse.setEnabled(true);
            txtHausnummer.setEnabled(true);
            lblHausnummer.setEnabled(true);
        } else {
            // outside of Wuppertal
            cboStreet.setEnabled(false);
            cboStreet.setSelectedItem(null);
            lblStrasse.setEnabled(false);
            txtHausnummer.setEnabled(false);
            txtHausnummer.setText("");
            lblHausnummer.setEnabled(false);
        }
    } //GEN-LAST:event_cboOrtItemStateChanged

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        try {
            DevelopmentTools.initSessionManagerFromRMIConnectionOnLocalhost(
                "WUNDA_BLAU",      // NOI18N
                "Administratoren", // NOI18N
                "admin",           // NOI18N
                "kif");            // NOI18N
            final JScrollPane jsp = new JScrollPane(new Sb_StadtbildWindowSearch());
            DevelopmentTools.showTestFrame(jsp, 800, 1000);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public JComponent getSearchWindowComponent() {
        return this;
    }

    @Override
    public MetaObjectNodeServerSearch getServerSearch() {
        return getServerSearch(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   geometry  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */

    public MetaObjectNodeServerSearch getServerSearch(final Geometry geometry) {
        final MetaObjectNodesStadtbildSerieSearchStatement stadtbildSerieSearchStatement =
            new MetaObjectNodesStadtbildSerieSearchStatement(SessionManager.getSession().getUser());

        final String imageNrFrom = txtImageNrFrom.getText();
        final String imageNrTo = txtImageNrTo.getText();
        if (StringUtils.isNotBlank(imageNrFrom) && StringUtils.isNotBlank(imageNrTo)) {
            if (!SIX_DIGIT_INTEGER_PATTERN.matcher(imageNrFrom).matches()
                        || !SIX_DIGIT_INTEGER_PATTERN.matcher(imageNrTo).matches()) {
                JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(
                        Sb_StadtbildWindowSearch.class,
                        "Sb_StadtbildWindowSearch.getServerSearch().dialog.message"),
                    NbBundle.getMessage(
                        Sb_StadtbildWindowSearch.class,
                        "Sb_StadtbildWindowSearch.getServerSearch().dialog.title"),
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        stadtbildSerieSearchStatement.setImageNrTo(imageNrTo);
        stadtbildSerieSearchStatement.setImageNrFrom(imageNrFrom);

        final ArrayList<MetaObjectNodesStadtbildSerieSearchStatement.Bildtyp> bildtyp =
            new ArrayList<MetaObjectNodesStadtbildSerieSearchStatement.Bildtyp>();
        if (chboBodennaheAufnahme.isSelected()) {
            bildtyp.add(MetaObjectNodesStadtbildSerieSearchStatement.Bildtyp.BODENNAH);
        }
        if (chboLuftbildschraegaufnahme.isSelected()) {
            bildtyp.add(MetaObjectNodesStadtbildSerieSearchStatement.Bildtyp.LUFTSCHRAEG);
        }
        if (chboLuftbildsenkrechtaufnahme.isSelected()) {
            bildtyp.add(MetaObjectNodesStadtbildSerieSearchStatement.Bildtyp.LUFTSENK);
        }
        stadtbildSerieSearchStatement.setBildtypen(bildtyp);

        final ArrayList<Integer> suchwortIDs = new ArrayList<Integer>();
        for (final Object object : ((DefaultListModel<CidsBean>)lstSuchworte.getModel()).toArray()) {
            final Integer id = ((CidsBean)object).getPrimaryKeyValue();
            suchwortIDs.add(id);
        }
        stadtbildSerieSearchStatement.setSuchwoerterIDs(suchwortIDs);

        final Date[] fromDate_tillDate = sb_StadtbilderTimeTabs.chooseDates();
        stadtbildSerieSearchStatement.setFrom(fromDate_tillDate[0]);
        stadtbildSerieSearchStatement.setTill(fromDate_tillDate[1]);

        final CidsBean strasse = (CidsBean)cboStreet.getSelectedItem();
        if (strasse != null) {
            stadtbildSerieSearchStatement.setStreetID(strasse.getPrimaryKeyValue().toString());
        }

        final CidsBean ort = (CidsBean)cboOrt.getSelectedItem();
        if (ort != null) {
            stadtbildSerieSearchStatement.setOrtID(ort.getPrimaryKeyValue().toString());
        }

        final String hausnummer = txtHausnummer.getText();
        stadtbildSerieSearchStatement.setHausnummer(hausnummer);

        // Geometry
        Geometry geometryToSearchFor = null;
        if (geometry != null) {
            geometryToSearchFor = geometry;
        } else {
            if (cbMapSearch.isSelected()) {
                geometryToSearchFor =
                    ((XBoundingBox)CismapBroker.getInstance().getMappingComponent().getCurrentBoundingBox())
                            .getGeometry();
            }
        }
        final Geometry transformedBoundingBox;
        if (geometryToSearchFor != null) {
            transformedBoundingBox = CrsTransformer.transformToDefaultCrs(geometryToSearchFor);
            transformedBoundingBox.setSRID(CismapBroker.getInstance().getDefaultCrsAlias());
        } else {
            transformedBoundingBox = null;
        }
        stadtbildSerieSearchStatement.setGeometryToSearchFor(transformedBoundingBox);

        return stadtbildSerieSearchStatement;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(Sb_StadtbildWindowSearch.class, "Sb_StadtbildWindowSearch.name");
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public MetaObjectNodeServerSearch assembleSearch() {
        return getServerSearch();
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void searchStarted() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param  numberOfResults  DOCUMENT ME!
     */
    @Override
    public void searchDone(final int numberOfResults) {
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void searchCanceled() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean suppressEmptyResultMessage() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean checkActionTag() {
        return ObjectRendererUtils.checkActionTag(ACTION_TAG);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (Sb_StadtbildserieCreateSearchGeometryListener.ACTION_SEARCH_STARTED.equals(evt.getPropertyName())) {
            if ((evt.getNewValue() != null) && (evt.getNewValue() instanceof Geometry)) {
                final MetaObjectNodeServerSearch search = getServerSearch((Geometry)evt.getNewValue());
                CidsSearchExecutor.searchAndDisplayResultsWithDialog(search);
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * A subclass of SearchControlPanel, which checks first how many results (Stadtbildserien) were found.
     *
     * @version  $Revision$, $Date$
     */
    private class CountSearchResultsSearchControlPanel extends SearchControlPanel {

        //~ Instance fields ----------------------------------------------------

        boolean showResults;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new CountSearchResultsSearchControlPanel object.
         *
         * @param  listener  DOCUMENT ME!
         */
        public CountSearchResultsSearchControlPanel(final SearchControlListener listener) {
            super(listener);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean checkIfSearchShouldBeStarted(final SwingWorker calledBySwingWorker,
                final MetaObjectNodeServerSearch search) {
            if (search instanceof MetaObjectNodesStadtbildSerieSearchStatement) {
                Integer amountResults = 0;
                try {
                    ((MetaObjectNodesStadtbildSerieSearchStatement)search).setPreparationExecution(true);
                    final Collection searchResult = SessionManager.getProxy()
                                .customServerSearch(SessionManager.getSession().getUser(), search);
                    if (!searchResult.isEmpty()) {
                        final Object firstResult = searchResult.toArray()[0];
                        if (firstResult instanceof Integer) {
                            amountResults = (Integer)firstResult;
                        }
                    }
                } catch (ConnectionException ex) {
                    LOG.error(ex, ex);
                } finally {
                    ((MetaObjectNodesStadtbildSerieSearchStatement)search).setPreparationExecution(false);
                }

                if (amountResults < 100) {
                    return true;
                } else {
                    showResults = false;
                    try {
                        final int amountOfResults = amountResults;
                        if (!calledBySwingWorker.isCancelled()) {
                            SwingUtilities.invokeAndWait(new Runnable() {

                                    @Override
                                    public void run() {
                                        final int choosenOption = JOptionPane.showConfirmDialog(
                                                StaticSwingTools.getParentFrame(
                                                    CountSearchResultsSearchControlPanel.this),
                                                "Es wurden "
                                                        + amountOfResults
                                                        + " Stadtbildserien gefunden. Sollen diese angezeigt werden?",
                                                "Große Anzahl an Suchergebnissen",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE);
                                        showResults = choosenOption == JOptionPane.YES_OPTION;
                                    }
                                });
                        }
                    } catch (InterruptedException ex) {
                        LOG.error("Search results will not be shown.", ex);
                    } catch (InvocationTargetException ex) {
                        LOG.error("Search results will not be shown.", ex);
                    }
                    return showResults;
                }
            } else {
                return true;
            }
        }
    }
}