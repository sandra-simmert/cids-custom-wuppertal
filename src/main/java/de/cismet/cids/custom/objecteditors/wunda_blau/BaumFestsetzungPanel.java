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

import Sirius.server.middleware.types.MetaClass;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import de.cismet.cids.client.tools.DevelopmentTools;
import de.cismet.cids.custom.objecteditors.utils.BaumConfProperties;
import de.cismet.cids.custom.objectrenderer.utils.CidsBeanSupport;
import de.cismet.cids.custom.objectrenderer.utils.DefaultPreviewMapPanel;
import org.apache.log4j.Logger;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.CidsBeanStore;
import de.cismet.cids.dynamics.Disposable;
import de.cismet.cids.editors.DefaultBindableDateChooser;
import de.cismet.cids.editors.DefaultBindableReferenceCombo;
import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.navigator.utils.ClassCacheMultiple;
import de.cismet.cismap.cids.geometryeditor.DefaultCismapGeometryComboBoxEditor;
import de.cismet.cismap.commons.BoundingBox;
import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;
import de.cismet.tools.gui.RoundedPanel;
import de.cismet.tools.gui.SemiRoundedPanel;
import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.MissingResourceException;
import java.util.Objects;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.openide.awt.Mnemonics;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * DOCUMENT ME!
 *
 * @author   sandra
 * @version  $Revision$, $Date$
 */
public class BaumFestsetzungPanel extends javax.swing.JPanel implements Disposable, CidsBeanStore, ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------
    private static final Logger LOG = Logger.getLogger(BaumFestsetzungPanel.class);
    
    private static final MetaClass MC__ART;
    static {
        final ConnectionContext connectionContext = ConnectionContext.create(
                ConnectionContext.Category.STATIC,
                BaumErsatzPanel.class.getSimpleName());
        MC__ART = ClassCacheMultiple.getMetaClass(
                "WUNDA_BLAU",
                "BAUM_ART",
                connectionContext);
    }
    
    
    public static final String FIELD__ART = "fk_art";                           // baum_festsetzung
    public static final String FIELD__UMFANG = "umfang";                        // baum_festsetzung
    public static final String FIELD__DATUM = "datum";                        // baum_festsetzung
    public static final String FIELD__GEOM = "fk_geom";                         // baum_festsetzung
    public static final String FIELD__BEMERKUNG = "bemerkung";                  // baum_festsetzung
    public static final String FIELD__GEOREFERENZ = "fk_geom";                  // baum_festsetzung
    
    
    public static final String FIELD__GEO_FIELD = "geo_field";                  // geom
    public static final String FIELD__GEOREFERENZ__GEO_FIELD = "fk_geom.geo_field"; // baum_festsetzung_geom
    
    public static final String TABLE_GEOM = "geom";
    public static final String BUNDLE_NOART = 
            "BaumFestsetzungPanel.prepareForSave().noArt";
    public static final String BUNDLE_NOUMFANG = 
            "BaumFestsetzungPanel.prepareForSave().noUmfang";
    public static final String BUNDLE_NOGEOM = 
            "BaumFestsetzungPanel.prepareForSave().noGeom";
    public static final String BUNDLE_WRONGGEOM = 
            "BaumFestsetzungPanel.prepareForSave().wrongGeom";
    public static final String BUNDLE_NODATE = 
            "BaumFestsetzungPanel.prepareForSave().noDatum";
    public static final String BUNDLE_PANE_PREFIX =
            "BaumFestsetzungPanel.prepareForSave().JOptionPane.message.prefix";
    public static final String BUNDLE_PANE_SUFFIX =
            "BaumFestsetzungPanel.prepareForSave().JOptionPane.message.suffix";
    public static final String BUNDLE_PANE_TITLE = 
            "BaumFestsetzungPanel.prepareForSave().JOptionPane.title";
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        bindingGroup = new BindingGroup();

        panContent = new JPanel();
        panFest = new JPanel();
        lblHoehe = new JLabel();
        txtHoeheF = new JTextField();
        lblUmfang = new JLabel();
        txtUmfangF = new JTextField();
        lblArt = new JLabel();
        cbArtF = new DefaultBindableReferenceCombo(MC__ART);
        ;
        lblGeom = new JLabel();
        if (isEditor){
            cbGeomFest = new DefaultCismapGeometryComboBoxEditor();
        }
        lblDatum = new JLabel();
        dcDatum = new DefaultBindableDateChooser();
        lblBemerkung = new JLabel();
        scpBemerkung = new JScrollPane();
        taBemerkungF = new JTextArea();
        panGeometrie = new JPanel();
        panLage = new JPanel();
        rpKarte = new RoundedPanel();
        panPreviewMap = new DefaultPreviewMapPanel();
        semiRoundedPanel7 = new SemiRoundedPanel();
        lblKarte = new JLabel();

        setName("Form"); // NOI18N
        setOpaque(false);
        setLayout(new GridBagLayout());

        panContent.setName(""); // NOI18N
        panContent.setOpaque(false);
        panContent.setLayout(new GridBagLayout());

        panFest.setName("panFest"); // NOI18N
        panFest.setOpaque(false);
        panFest.setLayout(new GridBagLayout());

        lblHoehe.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblHoehe, "Höhe [m]:");
        lblHoehe.setName("lblHoehe"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panFest.add(lblHoehe, gridBagConstraints);

        txtHoeheF.setName("txtHoeheF"); // NOI18N

        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.hoehe}"), txtHoeheF, BeanProperty.create("text"));
        binding.setSourceNullValue("");
        binding.setSourceUnreadableValue("");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panFest.add(txtHoeheF, gridBagConstraints);

        lblUmfang.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblUmfang, "Umfang [cm]:");
        lblUmfang.setName("lblUmfang"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panFest.add(lblUmfang, gridBagConstraints);

        txtUmfangF.setName("txtUmfangF"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.umfang}"), txtUmfangF, BeanProperty.create("text"));
        binding.setSourceNullValue("");
        binding.setSourceUnreadableValue("");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panFest.add(txtUmfangF, gridBagConstraints);

        lblArt.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblArt, "Art:");
        lblArt.setName("lblArt"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panFest.add(lblArt, gridBagConstraints);

        cbArtF.setFont(new Font("Dialog", 0, 12)); // NOI18N
        cbArtF.setMaximumRowCount(15);
        cbArtF.setName("cbArtF"); // NOI18N
        cbArtF.setPreferredSize(new Dimension(100, 24));

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_art}"), cbArtF, BeanProperty.create("selectedItem"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panFest.add(cbArtF, gridBagConstraints);

        lblGeom.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblGeom, "Geometrie:");
        lblGeom.setName("lblGeom"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panFest.add(lblGeom, gridBagConstraints);

        if (isEditor){
            cbGeomFest.setFont(new Font("Dialog", 0, 12)); // NOI18N
            cbGeomFest.setName("cbGeomFest"); // NOI18N

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_geom}"), cbGeomFest, BeanProperty.create("selectedItem"));
            binding.setSourceNullValue(null);
            binding.setSourceUnreadableValue(null);
            binding.setConverter(((DefaultCismapGeometryComboBoxEditor)cbGeomFest).getConverter());
            bindingGroup.addBinding(binding);

        }
        if (isEditor){
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            panFest.add(cbGeomFest, gridBagConstraints);
        }

        lblDatum.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblDatum.setText(NbBundle.getMessage(BaumFestsetzungPanel.class, "BaumFestsetzungPanel.lblDatum.text")); // NOI18N
        lblDatum.setName("lblDatum"); // NOI18N
        lblDatum.setRequestFocusEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 4, 5);
        panFest.add(lblDatum, gridBagConstraints);

        dcDatum.setName("dcDatum"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.datum}"), dcDatum, BeanProperty.create("date"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        binding.setConverter(dcDatum.getConverter());
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panFest.add(dcDatum, gridBagConstraints);

        lblBemerkung.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblBemerkung, "Bemerkung:");
        lblBemerkung.setName("lblBemerkung"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panFest.add(lblBemerkung, gridBagConstraints);

        scpBemerkung.setName("scpBemerkung"); // NOI18N
        scpBemerkung.setOpaque(false);

        taBemerkungF.setLineWrap(true);
        taBemerkungF.setRows(2);
        taBemerkungF.setWrapStyleWord(true);
        taBemerkungF.setName("taBemerkungF"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.bemerkung}"), taBemerkungF, BeanProperty.create("text"));
        binding.setSourceNullValue("");
        binding.setSourceUnreadableValue("");
        bindingGroup.addBinding(binding);

        scpBemerkung.setViewportView(taBemerkungF);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panFest.add(scpBemerkung, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panContent.add(panFest, gridBagConstraints);

        panGeometrie.setName("panGeometrie"); // NOI18N
        panGeometrie.setOpaque(false);
        panGeometrie.setLayout(new GridBagLayout());

        panLage.setName("panLage"); // NOI18N
        panLage.setOpaque(false);
        panLage.setLayout(new GridBagLayout());

        rpKarte.setName(""); // NOI18N
        rpKarte.setLayout(new GridBagLayout());

        panPreviewMap.setName("panPreviewMap"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rpKarte.add(panPreviewMap, gridBagConstraints);

        semiRoundedPanel7.setBackground(Color.darkGray);
        semiRoundedPanel7.setName("semiRoundedPanel7"); // NOI18N
        semiRoundedPanel7.setLayout(new GridBagLayout());

        lblKarte.setForeground(new Color(255, 255, 255));
        Mnemonics.setLocalizedText(lblKarte, NbBundle.getMessage(BaumFestsetzungPanel.class, "BaumFestsetzungPanel.lblKarte.text")); // NOI18N
        lblKarte.setName("lblKarte"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 10, 5, 5);
        semiRoundedPanel7.add(lblKarte, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        rpKarte.add(semiRoundedPanel7, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLage.add(rpKarte, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGeometrie.add(panLage, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 0, 0, 2);
        panContent.add(panGeometrie, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panContent, gridBagConstraints);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    //~ Instance fields --------------------------------------------------------
    private final boolean isEditor;
    private final BaumSchadenPanel parentPanel;
    private final ConnectionContext connectionContext;
    private CidsBean cidsBean;
    public static final String GEOMTYPE = "Point";
    private final PropertyChangeListener changeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (!(Objects.equals(evt.getOldValue(), evt.getNewValue()))){
                    if ((parentPanel != null) && (parentPanel.parentPanel != null) && (parentPanel.getCidsBean() != null)) {
                        //parentPanel.getCidsBean().setArtificialChangeFlag(true);
                        //LOG.warn(evt.getPropertyName());
                        parentPanel.parentPanel.parentEditor.getCidsBean().setArtificialChangeFlag(true); 
                        parentPanel.getCidsBean().setArtificialChangeFlag(true);
                        parentPanel.setChangedFestBeans(cidsBean);
                    }
                    if ((parentPanel != null) && (parentPanel.parentEditor != null) && (parentPanel.getCidsBean() != null)){
                        parentPanel.parentEditor.getCidsBean().setArtificialChangeFlag(true);
                        parentPanel.getCidsBean().setArtificialChangeFlag(true);
                        parentPanel.setChangedFestBeans(cidsBean);
                    }
                    if (FIELD__GEOM.equals(evt.getPropertyName())){
                        setMapWindow();
                    }
                }
            }
        };
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    JComboBox<String> cbArtF;
    JComboBox cbGeomFest;
    DefaultBindableDateChooser dcDatum;
    JLabel lblArt;
    JLabel lblBemerkung;
    JLabel lblDatum;
    JLabel lblGeom;
    JLabel lblHoehe;
    JLabel lblKarte;
    JLabel lblUmfang;
    JPanel panContent;
    JPanel panFest;
    JPanel panGeometrie;
    JPanel panLage;
    DefaultPreviewMapPanel panPreviewMap;
    RoundedPanel rpKarte;
    JScrollPane scpBemerkung;
    SemiRoundedPanel semiRoundedPanel7;
    JTextArea taBemerkungF;
    JTextField txtHoeheF;
    JTextField txtUmfangF;
    private BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BaumFestsetzungPanel object.
     */
    public BaumFestsetzungPanel() {
        this(null,false, ConnectionContext.createDeprecated());
    }

    
 
    /**
     * Creates new form BaumFestsetzungPanel.
     *
     * @param parentPanel
     * @param  editable             DOCUMENT ME!
     * @param  connectionContext    DOCUMENT ME!
     */
    public BaumFestsetzungPanel(final BaumSchadenPanel parentPanel, final boolean editable,
            final ConnectionContext connectionContext) {
        this.isEditor = editable;
        this.connectionContext = connectionContext;
        initComponents();
        if (isEditor) {
            ((DefaultCismapGeometryComboBoxEditor)cbGeomFest).setLocalRenderFeatureString(FIELD__GEOM);
        }
        this.parentPanel = parentPanel;
    }

    //~ Methods ----------------------------------------------------------------
    public static void main(final String[] args) throws Exception {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        final MappingComponent mc = new MappingComponent();
        CismapBroker.getInstance().setMappingComponent(mc);
        DevelopmentTools.createEditorFromRestfulConnection(
            DevelopmentTools.RESTFUL_CALLSERVER_CALLSERVER,
            "WUNDA_BLAU",
            null,
            true,
            "baum_festsetzung",
            1,
            800,
            600);
    }   
    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
        
    @Override
    public void dispose() {
        bindingGroup.unbind();
        cidsBean = null;
        if (this.isEditor) {
            ((DefaultCismapGeometryComboBoxEditor)cbGeomFest).dispose();
        }
    }

    @Override
    public CidsBean getCidsBean() {
        return this.cidsBean;
    }

    @Override
    public void setCidsBean(CidsBean cidsBean) {
        if (!(Objects.equals(this.cidsBean, cidsBean))){
            if (isEditor && (this.cidsBean != null)) {
                this.cidsBean.removePropertyChangeListener(changeListener);
            }
            try{
                bindingGroup.unbind();
                this.cidsBean = cidsBean;
                if (this.cidsBean != null){
                    DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                        bindingGroup,
                        this.cidsBean,
                        getConnectionContext());
                } 

                setMapWindow();
                bindingGroup.bind();
                if (isEditor && (this.cidsBean != null)) {
                        cidsBean.addPropertyChangeListener(changeListener);
                }
                cbGeomFest.updateUI();
            } catch (final Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            
        //cbGeomFest.updateUI();
        }
        
    }
    public boolean prepareForSave() {
        return everythingOk(cidsBean);
    }
    
    public boolean everythingOk (CidsBean cbOkay){
        boolean save = true;
        final StringBuilder errorMessage = new StringBuilder();
        // Art muss angegeben werden
        try {
            //if (cbArtF.getSelectedItem() == null) {
            if (cbOkay.getProperty(FIELD__ART) == null){//cbOkay.getProperty(FIELD__ART).toString().isEmpty()){
                LOG.warn("No art specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(BaumFestsetzungPanel.class, BUNDLE_NOART));
            }
        } catch (final MissingResourceException ex) {
            LOG.warn("Art not given.", ex); 
            save = false;
        }
        //Umfang muss vorhanden sein
        try {
            //if (txtUmfangF.getText().trim().isEmpty()) {
            if (cbOkay.getProperty(FIELD__UMFANG) == null){
                LOG.warn("No umfang specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(BaumFestsetzungPanel.class, BUNDLE_NOUMFANG));
            } 
        } catch (final MissingResourceException ex) {
            LOG.warn("Umfang not given.", ex);
            save = false;
        }
        // georeferenz muss gefüllt sein
        try {
            //if (cidsBean.getProperty(FIELD__GEOREFERENZ) == null) {
            if (cbOkay.getProperty(FIELD__GEOREFERENZ) == null){
                LOG.warn("No geom specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(BaumFestsetzungPanel.class, BUNDLE_NOGEOM));
            } else {
                //final CidsBean geom_pos = (CidsBean)cidsBean.getProperty(FIELD__GEOREFERENZ);
                final CidsBean geom_pos = (CidsBean)cbOkay.getProperty(FIELD__GEOREFERENZ);
                if (!((Geometry)geom_pos.getProperty(FIELD__GEO_FIELD)).getGeometryType().equals(GEOMTYPE)) {
                    LOG.warn("Wrong geom specified. Skip persisting.");
                    errorMessage.append(NbBundle.getMessage(BaumFestsetzungPanel.class, BUNDLE_WRONGGEOM));
                }
            }
        } catch (final MissingResourceException ex) {
            LOG.warn("Geom not given.", ex);
            save = false;
        }
        // Datum muss angegeben werden
        try {
            if (cbOkay.getProperty(FIELD__DATUM) == null) {
                LOG.warn("No datum specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(BaumFestsetzungEditor.class, BUNDLE_NODATE));
            }
        } catch (final MissingResourceException ex) {
            LOG.warn("Datum not given.", ex);
            save = false;
        }
        
        
        if (errorMessage.length() > 0) {
            JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                NbBundle.getMessage(BaumFestsetzungPanel.class, BUNDLE_PANE_PREFIX)
                        + errorMessage.toString()
                        + NbBundle.getMessage(BaumFestsetzungPanel.class, BUNDLE_PANE_SUFFIX),
                NbBundle.getMessage(BaumFestsetzungPanel.class, BUNDLE_PANE_TITLE),
                JOptionPane.WARNING_MESSAGE);

            return false;
        }
        return save;
    }
    /**
     * DOCUMENT ME!
     */
    public void setMapWindow() {
        final CidsBean cb = this.getCidsBean();
        if (cb != null){
            try {
                Double bufferMeter = 0.0;
                try{
                    bufferMeter = BaumConfProperties.getInstance().getBufferMeter();
                } catch (final Exception ex) {
                    Exceptions.printStackTrace(ex);
                    LOG.warn("Get no conf properties.", ex);
                }
                if (cb.getProperty(FIELD__GEOM) != null) {
                    panPreviewMap.initMap(cb, FIELD__GEOREFERENZ__GEO_FIELD, bufferMeter);
                } else {
                    final int srid = CrsTransformer.extractSridFromCrs(CismapBroker.getInstance().getSrs().getCode());
                    final BoundingBox initialBoundingBox;
                    initialBoundingBox = CismapBroker.getInstance().getMappingComponent().getMappingModel()
                                .getInitialBoundingBox();
                    final Point centerPoint = initialBoundingBox.getGeometry(srid).getCentroid();

                    final MetaClass geomMetaClass = ClassCacheMultiple.getMetaClass(
                            CidsBeanSupport.DOMAIN_NAME,
                            TABLE_GEOM,
                            getConnectionContext());
                    final CidsBean newGeom = geomMetaClass.getEmptyInstance(getConnectionContext()).getBean();
                    newGeom.setProperty(FIELD__GEO_FIELD, centerPoint);
                    panPreviewMap.initMap(newGeom, FIELD__GEO_FIELD, bufferMeter);
                }
            } catch (final Exception ex) {
                Exceptions.printStackTrace(ex);
                LOG.warn("Map window not set.", ex);
            }
        }
    }
}