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
import Sirius.server.middleware.types.MetaClass;

import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import com.vividsolutions.jts.geom.Coordinate;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

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
import de.cismet.cids.custom.clientutils.HexcolorFormatter;

import de.cismet.cids.custom.objecteditors.utils.RendererTools;
import de.cismet.cids.custom.objecteditors.utils.SubConfProperties;
import de.cismet.cids.custom.objecteditors.utils.TableUtils;
import de.cismet.cids.custom.objecteditors.wunda_blau.albo.ComboBoxFilterDialog;
import de.cismet.cids.custom.objectrenderer.utils.CidsBeanSupport;
import de.cismet.cids.custom.wunda_blau.search.server.AbstractMonToLwmoSearch;
import de.cismet.cids.custom.wunda_blau.search.server.BaumAnsprechpartnerLightweightSearch;
import de.cismet.cids.custom.wunda_blau.search.server.BaumFotosDokLightweightSearch;
import de.cismet.cids.custom.wunda_blau.search.server.RedundantObjectSearch;
import de.cismet.cids.custom.wunda_blau.search.server.SubGebietLightweightSearch;
import de.cismet.cids.custom.wunda_blau.search.server.SubGebieteNextSearch;
import de.cismet.cids.custom.wunda_blau.search.server.SubObjectGeomLightweightSearch;
import de.cismet.cids.custom.wunda_blau.search.server.SubObjectGeomSearch;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.DefaultBindableDateChooser;
import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.FastBindableReferenceCombo;
import de.cismet.cids.editors.SaveVetoable;
import de.cismet.cids.editors.hooks.BeforeSavingHook;
import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.cismap.cids.geometryeditor.DefaultCismapGeometryComboBoxEditor;
import de.cismet.cismap.commons.features.PureNewFeature;

import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.RasterfariDocumentLoaderPanel;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.connectioncontext.ConnectionContext;


import de.cismet.tools.gui.RoundedPanel;
import de.cismet.tools.gui.SemiRoundedPanel;
import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.awt.HeadlessException;
import java.util.EventObject;
import java.util.logging.Level;
import javax.swing.table.TableCellEditor;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.openide.util.Exceptions;
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

    private static Double bufferGebiet;
    private static String FOTOS;
    private static String DOKUMENTE;
    private static String RASTERFARI;
    private static final String FOTOS_TOSTRING_TEMPLATE = "%s";
    private static final String[] FOTOS_TOSTRING_FIELDS = { "name" };

    private static final Logger LOG = Logger.getLogger(SubObjectEditor.class);
    public static final String REDUNDANT_TOSTRING_TEMPLATE = "%s";
    public static final String[] REDUNDANT_TOSTRING_FIELDS = { "name", "id" };
    public static final String REDUNDANT_TABLE = "sub_object";
    public static final String NEXT_TOSTRING_TEMPLATE = "%s";
    public static final String[] NEXT_TOSTRING_FIELDS = { "name" };

    public static final String FIELD__NAME = "name";                                        // sub_object
    public static final String FIELD__ID = "id";                                            // sub_object
    public static final String FIELD__GEOREFERENZ_POINT = "fk_geom_point";                  // sub_object
    public static final String FIELD__GEOREFERENZ_LINE = "fk_geom_line";                    // sub_object
    public static final String FIELD__GEOREFERENZ_POLY = "fk_geom_polygon";                 // sub_object
    public static final String FIELD__UNTERKATEGORIE = "fk_unterkategorie";                 // sub_object
    public static final String FIELD__GEBIET = "arr_gebiet";                                // sub_object
    public static final String FIELD__OBJECT = "arr_object";                                // sub_object
    public static final String FIELD__FARBE = "farbe";                                       // sub_object
    public static final String FIELD__GEOM_POINT = "fk_geom_point.geo_field";                // sub_object-geom
    public static final String FIELD__FOTONAME = "name";                                    // sub_fotos
    public static final String FIELD__UNTERKATEGORIE_GEOMTYP = "fk_unterkategorie.fk_geometrietyp"; 
    public static final String FIELD__UNTERKATEGORIE_KANN = "fk_unterkategorie.arr_weitere_info_kann"; 
    public static final String FIELD__UNTERKATEGORIE_MUSS = "fk_unterkategorie.arr_weitere_info_muss";
    public static final String FIELD__OBJECT_TYP = "typ";                                   // baum_object_geom
    public static final String FIELD__OBJECT_GEOM = "fk_geom";                              // baum_object_geom
    public static final String FIELD__SCHLUESSEL = "schluessel";                                  
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
    public static final String BUNDLE_GEBIET_QUESTION =
        "SubObjectEditor.btnRemoveGebietActionPerformed().question";
    public static final String BUNDLE_GEBIET_TITLE = "SubObjectEditor.btnRemoveGebietActionPerformed().title";
    public static final String BUNDLE_OBJECT_QUESTION =
        "SubObjectEditor.btnRemoveObjectActionPerformed().question";
    public static final String BUNDLE_OBJECT_TITLE = "SubObjectEditor.btnRemoveObjectActionPerformed().title";
    public static final String BUNDLE_GEBIET_ERRORTITLE =
        "SubObjectEditor.btnRemoveGebietActionPerformed().errortitle";
    public static final String BUNDLE_GEBIET_ERRORTEXT =
        "SubObjectEditor.btnRemoveGebietActionPerformed().errortext";
    public static final String BUNDLE_OBJECT_ERRORTITLE =
        "SubObjectEditor.btnRemoveObjectActionPerformed().errortitle";
    public static final String BUNDLE_OBJECT_ERRORTEXT =
        "SubObjectEditor.btnRemoveObjectActionPerformed().errortext";
    public static final String BUNDLE_PANE_TITLE = "SubObjectEditor.isOkForSaving().JOptionPane.title";
    public static final String BUNDLE_PANE_PREFIX_GEBIET =
        "SubObjectEditor.btnCreateGebieteActionPerformed().JOptionPane.message.prefix";
    public static final String BUNDLE_PANE_PREFIX_OBJECT =
        "SubObjectEditor.btnAddObjectActionPerformed().JOptionPane.message.prefix";
    public static final String BUNDLE_PANE_TITLE_OBJECT =
        "SubObjectEditor.btnAddObjectActionPerformed().JOptionPane.title.add";
    public static final String BUNDLE_PANE_OBJECT =
        "SubObjectEditor.btnAddObjectActionPerformed().JOptionPane.message.add";
    public static final String BUNDLE_PANE_TITLE_GEBIET =
        "SubObjectEditor.btnCreateGebieteActionPerformed().JOptionPane.title.add";
    public static final String BUNDLE_PANE_GEBIET =
        "SubObjectEditor.btnCreateGebieteActionPerformed().JOptionPane.message.add";
    public static final String BUNDLE_PANE_MESSAGE_DEL =
        "SubObjectEditor.btnDeleteGebieteActionPerformed().JOptionPane.message";
    public static final String BUNDLE_PANE_TITLE_DEL =
        "SubObjectEditor.btnDeleteGebieteActionPerformed().JOptionPane.title";
    public static final String BUNDLE_PANE_MESSAGE_DEL_OBJECT =
        "SubObjectEditor.btnDeleteObjectsActionPerformed().JOptionPane.message";
    public static final String BUNDLE_PANE_TITLE_DEL_OBJECT =
        "SubObjectEditor.btnDeleteObjectsActionPerformed().JOptionPane.title";
    public static final String BUNDLE_PANE_MESSAGE_ADD =
        "SubObjectEditor.btnCreateGebieteActionPerformed().JOptionPane.message";
    public static final String BUNDLE_PANE_TITLE_ADD =
        "SubObjectEditor.btnCreateGebieteActionPerformed().JOptionPane.title";
    public static final String BUNDLE_PANE_PREFIX_GEBIET_NO =
        "SubObjectEditor.btnCreateGebieteActionPerformed().JOptionPane.message.prefix.no";
    public static final String BUNDLE_PANE_TITLE_GEBIET_NO =
        "SubObjectEditor.btnCreateGebieteActionPerformed().JOptionPane.title.add.no";
    public static final String BUNDLE_PANE_GEBIET_NO =
        "SubObjectEditor.btnCreateGebieteActionPerformed().JOptionPane.message.add.no";
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
    private SubObjectGeomLightweightSearch searchObjectGeomLw;

    private SubObjectGeomSearch searchObjectGeom;
    
    private Boolean redundantName = false;
    private SwingWorker worker_name;

    private final boolean editor;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAddGebiet;
    private JButton btnAddObject;
    private JButton btnCreateGebiete;
    private JButton btnDeleteGebiete;
    private JButton btnDeleteObjects;
    private JButton btnRemGebiet;
    private JButton btnRemObject;
    private JComboBox cbLine;
    private JComboBox cbPoint;
    private JComboBox cbPolygon;
    FastBindableReferenceCombo cbUnter;
    private JCheckBox chOffen;
    private JCheckBox chWartung;
    private ComboBoxFilterDialog comboBoxFilterDialogGebiet;
    private ComboBoxFilterDialog comboBoxFilterDialogObject;
    private CustomLagePanel customLagePanelLine;
    private CustomLagePanel customLagePanelPoint;
    private CustomLagePanel customLagePanelPolygon;
    DefaultBindableDateChooser dcDatum;
    DefaultBindableDateChooser dcDatum1;
    private Box.Filler filler2;
    private Box.Filler filler4;
    private Box.Filler filler5;
    private Box.Filler filler6;
    private Box.Filler filler7;
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
    private JLabel lblFarbe2;
    private JLabel lblFarbeAnzeige;
    private JLabel lblGebiet;
    private JLabel lblHeaderDocument;
    private JLabel lblHeaderListe;
    private JLabel lblHeaderPages;
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
    private JPanel panContent;
    private JPanel panDaten;
    private JPanel panGebiet;
    private JPanel panGebieteAdd;
    private JPanel panGeometrieLine;
    private JPanel panGeometriePoint;
    private JPanel panGeometriePolygon;
    private JPanel panInfo;
    private JPanel panObject;
    private JPanel panObjectAdd;
    private JPanel panObjekt;
    private JPanel panOffen;
    private JPanel pnlBild;
    private JPanel pnlCard1;
    private RoundedPanel pnlDocument;
    private SemiRoundedPanel pnlHeaderDocument;
    private SemiRoundedPanel pnlHeaderListe;
    private SemiRoundedPanel pnlHeaderPages;
    private RoundedPanel pnlListe;
    private RoundedPanel pnlPages;
    private RasterfariDocumentLoaderPanel rasterfariDocumentLoaderPanel1;
    private JScrollPane scpBemerkung;
    private JScrollPane scpFotos;
    private JScrollPane scpLstGebiet;
    private JScrollPane scpLstObjekt;
    private JScrollPane scpPages;
    private JTextArea taBemerkung;
    private JTextField txtAdd;
    private JFormattedTextField txtFarbe;
    private JTextField txtName;
    private JTextField txtWI1;
    private JTextField txtWI2;
    private JTextField txtWIn;
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
        this.searchObjectGeom = new SubObjectGeomSearch();
        //searchObjectGeom.setGebietId(2);
        searchObjectGeom.setPoint(new Point(new Coordinate(374483,5680933,0),new PrecisionModel(), 25832 ));
        this.searchObjectGeomLw = new SubObjectGeomLightweightSearch(searchObjectGeom);
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

        if (isEditor()) {
            ((DefaultCismapGeometryComboBoxEditor)cbPoint).setLocalRenderFeatureString(FIELD__GEOREFERENZ_POINT);
            ((DefaultCismapGeometryComboBoxEditor)cbLine).setLocalRenderFeatureString(FIELD__GEOREFERENZ_LINE);
            ((DefaultCismapGeometryComboBoxEditor)cbPolygon).setLocalRenderFeatureString(FIELD__GEOREFERENZ_POLY);
        }
        
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

        comboBoxFilterDialogGebiet = new ComboBoxFilterDialog(null, new SubGebietLightweightSearch(), "Gebiet auswählen", getConnectionContext());
        comboBoxFilterDialogObject = new ComboBoxFilterDialog(null, searchObjectGeomLw, "Object auswählen", getConnectionContext(), true);
        panContent = new RoundedPanel();
        panObject = new JPanel();
        pnlCard1 = new JPanel();
        jTabbedPane = new JTabbedPane();
        jPanelAllgemein = new JPanel();
        panGeometriePoint = new JPanel();
        customLagePanelPoint = new CustomLagePanel();
        panDaten = new JPanel();
        lblName = new JLabel();
        txtName = new JTextField();
        lblFarbe = new JLabel();
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
        txtFarbe = new JFormattedTextField(new HexcolorFormatter());
        lblFarbeAnzeige = new JLabel();
        panGeometrieLine = new JPanel();
        customLagePanelLine = new CustomLagePanel();
        panGeometriePolygon = new JPanel();
        customLagePanelPolygon = new CustomLagePanel();
        jPanelZugehoerig = new JPanel();
        jPanelZu = new JPanel();
        lblGebiet = new JLabel();
        panGebiet = new JPanel();
        scpLstGebiet = new JScrollPane();
        lstGebiet = new JList();
        panGebieteAdd = new JPanel();
        btnAddGebiet = new JButton();
        btnRemGebiet = new JButton();
        filler6 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        btnCreateGebiete = new JButton();
        btnDeleteGebiete = new JButton();
        lblObjekt = new JLabel();
        panObjekt = new JPanel();
        scpLstObjekt = new JScrollPane();
        lstObjekt = new JList();
        panObjectAdd = new JPanel();
        btnAddObject = new JButton();
        btnRemObject = new JButton();
        filler7 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        btnDeleteObjects = new JButton();
        filler2 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        jPanelWeitInfo = new JPanel();
        panInfo = new JPanel();
        lblName1 = new JLabel();
        txtWI1 = new JTextField();
        lblFarbe1 = new JLabel();
        txtWIn = new JTextField();
        lblFarbe2 = new JLabel();
        txtWI2 = new JTextField();
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
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGeometriePoint.add(customLagePanelPoint, gridBagConstraints);

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

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_unterkategorie}"), cbUnter, BeanProperty.create("selectedItem"));
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
            cbPoint.setEnabled(false);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_geom_point}"), cbPoint, BeanProperty.create("selectedItem"));
            binding.setSourceNullValue(null);
            binding.setSourceUnreadableValue(null);
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
            cbLine.setEnabled(false);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_geom_line}"), cbLine, BeanProperty.create("selectedItem"));
            binding.setSourceNullValue(null);
            binding.setSourceUnreadableValue(null);
            binding.setConverter(((DefaultCismapGeometryComboBoxEditor)cbLine).getConverter());
            bindingGroup.addBinding(binding);

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
            cbPolygon.setEnabled(false);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_geom_polygon}"), cbPolygon, BeanProperty.create("selectedItem"));
            binding.setSourceNullValue(null);
            binding.setSourceUnreadableValue(null);
            binding.setConverter(((DefaultCismapGeometryComboBoxEditor)cbPolygon).getConverter());
            bindingGroup.addBinding(binding);

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

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.dgm_wert}"), txtWert, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

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

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.dgm_addition}"), txtAdd, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtAdd, gridBagConstraints);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.farbe}"), txtFarbe, BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtFarbe, gridBagConstraints);

        lblFarbeAnzeige.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblFarbeAnzeige.setOpaque(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new Insets(2, 5, 2, 2);
        panDaten.add(lblFarbeAnzeige, gridBagConstraints);

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
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGeometrieLine.add(customLagePanelLine, gridBagConstraints);

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
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGeometriePolygon.add(customLagePanelPolygon, gridBagConstraints);

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

        ELProperty eLProperty = ELProperty.create("${cidsBean.arr_gebiet}");
        JListBinding jListBinding = SwingBindings.createJListBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, lstGebiet);
        bindingGroup.addBinding(jListBinding);

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

        panGebieteAdd.setAlignmentX(0.0F);
        panGebieteAdd.setAlignmentY(1.0F);
        panGebieteAdd.setFocusable(false);
        panGebieteAdd.setOpaque(false);
        panGebieteAdd.setLayout(new GridBagLayout());

        btnAddGebiet.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        btnAddGebiet.setBorderPainted(false);
        btnAddGebiet.setContentAreaFilled(false);
        btnAddGebiet.setFocusPainted(false);
        btnAddGebiet.setMaximumSize(new Dimension(45, 22));
        btnAddGebiet.setMinimumSize(new Dimension(45, 22));
        btnAddGebiet.setPreferredSize(new Dimension(45, 22));
        btnAddGebiet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddGebietActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panGebieteAdd.add(btnAddGebiet, gridBagConstraints);

        btnRemGebiet.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_remove_mini.png"))); // NOI18N
        btnRemGebiet.setBorderPainted(false);
        btnRemGebiet.setContentAreaFilled(false);
        btnRemGebiet.setFocusPainted(false);
        btnRemGebiet.setMaximumSize(new Dimension(45, 22));
        btnRemGebiet.setMinimumSize(new Dimension(45, 22));
        btnRemGebiet.setPreferredSize(new Dimension(45, 22));
        btnRemGebiet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRemGebietActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panGebieteAdd.add(btnRemGebiet, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        panGebieteAdd.add(filler6, gridBagConstraints);

        btnCreateGebiete.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/wizard.png"))); // NOI18N
        btnCreateGebiete.setToolTipText("Standorte anlegen");
        btnCreateGebiete.setMaximumSize(new Dimension(45, 28));
        btnCreateGebiete.setMinimumSize(new Dimension(45, 28));
        btnCreateGebiete.setPreferredSize(new Dimension(45, 28));
        btnCreateGebiete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCreateGebieteActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(15, 2, 2, 2);
        panGebieteAdd.add(btnCreateGebiete, gridBagConstraints);

        btnDeleteGebiete.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit-delete.png"))); // NOI18N
        btnDeleteGebiete.setToolTipText("Standorte entfernen");
        btnDeleteGebiete.setMaximumSize(new Dimension(45, 21));
        btnDeleteGebiete.setMinimumSize(new Dimension(45, 21));
        btnDeleteGebiete.setPreferredSize(new Dimension(45, 28));
        btnDeleteGebiete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDeleteGebieteActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(5, 2, 2, 2);
        panGebieteAdd.add(btnDeleteGebiete, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 2, 2, 2);
        panGebiet.add(panGebieteAdd, gridBagConstraints);

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

        eLProperty = ELProperty.create("${cidsBean.arr_object}");
        jListBinding = SwingBindings.createJListBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, lstObjekt);
        bindingGroup.addBinding(jListBinding);

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

        panObjectAdd.setAlignmentX(0.0F);
        panObjectAdd.setAlignmentY(1.0F);
        panObjectAdd.setFocusable(false);
        panObjectAdd.setOpaque(false);
        panObjectAdd.setLayout(new GridBagLayout());

        btnAddObject.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        btnAddObject.setBorderPainted(false);
        btnAddObject.setContentAreaFilled(false);
        btnAddObject.setFocusPainted(false);
        btnAddObject.setMaximumSize(new Dimension(45, 22));
        btnAddObject.setMinimumSize(new Dimension(45, 22));
        btnAddObject.setPreferredSize(new Dimension(45, 22));
        btnAddObject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddObjectActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panObjectAdd.add(btnAddObject, gridBagConstraints);

        btnRemObject.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_remove_mini.png"))); // NOI18N
        btnRemObject.setBorderPainted(false);
        btnRemObject.setContentAreaFilled(false);
        btnRemObject.setFocusPainted(false);
        btnRemObject.setMaximumSize(new Dimension(45, 22));
        btnRemObject.setMinimumSize(new Dimension(45, 22));
        btnRemObject.setPreferredSize(new Dimension(45, 22));
        btnRemObject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRemObjectActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panObjectAdd.add(btnRemObject, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        panObjectAdd.add(filler7, gridBagConstraints);

        btnDeleteObjects.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit-delete.png"))); // NOI18N
        btnDeleteObjects.setToolTipText("Standorte entfernen");
        btnDeleteObjects.setMaximumSize(new Dimension(45, 21));
        btnDeleteObjects.setMinimumSize(new Dimension(45, 21));
        btnDeleteObjects.setPreferredSize(new Dimension(45, 28));
        btnDeleteObjects.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDeleteObjectsActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(5, 2, 2, 2);
        panObjectAdd.add(btnDeleteObjects, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 2, 2, 2);
        panObjekt.add(panObjectAdd, gridBagConstraints);

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

        txtWI1.setEnabled(false);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.weitere_info_1}"), txtWI1, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInfo.add(txtWI1, gridBagConstraints);

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

        txtWIn.setEnabled(false);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.weitere_info_n}"), txtWIn, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInfo.add(txtWIn, gridBagConstraints);

        lblFarbe2.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblFarbe2.setText("Weitere Info 2:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInfo.add(lblFarbe2, gridBagConstraints);

        txtWI2.setEnabled(false);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.weitere_info_2}"), txtWI2, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInfo.add(txtWI2, gridBagConstraints);

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
        if (getCidsBean().getProperty(FIELD__GEOREFERENZ_POINT) == null) {
                // Meldung nicht moeglich
                JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_PREFIX_GEBIET)
                    + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_GEBIET)
                    + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_SUFFIX),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_TITLE_GEBIET),
                    JOptionPane.WARNING_MESSAGE);
        } else {
            if (getCidsBean() != null) {
                final Object selectedItem = comboBoxFilterDialogGebiet.showAndGetSelected();
                try {
                    if (selectedItem instanceof CidsBean) {
                        cidsBean = TableUtils.addBeanToCollectionWithMessage(StaticSwingTools.getParentFrame(this),
                                getCidsBean(),
                                FIELD__GEBIET,
                                (CidsBean)selectedItem);
                    }
                } catch (Exception ex) {
                    LOG.error(ex, ex);
                }
            }
        }
    }//GEN-LAST:event_btnAddGebietActionPerformed

    private void btnRemGebietActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRemGebietActionPerformed
        final Object selection = lstGebiet.getSelectedValue();
        if (selection != null) {
            final int answer = JOptionPane.showConfirmDialog(StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_GEBIET_QUESTION),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_GEBIET_TITLE),
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    cidsBean = TableUtils.deleteItemFromList(getCidsBean(), FIELD__GEBIET, selection, false);
                } catch (Exception ex) {
                    final ErrorInfo ei = new ErrorInfo(
                            BUNDLE_GEBIET_ERRORTITLE,
                            BUNDLE_GEBIET_ERRORTEXT,
                            null,
                            null,
                            ex,
                            Level.SEVERE,
                            null);
                    JXErrorPane.showDialog(this, ei);
                }
            }
        }
    }//GEN-LAST:event_btnRemGebietActionPerformed

    private void btnCreateGebieteActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnCreateGebieteActionPerformed
        try {
            if (getCidsBean().getProperty(FIELD__GEOREFERENZ_POINT) == null) {
                // Meldung nicht moeglich
                JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_PREFIX_GEBIET)
                    + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_GEBIET)
                    + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_SUFFIX),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_TITLE_GEBIET),
                    JOptionPane.WARNING_MESSAGE);
            } else {
                // Meldung: wirklich mit loeschen?
                final int answer = JOptionPane.showConfirmDialog(
                    StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_MESSAGE_ADD),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_TITLE_ADD),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (answer == JOptionPane.YES_OPTION) {
                    deleteGebiete();
                    createGebiete();
                }
            }
        } catch (HeadlessException | MissingResourceException e) {
            LOG.error("Cannot add new sub_gebiet objects", e);
        }
    }//GEN-LAST:event_btnCreateGebieteActionPerformed

    public void createGebiete(){
        final SubGebieteNextSearch nextSearch = new SubGebieteNextSearch(
                NEXT_TOSTRING_TEMPLATE,
                NEXT_TOSTRING_FIELDS,
                (Geometry)getCidsBean().getProperty(FIELD__GEOM_POINT),
                bufferGebiet);
        try {
            final Collection<MetaObjectNode> mons;
            //Collection<CidsBean> collGebiet =
            mons =
                SessionManager.getProxy().customServerSearch(
                        SessionManager.getSession().getUser(),
                        nextSearch,
                        getConnectionContext());
            final List<CidsBean> beansGebiet = new ArrayList<>();
            if (!mons.isEmpty()) {
                for (final MetaObjectNode mon : mons) {
                    beansGebiet.add(SessionManager.getProxy().getMetaObject(
                            mon.getObjectId(),
                            mon.getClassId(),
                            "WUNDA_BLAU",
                            getConnectionContext()).getBean());
                }
                try {
                    getCidsBean().addCollectionElements(FIELD__GEBIET, beansGebiet);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                    LOG.warn("problem in createGebiet, setproperty.", ex);
                }
            } else{
                // Meldung keine Gebiete gefunden
                JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_PREFIX_GEBIET_NO)
                    + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_GEBIET_NO)
                    + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_SUFFIX),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_TITLE_GEBIET_NO),
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (ConnectionException ex) {
            LOG.warn("problem in createGebiet.", ex);
        }
    }
    
    private void btnDeleteGebieteActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDeleteGebieteActionPerformed
        // Meldung: wirklich loeschen?
        final int answer = JOptionPane.showConfirmDialog(
            StaticSwingTools.getParentFrame(this),
            NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_MESSAGE_DEL),
            NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_TITLE_DEL),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            deleteGebiete();
        }
    }//GEN-LAST:event_btnDeleteGebieteActionPerformed

    private void btnAddObjectActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAddObjectActionPerformed
        if (getCidsBean() != null) {
            if (getCidsBean().getProperty(FIELD__GEOREFERENZ_POINT) == null) {
                // Meldung nicht moeglich
                JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_PREFIX_OBJECT)
                    + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_OBJECT)
                    + NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_SUFFIX),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_TITLE_OBJECT),
                    JOptionPane.WARNING_MESSAGE);
            } else {
                if (getCidsBean().getProperty(FIELD__GEOM_POINT) != null){
                    //searchObjectGeomLw.getMonSearch().setGebietId(getCidsBean().getPrimaryKeyValue());
                    //searchObjectGeomLw.getMonSearch().setPoint((Geometry) getCidsBean().getProperty(FIELD__GEOM_POINT));
                    searchObjectGeom.setGebietId(getCidsBean().getPrimaryKeyValue());
                    searchObjectGeom.setPoint((Geometry) getCidsBean().getProperty(FIELD__GEOM_POINT));
                    comboBoxFilterDialogObject.refresh();
                    final Object selectedItem = comboBoxFilterDialogObject.showAndGetSelected();
                    try {
                        if (selectedItem instanceof CidsBean) {
                            cidsBean = TableUtils.addBeanToCollectionWithMessage(StaticSwingTools.getParentFrame(this),
                                    getCidsBean(),
                                    FIELD__OBJECT,
                                    (CidsBean)selectedItem);
                        }
                    } catch (Exception ex) {
                        LOG.error(ex, ex);
                    } 
                } else{

                }
            }        
        }
    }//GEN-LAST:event_btnAddObjectActionPerformed

    private void btnRemObjectActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRemObjectActionPerformed
        final Object selection = lstObjekt.getSelectedValue();
        if (selection != null) {
            final int answer = JOptionPane.showConfirmDialog(StaticSwingTools.getParentFrame(this),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_OBJECT_QUESTION),
                    NbBundle.getMessage(SubObjectEditor.class, BUNDLE_OBJECT_TITLE),
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    cidsBean = TableUtils.deleteItemFromList(getCidsBean(), FIELD__OBJECT, selection, false);
                } catch (Exception ex) {
                    final ErrorInfo ei = new ErrorInfo(
                            BUNDLE_OBJECT_ERRORTITLE,
                            BUNDLE_OBJECT_ERRORTEXT,
                            null,
                            null,
                            ex,
                            Level.SEVERE,
                            null);
                    JXErrorPane.showDialog(this, ei);
                }
            }
        }
    }//GEN-LAST:event_btnRemObjectActionPerformed

    private void btnDeleteObjectsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDeleteObjectsActionPerformed
        // Meldung: wirklich loeschen?
        final int answer = JOptionPane.showConfirmDialog(
            StaticSwingTools.getParentFrame(this),
            NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_MESSAGE_DEL_OBJECT),
            NbBundle.getMessage(SubObjectEditor.class, BUNDLE_PANE_TITLE_DEL_OBJECT),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            deleteObjects();
        }
    }//GEN-LAST:event_btnDeleteObjectsActionPerformed

    public void deleteGebiete(){
        /*final Collection<CidsBean> collectionGebiet = 
                getCidsBean().getBeanCollectionProperty(FIELD__GEBIET);
        for(CidsBean deleteBean:collectionGebiet){
            try {
                cidsBean = TableUtils.deleteItemFromList(getCidsBean(), FIELD__GEBIET, deleteBean, false);
            } catch (Exception ex) {
                final ErrorInfo ei = new ErrorInfo(
                        BUNDLE_GEBIET_ERRORTITLE,
                        BUNDLE_GEBIET_ERRORTEXT,
                        null,
                        null,
                        ex,
                        Level.SEVERE,
                        null);
                JXErrorPane.showDialog(this, ei);
            }
        }*/
        getCidsBean().getBeanCollectionProperty(FIELD__GEBIET).clear();
    }
    
    public void deleteObjects(){
        getCidsBean().getBeanCollectionProperty(FIELD__OBJECT).clear();
    }
    
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
            // Wenn mit mehreren Geoms(Liste) gearbeitet wird
            if (isEditor()) {
                if (getCidsBean() != null) {
                    ((DefaultCismapGeometryComboBoxEditor)cbPoint).setCidsMetaObject(getCidsBean()
                                .getMetaObject());
                    ((DefaultCismapGeometryComboBoxEditor)cbPoint).initForNewBinding();
                    ((DefaultCismapGeometryComboBoxEditor)cbLine).setCidsMetaObject(getCidsBean()
                                .getMetaObject());
                    ((DefaultCismapGeometryComboBoxEditor)cbLine).initForNewBinding();
                    ((DefaultCismapGeometryComboBoxEditor)cbPolygon).setCidsMetaObject(getCidsBean()
                                .getMetaObject());
                    ((DefaultCismapGeometryComboBoxEditor)cbPolygon).initForNewBinding();
                } else {
                    ((DefaultCismapGeometryComboBoxEditor)cbPoint).initForNewBinding();
                    cbPoint.setSelectedIndex(-1);
                    ((DefaultCismapGeometryComboBoxEditor)cbLine).initForNewBinding();
                    cbLine.setSelectedIndex(-1);
                    ((DefaultCismapGeometryComboBoxEditor)cbPolygon).initForNewBinding();
                    cbPolygon.setSelectedIndex(-1);
                }
            }
            // 8.5.17 s.Simmert: Methodenaufruf, weil sonst die Comboboxen nicht gefüllt werden
            // evtl. kann dies verbessert werden.
            DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                bindingGroup,
                cb,
                getConnectionContext());
            setMapWindow(customLagePanelPoint, FIELD__GEOREFERENZ_POINT);
            setMapWindow(customLagePanelLine, FIELD__GEOREFERENZ_LINE);
            setMapWindow(customLagePanelPolygon, FIELD__GEOREFERENZ_POLY);
            bindingGroup.bind();
               
            setTitle(getTitle());
            if (getCidsBean()!=null){
                allowWeitereInfo();
                allowGeom();
            }
            if (isEditor()) {
                cbPoint.updateUI();
                cbLine.updateUI();
                cbPolygon.updateUI();
            }
            if (getCidsBean() != null && getCidsBean().getProperty(FIELD__FARBE) != null){
                showColor(getCidsBean().getProperty(FIELD__FARBE).toString());
            }
        } catch (Exception ex) {
            LOG.error("Bean not set", ex);
        }
        //loadFotoDokList();
        //showFoto();
       /* if ((baumBeans != null) && (baumBeans.size() > 0)) {
            xtGeom.setRowSelectionInterval(0, 0);
            if (isEditor()) {
                getSorteCellEditor().getComboBox().setEnabled(true);
                //checkInsideArea();
            }
        }*/
    }
    
    private void allowGeom(){
        cbPoint.setEnabled(false);
        cbPolygon.setEnabled(false);
        cbLine.setEnabled(false);
        final CidsBean beanGeomtyp = (CidsBean)getCidsBean().getProperty(
                        FIELD__UNTERKATEGORIE_GEOMTYP);
        if (beanGeomtyp != null) {
            switch (beanGeomtyp.getProperty(FIELD__SCHLUESSEL).toString()){
                case "p":{
                    cbPoint.setEnabled(true);
                    break;
                }
                case "pl":{
                    cbPoint.setEnabled(true);
                    cbLine.setEnabled(true);
                    break;
                }
                case "pf":{
                    cbPoint.setEnabled(true);
                    cbPolygon.setEnabled(true);
                    break;
                }
                case "plf":{
                    cbPoint.setEnabled(true);
                    cbLine.setEnabled(true);
                    cbPolygon.setEnabled(true);
                    break;
                }
            }
        }
    }

    private void allowWeitereInfo(){
        txtWI1.setText("");
        txtWI2.setText("");
        txtWIn.setText(""); //Evtl erst beim Speichern
        txtWI1.setEnabled(false);
        txtWI2.setEnabled(false);
        txtWIn.setEnabled(false);
        final Collection<CidsBean> collectionKann = getCidsBean().getBeanCollectionProperty(
                        FIELD__UNTERKATEGORIE_KANN);
        if ((collectionKann != null) && !collectionKann.isEmpty()) {
            for (CidsBean beanKann:collectionKann){
                switch (beanKann.getProperty(FIELD__SCHLUESSEL).toString()){
                    case "direction":{
                        txtWIn.setEnabled(true);
                        break;
                    }
                    case "count":{
                        txtWI1.setEnabled(true);
                        break;
                    }
                    case "type":{
                        txtWI2.setEnabled(true);
                        break;
                    }
                }
            }
        }
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
    private void setMapWindow(CustomLagePanel panel, String field) {
        String mapUrl = null;
        Double buffer = 0.0;
        try {
            mapUrl = SubConfProperties.getInstance().getMapUrl();
            buffer = SubConfProperties.getInstance().getBufferMeter();
        } catch (final Exception ex) {
            LOG.warn("Get no conf properties.", ex);
        }
        panel.setMapWindow(getCidsBean(),
            getConnectionContext(),
            mapUrl,
            buffer,
            field);
    }

    /**
     * DOCUMENT ME!
     */
    private void initProperties() {
        try {
            bufferGebiet = SubConfProperties.getInstance().getBufferGebiet();
            //DOKUMENTE = BaumConfProperties.getInstance().getOrdnerDokumente();
            //FOTOS = BaumConfProperties.getInstance().getOrdnerFotos();
            //RASTERFARI = BaumConfProperties.getInstance().getUrlRasterfari();
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
                fotoUrl =  "/" + gebiet + "/" + FOTOS + "/" + fotoName;
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
            ((DefaultCismapGeometryComboBoxEditor)cbLine).dispose();
            ((DefaultCismapGeometryComboBoxEditor)cbPolygon).dispose();
            if (cbPoint != null) {
                ((DefaultCismapGeometryComboBoxEditor)cbPoint).setCidsMetaObject(null);
                cbPoint = null;
            }
            if (cbLine != null) {
                ((DefaultCismapGeometryComboBoxEditor)cbLine).setCidsMetaObject(null);
                cbLine = null;
            }
            if (cbPolygon != null) {
                ((DefaultCismapGeometryComboBoxEditor)cbPolygon).setCidsMetaObject(null);
                cbPolygon = null;
            }
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
        if (evt.getPropertyName().equals(FIELD__GEOREFERENZ_POINT)) {
            setMapWindow(customLagePanelPoint, FIELD__GEOREFERENZ_POINT);
        }
        if (evt.getPropertyName().equals(FIELD__GEOREFERENZ_LINE)) {
            setMapWindow(customLagePanelLine, FIELD__GEOREFERENZ_LINE);
        }
        if (evt.getPropertyName().equals(FIELD__GEOREFERENZ_POLY)) {
            setMapWindow(customLagePanelPolygon, FIELD__GEOREFERENZ_POLY);
        }
        if (evt.getPropertyName().equals(FIELD__UNTERKATEGORIE)) {
            allowWeitereInfo();
            allowGeom();
        }
        if (evt.getPropertyName().equals(FIELD__FARBE)) {
            showColor(evt.getNewValue().toString());
        }
    }

    public void showColor(String value){
        Color c = new Color(
            Integer.valueOf(value.substring(1, 3), 16), 
            Integer.valueOf(value.substring(3, 5), 16), 
            Integer.valueOf(value.substring(5, 7), 16));

            lblFarbeAnzeige.setBackground(c);
            lblFarbeAnzeige.setOpaque(true);
    }
    
    @Override
    public boolean isOkForSaving() {
        
            boolean save = true;
            final StringBuilder errorMessage = new StringBuilder();
            

            // name vorhanden
           /* try {
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
            }*/

            // georeferenz muss gefüllt sein
            try {
                if (getCidsBean().getProperty(FIELD__GEOREFERENZ_POINT) == null) {
                    LOG.warn("No geom specified. Skip persisting.");
                    errorMessage.append(NbBundle.getMessage(SubObjectEditor.class, BUNDLE_NOGEOMPOINT));
                    save = false;
                } else {
                    final CidsBean geom_pos = (CidsBean)getCidsBean().getProperty(FIELD__GEOREFERENZ_POINT);
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
    
    class DefaultCismapGeometryComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor {

        //~ Static fields/initializers -----------------------------------------

        public static final String FIELD__GEO_FIELD = "geo_field"; // geom
        public static final String TABLE_GEOM = "geom";

        //~ Instance fields ----------------------------------------------------

        private final DefaultCismapGeometryComboBoxEditor comboBox;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DefaultBindableComboboxCellEditor object.
         */
        public DefaultCismapGeometryComboBoxCellEditor() {
            comboBox = new DefaultCismapGeometryComboBoxEditor(true);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public boolean isCellEditable(final EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent)anEvent).getClickCount() >= 2;
            }
            return true;
        }

        @Override
        public Object getCellEditorValue() {
            final MetaClass geomMetaClass = ClassCacheMultiple.getMetaClass(
                    CidsBeanSupport.DOMAIN_NAME,
                    TABLE_GEOM,
                    getConnectionContext());
            CidsBean newGeom = null;
            if (null != comboBox.getSelectedItem()) {
                if (comboBox.getSelectedItem() instanceof PureNewFeature) {
                    final Geometry geom = ((PureNewFeature)comboBox.getSelectedItem()).getGeometry();
                    newGeom = geomMetaClass.getEmptyInstance(getConnectionContext()).getBean();
                    try {
                        newGeom.setProperty(FIELD__GEO_FIELD, geom);
                    } catch (final Exception ex) {
                        LOG.warn("Geom not set.", ex);
                    }
                }
            }
            return newGeom;
        }

        @Override
        public Component getTableCellEditorComponent(final JTable table,
                final Object value,
                final boolean isSelected,
                final int row,
                final int column) {
            comboBox.setSelectedItem(value);

            return comboBox;
        }
    }
}
