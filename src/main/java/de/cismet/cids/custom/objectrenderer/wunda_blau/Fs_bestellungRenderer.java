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
package de.cismet.cids.custom.objectrenderer.wunda_blau;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;

import com.vividsolutions.jts.geom.Geometry;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

import org.jdesktop.beansbinding.Converter;

import java.awt.Color;
import java.awt.EventQueue;

import java.text.DateFormat;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.SwingWorker;

import de.cismet.cids.custom.objectrenderer.converter.SQLDateToStringConverter;
import de.cismet.cids.custom.objectrenderer.utils.ObjectRendererUtils;
import de.cismet.cids.custom.utils.alkis.AlkisConstants;
import de.cismet.cids.custom.wunda_blau.search.server.CidsAlkisSearchStatement;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.features.DefaultStyledFeature;
import de.cismet.cismap.commons.features.StyledFeature;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWMS;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWmsGetMapUrl;

import de.cismet.tools.gui.FooterComponentProvider;
import de.cismet.tools.gui.TitleComponentProvider;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class Fs_bestellungRenderer extends javax.swing.JPanel implements CidsBeanRenderer,
    TitleComponentProvider,
    FooterComponentProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            Fs_bestellungRenderer.class);

    private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM,
            DateFormat.SHORT);

    private static final Converter<Boolean, String> CONVERTER_BEZUGSWEG = new Converter<Boolean, String>() {

            @Override
            public String convertForward(final Boolean s) {
                if (Boolean.TRUE.equals(s)) {
                    return "Postweg";
                } else {
                    return "Download";
                }
            }

            @Override
            public Boolean convertReverse(final String t) {
                return null;
            }
        };

    private static final Converter<Boolean, String> CONVERTER_STATUS = new Converter<Boolean, String>() {

            @Override
            public String convertForward(final Boolean s) {
                if (Boolean.TRUE.equals(s)) {
                    return "erledigt";
                } else {
                    return "in Bearbeitung";
                }
            }

            @Override
            public Boolean convertReverse(final String t) {
                return null;
            }
        };

    //~ Instance fields --------------------------------------------------------

    private String title;
    private CidsBean cidsBean;
    private MetaObjectNode flurstueckMon;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXHyperlink hlEMailValue;
    private org.jdesktop.swingx.JXHyperlink hlFlurstueckValue;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel labInfoTitle;
    private javax.swing.JLabel labInfoTitle1;
    private javax.swing.JLabel labInfoTitle2;
    private javax.swing.JLabel lblBezugsweg;
    private javax.swing.JLabel lblBezugswegValue;
    private javax.swing.JLabel lblEMail;
    private javax.swing.JLabel lblEingegangenAm;
    private javax.swing.JLabel lblEingegangenAmValue;
    private javax.swing.JLabel lblFlurstueck;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblFormatValue;
    private javax.swing.JLabel lblLaFirma;
    private javax.swing.JLabel lblLaFirmaValue;
    private javax.swing.JLabel lblLaName;
    private javax.swing.JLabel lblLaNameValue;
    private javax.swing.JLabel lblLaOrt;
    private javax.swing.JLabel lblLaOrtValue;
    private javax.swing.JLabel lblLaStrasse;
    private javax.swing.JLabel lblLaStrasseValue;
    private javax.swing.JLabel lblMassstab;
    private javax.swing.JLabel lblMassstabValue;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNameValue;
    private javax.swing.JLabel lblOrt;
    private javax.swing.JLabel lblOrtValue;
    private javax.swing.JLabel lblProdukt;
    private javax.swing.JLabel lblProduktValue;
    private javax.swing.JLabel lblRaFirma;
    private javax.swing.JLabel lblRaFirmaValue;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusValue;
    private javax.swing.JLabel lblStrasse;
    private javax.swing.JLabel lblStrasseValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitleValue;
    private javax.swing.JLabel lblTransId;
    private javax.swing.JLabel lblTransIdValue;
    private de.cismet.cismap.commons.gui.MappingComponent mappingComponent1;
    private javax.swing.JPanel panAdressen;
    private javax.swing.JPanel panFiller;
    private javax.swing.JPanel panFooter;
    private javax.swing.JPanel panInfo;
    private javax.swing.JPanel panLieferanschrift;
    private javax.swing.JPanel panMain;
    private javax.swing.JPanel panMap;
    private javax.swing.JPanel panRechnungsanschrift;
    private javax.swing.JPanel panTitle;
    private de.cismet.tools.gui.SemiRoundedPanel semiRoundedPanel1;
    private de.cismet.tools.gui.SemiRoundedPanel semiRoundedPanel2;
    private de.cismet.tools.gui.SemiRoundedPanel semiRoundedPanel3;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form Fs_BestellungRenderer.
     */
    public Fs_bestellungRenderer() {
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  landparceBean  DOCUMENT ME!
     */
    private void initMap(final CidsBean landparceBean) {
        final Runnable mapRunnable = new Runnable() {

                @Override
                public void run() {
                    final Geometry geom = (Geometry)landparceBean.getProperty("geometrie.geo_field");
                    final Geometry pureGeom = CrsTransformer.transformToGivenCrs(
                            geom,
                            AlkisConstants.COMMONS.SRS_SERVICE);
                    final de.cismet.cismap.commons.XBoundingBox box = new de.cismet.cismap.commons.XBoundingBox(
                            pureGeom.getEnvelope().buffer(AlkisConstants.COMMONS.GEO_BUFFER));
                    final ActiveLayerModel mappingModel = new ActiveLayerModel();
                    mappingModel.setSrs(AlkisConstants.COMMONS.SRS_SERVICE);
                    mappingModel.addHome(new XBoundingBox(
                            box.getX1(),
                            box.getY1(),
                            box.getX2(),
                            box.getY2(),
                            AlkisConstants.COMMONS.SRS_SERVICE,
                            true));
                    final SimpleWMS swms = new SimpleWMS(new SimpleWmsGetMapUrl(
                                AlkisConstants.COMMONS.MAP_CALL_STRING));
                    swms.setName("Flurstueck");
                    final StyledFeature dsf = new DefaultStyledFeature();
                    dsf.setGeometry(pureGeom);
                    dsf.setFillingPaint(new Color(1, 0, 0, 0.75F));
                    // add the raster layer to the model
                    mappingModel.addLayer(swms);
                    // set the model
                    mappingComponent1.setMappingModel(mappingModel);
                    // initial positioning of the map
                    final int duration = mappingComponent1.getAnimationDuration();
                    mappingComponent1.setAnimationDuration(0);
                    mappingComponent1.gotoInitialBoundingBox();
                    // interaction mode
                    mappingComponent1.setInteractionMode(MappingComponent.ZOOM);
                    // finally when all configurations are done ...
                    mappingComponent1.unlock();
                    mappingComponent1.addCustomInputListener("MUTE", new PBasicInputEventHandler() {

                            @Override
                            public void mouseClicked(final PInputEvent evt) {
                                if (evt.getClickCount() > 1) {
                                    final CidsBean bean = cidsBean;
                                    ObjectRendererUtils.switchToCismapMap();
                                    ObjectRendererUtils.addBeanGeomAsFeatureToCismapMap(bean, false);
                                }
                            }
                        });
                    mappingComponent1.setInteractionMode("MUTE");
                    mappingComponent1.getFeatureCollection().addFeature(dsf);
                    mappingComponent1.setAnimationDuration(duration);
                }
            };
        if (EventQueue.isDispatchThread()) {
            mapRunnable.run();
        } else {
            EventQueue.invokeLater(mapRunnable);
        }
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

        panTitle = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblTitleValue = new javax.swing.JLabel();
        panFooter = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panMain = new javax.swing.JPanel();
        panInfo = new javax.swing.JPanel();
        semiRoundedPanel1 = new de.cismet.tools.gui.SemiRoundedPanel();
        labInfoTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblTransId = new javax.swing.JLabel();
        lblTransIdValue = new javax.swing.JLabel();
        lblEingegangenAm = new javax.swing.JLabel();
        lblEingegangenAmValue = new javax.swing.JLabel();
        lblBezugsweg = new javax.swing.JLabel();
        lblBezugswegValue = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblStatusValue = new javax.swing.JLabel();
        lblFlurstueck = new javax.swing.JLabel();
        hlFlurstueckValue = new org.jdesktop.swingx.JXHyperlink();
        lblProdukt = new javax.swing.JLabel();
        lblProduktValue = new javax.swing.JLabel();
        lblFormat = new javax.swing.JLabel();
        lblFormatValue = new javax.swing.JLabel();
        lblMassstab = new javax.swing.JLabel();
        lblMassstabValue = new javax.swing.JLabel();
        panMap = new javax.swing.JPanel();
        mappingComponent1 = new de.cismet.cismap.commons.gui.MappingComponent();
        panAdressen = new javax.swing.JPanel();
        panLieferanschrift = new javax.swing.JPanel();
        semiRoundedPanel2 = new de.cismet.tools.gui.SemiRoundedPanel();
        labInfoTitle1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblLaFirma = new javax.swing.JLabel();
        lblLaFirmaValue = new javax.swing.JLabel();
        lblLaName = new javax.swing.JLabel();
        lblLaNameValue = new javax.swing.JLabel();
        lblLaStrasse = new javax.swing.JLabel();
        lblLaStrasseValue = new javax.swing.JLabel();
        lblLaOrt = new javax.swing.JLabel();
        lblLaOrtValue = new javax.swing.JLabel();
        lblEMail = new javax.swing.JLabel();
        hlEMailValue = new org.jdesktop.swingx.JXHyperlink();
        panRechnungsanschrift = new javax.swing.JPanel();
        semiRoundedPanel3 = new de.cismet.tools.gui.SemiRoundedPanel();
        labInfoTitle2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblRaFirma = new javax.swing.JLabel();
        lblRaFirmaValue = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblNameValue = new javax.swing.JLabel();
        lblStrasse = new javax.swing.JLabel();
        lblStrasseValue = new javax.swing.JLabel();
        lblOrt = new javax.swing.JLabel();
        lblOrtValue = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        panFiller = new javax.swing.JPanel();

        panTitle.setOpaque(false);
        panTitle.setLayout(new java.awt.GridBagLayout());

        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(lblTitle, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblTitle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panTitle.add(lblTitle, gridBagConstraints);

        lblTitleValue.setForeground(new java.awt.Color(255, 255, 255));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${title}"), lblTitleValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panTitle.add(lblTitleValue, gridBagConstraints);

        panFooter.setOpaque(false);
        panFooter.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.jLabel1.text")); // NOI18N
        panFooter.add(jLabel1, new java.awt.GridBagConstraints());

        setLayout(new java.awt.GridBagLayout());

        panMain.setOpaque(false);
        panMain.setLayout(new java.awt.GridBagLayout());

        panInfo.setOpaque(false);
        panInfo.setLayout(new java.awt.GridBagLayout());

        semiRoundedPanel1.setBackground(java.awt.Color.darkGray);
        semiRoundedPanel1.setLayout(new java.awt.GridBagLayout());

        labInfoTitle.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(labInfoTitle, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.labInfoTitle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        semiRoundedPanel1.add(labInfoTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panInfo.add(semiRoundedPanel1, gridBagConstraints);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(lblTransId, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblTransId.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblTransId, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.transid}"), lblTransIdValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblTransIdValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblEingegangenAm, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblEingegangenAm.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblEingegangenAm, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.eingegangen_am}"), lblEingegangenAmValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        binding.setConverter(new SQLDateToStringConverter(DATE_FORMAT));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblEingegangenAmValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblBezugsweg, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblBezugsweg.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblBezugsweg, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.postweg}"), lblBezugswegValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        binding.setConverter(CONVERTER_BEZUGSWEG);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblBezugswegValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblStatus, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblStatus.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblStatus, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.erledigt}"), lblStatusValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        binding.setConverter(CONVERTER_STATUS);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblStatusValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblFlurstueck, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblFlurstueck.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblFlurstueck, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.landparcelcode != null}"), hlFlurstueckValue, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.landparcelcode}"), hlFlurstueckValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        hlFlurstueckValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hlFlurstueckValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(hlFlurstueckValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblProdukt, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblProdukt.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblProdukt, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_produkt.name}"), lblProduktValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblProduktValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblFormat, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblFormat.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblFormat, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_format.format}"), lblFormatValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblFormatValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblMassstab, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblMassstab.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblMassstab, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("1:${cidsBean.massstab}"), lblMassstabValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(lblMassstabValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInfo.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        panMain.add(panInfo, gridBagConstraints);

        panMap.setMinimumSize(new java.awt.Dimension(200, 100));
        panMap.setPreferredSize(new java.awt.Dimension(400, 200));
        panMap.setLayout(new java.awt.BorderLayout());
        panMap.add(mappingComponent1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 0);
        panMain.add(panMap, gridBagConstraints);

        panAdressen.setOpaque(false);
        panAdressen.setLayout(new java.awt.GridLayout(1, 0, 20, 20));

        panLieferanschrift.setOpaque(false);
        panLieferanschrift.setLayout(new java.awt.GridBagLayout());

        semiRoundedPanel2.setBackground(java.awt.Color.darkGray);
        semiRoundedPanel2.setLayout(new java.awt.GridBagLayout());

        labInfoTitle1.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(labInfoTitle1, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.labInfoTitle1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        semiRoundedPanel2.add(labInfoTitle1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panLieferanschrift.add(semiRoundedPanel2, gridBagConstraints);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(lblLaFirma, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblLaFirma.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblLaFirma, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_adresse_versand.firma}"), lblLaFirmaValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblLaFirmaValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblLaName, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblLaName.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblLaName, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_adresse_versand.vorname} ${cidsBean.fk_adresse_versand.name}"), lblLaNameValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblLaNameValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblLaStrasse, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblLaStrasse.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblLaStrasse, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_adresse_versand.strasse} ${cidsBean.fk_adresse_versand.hausnummer}"), lblLaStrasseValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblLaStrasseValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblLaOrt, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblLaOrt.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblLaOrt, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_adresse_versand.plz} ${cidsBean.fk_adresse_versand.ort}"), lblLaOrtValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblLaOrtValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblEMail, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblEMail.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblEMail, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.email != null}"), hlEMailValue, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.email}"), hlEMailValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(hlEMailValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLieferanschrift.add(jPanel2, gridBagConstraints);

        panAdressen.add(panLieferanschrift);

        panRechnungsanschrift.setOpaque(false);
        panRechnungsanschrift.setLayout(new java.awt.GridBagLayout());

        semiRoundedPanel3.setBackground(java.awt.Color.darkGray);
        semiRoundedPanel3.setLayout(new java.awt.GridBagLayout());

        labInfoTitle2.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(labInfoTitle2, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.labInfoTitle2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        semiRoundedPanel3.add(labInfoTitle2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panRechnungsanschrift.add(semiRoundedPanel3, gridBagConstraints);

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(lblRaFirma, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblRaFirma.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(lblRaFirma, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_adresse_rechnung.firma}"), lblRaFirmaValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(lblRaFirmaValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblName, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblName.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(lblName, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_adresse_rechnung.vorname} ${cidsBean.fk_adresse_rechnung.name}"), lblNameValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(lblNameValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblStrasse, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblStrasse.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(lblStrasse, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_adresse_rechnung.strasse} ${cidsBean.fk_adresse_rechnung.hausnummer}"), lblStrasseValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(lblStrasseValue, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblOrt, org.openide.util.NbBundle.getMessage(Fs_bestellungRenderer.class, "Fs_bestellungRenderer.lblOrt.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(lblOrt, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${cidsBean.fk_adresse_rechnung.plz} ${cidsBean.fk_adresse_rechnung.ort}"), lblOrtValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(lblOrtValue, gridBagConstraints);

        jPanel4.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRechnungsanschrift.add(jPanel3, gridBagConstraints);

        panAdressen.add(panRechnungsanschrift);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panMain.add(panAdressen, gridBagConstraints);

        panFiller.setOpaque(false);

        javax.swing.GroupLayout panFillerLayout = new javax.swing.GroupLayout(panFiller);
        panFiller.setLayout(panFillerLayout);
        panFillerLayout.setHorizontalGroup(
            panFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 846, Short.MAX_VALUE)
        );
        panFillerLayout.setVerticalGroup(
            panFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(panFiller, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panMain, gridBagConstraints);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void hlFlurstueckValueActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hlFlurstueckValueActionPerformed
        if (flurstueckMon != null) {
            ComponentRegistry.getRegistry().getDescriptionPane().gotoMetaObjectNode(flurstueckMon, false);
        }
    }//GEN-LAST:event_hlFlurstueckValueActionPerformed

    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        bindingGroup.unbind();
        this.flurstueckMon = null;
        this.cidsBean = cidsBean;

        if (cidsBean != null) {
            new SwingWorker<CidsBean, Void>() {

                    @Override
                    protected CidsBean doInBackground() throws Exception {
                        final CidsAlkisSearchStatement search = new CidsAlkisSearchStatement(
                                CidsAlkisSearchStatement.Resulttyp.FLURSTUECK,
                                CidsAlkisSearchStatement.SucheUeber.FLURSTUECKSNUMMER,
                                (String)cidsBean.getProperty("landparcelcode"),
                                null);
                        final Collection<MetaObjectNode> mons = SessionManager.getProxy()
                                    .customServerSearch(SessionManager.getSession().getUser(), search);
                        if (!mons.isEmpty()) {
                            final MetaObjectNode mon = mons.iterator().next();
                            flurstueckMon = mon;
                            final MetaObject mo = SessionManager.getProxy()
                                        .getMetaObject(SessionManager.getSession().getUser(),
                                            mon.getObjectId(),
                                            mon.getClassId(),
                                            mon.getDomain());
                            return mo.getBean();
                        } else {
                            return null;
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            final CidsBean landparcelBean = get();
                            initMap(landparcelBean);
                        } catch (final Exception ex) {
                            LOG.warn(ex, ex);
                        }
                    }
                }.execute();
        }

        bindingGroup.bind();
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public JComponent getTitleComponent() {
        return panTitle;
    }

    @Override
    public JComponent getFooterComponent() {
        return panFooter;
    }
}