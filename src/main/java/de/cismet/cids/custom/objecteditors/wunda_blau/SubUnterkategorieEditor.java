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

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;

import org.openide.util.NbBundle;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.MissingResourceException;

import javax.swing.*;

import de.cismet.cids.client.tools.DevelopmentTools;
import de.cismet.cids.custom.clientutils.HexcolorFormatter;
import de.cismet.cids.custom.objecteditors.utils.RendererTools;

import de.cismet.cids.custom.wunda_blau.search.server.RedundantObjectSearch;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.editors.DefaultBindableLabelsPanel;
import de.cismet.cids.editors.DefaultBindableReferenceCombo;

import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.FastBindableReferenceCombo;
import de.cismet.cids.editors.SaveVetoable;
import de.cismet.cids.editors.hooks.BeforeSavingHook;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.tools.gui.RoundedPanel;
import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
/**
 * DOCUMENT ME!
 *
 * @author   sandra
 * @version  $Revision$, $Date$
 */
public class SubUnterkategorieEditor extends DefaultCustomObjectEditor implements CidsBeanRenderer,
    SaveVetoable,
    BeforeSavingHook,
    PropertyChangeListener {
    //~ Static fields/initializers ---------------------------------------------
    private static final Logger LOG = Logger.getLogger(SubUnterkategorieEditor.class);
    public static final String REDUNDANT_TOSTRING_TEMPLATE = "%s";
    public static final String[] REDUNDANT_TOSTRING_FIELDS = { "name", "id" };
    public static final String REDUNDANT_TABLE = "sub_unterkategorie";
    
    private static DefaultBindableReferenceCombo.Option SORTING_OPTION =
        new DefaultBindableReferenceCombo.SortingColumnOption("name");

    public static final String FIELD__NAME = "name";                                         // sub_Unterkategorie
    public static final String FIELD__KANN = "arr_weitere_info_kann";                        // sub_Unterkategorie
    public static final String FIELD__MUSS = "arr_weitere_info_muss";                        // sub_Unterkategorie
    public static final String FIELD__FARBE = "farbe";                                       // sub_Unterkategorie
    public static final String FIELD__ID = "id";                                            // sub_Kategorie
    public static final String TABLE_NAME = "sub_unterkategorie";

    public static final String BUNDLE_NONAME = "SubUnterkategorieEditor.isOkForSaving().noName";
    public static final String BUNDLE_DUPLICATENAME = "SubUnterkategorieEditor.isOkForSaving().duplicateName";
    public static final String BUNDLE_NOFARBE = "SubUnterkategorieEditor.isOkForSaving().noFarbe";
    public static final String BUNDLE_NOSIGNATUR = "SubUnterkategorieEditor.isOkForSaving().noSignatur";
    public static final String BUNDLE_PANE_PREFIX = "SubUnterkategorieEditor.isOkForSaving().JOptionPane.message.prefix";
    public static final String BUNDLE_PANE_SUFFIX = "SubUnterkategorieEditor.isOkForSaving().JOptionPane.message.suffix";
    public static final String BUNDLE_PANE_TITLE = "SubUnterkategorieEditor.isOkForSaving().JOptionPane.title";
    private static final String TITLE_NEW_UNTERKATEGORIE = "eine neue UnterKategorie anlegen...";


    //~ Enums ------------------------------------------------------------------


    //~ Instance fields --------------------------------------------------------
    private Boolean redundantName = false;
    private SwingWorker worker_name;
    private final Collection<DefaultBindableLabelsPanel> labelsPanels = new ArrayList<>();
    private final boolean editor;
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private DefaultBindableLabelsPanel blpKann;
    private DefaultBindableLabelsPanel blpMuss;
    FastBindableReferenceCombo cbGeometrietyp;
    FastBindableReferenceCombo cbKategorie;
    private Box.Filler filler1;
    private JLabel lblFarbe;
    private JLabel lblFarbeAnzeige;
    private JLabel lblGeometrietyp;
    private JLabel lblKann;
    private JLabel lblKategorie;
    private JLabel lblMuss;
    private JLabel lblName;
    private JLabel lblSignatur;
    private JPanel panContent;
    private JPanel panKategorie;
    private JFormattedTextField txtFarbe;
    private JTextField txtName;
    private JTextField txtSignatur;
    private BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form.
     */
    public SubUnterkategorieEditor() {
        this(true);
    }

    /**
     * Creates a new SubKategorieEditor object.
     *
     * @param  boolEditor  DOCUMENT ME!
     */
    public SubUnterkategorieEditor(final boolean boolEditor) {
        this.editor = boolEditor;
    }

    //~ Methods ----------------------------------------------------------------

    
    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        super.initWithConnectionContext(connectionContext);
        initProperties();
        initComponents();
        for (final DefaultBindableLabelsPanel labelsPanel : Arrays.asList(blpKann, blpMuss)) {
            labelsPanel.initWithConnectionContext(getConnectionContext());
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

        panContent = new RoundedPanel();
        panKategorie = new JPanel();
        lblName = new JLabel();
        txtName = new JTextField();
        lblFarbe = new JLabel();
        lblSignatur = new JLabel();
        txtSignatur = new JTextField();
        lblMuss = new JLabel();
        blpMuss = new DefaultBindableLabelsPanel(isEditor(), "Muss-Attribut:", SORTING_OPTION);
        lblKann = new JLabel();
        blpKann = new DefaultBindableLabelsPanel(isEditor(), "Kann-Attribute:", SORTING_OPTION);
        lblGeometrietyp = new JLabel();
        cbGeometrietyp = new FastBindableReferenceCombo();
        lblKategorie = new JLabel();
        cbKategorie = new FastBindableReferenceCombo();
        txtFarbe = new JFormattedTextField(new HexcolorFormatter());
        lblFarbeAnzeige = new JLabel();
        filler1 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 0));

        setLayout(new GridBagLayout());

        panContent.setName(""); // NOI18N
        panContent.setOpaque(false);
        panContent.setLayout(new GridBagLayout());

        panKategorie.setOpaque(false);
        panKategorie.setLayout(new GridBagLayout());

        lblName.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblName.setText("Name:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panKategorie.add(lblName, gridBagConstraints);

        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.name}"), txtName, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panKategorie.add(txtName, gridBagConstraints);

        lblFarbe.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblFarbe.setText("Farbe:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panKategorie.add(lblFarbe, gridBagConstraints);

        lblSignatur.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblSignatur.setText("Signatur:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panKategorie.add(lblSignatur, gridBagConstraints);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.signatur}"), txtSignatur, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panKategorie.add(txtSignatur, gridBagConstraints);

        lblMuss.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblMuss.setText("Muss:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panKategorie.add(lblMuss, gridBagConstraints);

        blpMuss.setEnabled(false);
        blpMuss.setOpaque(false);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.arr_weitere_info_muss}"), blpMuss, BeanProperty.create("selectedElements"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 0, 5, 2);
        panKategorie.add(blpMuss, gridBagConstraints);

        lblKann.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblKann.setText("Kann:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panKategorie.add(lblKann, gridBagConstraints);

        blpKann.setEnabled(false);
        blpKann.setOpaque(false);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.arr_weitere_info_kann}"), blpKann, BeanProperty.create("selectedElements"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 0, 5, 2);
        panKategorie.add(blpKann, gridBagConstraints);

        lblGeometrietyp.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblGeometrietyp.setText("Geometrietyp:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panKategorie.add(lblGeometrietyp, gridBagConstraints);

        cbGeometrietyp.setMaximumRowCount(20);
        cbGeometrietyp.setModel(new LoadModelCb());

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_geometrietyp}"), cbGeometrietyp, BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panKategorie.add(cbGeometrietyp, gridBagConstraints);

        lblKategorie.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblKategorie.setText("Kategorie:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panKategorie.add(lblKategorie, gridBagConstraints);

        cbKategorie.setMaximumRowCount(20);
        cbKategorie.setModel(new LoadModelCb());

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_kategorie}"), cbKategorie, BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panKategorie.add(cbKategorie, gridBagConstraints);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.farbe}"), txtFarbe, BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panKategorie.add(txtFarbe, gridBagConstraints);

        lblFarbeAnzeige.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblFarbeAnzeige.setOpaque(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new Insets(2, 5, 2, 2);
        panKategorie.add(lblFarbeAnzeige, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        panContent.add(panKategorie, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panContent.add(filler1, gridBagConstraints);

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
                LOG.info("remove propchange sub_unterkategorie: " + getCidsBean());
                getCidsBean().removePropertyChangeListener(this);
            }
            labelsPanels.clear();
            blpKann.clear();
            blpMuss.clear();
            bindingGroup.unbind();
            this.cidsBean = cb;
            if (isEditor() && (getCidsBean() != null)) {
                LOG.info("add propchange sub_unterkategorie: " + getCidsBean());
                getCidsBean().addPropertyChangeListener(this);
            }
             // 8.5.17 s.Simmert: Methodenaufruf, weil sonst die Comboboxen nicht gefüllt werden
            // evtl. kann dies verbessert werden.
            DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                bindingGroup,
                cb,
                getConnectionContext());
            bindingGroup.bind();
            setTitle(getTitle());
            if (getCidsBean() != null){
                labelsPanels.addAll(Arrays.asList(blpKann, blpMuss));
            }
            if (getCidsBean() != null && getCidsBean().getProperty(FIELD__FARBE) != null){
                showColor(getCidsBean().getProperty(FIELD__FARBE).toString());
            }
        } catch (Exception ex) {
            LOG.error("Bean not set", ex);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setReadOnly() {
        if (!(isEditor())) {
            RendererTools.makeReadOnly(txtFarbe);
            RendererTools.makeReadOnly(txtName);
            RendererTools.makeReadOnly(txtSignatur);
            cbGeometrietyp.setEnabled(false);
            cbKategorie.setEnabled(false);
            blpKann.setEnabled(false);
            blpMuss.setEnabled(false);
        }
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
            return TITLE_NEW_UNTERKATEGORIE;
        } else {
            return getCidsBean().toString();
        }
    }

    
    @Override
    public void dispose() { 
        labelsPanels.clear();
        setCidsBean(null);
    }

    @Override
    public void setTitle(String title) {
        if (title == null) {
            title = "<Error>";
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case FIELD__KANN: {
                if(evt.getNewValue() != evt.getOldValue()){
                    final List<CidsBean> listKann = (List<CidsBean>)getCidsBean().getProperty(FIELD__KANN);
                    List<CidsBean> listMuss = (List<CidsBean>) getCidsBean().getProperty(FIELD__MUSS);
                    List <CidsBean> listMussNeu = new ArrayList<>();
                    for (CidsBean mussBean:listMuss){
                        if (listKann.contains(mussBean)){
                            listMussNeu.add(mussBean);
                        }
                    }
                    if (!listMussNeu.equals(listMuss)){
                        try{
                            //blpMuss.setSelectedElements(listMussNeu);
                            //blpMuss.reload(true);
                            if(listMussNeu.isEmpty()){
                                listMuss.clear();
                            } else {
                                listMuss.retainAll(listMussNeu);
                            }
                            blpMuss.reload(true);
                        } catch (Exception ex){
                            LOG.error("Bean (muss) not set", ex);
                        }
                    }
                }
            }
            case FIELD__MUSS: {
                if(evt.getNewValue() != evt.getOldValue()){
                    final List<CidsBean> listKann = (List<CidsBean>)getCidsBean().getProperty(FIELD__KANN);
                    List<CidsBean> listMuss = (List<CidsBean>) getCidsBean().getProperty(FIELD__MUSS);
                    List <CidsBean> listKannNeu = new ArrayList<>();
                    List <CidsBean> listKannAdd = new ArrayList<>();
                    for (CidsBean kannBean:listMuss){
                        if (!listKann.contains(kannBean)){
                            listKannAdd.add(kannBean);
                        }
                    }
                    if (!listKannAdd.isEmpty()){
                        listKannNeu.addAll(listKann);
                        listKannNeu.addAll(listKannAdd);
                        //blpKann.setSelectedElements(listKannNeu);
                        //blpKann.reload(true);
                       listKann.addAll(listKannAdd);
                       blpKann.reload(true);
                    }
                }
            }
            case FIELD__FARBE: {
                showColor(evt.getNewValue().toString());
            }
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
        try {
            if (txtName.getText().trim().isEmpty()) {
                LOG.warn("No name specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(SubUnterkategorieEditor.class, BUNDLE_NONAME));
                save = false;
            } else {
                if (redundantName) {
                    LOG.warn("False name specified. Skip persisting.");
                    errorMessage.append(NbBundle.getMessage(SubUnterkategorieEditor.class, BUNDLE_DUPLICATENAME));
                    save = false;
                }
            }
        } catch (final MissingResourceException ex) {
            LOG.warn("Name not given.", ex);
            save = false;
        }
        // Signatur vorhanden
        try {
            if (txtSignatur.getText().trim().isEmpty()) {
                LOG.warn("No signatur specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(SubKategorieEditor.class, BUNDLE_NOSIGNATUR));
                save = false;
            } 
        } catch (final MissingResourceException ex) {
            LOG.warn("Color not given.", ex);
            save = false;
        }
            
        if (errorMessage.length() > 0) {
            JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                NbBundle.getMessage(SubUnterkategorieEditor.class, BUNDLE_PANE_PREFIX)
                        + errorMessage.toString()
                        + NbBundle.getMessage(SubUnterkategorieEditor.class, BUNDLE_PANE_SUFFIX),
                NbBundle.getMessage(SubUnterkategorieEditor.class, BUNDLE_PANE_TITLE),
                JOptionPane.WARNING_MESSAGE);
        }
        return save;
    }


    @Override
    public void beforeSaving() {
        final RedundantObjectSearch unterkategorieSearch = new RedundantObjectSearch(
                REDUNDANT_TOSTRING_TEMPLATE,
                REDUNDANT_TOSTRING_FIELDS,
                null,
                REDUNDANT_TABLE);
        final Collection<String> conditions = new ArrayList<>();
        conditions.add(FIELD__NAME + " ilike '" + txtName.getText().trim() + "'");
        conditions.add(FIELD__ID + " <> " + getCidsBean().getProperty(FIELD__ID));
        unterkategorieSearch.setWhere(conditions);
        try {
            redundantName =
                !(SessionManager.getProxy().customServerSearch(
                        SessionManager.getSession().getUser(),
                        unterkategorieSearch,
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
