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
package de.cismet.cids.custom.objecteditors.utils;

import Sirius.navigator.connection.SessionManager;

import Sirius.server.newuser.User;

import lombok.Getter;

import java.io.StringReader;

import java.util.Properties;

import de.cismet.cids.custom.utils.WundaBlauServerResources;

import de.cismet.cids.server.actions.GetServerResourceServerAction;

import de.cismet.connectioncontext.AbstractConnectionContext.Category;

import de.cismet.connectioncontext.ConnectionContext;

/**
 * DOCUMENT ME!
 *
 * @author   sandra
 * @version  $Revision$, $Date$
 */
@Getter
public class No2ConfProperties {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            No2ConfProperties.class);

    //~ Instance fields --------------------------------------------------------

    private final String fotoUrl;
    private final Double bufferMeter;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new No2ConfProperties object.
     *
     * @param  properties  DOCUMENT ME!
     */
    private No2ConfProperties(final Properties properties) {
        fotoUrl = readProperty(properties, "PICTURE_PATH", null);
        bufferMeter = Double.valueOf(readProperty(properties, "BUFFER_METER", null));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   properties    DOCUMENT ME!
     * @param   property      DOCUMENT ME!
     * @param   defaultValue  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String readProperty(final Properties properties, final String property, final String defaultValue) {
        String value = defaultValue;
        try {
            value = properties.getProperty(property, defaultValue);
        } catch (final Exception ex) {
            final String message = "could not read " + property + " from "
                        + WundaBlauServerResources.NO2_CONF_PROPERTIES.getValue()
                        + ". setting to default value: " + defaultValue;
            LOG.warn(message, ex);
        }
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static No2ConfProperties getInstance() {
        return No2ConfProperties.LazyInitialiser.INSTANCE;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final No2ConfProperties INSTANCE;

        static {
            No2ConfProperties instance = null;

            try {
                final User user = SessionManager.getSession().getUser();
                final Object ret = SessionManager.getSession()
                            .getConnection()
                            .executeTask(
                                user,
                                GetServerResourceServerAction.TASK_NAME,
                                "WUNDA_BLAU",
                                WundaBlauServerResources.NO2_CONF_PROPERTIES.getValue(),
                                ConnectionContext.create(Category.STATIC, No2ConfProperties.class.getSimpleName()));
                if (ret instanceof Exception) {
                    throw (Exception)ret;
                }
                final Properties properties = new Properties();
                properties.load(new StringReader((String)ret));
                instance = new No2ConfProperties(properties);
            } catch (final Exception ex) {
                LOG.error(ex, ex);
            }

            INSTANCE = instance;
        }

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }
}
