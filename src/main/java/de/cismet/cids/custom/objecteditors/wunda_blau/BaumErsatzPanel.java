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
import com.vividsolutions.jts.geom.Point;
import de.cismet.cids.client.tools.DevelopmentTools;
import de.cismet.cids.custom.objecteditors.utils.BaumConfProperties;
import de.cismet.cids.custom.objecteditors.utils.TableUtils;
import de.cismet.cids.custom.objectrenderer.utils.CidsBeanSupport;
import de.cismet.cids.custom.objectrenderer.utils.DefaultPreviewMapPanel;
import de.cismet.cids.custom.objectrenderer.utils.DivBeanTable;
import de.cismet.cids.custom.wunda_blau.search.server.AdresseLightweightSearch;
import de.cismet.cids.custom.wunda_blau.search.server.BaumArtLightweightSearch;
import org.apache.log4j.Logger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.CidsBeanStore;
import de.cismet.cids.dynamics.Disposable;
import de.cismet.cids.editors.DefaultBindableDateChooser;
import de.cismet.cids.editors.DefaultBindableReferenceCombo;
import de.cismet.cids.editors.DefaultBindableScrollableComboBox;
import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.FastBindableReferenceCombo;
import de.cismet.cids.navigator.utils.ClassCacheMultiple;
import de.cismet.cismap.cids.geometryeditor.DefaultCismapGeometryComboBoxEditor;
import de.cismet.cismap.commons.BoundingBox;
import de.cismet.cismap.commons.CrsTransformer;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.attributetable.DateCellEditor;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.ComboPopup;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingx.JXTable;
import org.openide.awt.Mnemonics;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * DOCUMENT ME!
 *
 * @author   sandra
 * @version  $Revision$, $Date$
 */
public class BaumErsatzPanel extends javax.swing.JPanel implements Disposable, CidsBeanStore, ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------
    private List<CidsBean> kontrolleBeans = new ArrayList<>();;
    private static final Logger LOG = Logger.getLogger(BaumErsatzPanel.class);
    private static final DefaultComboBoxModel NO_SELECTION_MODEL = new DefaultComboBoxModel(new Object[] {});
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
    
    private static final ComboBoxModel MUST_SET_MODEL_CB = new DefaultComboBoxModel(new String[] { "Die Daten bitte zuweisen......"});
    private static final ComboBoxModel LOAD_MODEL_CB = new DefaultComboBoxModel(new String[] { "Die Daten werden geladen......"});
    
    private final AdresseLightweightSearch hnrSearch = new AdresseLightweightSearch(
            AdresseLightweightSearch.Subject.HNR,
            ADRESSE_TOSTRING_TEMPLATE,
            ADRESSE_TOSTRING_FIELDS);
    
    public static final String FIELD__KONTROLLE = "n_kontrolle";                // baum_ersatz
    public static final String FIELD__DATE = "datum";                           // baum_kontrolle
    public static final String FIELD__STRASSE = "fk_strasse.strassenschluessel";// baum_ersatz
    public static final String FIELD__HNR = "fk_adresse";                       // baum_ersatz
    public static final String FIELD__DATUM = "pflanzdatum";                    // baum_ersatz
    public static final String FIELD__ART = "fk_art";                           // baum_ersatz 
    public static final String FIELD__ART_ID = "fk_art.id";                     // baum_ersatz --> art
    public static final String FIELD__GEOM = "fk_geom";                         // baum_ersatz
    public static final String FIELD__SELBST = "selbststaendig";                // baum_ersatz
    public static final String FIELD__BIS = "umsetzung_bis";                    // baum_ersatz
    public static final String FIELD__ANZAHL = "anzahl";                        // baum_ersatz
    public static final String FIELD__FIRMA = "firma";                          // baum_ersatz
    public static final String FIELD__BEMERKUNG = "bemerkung";                  // baum_ersatz
    public static final String FIELD__KONTROLLE_BEMERKUNG = "bemerkung";        // baum_ersatz
    public static final String FIELD__KONTROLLE_DATUM = "datum";                // baum_ersatz
    public static final String FIELD__GEOREFERENZ = "fk_geom";                  // baum_ersatz
    public static final String FIELD__STRASSE_NAME = "name";                    // strasse
    public static final String FIELD__STRASSE_KEY = "strassenschluessel";       // strasse
    
    public static final String FIELD__GEO_FIELD = "geo_field";                  // geom
    public static final String FIELD__GEOREFERENZ__GEO_FIELD = "fk_geom.geo_field"; // baum_ersatz_geom
    
    public static final String TABLE__NAME = "baum_ersatz";
    public static final String TABLE_GEOM = "geom";
    public static final String TABLE_SORTE = "baum_sorte";
    public static final String TABLE_NAME__KONTROLLE = "baum_kontrolle";
    
    private static final String[] KONTROLLE_COL_NAMES = new String[] { "Datum", "Bemerkung"};
    private static final String[] KONTROLLE_PROP_NAMES = new String[] {
            FIELD__KONTROLLE_DATUM,
            FIELD__KONTROLLE_BEMERKUNG
        };
    private static final Class[] KONTROLLE_PROP_TYPES = new Class[] {
            Date.class,
            String.class
        };
     private static final String[] STRASSE_COL_NAMES = new String[] { "Name", "Strassenschluessel"};
    private static final String[] STRASSE_PROP_NAMES = new String[] {
            FIELD__STRASSE_NAME,
            FIELD__STRASSE_KEY
        };
    private static final Class[] STRASSE_PROP_TYPES = new Class[] {
            String.class,
            Integer.class
        };
    
    public static final String ADRESSE_TOSTRING_TEMPLATE = "%s";
    public static final String[] ADRESSE_TOSTRING_FIELDS = {
            AdresseLightweightSearch.Subject.HNR.toString()
        };
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        bindingGroup = new BindingGroup();

        panDaten = new JPanel();
        panErsatz = new JPanel();
        panInhalt = new JPanel();
        lblBis = new JLabel();
        dcBis = new DefaultBindableDateChooser();
        lblAnzahl = new JLabel();
        txtAnzahl = new JTextField();
        lblDatum = new JLabel();
        dcDatum = new DefaultBindableDateChooser();
        lblSelbst = new JLabel();
        chSelbst = new JCheckBox();
        lblArt = new JLabel();
        cbArtE = new DefaultBindableScrollableComboBox(MC__ART);
        //new de.cismet.cids.editors.DefaultBindableReferenceCombo(MC__ART);
        ;
        lblSorte = new JLabel();
        cbSorte = new FastBindableReferenceCombo(
            sorteArtSearch,
            sorteArtSearch.getRepresentationPattern(),
            sorteArtSearch.getRepresentationFields()
        );
        lblFirma = new JLabel();
        txtFirma = new JTextField();
        lblBemerkung = new JLabel();
        scpBemerkung = new JScrollPane();
        taBemerkungE = new JTextArea();
        lblGeom = new JLabel();
        if (isEditor){
            cbGeomErsatz = new DefaultCismapGeometryComboBoxEditor();
        }
        lblStrasse = new JLabel();
        cbStrasse = new FastBindableReferenceCombo();
        lblHnr = new JLabel();
        cbHNr = new FastBindableReferenceCombo(
            hnrSearch,
            hnrSearch.getRepresentationPattern(),
            hnrSearch.getRepresentationFields()
        );
        panGeometrie = new JPanel();
        panLage = new JPanel();
        rpKarte = new RoundedPanel();
        panPreviewMap = new DefaultPreviewMapPanel();
        semiRoundedPanel7 = new SemiRoundedPanel();
        lblKarte = new JLabel();
        panKont = new JPanel();
        rpKont = new RoundedPanel();
        semiRoundedPanel8 = new SemiRoundedPanel();
        lblKont = new JLabel();
        panKontAdd = new JPanel();
        btnAddKont = new JButton();
        btnRemKont = new JButton();
        filler2 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        panKontDaten = new JPanel();
        jScrollPaneKont = new JScrollPane();
        xtKont = new JXTable();

        FormListener formListener = new FormListener();

        setName("Form"); // NOI18N
        setOpaque(false);
        setLayout(new GridBagLayout());

        panDaten.setName("panDaten"); // NOI18N
        panDaten.setOpaque(false);
        panDaten.setLayout(new GridBagLayout());

        panErsatz.setName("panErsatz"); // NOI18N
        panErsatz.setOpaque(false);
        panErsatz.setLayout(new GridBagLayout());

        panInhalt.setMinimumSize(new Dimension(100, 10));
        panInhalt.setName("panInhalt"); // NOI18N
        panInhalt.setOpaque(false);
        panInhalt.setPreferredSize(new Dimension(520, 270));
        panInhalt.setLayout(new GridBagLayout());

        lblBis.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblBis, "Umsetzung bis:");
        lblBis.setName("lblBis"); // NOI18N
        lblBis.setRequestFocusEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblBis, gridBagConstraints);

        dcBis.setName("dcBis"); // NOI18N

        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.umsetzung_bis}"), dcBis, BeanProperty.create("date"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        binding.setConverter(dcBis.getConverter());
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(dcBis, gridBagConstraints);

        lblAnzahl.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblAnzahl, "Anzahl:");
        lblAnzahl.setName("lblAnzahl"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        panInhalt.add(lblAnzahl, gridBagConstraints);

        txtAnzahl.setMinimumSize(new Dimension(40, 19));
        txtAnzahl.setName("txtAnzahl"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.anzahl}"), txtAnzahl, BeanProperty.create("text"));
        binding.setSourceNullValue("");
        binding.setSourceUnreadableValue("");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(txtAnzahl, gridBagConstraints);

        lblDatum.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblDatum, "Pflanzdatum:");
        lblDatum.setName("lblDatum"); // NOI18N
        lblDatum.setRequestFocusEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblDatum, gridBagConstraints);

        dcDatum.setName("dcDatum"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.pflanzdatum}"), dcDatum, BeanProperty.create("date"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        binding.setConverter(dcDatum.getConverter());
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(dcDatum, gridBagConstraints);

        lblSelbst.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblSelbst, "selbstständig:");
        lblSelbst.setName("lblSelbst"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        panInhalt.add(lblSelbst, gridBagConstraints);

        chSelbst.setContentAreaFilled(false);
        chSelbst.setName("chSelbst"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.selbststaendig}"), chSelbst, BeanProperty.create("selected"));
        binding.setSourceNullValue(false);
        binding.setSourceUnreadableValue(false);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(chSelbst, gridBagConstraints);

        lblArt.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblArt, "Art:");
        lblArt.setName("lblArt"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblArt, gridBagConstraints);

        cbArtE.setFont(new Font("Dialog", 0, 12)); // NOI18N
        cbArtE.setMaximumRowCount(15);
        cbArtE.setName("cbArtE"); // NOI18N
        cbArtE.setPreferredSize(new Dimension(100, 24));

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_art}"), cbArtE, BeanProperty.create("selectedItem"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);

        cbArtE.addActionListener(formListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(cbArtE, gridBagConstraints);

        lblSorte.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblSorte, "Sorte:");
        lblSorte.setName("lblSorte"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblSorte, gridBagConstraints);

        cbSorte.setMaximumRowCount(12);
        cbSorte.setEnabled(false);
        cbSorte.setName("cbSorte"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_sorte}"), cbSorte, BeanProperty.create("selectedItem"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(cbSorte, gridBagConstraints);

        lblFirma.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblFirma, "Firma:");
        lblFirma.setName("lblFirma"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblFirma, gridBagConstraints);

        txtFirma.setName("txtFirma"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.firma}"), txtFirma, BeanProperty.create("text"));
        binding.setSourceNullValue("");
        binding.setSourceUnreadableValue("");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(txtFirma, gridBagConstraints);

        lblBemerkung.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblBemerkung, "Bemerkung:");
        lblBemerkung.setName("lblBemerkung"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblBemerkung, gridBagConstraints);

        scpBemerkung.setName("scpBemerkung"); // NOI18N
        scpBemerkung.setOpaque(false);

        taBemerkungE.setLineWrap(true);
        taBemerkungE.setRows(2);
        taBemerkungE.setWrapStyleWord(true);
        taBemerkungE.setName("taBemerkungE"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.bemerkung}"), taBemerkungE, BeanProperty.create("text"));
        binding.setSourceNullValue("");
        binding.setSourceUnreadableValue("");
        bindingGroup.addBinding(binding);

        scpBemerkung.setViewportView(taBemerkungE);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 0, 2);
        panInhalt.add(scpBemerkung, gridBagConstraints);

        lblGeom.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblGeom, "Geometrie:");
        lblGeom.setName("lblGeom"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblGeom, gridBagConstraints);

        if (isEditor){
            cbGeomErsatz.setFont(new Font("Dialog", 0, 12)); // NOI18N
            cbGeomErsatz.setName("cbGeomErsatz"); // NOI18N

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_geom}"), cbGeomErsatz, BeanProperty.create("selectedItem"));
            binding.setSourceNullValue(null);
            binding.setSourceUnreadableValue(null);
            binding.setConverter(((DefaultCismapGeometryComboBoxEditor)cbGeomErsatz).getConverter());
            bindingGroup.addBinding(binding);

        }
        if (isEditor){
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            panInhalt.add(cbGeomErsatz, gridBagConstraints);
        }

        lblStrasse.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblStrasse, NbBundle.getMessage(BaumErsatzPanel.class, "BaumErsatzPanel.lblStrasse.text")); // NOI18N
        lblStrasse.setName("lblStrasse"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblStrasse, gridBagConstraints);

        cbStrasse.setMaximumRowCount(20);
        cbStrasse.setModel(new LoadModelCb());
        cbStrasse.setName("cbStrasse"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_strasse}"), cbStrasse, BeanProperty.create("selectedItem"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);

        cbStrasse.addActionListener(formListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(cbStrasse, gridBagConstraints);

        lblHnr.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        Mnemonics.setLocalizedText(lblHnr, NbBundle.getMessage(BaumErsatzPanel.class, "BaumErsatzPanel.lblHnr.text")); // NOI18N
        lblHnr.setName("lblHnr"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panInhalt.add(lblHnr, gridBagConstraints);

        cbHNr.setMaximumRowCount(20);
        cbHNr.setEnabled(false);
        cbHNr.setName("cbHNr"); // NOI18N

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.fk_adresse}"), cbHNr, BeanProperty.create("selectedItem"));
        binding.setSourceNullValue(null);
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panInhalt.add(cbHNr, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        panErsatz.add(panInhalt, gridBagConstraints);

        panGeometrie.setName("panGeometrie"); // NOI18N
        panGeometrie.setOpaque(false);
        panGeometrie.setLayout(new GridBagLayout());

        panLage.setMinimumSize(new Dimension(300, 142));
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
        Mnemonics.setLocalizedText(lblKarte, "Lage");
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 10, 0, 0);
        panErsatz.add(panGeometrie, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        panDaten.add(panErsatz, gridBagConstraints);

        panKont.setMaximumSize(new Dimension(410, 32767));
        panKont.setMinimumSize(new Dimension(410, 25));
        panKont.setName("panKont"); // NOI18N
        panKont.setOpaque(false);
        panKont.setPreferredSize(new Dimension(410, 25));
        panKont.setLayout(new GridBagLayout());

        rpKont.setName("rpKont"); // NOI18N
        rpKont.setLayout(new GridBagLayout());

        semiRoundedPanel8.setBackground(Color.darkGray);
        semiRoundedPanel8.setName("semiRoundedPanel8"); // NOI18N
        semiRoundedPanel8.setLayout(new GridBagLayout());

        lblKont.setForeground(new Color(255, 255, 255));
        lblKont.setText("Kontrollen");
        lblKont.setName("lblKont"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 8, 2, 2);
        semiRoundedPanel8.add(lblKont, gridBagConstraints);

        panKontAdd.setAlignmentX(0.0F);
        panKontAdd.setAlignmentY(1.0F);
        panKontAdd.setFocusable(false);
        panKontAdd.setName("panKontAdd"); // NOI18N
        panKontAdd.setOpaque(false);
        panKontAdd.setLayout(new GridBagLayout());

        btnAddKont.setBackground(new Color(0, 0, 0));
        btnAddKont.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        btnAddKont.setBorder(null);
        btnAddKont.setName("btnAddKont"); // NOI18N
        btnAddKont.setOpaque(false);
        btnAddKont.setPreferredSize(new Dimension(45, 13));
        btnAddKont.addActionListener(formListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panKontAdd.add(btnAddKont, gridBagConstraints);

        btnRemKont.setBackground(new Color(0, 0, 0));
        btnRemKont.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_remove_mini.png"))); // NOI18N
        btnRemKont.setBorder(null);
        btnRemKont.setName("btnRemKont"); // NOI18N
        btnRemKont.setOpaque(false);
        btnRemKont.setPreferredSize(new Dimension(45, 13));
        btnRemKont.addActionListener(formListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panKontAdd.add(btnRemKont, gridBagConstraints);

        filler2.setName("filler2"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 0, 0, 10);
        panKontAdd.add(filler2, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.insets = new Insets(5, 0, 5, 15);
        semiRoundedPanel8.add(panKontAdd, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        rpKont.add(semiRoundedPanel8, gridBagConstraints);

        panKontDaten.setName("panKontDaten"); // NOI18N
        panKontDaten.setLayout(new GridBagLayout());

        jScrollPaneKont.setName("jScrollPaneKont"); // NOI18N

        xtKont.setName("xtKont"); // NOI18N
        xtKont.setVisibleRowCount(7);
        jScrollPaneKont.setViewportView(xtKont);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panKontDaten.add(jScrollPaneKont, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rpKont.add(panKontDaten, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 0, 0, 0);
        panKont.add(rpKont, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        panDaten.add(panKont, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panDaten, gridBagConstraints);

        bindingGroup.bind();
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements ActionListener {
        FormListener() {}
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == cbArtE) {
                BaumErsatzPanel.this.cbArtEActionPerformed(evt);
            }
            else if (evt.getSource() == cbStrasse) {
                BaumErsatzPanel.this.cbStrasseActionPerformed(evt);
            }
            else if (evt.getSource() == btnAddKont) {
                BaumErsatzPanel.this.btnAddKontActionPerformed(evt);
            }
            else if (evt.getSource() == btnRemKont) {
                BaumErsatzPanel.this.btnRemKontActionPerformed(evt);
            }
        }
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddKontActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAddKontActionPerformed
        TableUtils.addObjectToTable(xtKont, TABLE_NAME__KONTROLLE, getConnectionContext());
    }//GEN-LAST:event_btnAddKontActionPerformed

    private void btnRemKontActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRemKontActionPerformed
        TableUtils.removeObjectsFromTable(xtKont);
    }//GEN-LAST:event_btnRemKontActionPerformed

    private void cbArtEActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cbArtEActionPerformed
        if (cidsBean != null && this.cidsBean.getProperty(FIELD__ART) != null){
            cbSorte.setSelectedItem(null);
            cbSorte.setEnabled(true);
            refreshSorte();
        }
    }//GEN-LAST:event_cbArtEActionPerformed

    private void cbStrasseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStrasseActionPerformed
        if (cidsBean != null && this.cidsBean.getProperty(FIELD__STRASSE) != null){
            cbHNr.setSelectedItem(null);
            cbHNr.setEnabled(true);
            refreshHnr();
        }
    }//GEN-LAST:event_cbStrasseActionPerformed

    //~ Instance fields --------------------------------------------------------
    private final boolean isEditor;
    private final BaumSchadenPanel parentPanel;
    private final ConnectionContext connectionContext;
    private CidsBean cidsBean;
    private final PropertyChangeListener changeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (!(Objects.equals(evt.getOldValue(), evt.getNewValue()))){
                    if ((parentPanel != null) && (parentPanel.parentPanel != null) && (parentPanel.getCidsBean() != null)) {
                        //parentPanel.getCidsBean().setArtificialChangeFlag(true);
                        //LOG.warn(evt.getPropertyName());
                        parentPanel.parentPanel.parentEditor.getCidsBean().setArtificialChangeFlag(true); 
                        parentPanel.getCidsBean().setArtificialChangeFlag(true);
                        parentPanel.setChangedErsatzBeans(cidsBean);
                    }
                    if ((parentPanel != null) && (parentPanel.parentEditor != null) && (parentPanel.getCidsBean() != null)){
                        parentPanel.parentEditor.getCidsBean().setArtificialChangeFlag(true);
                        parentPanel.getCidsBean().setArtificialChangeFlag(true);
                        parentPanel.setChangedErsatzBeans(cidsBean);
                    }
                    if (FIELD__GEOM.equals(evt.getPropertyName())){
                        setMapWindow();
                    }
                }
            }
        };
    private final BaumArtLightweightSearch sorteArtSearch;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    JButton btnAddKont;
    JButton btnRemKont;
    JComboBox<String> cbArtE;
    JComboBox cbGeomErsatz;
    FastBindableReferenceCombo cbHNr;
    FastBindableReferenceCombo cbSorte;
    FastBindableReferenceCombo cbStrasse;
    JCheckBox chSelbst;
    DefaultBindableDateChooser dcBis;
    DefaultBindableDateChooser dcDatum;
    Box.Filler filler2;
    JScrollPane jScrollPaneKont;
    JLabel lblAnzahl;
    JLabel lblArt;
    JLabel lblBemerkung;
    JLabel lblBis;
    JLabel lblDatum;
    JLabel lblFirma;
    JLabel lblGeom;
    JLabel lblHnr;
    JLabel lblKarte;
    JLabel lblKont;
    JLabel lblSelbst;
    JLabel lblSorte;
    JLabel lblStrasse;
    JPanel panDaten;
    JPanel panErsatz;
    JPanel panGeometrie;
    JPanel panInhalt;
    JPanel panKont;
    JPanel panKontAdd;
    JPanel panKontDaten;
    JPanel panLage;
    DefaultPreviewMapPanel panPreviewMap;
    RoundedPanel rpKarte;
    RoundedPanel rpKont;
    JScrollPane scpBemerkung;
    SemiRoundedPanel semiRoundedPanel7;
    SemiRoundedPanel semiRoundedPanel8;
    JTextArea taBemerkungE;
    JTextField txtAnzahl;
    JTextField txtFirma;
    JXTable xtKont;
    private BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BaumErsatzPanel object.
     */
    public BaumErsatzPanel() {
        this(null,false,ConnectionContext.createDeprecated());
    }

    
    /**
     * Creates new form BaumErsatzPanel.
     *
     * @param parentPanel
     * @param  editable  DOCUMENT ME!
     */
  /*  public BaumErsatzPanel(final BaumSchadenPanel parentPanel, final boolean editable) {
        this.isEditor = editable;
        this.sorteArtSearch = new BaumArtLightweightSearch(
                "%1$2s",
                new String[] { "NAME" });
        //cbSorte.setMetaClassFromTableName("WUNDA_BLAU", TABLE_SORTE);
        initComponents();
        if (isEditor) {
            ((DefaultCismapGeometryComboBoxEditor)cbGeomErsatz).setLocalRenderFeatureString(FIELD__GEOREFERENZ);
        }
        this.connectionContext = null;
        this.parentPanel = parentPanel;
    }*/
 
    /**
     * Creates new form BaumErsatzPanel.
     *
     * @param parentPanel
     * @param  editable             DOCUMENT ME!
     * @param  connectionContext    DOCUMENT ME!
     */
    public BaumErsatzPanel(final BaumSchadenPanel parentPanel, final boolean editable,
            final ConnectionContext connectionContext) {
        this.isEditor = editable;
        this.connectionContext = connectionContext;
        this.sorteArtSearch = new BaumArtLightweightSearch(
                "%1$2s",
                new String[] { "NAME" });
        //cbSorte.setMetaClassFromTableName("WUNDA_BLAU", TABLE_SORTE);
        initComponents();
        if (isEditor) {
            ((DefaultCismapGeometryComboBoxEditor)cbGeomErsatz).setLocalRenderFeatureString(FIELD__GEOREFERENZ);
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
            "baum_ersatz",
            1,
            800,
            600);
    }   
    
    private void refreshSorte() {
        if (cidsBean != null){
            sorteArtSearch.setArtId((Integer)cidsBean.getProperty(FIELD__ART_ID));

            new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        cbSorte.refreshModel();
                        return null;
                    }
            }.execute();
        }
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
            ((DefaultCismapGeometryComboBoxEditor)cbGeomErsatz).dispose();
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
          //  if (bindingGroup.getBinding(TABLE__NAME).isBound()){
                bindingGroup.unbind();
           // }
                this.cidsBean = cidsBean;
                if (this.cidsBean != null){
                    setKontrolleBeans(cidsBean.getBeanCollectionProperty(FIELD__KONTROLLE)); 
                    DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                        bindingGroup,
                        this.cidsBean,
                        getConnectionContext());
                } else{
                    setKontrolleBeans(null);
                    cbSorte.setEnabled(false);
                }

                setMapWindow();
                bindingGroup.bind();
                final DivBeanTable kontrolleModel = new DivBeanTable(
                            isEditor,
                            cidsBean,
                            FIELD__KONTROLLE,
                            KONTROLLE_COL_NAMES,
                            KONTROLLE_PROP_NAMES,                   
                            KONTROLLE_PROP_TYPES);
                    xtKont.setModel(kontrolleModel);
                    xtKont.getColumn(0).setCellEditor(new DateCellEditor());
                    //xtKont.getColumnModel().getColumn(0).setCellRenderer(xtKont.getDefaultRenderer(String.class));
                    xtKont.getColumn(0).setMaxWidth(80);
                    xtKont.getColumn(0).setMinWidth(80);
                    //Integer bemWidth = panGeometrie.getSize().width - panErsatz.getSize().width - 30;
                    //xtKont.getColumn(1).setMaxWidth(340);
                    //xtKont.getColumn(1).setPreferredWidth(300);
                    xtKont.packAll();
                    xtKont.addMouseMotionListener(new MouseAdapter(){
                        @Override
                        public void mouseMoved(MouseEvent e) {
                            int row=xtKont.rowAtPoint(e.getPoint());
                            int col=xtKont.columnAtPoint(e.getPoint());
                            if(row>-1 && col>-1){
                                Object value=xtKont.getValueAt(row, col);
                                if(null!=value && !"".equals(value)){
                                    xtKont.setToolTipText(value.toString());
                                }else{
                                    xtKont.setToolTipText(null);//keinTooltip anzeigen
                                }
                            }
                        }
                    });
                cbGeomErsatz.updateUI();
                cbSorte.updateUI();
                if (isEditor && (this.cidsBean != null)) {
                    cidsBean.addPropertyChangeListener(changeListener);
                    if(this.cidsBean.getProperty(FIELD__ART) != null){
                        cbSorte.setEnabled(true);
                    }
                    }
                if(isEditor){
                    StaticSwingTools.decorateWithFixedAutoCompleteDecorator(cbHNr);
                    {
                        final JList pop = ((ComboPopup)cbHNr.getUI().getAccessibleChild(cbHNr, 0))
                                    .getList();
                        final JTextField txt = (JTextField)cbHNr.getEditor().getEditorComponent();
                        cbHNr.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(final ActionEvent e) {
                                    final Object selectedValue = pop.getSelectedValue();
                                    txt.setText((selectedValue != null) ? String.valueOf(selectedValue) : "");
                                }
                            });
                    }
                }
                initComboboxHnr();
                if(this.cidsBean != null && this.cidsBean.getProperty(FIELD__STRASSE) != null){
                    cbHNr.setEnabled(true);
                    searchStreets();
                }
            } catch (final Exception ex) {
                Exceptions.printStackTrace(ex);
            }
         /*   if (cidsBean.getMetaObject().getStatus() != MetaObject.NEW){
                if (cidsBean.getProperty(FIELD__ART) != null){
                    cbSorte.setEnabled(true);
                }
            } else {
                cbSorte.setEnabled(false);
            }*/
        }
        
        
    }
    
    
    private Collection setStrasseCb(){
        final List<CidsBean> cblStrassen = this.getCidsBean().getBeanCollectionProperty(FIELD__STRASSE);
        final Collator umlautCollator = Collator.getInstance(Locale.GERMAN);
        umlautCollator.setStrength(Collator.SECONDARY);
        Collections.sort(cblStrassen, umlautCollator);
        return cblStrassen;
    }
    
    private void refreshHnr() { 
        if (cidsBean != null){
            String schluessel = cidsBean.getProperty(FIELD__STRASSE).toString();
            if (schluessel != null){

                hnrSearch.setKeyId(Integer.parseInt(schluessel.replaceFirst("0*","")));
                
                hnrSearch.setKeyId(Integer.parseInt(schluessel));

                new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws Exception {
                            cbHNr.refreshModel();

                            return null;
                        }
                    }.execute();
            }
        }
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param  cidsBeans  DOCUMENT ME!
     */
    public void setKontrolleBeans(final List<CidsBean> cidsBeans) {
        this.kontrolleBeans = cidsBeans;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<CidsBean> getKontrolleBeans() {
        return kontrolleBeans;
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
    
    private void initComboboxHnr() {
        new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    cbHNr.refreshModel();
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                    } catch (final InterruptedException | ExecutionException ex) {
                        LOG.error(ex, ex);
                    }  finally {
                        refreshHnr();
                    }
                }
            }.execute();
    }
    
    
    private void searchStreets() {
        if (getCidsBean() != null) {
            new SwingWorker<Collection, Void>() {

                    @Override
                    protected Collection doInBackground() throws Exception {
 /*
                        if (cidsBean == null || cidsBean.getProperty(FIELD__STRASSE) == null) {
                            cbStrasse.setModel(new MustSetModelCb());
                            return null;
                        }*/

                        return setStrasseCb();
                    }

                    @Override
                    protected void done() {
                        final Collection check;
                        try {
                            check = get();
                            if (check != null) {
                                final Collection colStreets = check;
                                cbStrasse.setModel(new DefaultComboBoxModel(colStreets.toArray()));
                                List<CidsBean> streetBeans = new ArrayList<>();
                                /*if (cidsBean.getProperty(FIELD__STRASSE) != null){
                                    for (final CidsBean cb : colStreets.toArray().){
                                        if(cb.getProperty(FIELD__STRASSE_KEY).toString().equals(cidsBean.getProperty(FIELD__STRASSE).toString())){
                                           streetBeans.add(cb);
                                           break;
                                        }
                                    }
                                }*/
                            }
                        } catch (final InterruptedException | ExecutionException ex) {
                            LOG.fatal(ex, ex);
                        }
                    }
                }.execute();
        }
    }
      
    class LoadModelCb extends DefaultComboBoxModel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LoadModelCb object.
         */
        public LoadModelCb() {
            super(new String[] { "Die Daten werden geladen......"});
        }
    } 
}