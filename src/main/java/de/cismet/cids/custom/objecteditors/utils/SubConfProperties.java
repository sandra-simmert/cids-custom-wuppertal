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
public class SubConfProperties {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SubConfProperties.class);

    //~ Instance fields --------------------------------------------------------

    
    private final String mapUrl;
    private final String mapUrlGLuft;
    private final String mapUrlGSPW2;
    private final String mapUrlGAlkis;
    private final Double bufferMeter;
    private final Double bufferGebiet;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SubProperties object.
     *
     * @param  properties  DOCUMENT ME!
     */
    private SubConfProperties(final Properties properties) {
        bufferMeter = Double.valueOf(readProperty(properties, "BUFFER_METER", null));
        bufferGebiet = Double.valueOf(readProperty(properties, "BUFFER_GEBIET_SEARCH", null));
        mapUrl = String.valueOf(readProperty(properties, "MAP_CALL_STRING", null));
        mapUrlGAlkis = String.valueOf(readProperty(properties, "MAP_CALL_STRING_GEB_ALKIS", null));
        mapUrlGLuft = String.valueOf(readProperty(properties, "MAP_CALL_STRING_GEB_LUFT", null));
        mapUrlGSPW2 = String.valueOf(readProperty(properties, "MAP_CALL_STRING_GEB_SPW2", null));
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
                        + WundaBlauServerResources.SUB_CONF_PROPERTIES.getValue()
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
    public static SubConfProperties getInstance() {
        return SubConfProperties.LazyInitialiser.INSTANCE;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final SubConfProperties INSTANCE;

        static {
            SubConfProperties instance = null;

            try {
                final User user = SessionManager.getSession().getUser();
                final Object ret = SessionManager.getSession()
                            .getConnection()
                            .executeTask(user,
                                GetServerResourceServerAction.TASK_NAME,
                                "WUNDA_BLAU",
                                WundaBlauServerResources.SUB_CONF_PROPERTIES.getValue(),
                                ConnectionContext.create(Category.STATIC, SubConfProperties.class.getSimpleName()));
                if (ret instanceof Exception) {
                    throw (Exception)ret;
                }
                final Properties properties = new Properties();
                properties.load(new StringReader((String)ret));
                instance = new SubConfProperties(properties);
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
