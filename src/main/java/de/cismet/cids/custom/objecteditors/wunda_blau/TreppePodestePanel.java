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

import org.apache.log4j.Logger;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import de.cismet.cids.custom.objectrenderer.utils.ObjectRendererUtils;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.Disposable;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class TreppePodestePanel extends javax.swing.JPanel implements Disposable {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(TreppePodestePanel.class);

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        final JScrollPane jScrollPane1 = new JScrollPane();
        jPanel1 = new JPanel();
        filler1 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
        if (netbeansDesignDummy) {
            treppePodestPanel2 = new TreppePodestPanel();
        }
        btnAddArt1 = new JButton();

        final FormListener formListener = new FormListener();

        setName("Form"); // NOI18N
        setOpaque(false);
        setLayout(new GridBagLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setOpaque(false);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new GridBagLayout());

        filler1.setName("filler1"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(filler1, gridBagConstraints);

        if (netbeansDesignDummy) {
            treppePodestPanel2.setName("treppePodestPanel2"); // NOI18N
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            jPanel1.add(treppePodestPanel2, gridBagConstraints);
        }

        jScrollPane1.setViewportView(jPanel1);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);
        jScrollPane1.getViewport().setOpaque(false);

        btnAddArt1.setIcon(new ImageIcon(
                getClass().getResource("/de/cismet/cids/custom/objecteditors/wunda_blau/edit_add_mini.png"))); // NOI18N
        btnAddArt1.setBorderPainted(false);
        btnAddArt1.setContentAreaFilled(false);
        btnAddArt1.setName("btnAddArt1");                                                                      // NOI18N
        btnAddArt1.addActionListener(formListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        add(btnAddArt1, gridBagConstraints);
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
            if (evt.getSource() == btnAddArt1) {
                TreppePodestePanel.this.btnAddArt1ActionPerformed(evt);
            }
        }
    } // </editor-fold>//GEN-END:initComponents

    //~ Instance fields --------------------------------------------------------

    private final boolean netbeansDesignDummy;
    private List<CidsBean> cidsBeans;
    private final boolean editable;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JButton btnAddArt1;
    Box.Filler filler1;
    JPanel jPanel1;
    TreppePodestPanel treppePodestPanel2;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TreppePodestePanel object.
     */
    public TreppePodestePanel() {
        this(true, true);
    }

    /**
     * Creates a new TreppePodestePanel object.
     *
     * @param  editable  DOCUMENT ME!
     */
    public TreppePodestePanel(final boolean editable) {
        this(editable, false);
    }

    /**
     * Creates new form TreppePodestePanel.
     *
     * @param  editable             DOCUMENT ME!
     * @param  netbeansDesignDummy  DOCUMENT ME!
     */
    public TreppePodestePanel(final boolean editable, final boolean netbeansDesignDummy) {
        this.netbeansDesignDummy = netbeansDesignDummy;
        this.editable = editable;
        initComponents();
        btnAddArt1.setVisible(editable);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBeans  DOCUMENT ME!
     */
    public void setCidsBeans(final List<CidsBean> cidsBeans) {
        jPanel1.removeAll();

        this.cidsBeans = cidsBeans;

        if (cidsBeans != null) {
            Collections.sort(cidsBeans, new TreppeEditor.TeilementComparator("nummer"));
            for (final CidsBean cidsBean : cidsBeans) {
                addPodestPanel(cidsBean);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBean  DOCUMENT ME!
     */
    private void addPodestPanel(final CidsBean cidsBean) {
        jPanel1.remove(filler1);
        final GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = cidsBeans.size();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(filler1, gridBagConstraints);

        new SwingWorker<JPanel, Void>() {

                @Override
                protected JPanel doInBackground() throws Exception {
                    final TreppePodestPanel panel = new TreppePodestPanel(editable);
                    panel.setCidsBean(cidsBean);
                    panel.setParent(TreppePodestePanel.this);
                    return panel;
                }

                @Override
                protected void done() {
                    try {
                        final JPanel panel = get();

                        final GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = cidsBeans.indexOf(cidsBean);
                        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                        gridBagConstraints.weightx = 1.0;
                        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
                        jPanel1.add(panel, gridBagConstraints);

                        jPanel1.repaint();
                    } catch (final Exception ex) {
                        final String message = "Fehler beim Hinzufügen des Podests.";
                        LOG.error(message, ex);
                        ObjectRendererUtils.showExceptionWindowToUser(message, ex, TreppePodestePanel.this);
                    }
                }
            }.execute();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  panel  DOCUMENT ME!
     */
    public void removePodestPanel(final TreppePodestPanel panel) {
        if (panel != null) {
            cidsBeans.remove(panel.getCidsBean());
            jPanel1.remove(panel);
            jPanel1.repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<CidsBean> getCidsBeans() {
        return cidsBeans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnAddArt1ActionPerformed(final ActionEvent evt) { //GEN-FIRST:event_btnAddArt1ActionPerformed
        try {
            final CidsBean cidsBean = CidsBean.createNewCidsBeanFromTableName("WUNDA_BLAU", "TREPPE_PODEST");
            cidsBean.setProperty("zustand", CidsBean.createNewCidsBeanFromTableName("WUNDA_BLAU", "TREPPE_ZUSTAND"));
            addPodestPanel(cidsBean);
            cidsBeans.add(cidsBean);
        } catch (final Exception ex) {
            final String message = "Fehler beim Erzeugen des Podests.";
            LOG.error(message, ex);
            ObjectRendererUtils.showExceptionWindowToUser(message, ex, this);
        }
    }                                                               //GEN-LAST:event_btnAddArt1ActionPerformed

    @Override
    public void dispose() {
        for (final Component comp : jPanel1.getComponents()) {
            if (comp instanceof TreppePodestPanel) {
                final TreppePodestPanel panel = (TreppePodestPanel)comp;
                panel.dispose();
                jPanel1.remove(panel);
            }
        }
    }
}