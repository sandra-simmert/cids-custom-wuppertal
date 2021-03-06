/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.wunda_blau.toolbaritem;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import org.openide.util.Exceptions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import de.cismet.cids.custom.wunda_blau.startuphooks.MotdWundaStartupHook;

import de.cismet.cids.navigator.utils.CidsClientToolbarItem;

import de.cismet.cids.server.actions.PublishCidsServerMessageAction;
import de.cismet.cids.server.actions.ServerActionParameter;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

import de.cismet.tools.StaticDebuggingTools;

import de.cismet.tools.gui.menu.CidsUiAction;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsClientToolbarItem.class)
public class TestSetMotdAction extends AbstractAction implements CidsClientToolbarItem,
    ConnectionContextStore,
    CidsUiAction {

    //~ Instance fields --------------------------------------------------------

    private ConnectionContext connectionContext = ConnectionContext.createDummy();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NavigatorDownloadManagerAction object.
     */
    public TestSetMotdAction() {
        putValue(Action.NAME, "MOTD");
        putValue(Action.SHORT_DESCRIPTION, "set MOTD (test)");
        putValue(CidsUiAction.CIDS_ACTION_KEY, "TestSetMotdAction");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }

    @Override
    public String getSorterString() {
        return "100";
    }

    @Override
    public boolean isVisible() {
        try {
            return StaticDebuggingTools.checkHomeForFile("MotdTestToolbarEnabled")
                        && SessionManager.getConnection()
                        .hasConfigAttr(SessionManager.getSession().getUser(),
                                "csa://"
                                + PublishCidsServerMessageAction.TASK_NAME,
                                getConnectionContext());
        } catch (ConnectionException ex) {
            return false;
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String message = JOptionPane.showInputDialog("MOTD ?");

        if (message != null) {
            try {
                SessionManager.getSession()
                        .getConnection()
                        .executeTask(
                            SessionManager.getSession().getUser(),
                            PublishCidsServerMessageAction.TASK_NAME,
                            SessionManager.getSession().getUser().getDomain(),
                            message,
                            getConnectionContext(),
                            new ServerActionParameter<>(
                                PublishCidsServerMessageAction.ParameterType.CATEGORY.toString(),
                                MotdWundaStartupHook.MOTD_MESSAGE_MOTD));
                SessionManager.getSession()
                        .getConnection()
                        .executeTask(
                            SessionManager.getSession().getUser(),
                            PublishCidsServerMessageAction.TASK_NAME,
                            SessionManager.getSession().getUser().getDomain(),
                            message.substring(0, Math.min(40, message.length())),
                            getConnectionContext(),
                            new ServerActionParameter<>(
                                PublishCidsServerMessageAction.ParameterType.CATEGORY.toString(),
                                MotdWundaStartupHook.MOTD_MESSAGE_TOTD));
            } catch (final ConnectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
