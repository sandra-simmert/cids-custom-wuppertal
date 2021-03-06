/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * Tester.java
 *
 * Created on 26.03.2009, 21:32:00
 */
package de.cismet.cids.custom.objecteditors.wunda_demo;

import Sirius.navigator.connection.Connection;
import Sirius.navigator.connection.ConnectionFactory;
import Sirius.navigator.connection.ConnectionInfo;
import Sirius.navigator.connection.ConnectionSession;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.connection.proxy.ConnectionProxy;

import Sirius.server.middleware.interfaces.proxy.CatalogueService;
import Sirius.server.middleware.interfaces.proxy.MetaService;
import Sirius.server.middleware.interfaces.proxy.SearchService;
import Sirius.server.middleware.interfaces.proxy.UserService;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.newuser.User;

import java.awt.BorderLayout;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

import javax.swing.JComponent;

import de.cismet.cids.editors.CidsObjectEditorFactory;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class Tester extends javax.swing.JFrame implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    static final String domain = "WUNDA_DEMO";
    static final int AAPERSON_CLASSID = 374;

    //~ Instance fields --------------------------------------------------------

    java.rmi.registry.Registry rmiRegistry = LocateRegistry.getRegistry(1099);

    // lookup des callservers
    Remote r;
    SearchService ss;
    CatalogueService cat;
    MetaService meta;
    UserService us;
    User u;
    JComponent generatedComponent;
    MetaObject preloaded;

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    private final ConnectionContext connectionContext;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form Tester.
     *
     * @param   connectionContext  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public Tester(final ConnectionContext connectionContext) throws Exception {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        // rmi registry lokaliseren

        this.connectionContext = connectionContext;
        // ich weiss, dass die server von callserver implementiert werden

        rmiRegistry = LocateRegistry.getRegistry(1099);

        // lookup des callservers
        r = (Remote)Naming.lookup("rmi://localhost/callServer");
        ss = (SearchService)r;
        cat = (CatalogueService)r;
        meta = (MetaService)r;
        us = (UserService)r;
        u = us.getUser(domain, "Demo", domain, "demo", "demo");

        ConnectionSession session = null;
        ConnectionProxy proxy = null;
        final ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setCallserverURL("rmi://localhost/callServer");
        connectionInfo.setPassword("demo");
        connectionInfo.setUsergroup("Demo");
        connectionInfo.setUserDomain("WUNDA_DEMO");
        connectionInfo.setUsergroupDomain("WUNDA_DEMO");
        connectionInfo.setUsername("demo");

        final Connection connection = ConnectionFactory.getFactory()
                    .createConnection(
                        "Sirius.navigator.connection.RMIConnection",
                        connectionInfo.getCallserverURL(),
                        false,
                        getConnectionContext());

        session = ConnectionFactory.getFactory()
                    .createSession(connection, connectionInfo, true, getConnectionContext());
        proxy = ConnectionFactory.getFactory()
                    .createProxy(
                            "Sirius.navigator.connection.proxy.DefaultConnectionProxyHandler",
                            session,
                            getConnectionContext());
        SessionManager.init(proxy);

        ClassCacheMultiple.setInstance(domain, getConnectionContext()); // , meta, u);

        initComponents();

        final MetaObject mo = meta.getMetaObject(u, 2, AAPERSON_CLASSID, domain, getConnectionContext());
        final JComponent c = CidsObjectEditorFactory.getInstance().getEditor(mo);
        getContentPane().add(c, BorderLayout.CENTER);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 800) / 2, (screenSize.height - 600) / 2, 800, 600);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        new Tester(ConnectionContext.createDeprecated()).setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
    }

    @Override
    public final ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
