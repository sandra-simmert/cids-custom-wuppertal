/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.featurerenderer.wunda_blau;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import javax.swing.JComponent;

import de.cismet.cids.custom.objecteditors.utils.ClientAlboProperties;
import de.cismet.cids.custom.utils.AlboProperties;

import de.cismet.cids.featurerenderer.CustomCidsFeatureRenderer;

import de.cismet.cismap.commons.Refreshable;
import de.cismet.cismap.commons.gui.piccolo.CustomFixedWidthStroke;

/**
 * DOCUMENT ME!
 *
 * @author   reinhard.verkennis
 * @version  $Revision$, $Date$
 */

public class AlboFlaecheFeatureRenderer extends CustomCidsFeatureRenderer {

    //~ Methods ----------------------------------------------------------------

    @Override
    public float getTransparency() {
        return 0.5f;
    }

    @Override
    public Paint getFillingStyle() {
        switch ((String)metaObject.getBean().getProperty("fk_art.schluessel")) {
            case "altablagerung": {
                return Color.getColor(ClientAlboProperties.getInstance().getAltablagerung_color(), Color.BLUE);
            }
            case "altstandort": {
                return Color.getColor(ClientAlboProperties.getInstance().getAltstandort_color(), Color.CYAN);
            }
            case "betriebsstandort": {
                return Color.getColor(ClientAlboProperties.getInstance().getBetriebsstandort_color(), Color.GREEN);
            }
            case "bewirtschaftungsschaden": {
                return Color.getColor(ClientAlboProperties.getInstance().getBewirtschaftungsschaden_color(),
                        Color.MAGENTA);
            }
            case "immission": {
                return Color.getColor(ClientAlboProperties.getInstance().getImmission_color(), Color.ORANGE);
            }
            case "materialaufbringung": {
                return Color.getColor(ClientAlboProperties.getInstance().getMaterialaufbringung_color(), Color.YELLOW);
            }
            case "ohne_verdacht": {
                return Color.getColor(ClientAlboProperties.getInstance().getOhne_verdacht_color(), Color.PINK);
            }
            case "schadensfall": {
                return Color.getColor(ClientAlboProperties.getInstance().getSchadensfall_color(), Color.RED);
            }
        }
        return Color.GRAY;
    }

    @Override
    public Stroke getLineStyle() {
        return new CustomFixedWidthStroke(1.5f);
    }

    @Override
    public Paint getLinePaint() {
        return new Color(0, 0, 0, 255);
    }

    @Override
    public JComponent getInfoComponent(final Refreshable refresh) {
        return null;
    }

    @Override
    public void assign() {
    }
}