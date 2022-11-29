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

import com.vividsolutions.jts.geom.Geometry;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import java.awt.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.MissingResourceException;

import javax.swing.*;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.custom.objecteditors.utils.SubConfProperties;
import de.cismet.cids.custom.objecteditors.wunda_blau.albo.ComboBoxFilterDialog;
import de.cismet.cids.custom.wunda_blau.search.server.RedundantObjectSearch;
import de.cismet.cids.custom.wunda_blau.search.server.SubPoiLightweightSearch;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.FastBindableReferenceCombo;
import de.cismet.cids.editors.SaveVetoable;
import de.cismet.cids.editors.hooks.BeforeSavingHook;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.cismap.cids.geometryeditor.DefaultCismapGeometryComboBoxEditor;

import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.tools.gui.RoundedPanel;
import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
/**
 * DOCUMENT ME!
 *
 * @author   sandra
 * @version  $Revision$, $Date$
 */
public class SubGebietEditor extends DefaultCustomObjectEditor implements CidsBeanRenderer,
    SaveVetoable,
    BeforeSavingHook,
    PropertyChangeListener {
    //~ Static fields/initializers ---------------------------------------------
    public static final String GEOMTYPE = "Polygon";

    private static final Logger LOG = Logger.getLogger(SubGebietEditor.class);
    public static final String REDUNDANT_TOSTRING_TEMPLATE = "%s";
    public static final String[] REDUNDANT_TOSTRING_FIELDS = { "name", "id" };
    public static final String REDUNDANT_TABLE = "sub_gebiet";
    

    public static final String FIELD__NAME = "name";                                        // sub_gebiet
    public static final String FIELD__ID = "id";                                            // sub_gebiet
    public static final String FIELD__POI = "fk_poi";                                       // sub_gebiet
    public static final String FIELD__GEOREFERENZ = "fk_geom";                              // sub_gebiet
    public static final String FIELD__GEO_FIELD = "geo_field";                              // geom
    public static final String FIELD__GEOREFERENZ__GEO_FIELD = "fk_geom.geo_field";         // sub_gebiet_geomsub_gebiet
    public static final String TABLE_NAME = "sub_gebiet";
    public static final String TABLE_GEOM = "geom";

    public static final String BUNDLE_NONAME = "SubGebietEditor.isOkForSaving().noName";
    public static final String BUNDLE_DUPLICATENAME = "SubGebietEditor.isOkForSaving().duplicateName";
    public static final String BUNDLE_NOPOI = "SubGebietEditor.isOkForSaving().noPoi";
    public static final String BUNDLE_NOGEOM = "SubGebietEditor.isOkForSaving().noGeom";
    public static final String BUNDLE_WRONGGEOM = "SubGebietEditor.isOkForSaving().wrongGeom";
    public static final String BUNDLE_PANE_PREFIX = "SubGebietEditor.isOkForSaving().JOptionPane.message.prefix";
    public static final String BUNDLE_PANE_SUFFIX = "SubGebietEditor.isOkForSaving().JOptionPane.message.suffix";
    public static final String BUNDLE_PANE_TITLE = "SubGebietEditor.isOkForSaving().JOptionPane.title";
    private static final String TITLE_NEW_GEBIET = "ein neues Gebiet anlegen...";
    private static Color colorAlarm = new java.awt.Color(255, 0, 0);


    /** DOCUMENT ME! */
    @Getter @Setter private static Exception errorNoSave = null;

    //~ Enums ------------------------------------------------------------------


    //~ Instance fields --------------------------------------------------------
    private Boolean redundantName = false;

    private final boolean editor;
    
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAddPoi;
    FastBindableReferenceCombo cbGenau;
    private JComboBox cbGeom;
    private JCheckBox chOffen;
    private ComboBoxFilterDialog comboBoxFilterDialogPoi;
    private CustomLagePanel customLagePanelAlkis;
    private CustomLagePanel customLagePanelLuft;
    private CustomLagePanel customLagePanelSPW;
    private JLabel lblBemerkung;
    private JLabel lblGenau;
    private JLabel lblGeom;
    private JLabel lblName;
    private JLabel lblName1;
    private JLabel lblOffen;
    private JLabel lblPoi;
    private JPanel panBemerkung;
    private JPanel panContent;
    private JPanel panDaten;
    private JPanel panGebiet;
    private JPanel panGeometrie;
    private JScrollPane scpBemerkung;
    private JTextArea taBemerkung;
    private JTextField txtName;
    private BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form.
     */
    public SubGebietEditor() {
        this(true);
    }

    /**
     * Creates a new SubGebietEditor object.
     *
     * @param  boolEditor  DOCUMENT ME!
     */
    public SubGebietEditor(final boolean boolEditor) {
        this.editor = boolEditor;
    }

    //~ Methods ----------------------------------------------------------------

    
    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        super.initWithConnectionContext(connectionContext);
        initProperties();
        initComponents();
        
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

        comboBoxFilterDialogPoi = new ComboBoxFilterDialog(null, new SubPoiLightweightSearch(), "POI auswählen", getConnectionContext());
        panContent = new RoundedPanel();
        panGebiet = new JPanel();
        panGeometrie = new JPanel();
        customLagePanelSPW = new CustomLagePanel();
        customLagePanelLuft = new CustomLagePanel();
        customLagePanelAlkis = new CustomLagePanel();
        panDaten = new JPanel();
        lblName = new JLabel();
        txtName = new JTextField();
        lblPoi = new JLabel();
        lblBemerkung = new JLabel();
        panBemerkung = new JPanel();
        scpBemerkung = new JScrollPane();
        taBemerkung = new JTextArea();
        lblGenau = new JLabel();
        cbGenau = new FastBindableReferenceCombo();
        lblGeom = new JLabel();
        if (isEditor()){
            cbGeom = new DefaultCismapGeometryComboBoxEditor();
        }
        btnAddPoi = new JButton();
        lblName1 = new JLabel();
        lblOffen = new JLabel();
        chOffen = new JCheckBox();

        setLayout(new GridBagLayout());

        panContent.setName(""); // NOI18N
        panContent.setOpaque(false);
        panContent.setLayout(new GridBagLayout());

        panGebiet.setOpaque(false);
        panGebiet.setLayout(new GridBagLayout());

        panGeometrie.setOpaque(false);
        panGeometrie.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGeometrie.add(customLagePanelSPW, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(0, 5, 0, 5);
        panGeometrie.add(customLagePanelLuft, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGeometrie.add(customLagePanelAlkis, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 10, 10, 10);
        panGebiet.add(panGeometrie, gridBagConstraints);

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
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtName, gridBagConstraints);

        lblPoi.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblPoi.setText("Poi:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        panDaten.add(lblPoi, gridBagConstraints);

        lblBemerkung.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblBemerkung.setText("Bemerkung:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblBemerkung, gridBagConstraints);

        panBemerkung.setOpaque(false);
        panBemerkung.setLayout(new GridBagLayout());

        taBemerkung.setColumns(20);
        taBemerkung.setLineWrap(true);
        taBemerkung.setRows(2);
        taBemerkung.setWrapStyleWord(true);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.bemerkung}"), taBemerkung, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

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
        panBemerkung.add(scpBemerkung, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(panBemerkung, gridBagConstraints);

        lblGenau.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblGenau.setText("Genauigkeit der Geometrie:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        panDaten.add(lblGenau, gridBagConstraints);

        cbGenau.setMaximumRowCount(20);
        cbGenau.setModel(new LoadModelCb());

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_genauigkeit}"), cbGenau, BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(cbGenau, gridBagConstraints);

        lblGeom.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblGeom.setText("Geometrie:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblGeom, gridBagConstraints);

        if (isEditor()){
            if (editor){
                cbGeom.setFont(new Font("Dialog", 0, 12)); // NOI18N
            }

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_geom}"), cbGeom, BeanProperty.create("selectedItem"));
            binding.setConverter(((DefaultCismapGeometryComboBoxEditor)cbGeom).getConverter());
            bindingGroup.addBinding(binding);

        }
        if (isEditor()){
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 3;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            panDaten.add(cbGeom, gridBagConstraints);
        }

        btnAddPoi.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        btnAddPoi.setBorderPainted(false);
        btnAddPoi.setContentAreaFilled(false);
        btnAddPoi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddPoiActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panDaten.add(btnAddPoi, gridBagConstraints);

        lblName1.setFont(new Font("DejaVu Sans", 0, 13)); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_poi.geographicidentifier}"), lblName1, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 5);
        panDaten.add(lblName1, gridBagConstraints);

        lblOffen.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblOffen.setText("Veröffentlicht:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        panDaten.add(lblOffen, gridBagConstraints);

        chOffen.setContentAreaFilled(false);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.veroeffentlicht}"), chOffen, BeanProperty.create("selected"));
        binding.setSourceNullValue(false);
        binding.setSourceUnreadableValue(false);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(chOffen, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 5, 10);
        panGebiet.add(panDaten, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        panContent.add(panGebiet, gridBagConstraints);

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

    private void btnAddPoiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAddPoiActionPerformed
        if (getCidsBean() != null) {
            final Object selectedItem = comboBoxFilterDialogPoi.showAndGetSelected();
            try {
                if (selectedItem instanceof CidsBean) {
                    getCidsBean().setProperty(FIELD__POI, selectedItem);
                    lblName1.setText(getCidsBean().getProperty(FIELD__POI).toString());
                }
            } catch (Exception ex) {
                LOG.error(ex, ex);
            } 
        }
    }//GEN-LAST:event_btnAddPoiActionPerformed

     
    public boolean isEditor() {
        return this.editor;
    }

    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cb) {
        try {
            if (isEditor() && (getCidsBean() != null)) {
                LOG.info("remove propchange sub_gebiet: " + getCidsBean());
                getCidsBean().removePropertyChangeListener(this);
            }
            bindingGroup.unbind();
            this.cidsBean = cb;
            if (isEditor() && (getCidsBean() != null)) {
                LOG.info("add propchange sub_gebiet: " + getCidsBean());
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
            //loadPoiList();
            
        } catch (Exception ex) {
            LOG.error("Bean not set", ex);
            if (isEditor()) {
                setErrorNoSave(ex);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setReadOnly() {
        if (!(isEditor())) {
            cbGenau.setEnabled(false);
            txtName.setEnabled(false);
            taBemerkung.setEnabled(false);
            lblGeom.setVisible(isEditor());
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setMapWindow() {
        String mapUrlSpw = null;
        String mapUrlAlkis = null;
        String mapUrlLuft = null;
        Double buffer = 0.0;
        try {
            mapUrlSpw = SubConfProperties.getInstance().getMapUrlGSPW2();
            mapUrlAlkis = SubConfProperties.getInstance().getMapUrlGAlkis();
            mapUrlLuft = SubConfProperties.getInstance().getMapUrlGLuft();
            buffer = SubConfProperties.getInstance().getBufferMeter();
        } catch (final Exception ex) {
            LOG.warn("Get no conf properties.", ex);
        }
        customLagePanelSPW.setMapWindow(getCidsBean(),
            getConnectionContext(),
            mapUrlSpw,
            buffer,
            FIELD__GEOREFERENZ);
        customLagePanelSPW.setTitleKarte("Stadtplan");
        customLagePanelAlkis.setMapWindow(getCidsBean(),
            getConnectionContext(),
            mapUrlAlkis,
            buffer,
            FIELD__GEOREFERENZ);
        customLagePanelAlkis.setTitleKarte("Stadtgrundkarte");
        customLagePanelLuft.setMapWindow(getCidsBean(),
            getConnectionContext(),
            mapUrlLuft,
            buffer,
            FIELD__GEOREFERENZ);
        customLagePanelLuft.setTitleKarte("Orthofoto");
    }

    /**
     * DOCUMENT ME!
     */
    private void initProperties() {
        try {
            //THEMA = BaumConfProperties.getInstance().getOrdnerThema();
        } catch (final Exception ex) {
            LOG.warn("Get no conf properties.", ex);
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
            return TITLE_NEW_GEBIET;
        } else {
            return getCidsBean().toString();
        }
    }

    
    @Override
    public void dispose() {
        customLagePanelSPW.dispose();
        customLagePanelAlkis.dispose();
        customLagePanelLuft.dispose();
        if (isEditor()) {
            ((DefaultCismapGeometryComboBoxEditor)cbGeom).dispose();
        } 
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
            if (isEditor()){
               //loadPoiList();
            }
        }
        if (evt.getPropertyName().equals(FIELD__NAME)) {
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
                errorMessage.append(NbBundle.getMessage(SubGebietEditor.class, BUNDLE_NONAME));
                save = false;
            } else {
                if (redundantName) {
                    LOG.warn("False name specified. Skip persisting.");
                    errorMessage.append(NbBundle.getMessage(SubGebietEditor.class, BUNDLE_DUPLICATENAME));
                    save = false;
                }
            }
        } catch (final MissingResourceException ex) {
            LOG.warn("Name not given.", ex);
            save = false;
        }
            // Poi muss angegeben werden
         try {
            if (getCidsBean().getProperty(FIELD__POI) == null) {
                LOG.warn("No poi specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(SubGebietEditor.class, BUNDLE_NOPOI));
                save = false;
            }
        } catch (final MissingResourceException ex) {
            LOG.warn("poi not given.", ex);
            save = false;
        }

        // georeferenz muss gefüllt sein
        try {
            if (getCidsBean().getProperty(FIELD__GEOREFERENZ) == null) {
                LOG.warn("No geom specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(SubGebietEditor.class, BUNDLE_NOGEOM));
                save = false;
            } else {
                final CidsBean geom_pos = (CidsBean)getCidsBean().getProperty(FIELD__GEOREFERENZ);
                if (!((Geometry)geom_pos.getProperty(FIELD__GEO_FIELD)).getGeometryType().equals(GEOMTYPE)) {
                    LOG.warn("Wrong geom specified. Skip persisting.");
                    errorMessage.append(NbBundle.getMessage(SubGebietEditor.class, BUNDLE_WRONGGEOM));
                    save = false;
                }
            }
        } catch (final MissingResourceException ex) {
            LOG.warn("Geom not given.", ex);
            save = false;
        }
        if (errorMessage.length() > 0) {
            JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                NbBundle.getMessage(SubGebietEditor.class, BUNDLE_PANE_PREFIX)
                        + errorMessage.toString()
                        + NbBundle.getMessage(SubGebietEditor.class, BUNDLE_PANE_SUFFIX),
                NbBundle.getMessage(SubGebietEditor.class, BUNDLE_PANE_TITLE),
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
        conditions.add(FIELD__NAME + " ilike '" + txtName.getText().trim() + "'");
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
