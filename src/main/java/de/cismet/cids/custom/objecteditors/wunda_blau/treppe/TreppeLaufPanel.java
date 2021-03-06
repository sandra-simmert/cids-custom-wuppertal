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
package de.cismet.cids.custom.objecteditors.wunda_blau.treppe;

import Sirius.server.middleware.types.MetaClass;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;

import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicSpinnerUI;

import de.cismet.cids.custom.objecteditors.utils.RendererTools;
import de.cismet.cids.custom.wunda_blau.search.server.TreppeMaterialArtLightweightSearch;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.CidsBeanStore;
import de.cismet.cids.dynamics.Disposable;

import de.cismet.cids.editors.DefaultBindableReferenceCombo;
import de.cismet.cids.editors.FastBindableReferenceCombo;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.gui.RoundedPanel;
import de.cismet.tools.gui.SemiRoundedPanel;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class TreppeLaufPanel extends javax.swing.JPanel implements CidsBeanStore,
    Disposable,
    ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(TreppeLaufPanel.class);
    private static final MetaClass MC__TREPPENLAUF_MATERIAL;

    static {
        final ConnectionContext connectionContext = ConnectionContext.create(
                ConnectionContext.Category.STATIC,
                TreppeLaufPanel.class.getSimpleName());
        MC__TREPPENLAUF_MATERIAL = ClassCacheMultiple.getMetaClass(
                "WUNDA_BLAU",
                "TREPPE_TREPPENLAUF_MATERIAL",
                connectionContext);
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

        final ButtonGroup buttonGroup1 = new ButtonGroup();
        final JPanel jPanel47 = new JPanel();
        final RoundedPanel panAllgemein3 = new RoundedPanel();
        final SemiRoundedPanel panBeschreibungTitle3 = new SemiRoundedPanel();
        final JLabel lblHeaderAllgemein4 = new JLabel();
        final JPanel panBeschreibungContent3 = new JPanel();
        final JPanel jPanel48 = new JPanel();
        final JLabel jLabel62 = new JLabel();
        final JPanel jPanel4 = new JPanel();
        final JLabel jLabel73 = new JLabel();
        jSpinner1 = new JSpinner();
        jTextField22 = new JTextField();
        final JSeparator jSeparator3 = new JSeparator();
        final JLabel jLabel66 = new JLabel();
        final JPanel jPanel3 = new JPanel();
        jSpinner4 = new JSpinner();
        jSpinner2 = new JSpinner();
        jSpinner3 = new JSpinner();
        final JLabel jLabel72 = new JLabel();
        final JLabel jLabel64 = new JLabel();
        final JLabel jLabel63 = new JLabel();
        final JLabel jLabel1 = new JLabel();
        final JLabel jLabel3 = new JLabel();
        final JLabel jLabel4 = new JLabel();
        final JSeparator jSeparator2 = new JSeparator();
        final JLabel jLabel61 = new JLabel();
        final JLabel jLabel2 = new JLabel();
        final JPanel jPanel2 = new JPanel();
        defaultBindableReferenceCombo1 = new DefaultBindableReferenceCombo(MC__TREPPENLAUF_MATERIAL, true, false);
        fastBindableReferenceCombo1 = new FastBindableReferenceCombo(
                materialArtSearch1,
                materialArtSearch1.getRepresentationPattern(),
                materialArtSearch1.getRepresentationFields());
        fastBindableReferenceCombo2 = new FastBindableReferenceCombo(
                materialArtSearch2,
                materialArtSearch2.getRepresentationPattern(),
                materialArtSearch2.getRepresentationFields());
        final JPanel jPanel5 = new JPanel();
        jCheckBox2 = new JCheckBox();
        jCheckBox1 = new JCheckBox();
        final JLabel jLabel60 = new JLabel();
        final JScrollPane jScrollPane4 = new JScrollPane();
        jTextArea3 = new JTextArea();
        final Box.Filler filler4 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        final Box.Filler filler1 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        final JPanel jPanel6 = new JPanel();
        treppeBauteilZustandKostenPanel7 = new TreppeBauteilZustandKostenPanel(editable);
        final Box.Filler filler3 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        btnRemoveArt1 = new JButton();
        final JSeparator jSeparator1 = new JSeparator();
        final Box.Filler filler2 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));

        final FormListener formListener = new FormListener();

        setName("Form"); // NOI18N
        setOpaque(false);
        setLayout(new GridBagLayout());

        jPanel47.setName("jPanel47"); // NOI18N
        jPanel47.setOpaque(false);
        jPanel47.setLayout(new GridBagLayout());

        panAllgemein3.setName("panAllgemein3"); // NOI18N
        panAllgemein3.setLayout(new GridBagLayout());

        panBeschreibungTitle3.setBackground(new Color(51, 51, 51));
        panBeschreibungTitle3.setName("panBeschreibungTitle3"); // NOI18N
        panBeschreibungTitle3.setLayout(new FlowLayout());

        lblHeaderAllgemein4.setForeground(new Color(255, 255, 255));
        lblHeaderAllgemein4.setHorizontalAlignment(SwingConstants.CENTER);
        Mnemonics.setLocalizedText(
            lblHeaderAllgemein4,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.lblHeaderAllgemein4.text")); // NOI18N
        lblHeaderAllgemein4.setName("lblHeaderAllgemein4");                                          // NOI18N
        panBeschreibungTitle3.add(lblHeaderAllgemein4);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        panAllgemein3.add(panBeschreibungTitle3, gridBagConstraints);

        panBeschreibungContent3.setName("panBeschreibungContent3"); // NOI18N
        panBeschreibungContent3.setOpaque(false);
        panBeschreibungContent3.setLayout(new GridBagLayout());

        jPanel48.setName("jPanel48"); // NOI18N
        jPanel48.setOpaque(false);
        jPanel48.setLayout(new GridBagLayout());

        Mnemonics.setLocalizedText(
            jLabel62,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel62.text")); // NOI18N
        jLabel62.setName("jLabel62");                                                     // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new Insets(1, 0, 1, 5);
        jPanel48.add(jLabel62, gridBagConstraints);

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setLayout(new GridBagLayout());

        Mnemonics.setLocalizedText(
            jLabel73,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel73.text")); // NOI18N
        jLabel73.setName("jLabel73");                                                     // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new Insets(1, 10, 1, 5);
        jPanel4.add(jLabel73, gridBagConstraints);

        jSpinner1.setModel(new SpinnerNumberModel(0, 0, null, 1));
        jSpinner1.setName("jSpinner1"); // NOI18N

        Binding binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.stufen}"),
                jSpinner1,
                BeanProperty.create("value"));
        binding.setSourceNullValue(0);
        binding.setSourceUnreadableValue(0);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 0, 1, 0);
        jPanel4.add(jSpinner1, gridBagConstraints);
        jSpinner1.setUI(new BasicSpinnerUI() {

                @Override
                protected Component createNextButton() {
                    return null;
                }

                @Override
                protected Component createPreviousButton() {
                    return null;
                }
            });

        jTextField22.setName("jTextField22"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.nummer}"),
                jTextField22,
                BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 0, 1, 0);
        jPanel4.add(jTextField22, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel48.add(jPanel4, gridBagConstraints);

        jSeparator3.setName("jSeparator3"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 0, 1, 0);
        jPanel48.add(jSeparator3, gridBagConstraints);

        Mnemonics.setLocalizedText(
            jLabel66,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel66.text")); // NOI18N
        jLabel66.setName("jLabel66");                                                     // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new Insets(1, 0, 1, 5);
        jPanel48.add(jLabel66, gridBagConstraints);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new GridBagLayout());

        jSpinner4.setModel(new SpinnerNumberModel(0.0d, 0.0d, null, 1.0E-4d));
        jSpinner4.setName("jSpinner4"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.hoehe}"),
                jSpinner4,
                BeanProperty.create("value"));
        binding.setSourceNullValue(0d);
        binding.setSourceUnreadableValue(0d);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 0, 1, 0);
        jPanel3.add(jSpinner4, gridBagConstraints);

        jSpinner2.setModel(new SpinnerNumberModel(0.0d, 0.0d, null, 1.0E-4d));
        jSpinner2.setName("jSpinner2"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.tiefe}"),
                jSpinner2,
                BeanProperty.create("value"));
        binding.setSourceNullValue(0d);
        binding.setSourceUnreadableValue(0d);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 10, 1, 0);
        jPanel3.add(jSpinner2, gridBagConstraints);

        jSpinner3.setModel(new SpinnerNumberModel(0.0d, 0.0d, null, 0.01d));
        jSpinner3.setName("jSpinner3"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.breite}"),
                jSpinner3,
                BeanProperty.create("value"));
        binding.setSourceNullValue(0d);
        binding.setSourceUnreadableValue(0d);
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 10, 1, 0);
        jPanel3.add(jSpinner3, gridBagConstraints);

        Mnemonics.setLocalizedText(
            jLabel72,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel72.text")); // NOI18N
        jLabel72.setName("jLabel72");                                                     // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(1, 10, 1, 0);
        jPanel3.add(jLabel72, gridBagConstraints);

        Mnemonics.setLocalizedText(
            jLabel64,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel64.text")); // NOI18N
        jLabel64.setName("jLabel64");                                                     // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(1, 0, 1, 0);
        jPanel3.add(jLabel64, gridBagConstraints);

        Mnemonics.setLocalizedText(
            jLabel63,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel63.text")); // NOI18N
        jLabel63.setName("jLabel63");                                                     // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(1, 10, 1, 0);
        jPanel3.add(jLabel63, gridBagConstraints);

        Mnemonics.setLocalizedText(jLabel1, NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1");                                                                                      // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 2, 0, 0);
        jPanel3.add(jLabel1, gridBagConstraints);

        Mnemonics.setLocalizedText(jLabel3, NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3");                                                                                      // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 2, 0, 0);
        jPanel3.add(jLabel3, gridBagConstraints);

        Mnemonics.setLocalizedText(jLabel4, NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4");                                                                                      // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 2, 0, 0);
        jPanel3.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel48.add(jPanel3, gridBagConstraints);

        jSeparator2.setName("jSeparator2"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 0, 1, 0);
        jPanel48.add(jSeparator2, gridBagConstraints);

        Mnemonics.setLocalizedText(
            jLabel61,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel61.text")); // NOI18N
        jLabel61.setName("jLabel61");                                                     // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new Insets(1, 0, 1, 5);
        jPanel48.add(jLabel61, gridBagConstraints);

        Mnemonics.setLocalizedText(jLabel2, NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2");                                                                                      // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new Insets(1, 0, 1, 5);
        jPanel48.add(jLabel2, gridBagConstraints);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new GridBagLayout());

        defaultBindableReferenceCombo1.setName("defaultBindableReferenceCombo1"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.material}"),
                defaultBindableReferenceCombo1,
                BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        defaultBindableReferenceCombo1.addActionListener(formListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 0, 1, 0);
        jPanel2.add(defaultBindableReferenceCombo1, gridBagConstraints);

        fastBindableReferenceCombo1.setName("fastBindableReferenceCombo1"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.material_art_1}"),
                fastBindableReferenceCombo1,
                BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(1, 10, 1, 0);
        jPanel2.add(fastBindableReferenceCombo1, gridBagConstraints);

        fastBindableReferenceCombo2.setName("fastBindableReferenceCombo2"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.material_art_2}"),
                fastBindableReferenceCombo2,
                BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(1, 10, 1, 0);
        jPanel2.add(fastBindableReferenceCombo2, gridBagConstraints);

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setOpaque(false);
        jPanel5.setLayout(new GridLayout(1, 2));

        buttonGroup1.add(jCheckBox2);
        Mnemonics.setLocalizedText(
            jCheckBox2,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jCheckBox2.text")); // NOI18N
        jCheckBox2.setContentAreaFilled(false);
        jCheckBox2.setName("jCheckBox2");                                                   // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.einteilig}"),
                jCheckBox2,
                BeanProperty.create("selected"));
        binding.setSourceNullValue(false);
        binding.setSourceUnreadableValue(false);
        bindingGroup.addBinding(binding);

        jPanel5.add(jCheckBox2);

        buttonGroup1.add(jCheckBox1);
        Mnemonics.setLocalizedText(
            jCheckBox1,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jCheckBox1.text")); // NOI18N
        jCheckBox1.setContentAreaFilled(false);
        jCheckBox1.setName("jCheckBox1");                                                   // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.mehrteilig}"),
                jCheckBox1,
                BeanProperty.create("selected"));
        binding.setSourceNullValue(false);
        binding.setSourceUnreadableValue(false);
        bindingGroup.addBinding(binding);

        jPanel5.add(jCheckBox1);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        jPanel2.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel48.add(jPanel2, gridBagConstraints);

        Mnemonics.setLocalizedText(
            jLabel60,
            NbBundle.getMessage(TreppeLaufPanel.class, "TreppeLaufPanel.jLabel60.text")); // NOI18N
        jLabel60.setVerticalAlignment(SwingConstants.TOP);
        jLabel60.setName("jLabel60");                                                     // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new Insets(1, 0, 1, 5);
        jPanel48.add(jLabel60, gridBagConstraints);

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jTextArea3.setColumns(20);
        jTextArea3.setLineWrap(true);
        jTextArea3.setWrapStyleWord(true);
        jTextArea3.setName("jTextArea3"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.bemerkung}"),
                jTextArea3,
                BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jScrollPane4.setViewportView(jTextArea3);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(1, 0, 0, 0);
        jPanel48.add(jScrollPane4, gridBagConstraints);

        filler4.setName("filler4"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(19, 0, 19, 0);
        jPanel48.add(filler4, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        panBeschreibungContent3.add(jPanel48, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAllgemein3.add(panBeschreibungContent3, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        jPanel47.add(panAllgemein3, gridBagConstraints);

        filler1.setName("filler1"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel47.add(filler1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 0, 0, 5);
        add(jPanel47, gridBagConstraints);

        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setOpaque(false);
        jPanel6.setLayout(new GridBagLayout());

        treppeBauteilZustandKostenPanel7.setName("treppeBauteilZustandKostenPanel7"); // NOI18N

        binding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                ELProperty.create("${cidsBean.zustand}"),
                treppeBauteilZustandKostenPanel7,
                BeanProperty.create("cidsBean"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel6.add(treppeBauteilZustandKostenPanel7, gridBagConstraints);

        filler3.setName("filler3"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(filler3, gridBagConstraints);

        btnRemoveArt1.setIcon(new ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_remove_mini.png"))); // NOI18N
        btnRemoveArt1.setBorderPainted(false);
        btnRemoveArt1.setContentAreaFilled(false);
        btnRemoveArt1.setName("btnRemoveArt1");                                                                   // NOI18N
        btnRemoveArt1.addActionListener(formListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        jPanel6.add(btnRemoveArt1, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 5, 0, 0);
        add(jPanel6, gridBagConstraints);

        jSeparator1.setName("jSeparator1"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        add(jSeparator1, gridBagConstraints);

        filler2.setName("filler2"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(filler2, gridBagConstraints);

        bindingGroup.bind();
    }

    /**
     * Code for dispatching events from components to event handlers.
     *
     * @version  $Revision$, $Date$
     */
    private class FormListener implements ActionListener {

        /**
         * Creates a new FormListener object.
         */
        FormListener() {
        }

        @Override
        public void actionPerformed(final ActionEvent evt) {
            if (evt.getSource() == defaultBindableReferenceCombo1) {
                TreppeLaufPanel.this.defaultBindableReferenceCombo1ActionPerformed(evt);
            } else if (evt.getSource() == btnRemoveArt1) {
                TreppeLaufPanel.this.btnRemoveArt1ActionPerformed(evt);
            }
        }
    } // </editor-fold>//GEN-END:initComponents

    //~ Instance fields --------------------------------------------------------

    private CidsBean cidsBean;
    private TreppeLaeufePanel parent;
    private final boolean editable;
    private final TreppeMaterialArtLightweightSearch materialArtSearch1;
    private final TreppeMaterialArtLightweightSearch materialArtSearch2;
    private final ConnectionContext connectionContext;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JButton btnRemoveArt1;
    DefaultBindableReferenceCombo defaultBindableReferenceCombo1;
    FastBindableReferenceCombo fastBindableReferenceCombo1;
    FastBindableReferenceCombo fastBindableReferenceCombo2;
    JCheckBox jCheckBox1;
    JCheckBox jCheckBox2;
    JSpinner jSpinner1;
    JSpinner jSpinner2;
    JSpinner jSpinner3;
    JSpinner jSpinner4;
    JTextArea jTextArea3;
    JTextField jTextField22;
    TreppeBauteilZustandKostenPanel treppeBauteilZustandKostenPanel7;
    private BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TreppeLaufPanel object.
     *
     * @param  connectionContext  DOCUMENT ME!
     */
    public TreppeLaufPanel(final ConnectionContext connectionContext) {
        this(true, connectionContext);
    }

    /**
     * Creates new form TreppePodestPanel.
     *
     * @param  editable           DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public TreppeLaufPanel(final boolean editable, final ConnectionContext connectionContext) {
        this.editable = editable;
        this.connectionContext = connectionContext;
        this.materialArtSearch1 = new TreppeMaterialArtLightweightSearch(
                TreppeMaterialArtLightweightSearch.SearchFor.TREPPENLAUF,
                "%1$2s",
                new String[] { "NAME" });
        this.materialArtSearch2 = new TreppeMaterialArtLightweightSearch(
                TreppeMaterialArtLightweightSearch.SearchFor.TREPPENLAUF,
                "%1$2s",
                new String[] { "NAME" });

        materialArtSearch1.setTypId(1);
        materialArtSearch2.setTypId(2);

        initComponents();
        RendererTools.makeDoubleSpinnerWithoutButtons(jSpinner2, 4);
        RendererTools.makeDoubleSpinnerWithoutButtons(jSpinner3, 2);
        RendererTools.makeDoubleSpinnerWithoutButtons(jSpinner4, 4);
        jTextArea3.addKeyListener(new RendererTools.NoTabTextAreaKeyAdapter());
        if (!editable) {
            RendererTools.makeReadOnly(jSpinner1);
            RendererTools.makeReadOnly(jSpinner2);
            RendererTools.makeReadOnly(jSpinner3);
            RendererTools.makeReadOnly(jSpinner4);
            RendererTools.makeReadOnly(defaultBindableReferenceCombo1);
            RendererTools.makeReadOnly(fastBindableReferenceCombo1);
            RendererTools.makeReadOnly(fastBindableReferenceCombo2);
            RendererTools.makeReadOnly(jCheckBox1);
            RendererTools.makeReadOnly(jCheckBox2);
            RendererTools.makeReadOnly(jTextArea3);
            RendererTools.makeReadOnly(jTextField22);
        }
        btnRemoveArt1.setVisible(editable);

        fastBindableReferenceCombo1.setMetaClassFromTableName("WUNDA_BLAU", "treppe_treppenlauf_material_art");
        fastBindableReferenceCombo2.setMetaClassFromTableName("WUNDA_BLAU", "treppe_treppenlauf_material_art");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnRemoveArt1ActionPerformed(final ActionEvent evt) { //GEN-FIRST:event_btnRemoveArt1ActionPerformed
        parent.getCidsBeans().remove(cidsBean);
        parent.removeLaufPanel(this);
    }                                                                  //GEN-LAST:event_btnRemoveArt1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void defaultBindableReferenceCombo1ActionPerformed(final ActionEvent evt) { //GEN-FIRST:event_defaultBindableReferenceCombo1ActionPerformed
        refreshMaterialTyp();
        fastBindableReferenceCombo1.setSelectedItem(null);
        fastBindableReferenceCombo2.setSelectedItem(null);
    }                                                                                   //GEN-LAST:event_defaultBindableReferenceCombo1ActionPerformed

    /**
     * DOCUMENT ME!
     */
    private void refreshMaterialTyp() {
        materialArtSearch1.setMaterialId((Integer)cidsBean.getProperty("material.id"));
        materialArtSearch2.setMaterialId((Integer)cidsBean.getProperty("material.id"));

        new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    fastBindableReferenceCombo1.refreshModel();
                    fastBindableReferenceCombo2.refreshModel();
                    return null;
                }
            }.execute();
    }

    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        bindingGroup.unbind();
        this.cidsBean = cidsBean;
        bindingGroup.bind();

        if (cidsBean != null) {
            refreshMaterialTyp();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  parent  DOCUMENT ME!
     */
    public void setParent(final TreppeLaeufePanel parent) {
        this.parent = parent;
    }

    @Override
    public void dispose() {
        bindingGroup.unbind();
        treppeBauteilZustandKostenPanel7.dispose();
        cidsBean = null;
        parent = null;
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
