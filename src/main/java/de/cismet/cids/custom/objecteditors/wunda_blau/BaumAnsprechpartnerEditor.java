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
import Sirius.navigator.ui.RequestsFullSizeComponent;

import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;

import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.DecimalFormat;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import de.cismet.cids.custom.objecteditors.utils.RendererTools;
import de.cismet.cids.custom.objecteditors.utils.TableUtils;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.BindingGroupStore;
import de.cismet.cids.editors.DefaultCustomObjectEditor;
import de.cismet.cids.editors.EditorClosedEvent;
import de.cismet.cids.editors.EditorSaveListener;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.connectioncontext.ConnectionContext;

import de.cismet.tools.gui.RoundedPanel;
import de.cismet.tools.gui.StaticSwingTools;

import de.cismet.cids.custom.objecteditors.wunda_blau.BaumAnsprechpartnerEditor.TelefonCellEditor;
import de.cismet.cids.custom.objectrenderer.utils.DivBeanTable;
import de.cismet.cids.custom.wunda_blau.search.server.RedundantObjectSearch;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import org.jdesktop.swingx.JXTable;

/**
 * DOCUMENT ME!
 *
 * @author   sandra
 * @version  $Revision$, $Date$
 */
public class BaumAnsprechpartnerEditor extends DefaultCustomObjectEditor implements CidsBeanRenderer,
    EditorSaveListener,
    BindingGroupStore,
    PropertyChangeListener,
    RequestsFullSizeComponent {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(BaumAnsprechpartnerEditor.class);
    public static final String REDUNDANT_TOSTRING_TEMPLATE = "%s";
    public static final String[] REDUNDANT_TOSTRING_FIELDS = {"name", "id"};
    public static final String REDUNDANT_TABLE = "baum_ansprechpartner";

    public static final String TABLE_NAME = "baum_ansprechpartner";
    public static final String FIELD__NAME = "name";
    public static final String FIELD__SCHLUESSEL = "schluessel";
    public static final String FIELD__TELEFON = "telefon";
    public static final String FIELD__TELEFONE = "n_telefone";
    public static final String FIELD__ID = "id";
    public static final String TABLE_NAME_TELEFON = "baum_telefon";
    
    private static String TITLE_NEW_AP = "einen neuen Ansprechpartner anlegen..."; 

    public static final String BUNDLE_NONAME = "BaumAnsprechpartnerEditor.prepareForSave().noName";
    public static final String BUNDLE_DUPLICATENAME = "BaumAnsprechpartnerEditor.prepareForSave().duplicateName";
    public static final String BUNDLE_DUPLICATEKEY = "BaumAnsprechpartnerEditor.prepareForSave().duplicateSchluessel";
    public static final String BUNDLE_PANE_PREFIX =
        "BaumAnsprechpartnerEditor.prepareForSave().JOptionPane.message.prefix";
    public static final String BUNDLE_PANE_SUFFIX =
        "BaumAnsprechpartnerEditor.prepareForSave().JOptionPane.message.suffix";
    public static final String BUNDLE_PANE_TITLE = "BaumAnsprechpartnerEditor.prepareForSave().JOptionPane.title";

    public static final String BUNDLE_HP_TITLE = "BaumAnsprechpartnerEditor.xhHomepageActionPerformed.title";
    public static final String BUNDLE_HP_TEXT = "BaumAnsprechpartnerEditor.xhHomepageActionPerformed.text";

    public static final Pattern TEL_FILLING_PATTERN = Pattern.compile("(|\\+(-|[0-9])*)");
    public static final Pattern TEL_MATCHING_PATTERN = Pattern.compile("\\+[0-9]{1,3}(-[0-9]+){1,}");
    
    private static final String[] TELEFON_COL_NAMES = new String[] { "Telefon", "Bemerkung" };
    private static final String[] TELEFON_PROP_NAMES = new String[] {"nummer", "bemerkung"};
    private static final Class[] TELEFON_PROP_TYPES = new Class[] {
            String.class,
            String.class
        };

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static enum OtherTableCases {

        //~ Enum constants -----------------------------------------------------

        REDUNDANT_ATT_KEY, REDUNDANT_ATT_NAME
    }

    //~ Instance fields --------------------------------------------------------

    private SwingWorker worker_key;
    private SwingWorker worker_name;

    private Boolean redundantName = false;
    private Boolean redundantKey = false;
    
    private DivBeanTable telefonModel;

    private boolean isEditor = true;

    private final ImageIcon statusFalsch = new ImageIcon(
            getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/status-busy.png"));
    private final ImageIcon statusOk = new ImageIcon(
            getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/status.png"));

    private final RegexPatternFormatter telPatternFormatter = new RegexPatternFormatter(
            TEL_FILLING_PATTERN,
            TEL_MATCHING_PATTERN);
    
    private final RedundantObjectSearch apSearch = new RedundantObjectSearch(
            REDUNDANT_TOSTRING_TEMPLATE,
            REDUNDANT_TOSTRING_FIELDS,
            null,
            REDUNDANT_TABLE);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAddTelefon;
    private JButton btnRemTelefon;
    private Box.Filler filler1;
    private Box.Filler filler3;
    private JFormattedTextField ftxtPlz;
    private JScrollPane jScrollPaneTelefon;
    private JLabel lblBemerkung;
    private JLabel lblHnr;
    private JLabel lblMail;
    private JLabel lblName;
    private JLabel lblOrt;
    private JLabel lblPlz;
    private JLabel lblStrasse;
    private JPanel panContent;
    private JPanel panDaten;
    private JPanel panFillerUnten;
    private JPanel panFillerUnten1;
    private JPanel panTel;
    private JPanel panTelefon;
    private JPanel panTelefonAdd;
    private JScrollPane scpBemerkung;
    private JTextArea taBemerkung;
    private JTextField txtHnr;
    private JTextField txtMail;
    private JTextField txtName;
    private JTextField txtOrt;
    private JTextField txtStrasse;
    private JXTable xtTelefon;
    private BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form.
     */
    public BaumAnsprechpartnerEditor() {
    }

    /**
     * Creates a new BaumAnsprechpartnerEditor object.
     *
     * @param  boolEditor  DOCUMENT ME!
     */
    public BaumAnsprechpartnerEditor(final boolean boolEditor) {
        this.isEditor = boolEditor;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        super.initWithConnectionContext(connectionContext);
        initComponents();
        txtName.getDocument().addDocumentListener(new DocumentListener() {

                // Immer, wenn der Name geändert wird, wird dieser überprüft.
                @Override
                public void insertUpdate(final DocumentEvent e) {
                    checkAttributes();
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    checkAttributes();
                }

                @Override
                public void changedUpdate(final DocumentEvent e) {
                    checkAttributes();
                }
            });
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
        panDaten = new JPanel();
        lblName = new JLabel();
        txtName = new JTextField();
        lblStrasse = new JLabel();
        txtStrasse = new JTextField();
        filler3 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(32767, 0));
        lblHnr = new JLabel();
        txtHnr = new JTextField();
        lblPlz = new JLabel();
        ftxtPlz = new JFormattedTextField();
        lblOrt = new JLabel();
        txtOrt = new JTextField();
        panTelefon = new JPanel();
        panTel = new JPanel();
        jScrollPaneTelefon = new JScrollPane();
        xtTelefon = new JXTable();
        panTelefonAdd = new JPanel();
        btnAddTelefon = new JButton();
        btnRemTelefon = new JButton();
        filler1 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        lblMail = new JLabel();
        txtMail = new JTextField();
        lblBemerkung = new JLabel();
        scpBemerkung = new JScrollPane();
        taBemerkung = new JTextArea();
        panFillerUnten1 = new JPanel();
        panFillerUnten = new JPanel();

        setAutoscrolls(true);
        setMinimumSize(new Dimension(600, 646));
        setPreferredSize(new Dimension(600, 737));
        setLayout(new GridBagLayout());

        panContent.setAutoscrolls(true);
        panContent.setMaximumSize(new Dimension(450, 2147483647));
        panContent.setMinimumSize(new Dimension(450, 488));
        panContent.setName(""); // NOI18N
        panContent.setOpaque(false);
        panContent.setPreferredSize(new Dimension(450, 961));
        panContent.setLayout(new GridBagLayout());

        panDaten.setOpaque(false);
        panDaten.setLayout(new GridBagLayout());

        lblName.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblName.setText("Name:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblName, gridBagConstraints);

        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.name}"), txtName, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtName, gridBagConstraints);

        lblStrasse.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblStrasse.setText("Straße:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblStrasse, gridBagConstraints);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.strasse}"), txtStrasse, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtStrasse, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        panDaten.add(filler3, gridBagConstraints);

        lblHnr.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblHnr.setText("Hnr:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblHnr, gridBagConstraints);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.hausnummer}"), txtHnr, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.7;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtHnr, gridBagConstraints);

        lblPlz.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblPlz.setText("PLZ:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblPlz, gridBagConstraints);

        ftxtPlz.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#####"))));

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.plz}"), ftxtPlz, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(ftxtPlz, gridBagConstraints);

        lblOrt.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblOrt.setText("Ort:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblOrt, gridBagConstraints);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.ort}"), txtOrt, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.7;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtOrt, gridBagConstraints);

        panTelefon.setOpaque(false);
        panTelefon.setLayout(new GridBagLayout());

        panTel.setMinimumSize(new Dimension(26, 80));
        panTel.setOpaque(false);
        panTel.setLayout(new GridBagLayout());

        xtTelefon.setVisibleRowCount(6);
        jScrollPaneTelefon.setViewportView(xtTelefon);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panTel.add(jScrollPaneTelefon, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTelefon.add(panTel, gridBagConstraints);

        panTelefonAdd.setAlignmentX(0.0F);
        panTelefonAdd.setAlignmentY(1.0F);
        panTelefonAdd.setFocusable(false);
        panTelefonAdd.setOpaque(false);
        panTelefonAdd.setLayout(new GridBagLayout());

        btnAddTelefon.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        btnAddTelefon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddTelefonActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panTelefonAdd.add(btnAddTelefon, gridBagConstraints);

        btnRemTelefon.setIcon(new ImageIcon(getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_remove_mini.png"))); // NOI18N
        btnRemTelefon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRemTelefonActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 0, 2, 0);
        panTelefonAdd.add(btnRemTelefon, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panTelefonAdd.add(filler1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.insets = new Insets(2, 5, 2, 2);
        panTelefon.add(panTelefonAdd, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panDaten.add(panTelefon, gridBagConstraints);

        lblMail.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblMail.setText("Email:");
        lblMail.setToolTipText("");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblMail, gridBagConstraints);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.mail}"), txtMail, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.7;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(txtMail, gridBagConstraints);

        lblBemerkung.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        lblBemerkung.setText("Bemerkung:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 0, 2, 5);
        panDaten.add(lblBemerkung, gridBagConstraints);

        scpBemerkung.setOpaque(false);

        taBemerkung.setLineWrap(true);
        taBemerkung.setRows(2);
        taBemerkung.setWrapStyleWord(true);

        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${cidsBean.bemerkung}"), taBemerkung, BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        scpBemerkung.setViewportView(taBemerkung);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panDaten.add(scpBemerkung, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 0, 10);
        panContent.add(panDaten, gridBagConstraints);

        panFillerUnten1.setName(""); // NOI18N
        panFillerUnten1.setOpaque(false);

        GroupLayout panFillerUnten1Layout = new GroupLayout(panFillerUnten1);
        panFillerUnten1.setLayout(panFillerUnten1Layout);
        panFillerUnten1Layout.setHorizontalGroup(panFillerUnten1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panFillerUnten1Layout.setVerticalGroup(panFillerUnten1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        panContent.add(panFillerUnten1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        add(panContent, gridBagConstraints);

        panFillerUnten.setName(""); // NOI18N
        panFillerUnten.setOpaque(false);

        GroupLayout panFillerUntenLayout = new GroupLayout(panFillerUnten);
        panFillerUnten.setLayout(panFillerUntenLayout);
        panFillerUntenLayout.setHorizontalGroup(panFillerUntenLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panFillerUntenLayout.setVerticalGroup(panFillerUntenLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        add(panFillerUnten, gridBagConstraints);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddTelefonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAddTelefonActionPerformed
        TableUtils.addObjectToTable(xtTelefon, TABLE_NAME_TELEFON, getConnectionContext());
    }//GEN-LAST:event_btnAddTelefonActionPerformed

    private void btnRemTelefonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRemTelefonActionPerformed
        TableUtils.removeObjectsFromTable(xtTelefon);
    }//GEN-LAST:event_btnRemTelefonActionPerformed


    @Override
    public boolean prepareForSave() {
        boolean save = true;
        final StringBuilder errorMessage = new StringBuilder();

        // name vorhanden
        try {
            if (txtName.getText().trim().isEmpty()) {
                LOG.warn("No name specified. Skip persisting.");
                errorMessage.append(NbBundle.getMessage(BaumAnsprechpartnerEditor.class, BUNDLE_NONAME));
            } else {
                if (this.cidsBean.getMetaObject().getStatus() == MetaObject.NEW) {
                    cidsBean.setProperty(FIELD__SCHLUESSEL, txtName.getText().trim());
                }
                if (redundantName) {
                    LOG.warn("Duplicate name specified. Skip persisting.");
                    errorMessage.append(NbBundle.getMessage(BaumAnsprechpartnerEditor.class, BUNDLE_DUPLICATENAME));
                } else {
                    if (redundantKey) {
                        LOG.warn("Duplicate key specified. Skip persisting.");
                        errorMessage.append(NbBundle.getMessage(BaumAnsprechpartnerEditor.class, BUNDLE_DUPLICATEKEY));
                    } else {
                    }
                }
            }
        } catch (final Exception ex) {
            LOG.warn("Name not given.", ex);
            save = false;
        }

        if (errorMessage.length() > 0) {
            JOptionPane.showMessageDialog(StaticSwingTools.getParentFrame(this),
                NbBundle.getMessage(BaumAnsprechpartnerEditor.class,
                    BUNDLE_PANE_PREFIX)
                        + errorMessage.toString()
                        + NbBundle.getMessage(BaumAnsprechpartnerEditor.class,
                            BUNDLE_PANE_SUFFIX),
                NbBundle.getMessage(BaumAnsprechpartnerEditor.class, BUNDLE_PANE_TITLE),
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
            if (isEditor && (this.cidsBean != null)) {
                LOG.info("remove propchange baum_ansprechpartner: " + this.cidsBean);
                this.cidsBean.removePropertyChangeListener(this);
            }
            bindingGroup.unbind();
            this.cidsBean = cb;
            if (isEditor && (this.cidsBean != null)) {
                LOG.info("add propchange baum_ansprechpartner: " + this.cidsBean);
                this.cidsBean.addPropertyChangeListener(this);
            }

            // 8.5.17 s.Simmert: Methodenaufruf, weil sonst die Comboboxen nicht gefüllt werden
            // evtl. kann dies verbessert werden.
            DefaultCustomObjectEditor.setMetaClassInformationToMetaClassStoreComponentsInBindingGroup(
                bindingGroup,
                cb,
                getConnectionContext());
            bindingGroup.bind();
            //saveValidTel(String.valueOf(cidsBean.getProperty(FIELD__TELEFON)));
            telefonModel = new DivBeanTable(
                    isEditor,
                    cidsBean,
                    FIELD__TELEFONE,
                    TELEFON_COL_NAMES,
                    TELEFON_PROP_NAMES,
                    TELEFON_PROP_TYPES);
            xtTelefon.setModel(telefonModel);
        
            xtTelefon.getColumn(0).setCellEditor(new TelefonCellEditor());
        } catch (final Exception ex) {
            Exceptions.printStackTrace(ex);
            LOG.error("Bean not set.", ex);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setReadOnly() {
        if (!(isEditor)) {
            RendererTools.makeReadOnly(ftxtPlz);
            RendererTools.makeReadOnly(txtMail);
            RendererTools.makeReadOnly(txtOrt);
            RendererTools.makeReadOnly(txtHnr);
            RendererTools.makeReadOnly(txtStrasse);
            RendererTools.makeReadOnly(txtName);
            RendererTools.makeReadOnly(taBemerkung);
            RendererTools.makeReadOnly(xtTelefon);
        }
    }

    /**
     * DOCUMENT ME!
     */

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public String getTitle() {
        if (cidsBean.getMetaObject().getStatus() == MetaObject.NEW){
            return TITLE_NEW_AP;
        } else {
            return cidsBean.toString();
        }
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

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        // throw new UnsupportedOperationException("Not supported yet."); To change body of generated methods, choose
        // Tools | Templates.
    }

    /**
     * DOCUMENT ME!
     *
     * @param  field  DOCUMENT ME!
     * @param  fall   DOCUMENT ME!
     */
    private void checkName(final String field, final OtherTableCases fall) {
        // Worker Aufruf, ob das Objekt schon existiert
        final Collection<String> conditions = new ArrayList<>();
        conditions.add(field + " ilike '" + txtName.getText().trim() + "'");
        conditions.add(FIELD__ID + " <> " + cidsBean.getProperty(FIELD__ID));
        valueFromOtherTable(conditions, fall);
    }

    /**
     * DOCUMENT ME!
     */
    private void checkAttributes() {
        checkName(FIELD__NAME, OtherTableCases.REDUNDANT_ATT_NAME);
        checkName(FIELD__SCHLUESSEL, OtherTableCases.REDUNDANT_ATT_KEY);
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param  where        DOCUMENT ME!
     * @param  fall         DOCUMENT ME!
     */
    private void valueFromOtherTable(final Collection<String> where, final OtherTableCases fall) {
        final SwingWorker<Collection<MetaObjectNode>, Void> worker = new SwingWorker<Collection<MetaObjectNode>, Void>() {
            
                @Override
                protected Collection<MetaObjectNode> doInBackground() throws Exception {
                    apSearch.setWhere(where);
                    apSearch.setTable(REDUNDANT_TABLE);
                    return SessionManager.getProxy().customServerSearch(
                            SessionManager.getSession().getUser(),
                            apSearch,
                            getConnectionContext());
                } 

                @Override
                protected void done() {
                    final Collection<MetaObjectNode> check;
                    try {
                        if (!isCancelled()) {
                            check = get();
                            switch (fall) {
                                case REDUNDANT_ATT_KEY: {  // check redundant key
                                    redundantKey = !check.isEmpty();
                                    break;
                                }
                                case REDUNDANT_ATT_NAME: { // check redundant name
                                    redundantName = !check.isEmpty();
                                    break;
                                }
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        LOG.warn("problem in Worker: load values.", e);
                    }
                }
            };
        if (fall.equals(OtherTableCases.REDUNDANT_ATT_NAME)) {
            if (worker_name != null) {
                worker_name.cancel(true);
            }
            worker_name = worker;
            worker_name.execute();
        } else {
            if (worker_key != null) {
                worker_key.cancel(true);
            }
            worker_key = worker;
            worker_key.execute();
        }
    }
    public interface BaumAnsprechpartnerDescriptionPaneParent {
    }
    //~ Inner Classes ----------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */

    class RegexPatternFormatter extends DefaultFormatter {

        //~ Instance fields ----------------------------------------------------

        protected java.util.regex.Matcher fillingMatcher;
        protected java.util.regex.Matcher matchingMatcher;
        private Object lastValid = null;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new RegexPatternFormatter object.
         *
         * @param  fillingRegex   DOCUMENT ME!
         * @param  matchingRegex  DOCUMENT ME!
         */
        public RegexPatternFormatter(final Pattern fillingRegex, final Pattern matchingRegex) {
            setOverwriteMode(false);
            fillingMatcher = fillingRegex.matcher("");
            matchingMatcher = matchingRegex.matcher("");
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Object stringToValue(final String string) throws java.text.ParseException {
            if ((string == null) || string.isEmpty()) {
                lastValid = null;
                return null;
            }
            fillingMatcher.reset(string);

            if (!fillingMatcher.matches()) {
                throw new java.text.ParseException("does not match regex", 0);
            }

            final Object value = (String)super.stringToValue(string);

            matchingMatcher.reset(string);
            if (matchingMatcher.matches()) {
                lastValid = value;
            }
            return value;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Object getLastValid() {
            return lastValid;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  okValue  DOCUMENT ME!
         */
        public void setLastValid(final Object okValue) {
            if (lastValid == null) {
                lastValid = okValue;
            }
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class TelefonCellEditor extends DefaultCellEditor {
        public TelefonCellEditor(){
            super(new JFormattedTextField());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            final JFormattedTextField editor = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);

            if (value != null){
                JFormattedTextField.AbstractFormatter RegexPatternFormatter;

                editor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                                telPatternFormatter));
                editor.setValue(value);
            }
            editor.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        editor.setValue(telPatternFormatter.getLastValid());
                    }
                });
            editor.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent evt) {
                        editor.setValue(telPatternFormatter.getLastValid());
                    }
                });
            return editor;
        }

        @Override
        public boolean stopCellEditing() {
            try {
                // try to get the value
                this.getCellEditorValue();
                return super.stopCellEditing();
            } catch (Exception ex) {
                return false;
            }

        }

    }
    
    
}