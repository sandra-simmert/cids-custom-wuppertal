/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.utils;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public class Sb_stadtbildUtils {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            Sb_stadtbildUtils.class);

    private static final CidsBean WUPPERTAL;

    static {
        WUPPERTAL = getOrtWupertal();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Get the CidsBean of the Sb_Ort with the name 'Wuppertal'. Might be null.
     *
     * @return  DOCUMENT ME!
     */
    public static CidsBean getWUPPERTAL() {
        return WUPPERTAL;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static CidsBean getOrtWupertal() {
        try {
            final MetaClass ortClass = ClassCacheMultiple.getMetaClass(
                    "WUNDA_BLAU",
                    "sb_ort");
            if (ortClass != null) {
                final StringBuffer wuppertalQuery = new StringBuffer("select ").append(ortClass.getId())
                            .append(", ")
                            .append(ortClass.getPrimaryKey())
                            .append(" from ")
                            .append(ortClass.getTableName())
                            .append(" where name ilike 'Wuppertal'");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("SQL: wuppertalQuery:" + wuppertalQuery.toString());
                }
                final MetaObject[] wuppertal;
                try {
                    wuppertal = SessionManager.getProxy().getMetaObjectByQuery(wuppertalQuery.toString(), 0);
                    if (wuppertal.length > 0) {
                        return wuppertal[0].getBean();
                    }
                } catch (ConnectionException ex) {
                    LOG.error(ex, ex);
                }
            }
        } catch (Exception ex) {
            LOG.error("The Location Wuppertal could not be loaded.", ex);
        }
        return null;
    }
}