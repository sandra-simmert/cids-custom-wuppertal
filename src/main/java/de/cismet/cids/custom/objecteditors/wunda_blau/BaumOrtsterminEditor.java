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
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import de.cismet.cids.client.tools.DevelopmentTools;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.BindingGroup;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import java.util.Collections;
import java.util.concurrent.ExecutionException;


import javax.swing.*;

import de.cismet.cids.custom.objecteditors.utils.RendererTools;
import de.cismet.cids.custom.objectrenderer.utils.CidsBeanSupport;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.BindingGroupStore;
import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.EditorClosedEvent;
import de.cismet.cids.editors.EditorSaveListener;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.tools.gui.StaticSwingTools;

import de.cismet.cids.custom.objecteditors.wunda_blau.albo.ComboBoxFilterDialog;
import de.cismet.cids.custom.utils.CidsBeansTableModel;
import de.cismet.cids.custom.wunda_blau.search.server.BaumMeldungLightweightSearch;
import de.cismet.cids.custom.wunda_blau.search.server.BaumMeldungSearch;
import de.cismet.cids.editors.DefaultBindableDateChooser;
import de.cismet.cids.editors.converters.SqlDateToUtilDateConverter;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.tools.gui.RoundedPanel;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.MissingResourceException;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingx.JXTable;
/**
 * DOCUMENT ME!
 *
 * @author   sandra
 * @version  $Revision$, $Date$
 */
public class BaumOrtsterminEditor extends DefaultCustomObjectEditor implements CidsBeanRenderer,
    EditorSaveListener,
    BindingGroupStore,
    PropertyChangeListener {

    //~ Static fields/initializers ---------------------------------------------
    private MetaClass meldungMetaClass;
    private static final Comparator<Object> COMPARATOR = new Comparator<Object>() {

            @Override
           /* public int compare(final Object o1, final Object o2) {
                return AlphanumComparator.getInstance().compare(String.valueOf(o1), String.valueOf(o2));
            }*/
            public int compare(final Object o1, final Object o2) {
                    return String.valueOf(o1).compareTo(String.valueOf(o2));
                }
        };   
    public static final String GEOMTYPE = "Point";
    public static final int FOTO_WIDTH = 150;

    private List<CidsBean> teilBeans;
    private static final Logger LOG = Logger.getLogger(BaumOrtsterminEditor.class);
    private static final String[] MELDUNG_COL_NAMES = new String[] {  "Gebiet-Aktenzeichen", "Gebiet-Bemerkung", "Meldungsdatum", "Meldung-Bemerkung" };
    private static final String[] MELDUNG_PROP_NAMES = new String[] {
            "fk_gebiet.aktenzeichen",
            "fk_gebiet.bemerkung",
            "datum",
            "bemerkung"
        };
    private static final Class[] MELDUNG_PROP_TYPES = new Class[] {
            CidsBean.class,
            CidsBean.class, 
            Date.class,
            String.class
        };
    private static final String[] LOADING_COL_NAMES = new String[] { "Die Daten werden geladen......"};
    private static final String[] MUSTSET_COL_NAMES = new String[] { "Die Daten bitte zuweisen......"};
    
    private static final String TITLE_NEW_ORTSTERMIN = "einen neuen Ortstermin anlegen ....";
    
    public static final String FIELD__TEILNEHMER = "n_teilnehmer";              // baum_ortstermin
    public static final String FIELD__DATUM = "datum";                          // baum_ortstermin
    public static final String FIELD__ID = "id";                                // baum_ortstermin
    public static final String FIELD__MELDUNG = "fk_meldung";                   // baum_ortstermin
    public static final String FIELD__MELDUNG_ID = "fk_meldung.id";             // baum_meldung
    public static final String FIELD__MELDUNG_DATUM = "fk_meldung.datum";       // baum_meldung
    public static final String FIELD__GEBIET_AZ = "fk_meldung.fk_gebiet.aktenzeichen";       // baum_gebiet
    public static final String FIELD__NAME = "name";                            // baum_teilnehmer
    public static final String FIELD__MELDUNGEN = "n_ortstermine";              // baum_meldung
    public static final String FIELD__TEILNEHMER_OTSTERMIN = "fk_ortstermin";   // baum_teilnehmer
    public static final String FIELD__TEILNEHMER_NAME = "name";                 // baum_teilnehmer
    public static final String FIELD__TEILNEHMER_TELEFON = "telefon";           // baum_teilnehmer
    public static final String FIELD__TEILNEHMER_BEMERKUNG = "bemerkung";       // baum_teilnehmer
    public static final String TABLE_NAME__MELDUNG = "baum_meldung";
    public static final String TABLE_NAME__TEILNEHMER = "baum_teilnehmer"; 
    
    public static final String BUNDLE_PANE_PREFIX =
        "BaumOrtsterminEditor.prepareForSave().JOptionPane.message.prefix";
    public static final String BUNDLE_PANE_SUFFIX =
        "BaumOrtsterminEditor.prepareForSave().JOptionPane.message.suffix";
    public static final String BUNDLE_PANE_TITLE = "BaumOrtsterminEditor.prepareForSave().JOptionPane.title";
    public static final String BUNDLE_TEIL_QUESTION = "BaumOrtsterminEditor.btnRemoveTeilActionPerformed().question";
    public static final String BUNDLE_TEIL_TITLE = "BaumOrtsterminEditor.btnRemoveTeilActionPerformed().title";
    public static final String BUNDLE_TEIL_ERRORTITLE = "BaumOrtsterminEditor.btnRemoveTeilrActionPerformed().errortitle";
    public static final String BUNDLE_TEIL_ERRORTEXT = "BaumOrtsterminEditor.btnRemoveTeilActionPerformed().errortext";
    public static final String BUNDLE_NOMELDUNG = "BaumOrtsterminEditor.prepareForSave().noMeldung";
    
    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static enum OtherTableCases {

        //~ Enum constants -----------------------------------------------------

        SET_VALUE, REDUNDANT_ATT_NAME
    }

    //~ Instance fields --------------------------------------------------------
    
    private MetaClass teilnehmerMetaClass;

    private boolean isEditor = true;
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.cismet.cids.custom.objecteditors.wunda_blau.BaumOrtsterminPanel baumOrtsterminPanel;
    private javax.swing.JButton btnChangeGebiet;
    private de.cismet.cids.custom.objecteditors.wunda_blau.albo.ComboBoxFilterDialog comboBoxFilterDialogGebiet;
    private javax.swing.JScrollPane jScrollPaneMeldung;
    private javax.swing.JLabel lblGebiet_Meldung;
    private javax.swing.JPanel panContent;
    javax.swing.JPanel panOrtstermin;
    javax.swing.JPanel panOrtstermineMain;
    private de.cismet.cids.editors.converters.SqlDateToUtilDateConverter sqlDateToUtilDateConverter;
    private org.jdesktop.swingx.JXTable xtMeldung;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form.
     */
    public BaumOrtsterminEditor() {
    }

    /**
     * Creates a new BaumOrtsterminEditor object.
     *
     * @param  boolEditor  DOCUMENT ME!
     */
    public BaumOrtsterminEditor(final boolean boolEditor) {
        this.isEditor = boolEditor;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        super.initWithConnectionContext(connectionContext);
        initComponents();
        meldungMetaClass = ClassCacheMultiple.getMetaClass(
                CidsBeanSupport.DOMAIN_NAME,
                TABLE_NAME__MELDUNG,
                connectionContext);
        setReadOnly();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        sqlDateToUtilDateConverter = new de.cismet.cids.editors.converters.SqlDateToUtilDateConverter();
        comboBoxFilterDialogGebiet = new de.cismet.cids.custom.objecteditors.wunda_blau.albo.ComboBoxFilterDialog(null, new de.cismet.cids.custom.wunda_blau.search.server.BaumMeldungLightweightSearch(), "Gebiet-Meldung auswählen", getConnectionContext());
        panContent = new RoundedPanel();
        panOrtstermin = new javax.swing.JPanel();
        lblGebiet_Meldung = new javax.swing.JLabel();
        btnChangeGebiet = new javax.swing.JButton();
        jScrollPaneMeldung = new javax.swing.JScrollPane();
        xtMeldung = new org.jdesktop.swingx.JXTable();
        panOrtstermineMain = new javax.swing.JPanel();
        baumOrtsterminPanel = baumOrtsterminPanel = new BaumOrtsterminPanel(null, this, true, this.getConnectionContext());

        setLayout(new java.awt.GridBagLayout());

        panContent.setName(""); // NOI18N
        panContent.setOpaque(false);
        panContent.setLayout(new java.awt.GridBagLayout());

        panOrtstermin.setOpaque(false);
        panOrtstermin.setLayout(new java.awt.GridBagLayout());

        lblGebiet_Meldung.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblGebiet_Meldung.setText("Gebiet-Meldung:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panOrtstermin.add(lblGebiet_Meldung, gridBagConstraints);

        btnChangeGebiet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/custom/wunda_blau/res/tick_32.png"))); // NOI18N
        btnChangeGebiet.setToolTipText("Gebiet - Meldung zuweisen");
        btnChangeGebiet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeGebietActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 2);
        panOrtstermin.add(btnChangeGebiet, gridBagConstraints);
        btnChangeGebiet.setVisible(isEditor);

        xtMeldung.setModel(new OrtsterminMeldungTableModel());
        xtMeldung.setVisibleRowCount(1);
        jScrollPaneMeldung.setViewportView(xtMeldung);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOrtstermin.add(jScrollPaneMeldung, gridBagConstraints);

        panOrtstermineMain.setOpaque(false);
        panOrtstermineMain.setLayout(new java.awt.GridBagLayout());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean}"), baumOrtsterminPanel, org.jdesktop.beansbinding.BeanProperty.create("cidsBean"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOrtstermineMain.add(baumOrtsterminPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        panOrtstermin.add(panOrtstermineMain, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panContent.add(panOrtstermin, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panContent, gridBagConstraints);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChangeGebietActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnChangeGebietActionPerformed
        final Object selectedItem = comboBoxFilterDialogGebiet.showAndGetSelected();
        if (selectedItem instanceof CidsBean) {
            final CidsBean meldungBean = (CidsBean)selectedItem;
            setMeldungTable(meldungBean);

            xtMeldung.getTableHeader().setForeground(Color.BLACK);
            try {
                cidsBean.setProperty(FIELD__MELDUNG, meldungBean);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }//GEN-LAST:event_btnChangeGebietActionPerformed

    
    @Override
    public boolean prepareForSave() {
        boolean save = true;
        final StringBuilder errorMessage = new StringBuilder();
        
        if (!baumOrtsterminPanel.prepareForSave()){
          return false;
        }
        try {
            if (cidsBean.getProperty(FIELD__MELDUNG_ID) == null) {
                LOG.warn("No meldung specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(BaumOrtsterminEditor.class, BUNDLE_NOMELDUNG));
            }
        } catch (final MissingResourceException ex) {
            LOG.warn("Geom not given.", ex);
            save = false;
        }

        
        if (errorMessage.length() > 0) {
            JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                NbBundle.getMessage(BaumOrtsterminEditor.class, BUNDLE_PANE_PREFIX)
                        + errorMessage.toString()
                        + NbBundle.getMessage(BaumOrtsterminEditor.class, BUNDLE_PANE_SUFFIX),
                NbBundle.getMessage(BaumOrtsterminEditor.class, BUNDLE_PANE_TITLE),
                JOptionPane.WARNING_MESSAGE);

            return false;
        }
        return save;
    }

    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cb) {
        // dispose();  Wenn Aufruf hier, dann cbGeom.getSelectedItem()wird ein neu gezeichnetes Polygon nicht erkannt.
        try {
            bindingGroup.unbind();
            this.cidsBean = cb;
            if (teilBeans != null) {
                Collections.sort((List)teilBeans, COMPARATOR);
            }
        // 8.5.17 s.Simmert: Methodenaufruf, weil sonst die Comboboxen nicht gefüllt werden
            // evtl. kann dies verbessert werden.
            DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                bindingGroup,
                cb,
                getConnectionContext());
            bindingGroup.bind();
            //searchMeldungen();
            
            xtMeldung.getColumn(2).setWidth(100);
            xtMeldung.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
            if(cidsBean.getProperty(FIELD__MELDUNG) == null){
                xtMeldung.getTableHeader().setForeground(Color.red);
            }else{
                xtMeldung.getTableHeader().setForeground(Color.BLACK);
                setMeldungTable((CidsBean)cidsBean.getProperty(FIELD__MELDUNG));
            }
            xtMeldung.addMouseMotionListener(new MouseAdapter(){
                @Override
		public void mouseMoved(MouseEvent e) {
                    int row=xtMeldung.rowAtPoint(e.getPoint());
                    int col=xtMeldung.columnAtPoint(e.getPoint());
                    if(row>-1 && col>-1){
                        Object value=xtMeldung.getValueAt(row, col);
                        if(null!=value && !"".equals(value)){
                            xtMeldung.setToolTipText(value.toString());
                        }else{
                            xtMeldung.setToolTipText(null);//keinTooltip anzeigen
                        }
                    }
                }
            });
            } catch (final Exception ex) {
                Exceptions.printStackTrace(ex);
                LOG.error("Bean not set.", ex);
            }
    }
    
    
    private void setMeldungTable(CidsBean meldungBean){
        List<CidsBean> meldungBeans = new ArrayList<>();
        meldungBeans.add(meldungBean);
        //xtMeldung.setModel(new OrtsterminMeldungTableModel());
        ((OrtsterminMeldungTableModel)xtMeldung.getModel()).setCidsBeans(meldungBeans);
    }
    
    private void searchMeldungen() {
        xtMeldung.setModel(new LoadingTableModel());

        if (getCidsBean() != null) {
            new SwingWorker<List<CidsBean>, Void>() {

                    @Override
                    protected List<CidsBean> doInBackground() throws Exception {
                        final BaumMeldungSearch search = new BaumMeldungSearch();
                        search.setOrtsterminId((Integer)getCidsBean().getProperty(FIELD__ID));
                        search.setOrtsterminFKMeldung((Integer)getCidsBean().getProperty(FIELD__MELDUNG_ID));

                        final Collection<MetaObjectNode> mons = (Collection)SessionManager.getProxy()
                                    .customServerSearch(SessionManager.getSession().getUser(),
                                            search,
                                            getConnectionContext());

                        if (mons == null) {
                            xtMeldung.setModel(new MustSetTableModel());
                            return null;
                        }

                        final List<CidsBean> beans = new ArrayList<>();
                        for (final MetaObjectNode mon : mons) {
                            
                            beans.add(SessionManager.getProxy().getMetaObject(
                                    mon.getObjectId(),
                                    mon.getClassId(),
                                    "WUNDA_BLAU",
                                    getConnectionContext()).getBean());
                        }
                        return beans;
                    }

                    @Override
                    protected void done() {
                        try {
                            final List<CidsBean> beans = get();
                            List<CidsBean> meldungBeans = new ArrayList<>();
                            if (cidsBean.getProperty(FIELD__MELDUNG_ID) != null){
                                for (final CidsBean cb : beans){
                                    if(cb.getProperty(FIELD__ID).toString().equals(cidsBean.getProperty(FIELD__MELDUNG_ID).toString())){
                                       meldungBeans.add(cb);
                                       break;
                                    }
                                }
                            }
                            if(meldungBeans.isEmpty()){
                                xtMeldung.setModel(new MustSetTableModel());
                            }else{
                                xtMeldung.setModel(new OrtsterminMeldungTableModel());
                                ((OrtsterminMeldungTableModel)xtMeldung.getModel()).setCidsBeans(meldungBeans);
                            }
                        } catch (final InterruptedException | ExecutionException ex) {
                            LOG.fatal(ex, ex);
                        }
                    }
                }.execute();
        }
    }
    /**
     * DOCUMENT ME!
     */
    private void setReadOnly() {
        if (!(isEditor)) {
        }
        RendererTools.makeReadOnly(xtMeldung);
    }
    
    public static void main(final String[] args) throws Exception {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        final MappingComponent mc = new MappingComponent();
        CismapBroker.getInstance().setMappingComponent(mc);
        DevelopmentTools.createEditorFromRestfulConnection(
            DevelopmentTools.RESTFUL_CALLSERVER_CALLSERVER,
            "WUNDA_BLAU",
            null,
            true,
            "baum_ortstermin",
            1,
            800,
            600);
    }

    @Override
    public String getTitle() {
        if (cidsBean.getMetaObject().getStatus() == MetaObject.NEW){
            return TITLE_NEW_ORTSTERMIN;
        } else {
            return String.format("Gebiet: %s - Meldung: %s - Ortstermin: %s", cidsBean.getProperty(FIELD__GEBIET_AZ), cidsBean.getProperty(FIELD__MELDUNG_DATUM), cidsBean.getProperty(FIELD__DATUM));
        }
        
    }

    @Override
    public void dispose() {
        super.dispose();
     //   ((OrtsterminMeldungTableModel)xtMeldung.getModel()).clear();
    }
    
    @Override
    public void setTitle(final String string) {
    }

    @Override
    public void editorClosed(final EditorClosedEvent ece) {
    }

    @Override
    public BindingGroup getBindingGroup() {
        return bindingGroup;
    }

    class OrtsterminMeldungTableModel extends CidsBeansTableModel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new OrtsterminMeldungTableModel object.
         */
        public OrtsterminMeldungTableModel() {
            super(MELDUNG_PROP_NAMES, MELDUNG_COL_NAMES, MELDUNG_PROP_TYPES);
        }
    }  
    class LoadingTableModel extends CidsBeansTableModel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LoadingTableModel object.
         */
        public LoadingTableModel() {
            super( MELDUNG_PROP_NAMES,LOADING_COL_NAMES, MELDUNG_PROP_TYPES);
        }
    } 
    class MustSetTableModel extends CidsBeansTableModel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LoadingTableModel object.
         */
        public MustSetTableModel() {
            super( MELDUNG_PROP_NAMES,MUSTSET_COL_NAMES, MELDUNG_PROP_TYPES);
        }
    }
}
    
