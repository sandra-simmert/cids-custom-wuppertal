/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.objectrenderer.wunda_blau;

import Sirius.navigator.connection.SessionManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.vividsolutions.jts.geom.Geometry;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import org.openide.util.Exceptions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import java.io.IOException;

import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.cismet.cids.custom.objectrenderer.converter.SQLTimestampToStringConverter;
import de.cismet.cids.custom.utils.ByteArrayActionDownload;
import de.cismet.cids.custom.utils.alkis.AlkisConstants;
import de.cismet.cids.custom.wunda_blau.search.actions.VermessungsUnterlagenPortalDownloadAction;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.converters.BooleanToStringConverter;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.features.DefaultStyledFeature;
import de.cismet.cismap.commons.features.StyledFeature;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.gui.printing.PrintingWidget;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWMS;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWmsGetMapUrl;

import de.cismet.tools.gui.TitleComponentProvider;
import de.cismet.tools.gui.downloadmanager.ByteArrayDownload;
import de.cismet.tools.gui.downloadmanager.Download;
import de.cismet.tools.gui.downloadmanager.DownloadManager;
import de.cismet.tools.gui.downloadmanager.DownloadManagerDialog;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public class VermessungsunterlagenauftragRenderer extends JPanel implements CidsBeanRenderer, TitleComponentProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            VermessungsunterlagenauftragRenderer.class);

    //~ Instance fields --------------------------------------------------------

    /** DOCUMENT ME! */
    private CidsBean cidsBean;
    private String title;

    private final MappingComponent mappingComponent;
    private StyledFeature geometrieFeature;
    private StyledFeature geometrieSaumFeature;
    private StyledFeature flurstueckeFeature;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private org.jdesktop.swingx.JXHyperlink jXHyperlink1;
    private javax.swing.JLabel lblEingangsdatum;
    private javax.swing.JLabel lblEingangsdatum1;
    private javax.swing.JLabel lblErstellungsdatumZip;
    private javax.swing.JLabel lblErstellungsdatumZip1;
    private javax.swing.JLabel lblGeschBuchNummer;
    private javax.swing.JLabel lblGeschBuchNummer1;
    private javax.swing.JLabel lblKatasterId;
    private javax.swing.JLabel lblKatasterId1;
    private javax.swing.JLabel lblMitGrenzniederschriften;
    private javax.swing.JLabel lblMitGrenzniederschriften1;
    private javax.swing.JLabel lblMitPunktnummernreservierung;
    private javax.swing.JLabel lblMitPunktnummernreservierung1;
    private javax.swing.JLabel lblSaumAP;
    private javax.swing.JLabel lblSaumAP1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVermStelle;
    private javax.swing.JLabel lblVermStelle1;
    private javax.swing.JLabel lblVermessungsarten;
    private javax.swing.JPanel panContent;
    private javax.swing.JPanel panContent1;
    private javax.swing.JPanel panContent2;
    private javax.swing.JPanel panDetails4;
    private javax.swing.JPanel panTitle;
    private javax.swing.JPanel panTitleString;
    private javax.swing.JPanel pnlMap;
    private de.cismet.tools.gui.RoundedPanel roundedPanel2;
    private de.cismet.tools.gui.RoundedPanel roundedPanel3;
    private de.cismet.tools.gui.RoundedPanel roundedPanel4;
    private de.cismet.tools.gui.RoundedPanel roundedPanel7;
    private de.cismet.tools.gui.SemiRoundedPanel semiRoundedPanel3;
    private de.cismet.tools.gui.SemiRoundedPanel semiRoundedPanel4;
    private de.cismet.tools.gui.SemiRoundedPanel semiRoundedPanel5;
    private de.cismet.tools.gui.SemiRoundedPanel semiRoundedPanel8;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Sb_stadtbildserieEditor object.
     */
    public VermessungsunterlagenauftragRenderer() {
        initComponents();

        title = "";
        mappingComponent = new MappingComponent();
        pnlMap.setLayout(new BorderLayout());
        pnlMap.add(mappingComponent, BorderLayout.CENTER);
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

        panTitle = new javax.swing.JPanel();
        panTitleString = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        roundedPanel2 = new de.cismet.tools.gui.RoundedPanel();
        semiRoundedPanel3 = new de.cismet.tools.gui.SemiRoundedPanel();
        jLabel1 = new javax.swing.JLabel();
        panContent = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        lblGeschBuchNummer = new javax.swing.JLabel();
        lblVermStelle = new javax.swing.JLabel();
        lblKatasterId = new javax.swing.JLabel();
        lblEingangsdatum = new javax.swing.JLabel();
        lblErstellungsdatumZip = new javax.swing.JLabel();
        lblMitGrenzniederschriften = new javax.swing.JLabel();
        lblMitPunktnummernreservierung = new javax.swing.JLabel();
        lblSaumAP = new javax.swing.JLabel();
        lblVermessungsarten = new javax.swing.JLabel();
        jXHyperlink1 = new org.jdesktop.swingx.JXHyperlink();
        lblGeschBuchNummer1 = new javax.swing.JLabel();
        lblVermStelle1 = new javax.swing.JLabel();
        lblKatasterId1 = new javax.swing.JLabel();
        lblEingangsdatum1 = new javax.swing.JLabel();
        lblErstellungsdatumZip1 = new javax.swing.JLabel();
        lblMitGrenzniederschriften1 = new javax.swing.JLabel();
        lblMitPunktnummernreservierung1 = new javax.swing.JLabel();
        lblSaumAP1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<String>();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        roundedPanel3 = new de.cismet.tools.gui.RoundedPanel();
        semiRoundedPanel4 = new de.cismet.tools.gui.SemiRoundedPanel();
        jLabel2 = new javax.swing.JLabel();
        panContent1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<String>();
        roundedPanel4 = new de.cismet.tools.gui.RoundedPanel();
        semiRoundedPanel5 = new de.cismet.tools.gui.SemiRoundedPanel();
        jLabel3 = new javax.swing.JLabel();
        panContent2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<String>();
        jPanel2 = new javax.swing.JPanel();
        roundedPanel7 = new de.cismet.tools.gui.RoundedPanel();
        semiRoundedPanel8 = new de.cismet.tools.gui.SemiRoundedPanel();
        jLabel6 = new javax.swing.JLabel();
        panDetails4 = new javax.swing.JPanel();
        pnlMap = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();

        panTitle.setOpaque(false);
        panTitle.setLayout(new java.awt.BorderLayout());

        panTitleString.setOpaque(false);
        panTitleString.setLayout(new java.awt.GridBagLayout());

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.GridBagLayout());

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("TITLE");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(lblTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panTitleString.add(jPanel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panTitleString.add(filler1, gridBagConstraints);

        panTitle.add(panTitleString, java.awt.BorderLayout.CENTER);

        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        roundedPanel2.setLayout(new java.awt.GridBagLayout());

        semiRoundedPanel3.setBackground(new java.awt.Color(51, 51, 51));
        semiRoundedPanel3.setLayout(new java.awt.FlowLayout());

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Allgemeine Angaben zum Auftrag");
        semiRoundedPanel3.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        roundedPanel2.add(semiRoundedPanel3, gridBagConstraints);

        panContent.setOpaque(false);
        panContent.setLayout(new java.awt.GridBagLayout());

        lblStatus.setText("Status des Auftrags:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblStatus, gridBagConstraints);

        lblGeschBuchNummer.setText("Geschäftsbuchnummer:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblGeschBuchNummer, gridBagConstraints);

        lblVermStelle.setText("Vermessungsstelle:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblVermStelle, gridBagConstraints);

        lblKatasterId.setText("Katasteramts ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblKatasterId, gridBagConstraints);

        lblEingangsdatum.setText("Eingangsdatum:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblEingangsdatum, gridBagConstraints);

        lblErstellungsdatumZip.setText("Erstellungsdatum Zip:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblErstellungsdatumZip, gridBagConstraints);

        lblMitGrenzniederschriften.setText("Mit Grenzniederschriften:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblMitGrenzniederschriften, gridBagConstraints);

        lblMitPunktnummernreservierung.setText("Mit Punktnummernreservierung:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblMitPunktnummernreservierung, gridBagConstraints);

        lblSaumAP.setText("Saum AP-Suche:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblSaumAP, gridBagConstraints);

        lblVermessungsarten.setText("Vermessungsarten:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblVermessungsarten, gridBagConstraints);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.status != null}"),
                jXHyperlink1,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.status}"),
                jXHyperlink1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "status");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        binding.setConverter(new BooleanToStringConverter("OK - Download", "Fehler", "in Bearbeitung"));
        bindingGroup.addBinding(binding);

        jXHyperlink1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jXHyperlink1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(jXHyperlink1, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.geschaeftsbuchnummer}"),
                lblGeschBuchNummer1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "geschaeftsbuchnummer");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblGeschBuchNummer1, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.vermessungsstelle}"),
                lblVermStelle1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "vermessungsstelle");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblVermStelle1, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.katasteramtsid}"),
                lblKatasterId1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "katasteramtsid");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblKatasterId1, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.timestamp}"),
                lblEingangsdatum1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "timestamp");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        binding.setConverter(new SQLTimestampToStringConverter());
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblEingangsdatum1, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.zip_timestamp}"),
                lblErstellungsdatumZip1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "zip_timestamp");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        binding.setConverter(new SQLTimestampToStringConverter());
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblErstellungsdatumZip1, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.mit_grenzniederschriften}"),
                lblMitGrenzniederschriften1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "mit_grenzniederschriften");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        binding.setConverter(new BooleanToStringConverter("ja", "nein", "-"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblMitGrenzniederschriften1, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.nur_punktnummernreservierung}"),
                lblMitPunktnummernreservierung1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "nur_punktnummernreservierung");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        binding.setConverter(new BooleanToStringConverter("ja", "nein", "-"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblMitPunktnummernreservierung1, gridBagConstraints);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.saumap} m"),
                lblSaumAP1,
                org.jdesktop.beansbinding.BeanProperty.create("text"),
                "saumap");
        binding.setSourceNullValue("-");
        binding.setSourceUnreadableValue("-");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(lblSaumAP1, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 80));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 80));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create(
                "${cidsBean.vermessungsarten}");
        org.jdesktop.swingbinding.JListBinding jListBinding = org.jdesktop.swingbinding.SwingBindings
                    .createJListBinding(
                        org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                        this,
                        eLProperty,
                        jList1,
                        "vermessungsarten");
        bindingGroup.addBinding(jListBinding);

        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(jScrollPane1, gridBagConstraints);

        jPanel6.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        roundedPanel2.add(panContent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(roundedPanel2, gridBagConstraints);

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridBagLayout());

        roundedPanel3.setMaximumSize(new java.awt.Dimension(268, 165));
        roundedPanel3.setLayout(new java.awt.GridBagLayout());

        semiRoundedPanel4.setBackground(new java.awt.Color(51, 51, 51));
        semiRoundedPanel4.setLayout(new java.awt.FlowLayout());

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Antragsflurstücke");
        semiRoundedPanel4.add(jLabel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        roundedPanel3.add(semiRoundedPanel4, gridBagConstraints);

        panContent1.setOpaque(false);
        panContent1.setLayout(new java.awt.GridBagLayout());

        jPanel8.setMaximumSize(new java.awt.Dimension(258, 130));
        jPanel8.setMinimumSize(new java.awt.Dimension(258, 130));
        jPanel8.setOpaque(false);
        jPanel8.setPreferredSize(new java.awt.Dimension(258, 130));
        panContent1.add(jPanel8, new java.awt.GridBagConstraints());

        jScrollPane2.setMaximumSize(new java.awt.Dimension(258, 130));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(258, 130));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${cidsBean.flurstuecke}");
        jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                eLProperty,
                jList2);
        jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty.create(
                "${gemarkung}-${flur}-${flurstueck}"));
        bindingGroup.addBinding(jListBinding);

        jScrollPane2.setViewportView(jList2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent1.add(jScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        roundedPanel3.add(panContent1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(roundedPanel3, gridBagConstraints);

        roundedPanel4.setMaximumSize(new java.awt.Dimension(268, 165));
        roundedPanel4.setLayout(new java.awt.GridBagLayout());

        semiRoundedPanel5.setBackground(new java.awt.Color(51, 51, 51));
        semiRoundedPanel5.setLayout(new java.awt.FlowLayout());

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Punktnummernreservierungen");
        semiRoundedPanel5.add(jLabel3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        roundedPanel4.add(semiRoundedPanel5, gridBagConstraints);

        panContent2.setOpaque(false);
        panContent2.setLayout(new java.awt.GridBagLayout());

        jPanel9.setMaximumSize(new java.awt.Dimension(258, 130));
        jPanel9.setMinimumSize(new java.awt.Dimension(258, 130));
        jPanel9.setOpaque(false);
        jPanel9.setPreferredSize(new java.awt.Dimension(258, 130));
        panContent2.add(jPanel9, new java.awt.GridBagConstraints());

        jScrollPane3.setMaximumSize(new java.awt.Dimension(258, 130));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(258, 130));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${cidsBean.punktnummern}");
        jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                eLProperty,
                jList3);
        jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty.create(
                "${kilometerquadrat} - ${katasteramt} : ${anzahl}"));
        bindingGroup.addBinding(jListBinding);

        jScrollPane3.setViewportView(jList3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panContent2.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        roundedPanel4.add(panContent2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(roundedPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel1, gridBagConstraints);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        roundedPanel7.setLayout(new java.awt.GridBagLayout());

        semiRoundedPanel8.setBackground(new java.awt.Color(51, 51, 51));
        semiRoundedPanel8.setLayout(new java.awt.FlowLayout());

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Karte");
        semiRoundedPanel8.add(jLabel6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        roundedPanel7.add(semiRoundedPanel8, gridBagConstraints);

        panDetails4.setOpaque(false);
        panDetails4.setLayout(new java.awt.GridBagLayout());

        pnlMap.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panDetails4.add(pnlMap, gridBagConstraints);

        jPanel5.setOpaque(false);

        buttonGroup1.add(jToggleButton1);
        jToggleButton1.setText("Antragsflurstücke");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton1ActionPerformed(evt);
                }
            });
        jPanel5.add(jToggleButton1);

        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setText("Verm.-Gebiet ohne Saum");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton2ActionPerformed(evt);
                }
            });
        jPanel5.add(jToggleButton2);

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setText("Verm.-Gebiet mit Saum");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${cidsBean.saum > 0}"),
                jToggleButton3,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton3ActionPerformed(evt);
                }
            });
        jPanel5.add(jToggleButton3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panDetails4.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        roundedPanel7.add(panDetails4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(roundedPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel3, gridBagConstraints);

        bindingGroup.bind();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jToggleButton1ActionPerformed
        mappingComponent.getFeatureCollection().removeFeature(geometrieFeature);
        mappingComponent.getFeatureCollection().removeFeature(geometrieSaumFeature);
        mappingComponent.getFeatureCollection().addFeature(flurstueckeFeature);
    }                                                                                  //GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jToggleButton2ActionPerformed
        mappingComponent.getFeatureCollection().addFeature(geometrieFeature);
        mappingComponent.getFeatureCollection().removeFeature(geometrieSaumFeature);
        mappingComponent.getFeatureCollection().removeFeature(flurstueckeFeature);
    }                                                                                  //GEN-LAST:event_jToggleButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton3ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jToggleButton3ActionPerformed
        mappingComponent.getFeatureCollection().removeFeature(geometrieFeature);
        mappingComponent.getFeatureCollection().addFeature(geometrieSaumFeature);
        mappingComponent.getFeatureCollection().removeFeature(flurstueckeFeature);
    }                                                                                  //GEN-LAST:event_jToggleButton3ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jXHyperlink1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jXHyperlink1ActionPerformed
        if (Boolean.TRUE.equals(cidsBean.getProperty("status"))) {
            if (DownloadManagerDialog.getInstance().showAskingForUserTitleDialog(this)) {
                final String jobname = DownloadManagerDialog.getInstance().getJobName();
                final String schluessel = (String)cidsBean.getProperty("schluessel");
                final Download download = new ByteArrayActionDownload(
                        VermessungsUnterlagenPortalDownloadAction.TASK_NAME,
                        schluessel,
                        null,
                        "Vermessungsunterlagen",
                        jobname,
                        schluessel,
                        ".zip");
                DownloadManager.instance().add(download);
            }
        } else if (Boolean.FALSE.equals(cidsBean.getProperty("status"))) {
            final String exception_json = (String)cidsBean.getProperty("exception_json");
            Exception exception = null;
            boolean parseError = false;
            try {
                exception = new ObjectMapper().readValue(exception_json, Exception.class);
            } catch (final IOException ex) {
                exception = ex;
                parseError = true;
            }
            final ErrorInfo errorInfo = new ErrorInfo(
                    "Fehler",
                    parseError ? "Die Fehlermeldung konnte nicht geparst werden."
                               : (exception.getMessage() + "\n\nSiehe Details."),
                    null,
                    null,
                    exception,
                    Level.ALL,
                    null);
            JXErrorPane.showDialog(this, errorInfo);
        }
    }                                                                                //GEN-LAST:event_jXHyperlink1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBean  DOCUMENT ME!
     */
    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        bindingGroup.unbind();
        if (cidsBean != null) {
            this.cidsBean = cidsBean;
            DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                bindingGroup,
                this.cidsBean);
            initFeatures();
            initMap();

            bindingGroup.bind();
            setTitle((String)cidsBean.getProperty("schluessel"));
            buttonGroup1.setSelected(jToggleButton1.getModel(), true);
            jToggleButton1ActionPerformed(null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  title  DOCUMENT ME!
     */
    @Override
    public void setTitle(final String title) {
        if (title == null) {
            this.title = "<Error>";
        } else {
            this.title = "Bestellung Vermessungsunterlagen: " + title;
        }
        lblTitle.setText(this.title);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public JComponent getTitleComponent() {
        return panTitle;
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void dispose() {
        bindingGroup.unbind();
    }

    /**
     * DOCUMENT ME!
     */
    private void initFeatures() {
        if (cidsBean != null) {
            final Geometry geometrie = CrsTransformer.transformToGivenCrs((Geometry)cidsBean.getProperty(
                        "geometrie.geo_field"),
                    AlkisConstants.COMMONS.SRS_SERVICE);
            final Geometry geometrieSaum = CrsTransformer.transformToGivenCrs(
                    ((Geometry)cidsBean.getProperty("geometrie.geo_field")).buffer(
                        (Integer)cidsBean.getProperty("saumap")),
                    AlkisConstants.COMMONS.SRS_SERVICE);
            final Geometry geometrieFlurstuecke = CrsTransformer.transformToGivenCrs((Geometry)cidsBean.getProperty(
                        "geometrie_flurstuecke.geo_field"),
                    AlkisConstants.COMMONS.SRS_SERVICE);

            final StyledFeature geometrieFeature = new DefaultStyledFeature();
            geometrieFeature.setGeometry(geometrie);
            geometrieFeature.setFillingPaint(new Color(1, 0, 0, 0.5f));
            geometrieFeature.setLineWidth(1);
            geometrieFeature.setLinePaint(new Color(1, 0, 0, 1f));

            final StyledFeature geometrieSaumFeature = new DefaultStyledFeature();
            geometrieSaumFeature.setGeometry(geometrieSaum);
            geometrieSaumFeature.setFillingPaint(new Color(1, 0, 0, 0.5f));
            geometrieSaumFeature.setLineWidth(1);
            geometrieSaumFeature.setLinePaint(new Color(1, 0, 0, 1f));

            final StyledFeature flurstueckeFeature = new DefaultStyledFeature();
            flurstueckeFeature.setGeometry(geometrieFlurstuecke);
            flurstueckeFeature.setFillingPaint(new Color(1, 0, 0, 0.5f));
            flurstueckeFeature.setLineWidth(1);
            flurstueckeFeature.setLinePaint(new Color(1, 0, 0, 1f));
            this.geometrieFeature = geometrieFeature;
            this.geometrieSaumFeature = geometrieSaumFeature;
            this.flurstueckeFeature = flurstueckeFeature;
        } else {
            this.geometrieFeature = null;
            this.geometrieSaumFeature = null;
            this.flurstueckeFeature = null;
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void initMap() {
        if (cidsBean != null) {
            final Geometry combinedGeom = geometrieSaumFeature.getGeometry().union(flurstueckeFeature.getGeometry());

            final XBoundingBox box = new XBoundingBox(combinedGeom.getEnvelope().buffer(
                        AlkisConstants.COMMONS.GEO_BUFFER));
            final Runnable mapRunnable = new Runnable() {

                    @Override
                    public void run() {
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
                        swms.setName("Vermessungsunterlagen-Auftrag");

                        // add the raster layer to the model
                        mappingModel.addLayer(swms);
                        // set the model
                        mappingComponent.setMappingModel(mappingModel);
                        // initial positioning of the map
                        mappingComponent.gotoInitialBoundingBox();
                        // interaction mode
                        mappingComponent.setInteractionMode(MappingComponent.ZOOM);
                        // finally when all configurations are done ...
                        mappingComponent.unlock();
//                        previewMap.addCustomInputListener("MUTE", new PBasicInputEventHandler() {
//
//                                @Override
//                                public void mouseClicked(final PInputEvent evt) {
//                                    if (evt.getClickCount() > 1) {
//                                        final CidsBean bean = cidsBean;
//                                        ObjectRendererUtils.switchToCismapMap();
//                                        ObjectRendererUtils.addBeanGeomAsFeatureToCismapMap(bean, false);
//                                    }
//                                }
//                            });
//                        previewMap.setInteractionMode("MUTE");
                    }
                };
            if (EventQueue.isDispatchThread()) {
                mapRunnable.run();
            } else {
                EventQueue.invokeLater(mapRunnable);
            }
        }
    }
}