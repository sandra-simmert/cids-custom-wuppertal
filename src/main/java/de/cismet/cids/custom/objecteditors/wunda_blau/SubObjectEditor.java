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
package de.cismet.cids.custom.objecteditors.wunda_blau;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;

import com.vividsolutions.jts.geom.Geometry;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingx.JXBusyLabel;

import org.openide.util.NbBundle;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.*;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.custom.objecteditors.utils.BaumConfProperties;
import de.cismet.cids.custom.objecteditors.utils.RendererTools;
import de.cismet.cids.custom.objecteditors.wunda_blau.albo.ComboBoxFilterDialog;
import de.cismet.cids.custom.objectrenderer.utils.DefaultPreviewMapPanel;
import de.cismet.cids.custom.wunda_blau.search.server.BaumAnsprechpartnerLightweightSearch;
import de.cismet.cids.custom.wunda_blau.search.server.BaumFotosDokLightweightSearch;
import de.cismet.cids.custom.wunda_blau.search.server.RedundantObjectSearch;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.DefaultBindableDateChooser;
import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.FastBindableReferenceCombo;
import de.cismet.cids.editors.SaveVetoable;
import de.cismet.cids.editors.hooks.BeforeSavingHook;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.cismap.cids.geometryeditor.DefaultCismapGeometryComboBoxEditor;

import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.RasterfariDocumentLoaderPanel;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.connectioncontext.ConnectionContext;


import de.cismet.tools.gui.RoundedPanel;
import de.cismet.tools.gui.SemiRoundedPanel;
import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
/**
 * DOCUMENT ME!
 *
 * @author   sandra
 * @version  $Revision$, $Date$
 */
public class SubObjectEditor extends DefaultCustomObjectEditor implements CidsBeanRenderer,
    SaveVetoable,
    BeforeSavingHook,
    PropertyChangeListener,
    RasterfariDocumentLoaderPanel.Listener {

    //~ Static fields/initializers ---------------------------------------------

    private static String THEMA;
    private static String FOTOS;
    private static String DOKUMENTE;
    private static String RASTERFARI;
    private static final String FOTOS_TOSTRING_TEMPLATE = "%s";
    private static final String[] FOTOS_TOSTRING_FIELDS = { "name" };

    private static final Logger LOG = Logger.getLogger(SubObjectEditor.class);
    public static final String REDUNDANT_TOSTRING_TEMPLATE = "%s";
    public static final String[] REDUNDANT_TOSTRING_FIELDS = { "name", "id" };
    public static final String REDUNDANT_TABLE = "baum_gebiet";

    public static final String FIELD__NAME = "name";                                        // sub_object
    public static final String FIELD__ID = "id";                                            // sub_object
    public static final String FIELD__GEOREFERENZ = "fk_geom_point";                        // sub_object
    public static final String FIELD__FOTONAME = "name";                                    // sub_fotos
    public static final String FIELD__GEO_FIELD = "geo_field";                              // geom
    public static final String FIELD__GEOREFERENZ__GEO_FIELD = "fk_geom_Point.geo_field";   // sub_object_geom_pointt
    public static final String TABLE_NAME = "sub_object";
    public static final String TABLE_FOTOS = "sub_fotos";

    public static final String BUNDLE_NONAME = "SubObjectEditor.isOkForSaving().noName";
    public static final String BUNDLE_DUPLICATENAME = "SubObjectEditor.isOkForSaving().duplicateAz";
    public static final String BUNDLE_NOGEOMPOINT = "SubObjectEditor.isOkForSaving().noGeom";
    public static final String BUNDLE_WRONGGEOMPOINT = "SubObjectEditor.isOkForSaving().wrongGeomPoint";
    public static final String BUNDLE_PANE_PREFIX = "SubObjectEditor.isOkForSaving().JOptionPane.message.prefix";
    public static final String BUNDLE_PANE_SUFFIX = "SubObjectEditor.isOkForSaving().JOptionPane.message.suffix";
    public static final String BUNDLE_PANE_TITLE = "SubObjectEditor.isOkForSaving().JOptionPane.title";
    private static final String TITLE_NEW_OBJECT = "ein neues Objekt anlegen...";
    private static Color colorAlarm = new java.awt.Color(255, 0, 0);
    private static Color colorNormal = new java.awt.Color(0, 0, 0);


    /** DOCUMENT ME! */

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private enum DocumentCard {

        //~ Enum constants -----------------------------------------------------

        BUSY, DOCUMENT, NO_DOCUMENT, ERROR
    }

    //~ Instance fields --------------------------------------------------------
    private final BaumFotosDokLightweightSearch searchFotosDok;
    
    private Boolean redundantName = false;
    private SwingWorker worker_name;

    private final boolean editor;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAddGebiet;
    private JButton btnAddObjekt;
    private JButton btnRemoveGebiet;
    private JButton btnRemoveObjekt;
    private JComboBox cbLine;
    private JComboBox cbPoint;
    private JComboBox cbPolygon;
    FastBindableReferenceCombo cbUnter;
    private JCheckBox chOffen;
    private JCheckBox chWartung;
    private ComboBoxFilterDialog comboBoxFilterDialogGebiet;
    private ComboBoxFilterDialog comboBoxFilterDialogObject;
    DefaultBindableDateChooser dcDatum;
    DefaultBindableDateChooser dcDatum1;
    private Box.Filler filler1;
    private Box.Filler filler2;
    private Box.Filler filler3;
    private Box.Filler filler4;
    private Box.Filler filler5;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanelAllgemein;
    private JPanel jPanelFotoAuswahl;
    private JPanel jPanelFotos;
    private JPanel jPanelOffen;
    private JPanel jPanelWeitInfo;
    private JPanel jPanelZu;
    private JPanel jPanelZugehoerig;
    JTabbedPane jTabbedPane;
    private JXBusyLabel jxLBusy;
    private JLabel lblAdd;
    private JLabel lblDatum;
    private JLabel lblDatum1;
    private JLabel lblFarbe;
    private JLabel lblFarbe1;
    private JLabel lblGebiet;
    private JLabel lblHeaderDocument;
    private JLabel lblHeaderListe;
    private JLabel lblHeaderPages;
    private JLabel lblKarteLine;
    private JLabel lblKartePoint;
    private JLabel lblKartePolygon;
    private JLabel lblKeineFotos;
    private JLabel lblLine;
    private JLabel lblName;
    private JLabel lblName1;
    private JLabel lblObjekt;
    private JLabel lblOffen;
    private JLabel lblPoint;
    private JLabel lblPolygon;
    private JLabel lblUnter;
    private JLabel lblWartung;
    private JLabel lblWert;
    private JList lstFotos;
    private JList lstGebiet;
    private JList lstObjekt;
    private JList lstPages;
    private JPanel panButtonsGebiet;
    private JPanel panButtonsObjekt;
    private JPanel panContent;
    private JPanel panDaten;
    private JPanel panGebiet;
    private JPanel panGeometrieLine;
    private JPanel panGeometriePoint;
    private JPanel panGeometriePolygon;
    private JPanel panInfo;
    private JPanel panLageLine;
    private JPanel panLagePoint;
    private JPanel panLagePolygon;
    private JPanel panObject;
    private JPanel panObjekt;
    private JPanel panOffen;
    private DefaultPreviewMapPanel panPreviewMapLine;
    private DefaultPreviewMapPanel panPreviewMapPoint;
    private DefaultPreviewMapPanel panPreviewMapPolygon;
    private JPanel pnlBild;
    private JPanel pnlCard1;
    private RoundedPanel pnlDocument;
    private SemiRoundedPanel pnlHeaderDocument;
    private SemiRoundedPanel pnlHeaderListe;
    private SemiRoundedPanel pnlHeaderPages;
    private RoundedPanel pnlListe;
    private RoundedPanel pnlPages;
    private RasterfariDocumentLoaderPanel rasterfariDocumentLoaderPanel1;
    private RoundedPanel rpKarteLine;
    private RoundedPanel rpKartePoint;
    private RoundedPanel rpKartePolygon;
    private JScrollPane scpBemerkung;
    private JScrollPane scpFotos;
    private JScrollPane scpLstGebiet;
    private JScrollPane scpLstObjekt;
    private JScrollPane scpPages;
    private SemiRoundedPanel semiRoundedPanel7;
    private SemiRoundedPanel semiRoundedPanel8;
    private SemiRoundedPanel semiRoundedPanel9;
    private JTextArea taBemerkung;
    private JTextField txtAdd;
    private JTextField txtFarbe;
    private JTextField txtFarbe1;
    private JTextField txtName;
    private JTextField txtName1;
    private JTextField txtWert;
    private BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form.
     */
    public SubObjectEditor() {
        this(true);
    }

    /**
     * Creates a new SubObjectEditor object.
     *
     * @param  boolEditor  DOCUMENT ME!
     */
    public SubObjectEditor(final boolean boolEditor) {
        this.editor = boolEditor;
        searchFotosDok = new BaumFotosDokLightweightSearch(
                FOTOS_TOSTRING_TEMPLATE,
                FOTOS_TOSTRING_FIELDS);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void showMeasureIsLoading() {
        showDocumentCard(DocumentCard.BUSY);
    }

    @Override
    public void showMeasurePanel() {
        showDocumentCard(DocumentCard.DOCUMENT);
    }

    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        super.initWithConnectionContext(connectionContext);
        initProperties();
        initComponents();

        lstFotos.setCellRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    Object newValue = value;

                    if (value instanceof CidsBean) {
                        final CidsBean bean = (CidsBean)value;
                        newValue = bean.getProperty(FIELD__FOTONAME);

                        if (newValue == null) {
                            newValue = "unbenannt";
                        }
                    }
                    final Component compoName = super.getListCellRendererComponent(
                            list,
                            newValue,
                            index,
                            isSelected,
                            cellHasFocus);
                    compoName.setForeground(Color.black);
                    return compoName;
                }
            });
        
        if (lstFotos != null) {
            lstFotos.setSelectedIndex(0);
        }
        setReadOnly();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        bindingGroup = new BindingGroup();

        comboBoxFilterDialogGebiet = new ComboBoxFilterDialog(null, new BaumAnsprechpartnerLightweightSearch(), "Ansprechpartner/Melder auswählen", getConnectionContext());
        comboBoxFilterDialogObject = new ComboBoxFilterDialog(null, new BaumAnsprechpartnerLightweightSearch(), "Ansprechpartner/Melder auswählen", getConnectionContext());
        panContent = new RoundedPanel();
        panObject = new JPanel();
        pnlCard1 = new JPanel();
        jTabbedPane = new JTabbedPane();
        jPanelAllgemein = new JPanel();
        panGeometriePoint = new JPanel();
        panLagePoint = new JPanel();
        rpKartePoint = new RoundedPanel();
        panPreviewMapPoint = new DefaultPreviewMapPanel();
        semiRoundedPanel7 = new SemiRoundedPanel();
        lblKartePoint = new JLabel();
        panDaten = new JPanel();
        lblName = new JLabel();
        txtName = new JTextField();
        lblFarbe = new JLabel();
        txtFarbe = new JTextField();
        lblUnter = new JLabel();
        cbUnter = new FastBindableReferenceCombo();
        lblPoint = new JLabel();
        if (isEditor()){
            cbPoint = new DefaultCismapGeometryComboBoxEditor();
        }
        lblLine = new JLabel();
        if (isEditor()){
            cbLine = new DefaultCismapGeometryComboBoxEditor();
        }
        lblPolygon = new JLabel();
        if (isEditor()){
            cbPolygon = new DefaultCismapGeometryComboBoxEditor();
        }
        lblWert = new JLabel();
        txtWert = new JTextField();
        lblAdd = new JLabel();
        txtAdd = new JTextField();
        panGeometrieLine = new JPanel();
        panLageLine = new JPanel();
        rpKarteLine = new RoundedPanel();
        panPreviewMapLine = new DefaultPreviewMapPanel();
        semiRoundedPanel8 = new SemiRoundedPanel();
        lblKarteLine = new JLabel();
        panGeometriePolygon = new JPanel();
        panLagePolygon = new JPanel();
        rpKartePolygon = new RoundedPanel();
        panPreviewMapPolygon = new DefaultPreviewMapPanel();
        semiRoundedPanel9 = new SemiRoundedPanel();
        lblKartePolygon = new JLabel();
        jPanelZugehoerig = new JPanel();
        jPanelZu = new JPanel();
        lblGebiet = new JLabel();
        panGebiet = new JPanel();
        scpLstGebiet = new JScrollPane();
        lstGebiet = new JList();
        panButtonsGebiet = new JPanel();
        btnAddGebiet = new JButton();
        btnRemoveGebiet = new JButton();
        filler1 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        lblObjekt = new JLabel();
        panObjekt = new JPanel();
        scpLstObjekt = new JScrollPane();
        lstObjekt = new JList();
        panButtonsObjekt = new JPanel();
        btnAddObjekt = new JButton();
        btnRemoveObjekt = new JButton();
        filler3 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        filler2 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        jPanelWeitInfo = new JPanel();
        panInfo = new JPanel();
        lblName1 = new JLabel();
        txtName1 = new JTextField();
        lblFarbe1 = new JLabel();
        txtFarbe1 = new JTextField();
        filler5 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        jPanelFotos = new JPanel();
        jPanelFotoAuswahl = new JPanel();
        pnlDocument = new RoundedPanel();
        pnlHeaderDocument = new SemiRoundedPanel();
        lblHeaderDocument = new JLabel();
        pnlBild = new JPanel();
        jPanel1 = new JPanel();
        rasterfariDocumentLoaderPanel1 = new RasterfariDocumentLoaderPanel(
            RASTERFARI,
            this,
            getConnectionContext()
        );
        jPanel2 = new JPanel();
        jxLBusy = new JXBusyLabel(new Dimension(64,64));
        jPanel3 = new JPanel();
        lblKeineFotos = new JLabel();
        jPanel4 = new JPanel();
        jLabel2 = new JLabel();
        pnlPages = new RoundedPanel();
        pnlHeaderPages = new SemiRoundedPanel();
        lblHeaderPages = new JLabel();
        scpPages = new JScrollPane();
        lstPages = rasterfariDocumentLoaderPanel1.getLstPages();
        pnlListe = new RoundedPanel();
        pnlHeaderListe = new SemiRoundedPanel();
        lblHeaderListe = new JLabel();
        scpFotos = new JScrollPane();
        lstFotos = new JList();
        jPanelOffen = new JPanel();
        panOffen = new JPanel();
        lblOffen = new JLabel();
        chOffen = new JCheckBox();
        lblWartung = new JLabel();
        chWartung = new JCheckBox();
        lblDatum = new JLabel();
        dcDatum = new DefaultBindableDateChooser();
        lblDatum1 = new JLabel();
        dcDatum1 = new DefaultBindableDateChooser();
        scpBemerkung = new JScrollPane();
        taBemerkung = new JTextArea();
        filler4 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));

        setLayout(new GridBagLayout());

        panContent.setName(""); // NOI18N
        panContent.setOpaque(false);
        panContent.setLayout(new GridBagLayout());

        panObject.setOpaque(false);
        panObject.setLayout(new GridBagLayout());

        pnlCard1.setOpaque(false);
        pnlCard1.setLayout(new GridBagLayout());

        jPanelAllgemein.setOpaque(false);
        jPanelAllgemein.setLayout(new GridBagLayout());

        panGeometriePoint.setOpaque(false);
        panGeometriePoint.setLayout(new GridBagLayout());

        panLagePoint.setMinimumSize(new Dimension(300, 142));
        panLagePoint.setOpaque(false);
        panLagePoint.setLayout(new GridBagLayout());

        rpKartePoint.setName(""); // NOI18N
        rpKartePoint.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rpKartePoint.add(panPreviewMapPoint, gridBagConstraints);

        semiRoundedPanel7.setBackground(Color.darkGray);
        semiRoundedPanel7.setLayout(new GridBagLayout());

        lblKartePoint.setForeground(new Color(255, 255, 255));
        lblKartePoint.setText("Lage: Punkt");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 10, 5, 5);
        semiRoundedPanel7.add(lblKartePoint, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        rpKartePoint.add(semiRoundedPanel7, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLagePoint.add(rpKartePoint, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        panGeometriePoint.add(panLagePoint, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 10, 10, 10);
        jPanelAllgemein.add(panGeometriePoint, gridBagConstraints);

        panDaten.setOpaque(false);
        panDaten.setLayout(new GridBagLayout());

        lblName.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblName.setText("Name:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblName, gridBagConstraints);

        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.name}"), txtName, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtName, gridBagConstraints);

        lblFarbe.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblFarbe.setText("Farbe:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblFarbe, gridBagConstraints);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.aktenzeichen}"), txtFarbe, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtFarbe, gridBagConstraints);

        lblUnter.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblUnter.setText("Unterkategorie:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblUnter, gridBagConstraints);

        cbUnter.setMaximumRowCount(20);
        cbUnter.setModel(new LoadModelCb());

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_strasse}"), cbUnter, BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(cbUnter, gridBagConstraints);

        lblPoint.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblPoint.setText("Punkt:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblPoint, gridBagConstraints);

        if (isEditor()){
            if (editor){
                cbPoint.setFont(new Font("Dialog", 0, 12)); // NOI18N
            }

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_geom_point}"), cbPoint, BeanProperty.create("selectedItem"));
            binding.setConverter(((DefaultCismapGeometryComboBoxEditor)cbPoint).getConverter());
            bindingGroup.addBinding(binding);

        }
        if (isEditor()){
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 3;
            gridBagConstraints.gridwidth = 4;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            panDaten.add(cbPoint, gridBagConstraints);
        }

        lblLine.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblLine.setText("Linie:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblLine, gridBagConstraints);

        if (isEditor()){
            cbLine.setFont(new Font("Dialog", 0, 12)); // NOI18N
        }
        if (isEditor()){
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.gridwidth = 4;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            panDaten.add(cbLine, gridBagConstraints);
        }

        lblPolygon.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblPolygon.setText("Fläche:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblPolygon, gridBagConstraints);

        if (isEditor()){
            cbPolygon.setFont(new Font("Dialog", 0, 12)); // NOI18N
        }
        if (isEditor()){
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.gridwidth = 4;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            panDaten.add(cbPolygon, gridBagConstraints);
        }

        lblWert.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblWert.setText("dgm-Wert:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblWert, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtWert, gridBagConstraints);

        lblAdd.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblAdd.setText("Addition:");
        lblAdd.setToolTipText("");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblAdd, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtAdd, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 5, 10);
        jPanelAllgemein.add(panDaten, gridBagConstraints);

        panGeometrieLine.setOpaque(false);
        panGeometrieLine.setLayout(new GridBagLayout());

        panLageLine.setMinimumSize(new Dimension(300, 142));
        panLageLine.setOpaque(false);
        panLageLine.setLayout(new GridBagLayout());

        rpKarteLine.setName(""); // NOI18N
        rpKarteLine.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rpKarteLine.add(panPreviewMapLine, gridBagConstraints);

        semiRoundedPanel8.setBackground(Color.darkGray);
        semiRoundedPanel8.setLayout(new GridBagLayout());

        lblKarteLine.setForeground(new Color(255, 255, 255));
        lblKarteLine.setText("Lage: Linie");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 10, 5, 5);
        semiRoundedPanel8.add(lblKarteLine, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        rpKarteLine.add(semiRoundedPanel8, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLageLine.add(rpKarteLine, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        panGeometrieLine.add(panLageLine, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 10, 10, 10);
        jPanelAllgemein.add(panGeometrieLine, gridBagConstraints);

        panGeometriePolygon.setOpaque(false);
        panGeometriePolygon.setLayout(new GridBagLayout());

        panLagePolygon.setMinimumSize(new Dimension(300, 142));
        panLagePolygon.setOpaque(false);
        panLagePolygon.setLayout(new GridBagLayout());

        rpKartePolygon.setName(""); // NOI18N
        rpKartePolygon.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rpKartePolygon.add(panPreviewMapPolygon, gridBagConstraints);

        semiRoundedPanel9.setBackground(Color.darkGray);
        semiRoundedPanel9.setLayout(new GridBagLayout());

        lblKartePolygon.setForeground(new Color(255, 255, 255));
        lblKartePolygon.setText("Lage: Polygon");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 10, 5, 5);
        semiRoundedPanel9.add(lblKartePolygon, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        rpKartePolygon.add(semiRoundedPanel9, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLagePolygon.add(rpKartePolygon, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        panGeometriePolygon.add(panLagePolygon, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 10, 10, 10);
        jPanelAllgemein.add(panGeometriePolygon, gridBagConstraints);

        jTabbedPane.addTab("Allgemein", jPanelAllgemein);

        jPanelZugehoerig.setOpaque(false);
        jPanelZugehoerig.setLayout(new GridBagLayout());

        jPanelZu.setOpaque(false);
        jPanelZu.setLayout(new GridBagLayout());

        lblGebiet.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblGebiet.setText("Gebiete:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        jPanelZu.add(lblGebiet, gridBagConstraints);

        panGebiet.setLayout(new GridBagLayout());

        scpLstGebiet.setMinimumSize(new Dimension(258, 66));

        lstGebiet.setFont(new Font("Dialog", 0, 12)); // NOI18N
        lstGebiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstGebiet.setVisibleRowCount(4);
        scpLstGebiet.setViewportView(lstGebiet);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panGebiet.add(scpLstGebiet, gridBagConstraints);

        panButtonsGebiet.setOpaque(false);
        panButtonsGebiet.setLayout(new GridBagLayout());

        btnAddGebiet.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        btnAddGebiet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddGebietActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panButtonsGebiet.add(btnAddGebiet, gridBagConstraints);

        btnRemoveGebiet.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_remove_mini.png"))); // NOI18N
        btnRemoveGebiet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRemoveGebietActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panButtonsGebiet.add(btnRemoveGebiet, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        panButtonsGebiet.add(filler1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 2, 2, 2);
        panGebiet.add(panButtonsGebiet, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelZu.add(panGebiet, gridBagConstraints);

        lblObjekt.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblObjekt.setText("Objekte:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        jPanelZu.add(lblObjekt, gridBagConstraints);

        panObjekt.setLayout(new GridBagLayout());

        scpLstObjekt.setMinimumSize(new Dimension(258, 66));

        lstObjekt.setFont(new Font("Dialog", 0, 12)); // NOI18N
        lstObjekt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstObjekt.setVisibleRowCount(4);
        scpLstObjekt.setViewportView(lstObjekt);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panObjekt.add(scpLstObjekt, gridBagConstraints);

        panButtonsObjekt.setOpaque(false);
        panButtonsObjekt.setLayout(new GridBagLayout());

        btnAddObjekt.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        btnAddObjekt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddObjektActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panButtonsObjekt.add(btnAddObjekt, gridBagConstraints);

        btnRemoveObjekt.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_remove_mini.png"))); // NOI18N
        btnRemoveObjekt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRemoveObjektActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panButtonsObjekt.add(btnRemoveObjekt, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        panButtonsObjekt.add(filler3, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 2, 2, 2);
        panObjekt.add(panButtonsObjekt, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelZu.add(panObjekt, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelZugehoerig.add(jPanelZu, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelZugehoerig.add(filler2, gridBagConstraints);

        jTabbedPane.addTab("Zugehörig", jPanelZugehoerig);

        jPanelWeitInfo.setOpaque(false);
        jPanelWeitInfo.setLayout(new GridBagLayout());

        panInfo.setOpaque(false);
        panInfo.setLayout(new GridBagLayout());

        lblName1.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblName1.setText("Weitere Info 1:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInfo.add(lblName1, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInfo.add(txtName1, gridBagConstraints);

        lblFarbe1.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblFarbe1.setText("Weitere Info n:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInfo.add(lblFarbe1, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInfo.add(txtFarbe1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 5, 10);
        jPanelWeitInfo.add(panInfo, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelWeitInfo.add(filler5, gridBagConstraints);

        jTabbedPane.addTab("Infos", jPanelWeitInfo);

        jPanelFotos.setOpaque(false);
        jPanelFotos.setLayout(new GridBagLayout());

        jPanelFotoAuswahl.setOpaque(false);
        jPanelFotoAuswahl.setLayout(new GridBagLayout());

        pnlDocument.setLayout(new GridBagLayout());

        pnlHeaderDocument.setBackground(Color.darkGray);
        pnlHeaderDocument.setLayout(new GridBagLayout());

        lblHeaderDocument.setForeground(Color.white);
        lblHeaderDocument.setText("Foto");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        pnlHeaderDocument.add(lblHeaderDocument, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        pnlDocument.add(pnlHeaderDocument, gridBagConstraints);

        pnlBild.setOpaque(false);
        pnlBild.setLayout(new CardLayout());

        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(rasterfariDocumentLoaderPanel1, BorderLayout.CENTER);

        pnlBild.add(jPanel1, "DOCUMENT");

        jPanel2.setLayout(new BorderLayout());

        jxLBusy.setHorizontalAlignment(SwingConstants.CENTER);
        jxLBusy.setPreferredSize(new Dimension(64, 64));
        jPanel2.add(jxLBusy, BorderLayout.CENTER);

        pnlBild.add(jPanel2, "BUSY");

        jPanel3.setLayout(new BorderLayout());

        lblKeineFotos.setHorizontalAlignment(SwingConstants.CENTER);
        lblKeineFotos.setText("Für dieses Gebiet sind beim nächtlichen Abgleich keine Fotos vorhanden gewesen.");
        jPanel3.add(lblKeineFotos, BorderLayout.CENTER);

        pnlBild.add(jPanel3, "NO_DOCUMENT");

        jPanel4.setLayout(new BorderLayout());

        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Das Foto für dieses Gebiet kann nicht geladen werden.");
        jPanel4.add(jLabel2, BorderLayout.CENTER);

        pnlBild.add(jPanel4, "ERROR");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        gridBagConstraints.insets = new Insets(0, 0, 8, 0);
        pnlDocument.add(pnlBild, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 0, 0, 5);
        jPanelFotoAuswahl.add(pnlDocument, gridBagConstraints);

        pnlHeaderPages.setBackground(new Color(51, 51, 51));
        pnlHeaderPages.setLayout(new FlowLayout());

        lblHeaderPages.setForeground(new Color(255, 255, 255));
        lblHeaderPages.setText(NbBundle.getMessage(SubObjectEditor.class, "VermessungRissEditor.lblHeaderPages.text")); // NOI18N
        pnlHeaderPages.add(lblHeaderPages);

        pnlPages.add(pnlHeaderPages, BorderLayout.NORTH);

        scpPages.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scpPages.setMinimumSize(new Dimension(31, 75));
        scpPages.setOpaque(false);
        scpPages.setPreferredSize(new Dimension(85, 75));

        lstPages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstPages.setFixedCellWidth(75);
        scpPages.setViewportView(lstPages);

        pnlPages.add(scpPages, BorderLayout.CENTER);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets(5, 0, 0, 5);
        jPanelFotoAuswahl.add(pnlPages, gridBagConstraints);

        pnlListe.setMinimumSize(new Dimension(200, 49));

        pnlHeaderListe.setBackground(new Color(51, 51, 51));
        pnlHeaderListe.setLayout(new FlowLayout());

        lblHeaderListe.setForeground(new Color(255, 255, 255));
        lblHeaderListe.setText("Fotoliste");
        pnlHeaderListe.add(lblHeaderListe);

        pnlListe.add(pnlHeaderListe, BorderLayout.NORTH);

        scpFotos.setPreferredSize(new Dimension(80, 130));

        lstFotos.setModel(new DefaultListModel<>());
        lstFotos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstFotos.setFixedCellWidth(75);
        lstFotos.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                lstFotosValueChanged(evt);
            }
        });
        scpFotos.setViewportView(lstFotos);

        pnlListe.add(scpFotos, BorderLayout.CENTER);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.9;
        gridBagConstraints.insets = new Insets(10, 0, 0, 5);
        jPanelFotoAuswahl.add(pnlListe, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(0, 5, 0, 0);
        jPanelFotos.add(jPanelFotoAuswahl, gridBagConstraints);

        jTabbedPane.addTab("Fotos", jPanelFotos);

        jPanelOffen.setOpaque(false);
        jPanelOffen.setLayout(new GridBagLayout());

        panOffen.setOpaque(false);
        panOffen.setLayout(new GridBagLayout());

        lblOffen.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblOffen.setText("Veröffentlicht:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panOffen.add(lblOffen, gridBagConstraints);

        chOffen.setContentAreaFilled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panOffen.add(chOffen, gridBagConstraints);

        lblWartung.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblWartung.setText("Wartung:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panOffen.add(lblWartung, gridBagConstraints);

        chWartung.setContentAreaFilled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panOffen.add(chWartung, gridBagConstraints);

        lblDatum.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblDatum.setText("Bis:");
        lblDatum.setRequestFocusEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 4, 5);
        panOffen.add(lblDatum, gridBagConstraints);

        dcDatum.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panOffen.add(dcDatum, gridBagConstraints);

        lblDatum1.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblDatum1.setText("Von:");
        lblDatum1.setRequestFocusEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 4, 5);
        panOffen.add(lblDatum1, gridBagConstraints);

        dcDatum1.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panOffen.add(dcDatum1, gridBagConstraints);

        taBemerkung.setColumns(20);
        taBemerkung.setLineWrap(true);
        taBemerkung.setRows(2);
        taBemerkung.setWrapStyleWord(true);
        scpBemerkung.setViewportView(taBemerkung);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOffen.add(scpBemerkung, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        jPanelOffen.add(panOffen, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        jPanelOffen.add(filler4, gridBagConstraints);

        jTabbedPane.addTab("Offen", jPanelOffen);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlCard1.add(jTabbedPane, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panObject.add(pnlCard1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        panContent.add(panObject, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        add(panContent, gridBagConstraints);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void lstFotosValueChanged(final ListSelectionEvent evt) {//GEN-FIRST:event_lstFotosValueChanged
        showFoto();
    }//GEN-LAST:event_lstFotosValueChanged

    private void btnAddGebietActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAddGebietActionPerformed
        //StaticSwingTools.showDialog(StaticSwingTools.getParentFrame(EmobLadestationEditor.this), dlgAddGebiet, true);
    }//GEN-LAST:event_btnAddGebietActionPerformed

    private void btnRemoveGebietActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRemoveGebietActionPerformed
        /*final Object selection = lstGebiet.getSelectedValue();
        if (selection != null) {
            final int answer = JOptionPane.showConfirmDialog(
                StaticSwingTools.getParentFrame(this),
                NbBundle.getMessage(EmobLadestationEditor.class, BUNDLE_REMZUG_QUESTION),
                NbBundle.getMessage(EmobLadestationEditor.class, BUNDLE_REMZUG_TITLE),
                JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    cidsBean = TableUtils.deleteItemFromList(cidsBean, FIELD__ZUGANG, selection, false);
                } catch (Exception ex) {
                    final ErrorInfo ei = new ErrorInfo(
                        BUNDLE_REMZUG_ERRORTITLE,
                        BUNDLE_REMZUG_ERRORTEXT,
                        null,
                        null,
                        ex,
                        Level.SEVERE,
                        null);
                    JXErrorPane.showDialog(this, ei);
                }
            }
        }*/
    }//GEN-LAST:event_btnRemoveGebietActionPerformed

    private void btnAddObjektActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAddObjektActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddObjektActionPerformed

    private void btnRemoveObjektActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRemoveObjektActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemoveObjektActionPerformed

    public boolean isEditor() {
        return this.editor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  card  DOCUMENT ME!
     */
    private void showDocumentCard(final DocumentCard card) {
        ((CardLayout)pnlBild.getLayout()).show(pnlBild, card.toString());
    }
    
    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cb) {
        try {
            if (isEditor() && (getCidsBean() != null)) {
                LOG.info("remove propchange sub_object: " + getCidsBean());
                getCidsBean().removePropertyChangeListener(this);
            }
            bindingGroup.unbind();
            this.cidsBean = cb;
            if (isEditor() && (getCidsBean() != null)) {
                LOG.info("add propchange sub_object: " + getCidsBean());
                getCidsBean().addPropertyChangeListener(this);
            }
            // 8.5.17 s.Simmert: Methodenaufruf, weil sonst die Comboboxen nicht gefüllt werden
            // evtl. kann dies verbessert werden.
            DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                bindingGroup,
                cb,
                getConnectionContext());
            setMapWindow();
            bindingGroup.bind();
               
            setTitle(getTitle());
            
        } catch (Exception ex) {
            LOG.error("Bean not set", ex);
        }
        //loadFotoDokList();
        //showFoto();
    }

    /**
     * DOCUMENT ME!
     */
    private void setReadOnly() {
        if (!(isEditor())) {
            txtFarbe.setEnabled(false);
            cbUnter.setEnabled(false);
            RendererTools.makeReadOnly(cbUnter);
            txtName.setEnabled(false);
            taBemerkung.setEnabled(false);
            lblPoint.setVisible(isEditor());
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void loadFotoDokList() {
        try {
            searchFotosDok.setGebietId(getCidsBean().getPrimaryKeyValue());
            searchFotosDok.setTableName(TABLE_FOTOS);
            searchFotosDok.setRepresentationFields(FOTOS_TOSTRING_FIELDS);
            final Collection<MetaObjectNode> mons = SessionManager.getProxy()
                        .customServerSearch(searchFotosDok,
                            getConnectionContext());
            final List<CidsBean> beansFotos = new ArrayList<>();
            if (!mons.isEmpty()) {
                for (final MetaObjectNode mon : mons) {
                    beansFotos.add(SessionManager.getProxy().getMetaObject(
                            mon.getObjectId(),
                            mon.getClassId(),
                            "WUNDA_BLAU",
                            getConnectionContext()).getBean());
                }
                for (final CidsBean fotoBean : beansFotos) {
                    ((DefaultListModel)lstFotos.getModel()).addElement(fotoBean);
                }
                lstFotos.setSelectedIndex(0);
                lstFotos.addMouseMotionListener(new MouseMotionAdapter() {

                        @Override
                        public void mouseMoved(final MouseEvent e) {
                            final JList l = (JList)e.getSource();
                            final ListModel m = l.getModel();
                            final int index = l.locationToIndex(e.getPoint());
                            if (index > -1) {
                                l.setToolTipText(m.getElementAt(index).toString());
                            }
                        }
                    });
            }
        } catch (ConnectionException ex) {
            LOG.error("Error during loading fotos", ex);
        }
        
    }

    /**
     * DOCUMENT ME!
     */
    private void setMapWindow() {
        String mapUrl = null;
        try {
            mapUrl = BaumConfProperties.getInstance().getUrlDefault();
        } catch (final Exception ex) {
            LOG.warn("Get no conf properties.", ex);
        }
        /*lagePanel.setMapWindow(getCidsBean(),
            getConnectionContext(),
            mapUrl);*/
    }

    /**
     * DOCUMENT ME!
     */
    private void initProperties() {
        try {
            THEMA = BaumConfProperties.getInstance().getOrdnerThema();
            DOKUMENTE = BaumConfProperties.getInstance().getOrdnerDokumente();
            FOTOS = BaumConfProperties.getInstance().getOrdnerFotos();
            RASTERFARI = BaumConfProperties.getInstance().getUrlRasterfari();
        } catch (final Exception ex) {
            LOG.warn("Get no conf properties.", ex);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void showFoto() {
        final String az;
        final String id;
        final String gebiet;
        final String fotoUrl;
        final String fotoName;
        if (!lstFotos.isSelectionEmpty()) {
            try {
                fotoName = lstFotos.getSelectedValue().toString();
                az = "test";
                id = cidsBean.getPrimaryKeyValue().toString();
                gebiet = az + "_Id" + id;
                fotoUrl = THEMA + "/" + gebiet + "/" + FOTOS + "/" + fotoName;
                rasterfariDocumentLoaderPanel1.setDocument(fotoUrl);
                if (rasterfariDocumentLoaderPanel1.getCurrentPage() == -1) {
                    showDocumentCard(DocumentCard.ERROR);
                }
            } catch (final Exception ex) {
                LOG.warn("Get no foto.", ex);
                showDocumentCard(DocumentCard.ERROR);
            }
        } else {
            showDocumentCard(DocumentCard.NO_DOCUMENT);
        }
    }

    
    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        final MappingComponent mc = new MappingComponent();
        CismapBroker.getInstance().setMappingComponent(mc);
        DevelopmentTools.createEditorFromRestfulConnection(
            DevelopmentTools.RESTFUL_CALLSERVER_CALLSERVER,
            "WUNDA_BLAU",
            null,
            true,
            TABLE_NAME,
            1,
            800,
            600);
    }

    @Override
    public String getTitle() {
        if (getCidsBean().getMetaObject().getStatus() == MetaObject.NEW) {
            return TITLE_NEW_OBJECT;
        } else {
            return getCidsBean().toString();
        }
    }

    @Override
    public void dispose() {
        if (isEditor()) {
            ((DefaultCismapGeometryComboBoxEditor)cbPoint).dispose();
        } 
        rasterfariDocumentLoaderPanel1.dispose();
    }

    @Override
    public void setTitle(String title) {
        if (title == null) {
            title = "<Error>";
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(FIELD__GEOREFERENZ)) {
            setMapWindow();
        }
        
    }


    @Override
    public boolean isOkForSaving() {
        
            boolean save = true;
            final StringBuilder errorMessage = new StringBuilder();
            

            // name vorhanden
            try {
                if (txtName.getText().trim().isEmpty()) {
                    LOG.warn("No name specified. Skip persisting.");
                    errorMessage.append(NbBundle.getMessage(SubObjectEditor.class, BUNDLE_NONAME));
                    save = false;
                }  else {
                    if (redundantName) {
                        LOG.warn("Duplicate name specified. Skip persisting.");
                        errorMessage.append(NbBundle.getMessage(SubObjectEditor.class, BUNDLE_DUPLICATENAME));
                        save = false;
                    }   
                }
            } catch (final MissingResourceException ex) {
                LOG.warn("Name not given.", ex);
                save = false;
            }

            // georeferenz muss gefüllt sein
            try {
                if (getCidsBean().getProperty(FIELD__GEOREFERENZ) == null) {
                    LOG.warn("No geom specified. Skip persisting.");
                    errorMessage.append(NbBundle.getMessage(SubObjectEditor.class, BUNDLE_NOGEOMPOINT));
                    save = false;
                } else {
                    final CidsBean geom_pos = (CidsBean)getCidsBean().getProperty(FIELD__GEOREFERENZ);
                    if (!((Geometry)geom_pos.getProperty(FIELD__GEO_FIELD)).getGeometryType().equals("Point")) {
                        LOG.warn("Wrong geom specified. Skip persisting.");
                        errorMessage.append(NbBundle.getMessage(SubObjectEditor.class, BUNDLE_WRONGGEOMPOINT));
                        save = false;
                    }
                }
            } catch (final MissingResourceException ex) {
                LOG.warn("Geom Point not given.", ex);
                save = false;
            }
            if (errorMessage.length() > 0) {
                JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_PREFIX)
                            + errorMessage.toString()
                            + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_SUFFIX),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_TITLE),
                    JOptionPane.WARNING_MESSAGE);
            }
            return save;
        
    }

    @Override
    public void beforeSaving() {
        final RedundantObjectSearch gebietSearch = new RedundantObjectSearch(
                REDUNDANT_TOSTRING_TEMPLATE,
                REDUNDANT_TOSTRING_FIELDS,
                null,
                REDUNDANT_TABLE);
        final Collection<String> conditions = new ArrayList<>();
        conditions.add(FIELD__ID + " <> " + getCidsBean().getProperty(FIELD__ID));
        gebietSearch.setWhere(conditions);
        try {
            redundantName =
                !(SessionManager.getProxy().customServerSearch(
                        SessionManager.getSession().getUser(),
                        gebietSearch,
                        getConnectionContext())).isEmpty();
        } catch (ConnectionException ex) {
            LOG.warn("problem in beforeSaving.", ex);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class LoadModelCb extends DefaultComboBoxModel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LoadModelCb object.
         */
        public LoadModelCb() {
            super(new String[] { "Die Daten werden geladen......" });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class MustSetModelCb extends DefaultComboBoxModel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MustSetModelCb object.
         */
        public MustSetModelCb() {
            super(new String[] { "Die Daten bitte zuweisen......" });
        }
    }

}
