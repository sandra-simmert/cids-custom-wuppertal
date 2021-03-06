/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2011 thorsten
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.cids.custom.objectrenderer.utils.alkis;

import de.aedsicad.aaaweb.service.util.Address;
import de.aedsicad.aaaweb.service.util.Buchungsblatt;
import de.aedsicad.aaaweb.service.util.Buchungsstelle;
import de.aedsicad.aaaweb.service.util.LandParcel;
import de.aedsicad.aaaweb.service.util.Namensnummer;
import de.aedsicad.aaaweb.service.util.Owner;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class AlkisSoapUtils {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AlkisSoapUtils.class);

    public static final String NEWLINE = "<br>";
    public static final String LINK_SEPARATOR_TOKEN = "::";

    public static final String ALKIS_HTML_PRODUCTS_ENABLED = "custom.alkis.products.html.enabled";
    public static final String ALKIS_EIGENTUEMER = "custom.alkis.buchungsblatt@WUNDA_BLAU";

    public static final String ADRESS_HERKUNFT_KATASTERAMT = "Katasteramt";
    public static final String ADRESS_HERKUNFT_GRUNDBUCHAMT = "Grundbuchamt";

    private static final DateFormat DF = new SimpleDateFormat("dd.MM.yyyy");

    //~ Methods ----------------------------------------------------------------

    // --
    /**
     * DOCUMENT ME!
     *
     * @param   bean         DOCUMENT ME!
     * @param   description  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String generateLinkFromCidsBean(final CidsBean bean, final String description) {
        if ((bean != null) && (description != null)) {
            final int objectID = bean.getMetaObject().getId();
            final StringBuilder result = new StringBuilder("<a href=\"");
            result.append(bean.getMetaObject().getMetaClass().getID()).append(LINK_SEPARATOR_TOKEN).append(objectID);
            result.append("\">");
            result.append(description);
            result.append("</a>");
            return result.toString();
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   originatingFlurstueck  DOCUMENT ME!
     * @param   buchungsblatt          DOCUMENT ME!
     * @param   buchungsblattBean      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String buchungsblattToHtml(final CidsBean originatingFlurstueck,
            final Buchungsblatt buchungsblatt,
            final CidsBean buchungsblattBean) {
        final String alkisId = (String)originatingFlurstueck.getProperty("alkis_id");

        String pos = "";
        final Buchungsstelle[] buchungsstellen = buchungsblatt.getBuchungsstellen();
        for (final Buchungsstelle b : buchungsstellen) {
            for (final LandParcel lp : getLandparcelFromBuchungsstelle(b)) {
                if (lp.getLandParcelCode().equals(alkisId)) {
                    pos = b.getSequentialNumber();
                }
            }
        }

        final List<Owner> owners = Arrays.asList(buchungsblatt.getOwners());
        if ((owners != null) && (owners.size() > 0)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(
                "<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\" align=\"left\" valign=\"top\">");
//            infoBuilder.append("<tr><td width=\"200\"><b><a href=\"").append(generateBuchungsblattLinkInfo(buchungsblatt)).append("\">").append(buchungsblatt.getBuchungsblattCode()).append("</a></b></td><td>");
            sb.append("<tr><td width=\"200\">Nr. ")
                    .append(pos)
                    .append(" auf  <b>")
                    .append(generateLinkFromCidsBean(buchungsblattBean, buchungsblatt.getBuchungsblattCode()))
                    .append("</b></td><td>");
//            final Iterator<Owner> ownerIterator = owners.iterator();
//            if (ownerIterator.hasNext()) {
//                infoBuilder.append(ownerToString(ownerIterator.next(), ""));
//            }
            sb.append(buchungsblattOwnersToHtml(buchungsblatt));
            sb.append("</td></tr>");
            sb.append("</table>");
//            infoBuilder.append("</html>");
            return sb.toString();
//            lblBuchungsblattEigentuemer.setText(infoBuilder.toString());
        } else {
            return "";
//            lblBuchungsblattEigentuemer.setText("-");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   address  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String addressToString(final Address address) {
        if (address != null) {
            final StringBuilder addressStringBuilder = new StringBuilder();
            addressStringBuilder.append(getAddressBoldOpenTag(address));
            if (address.getStreet() != null) {
                addressStringBuilder.append(address.getStreet()).append(" ");
            }
            if (address.getHouseNumber() != null) {
                addressStringBuilder.append(address.getHouseNumber());
            }
            if (addressStringBuilder.length() > 0) {
                addressStringBuilder.append(NEWLINE);
            }
            if (address.getPostalCode() != null) {
                addressStringBuilder.append(address.getPostalCode()).append(" ");
            }
            if (address.getCity() != null) {
                addressStringBuilder.append(address.getCity());
            }
            if (addressStringBuilder.length() > 0) {
                addressStringBuilder.append(NEWLINE);
            }
            addressStringBuilder.append(getAdressPostfix(address));
            addressStringBuilder.append(NEWLINE);
            addressStringBuilder.append(getAddressBoldCloseTag(address));
            return addressStringBuilder.toString();
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   address  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String getAdressPostfix(final Address address) {
        final ResourceBundle bundle = ResourceBundle.getBundle(
                "de/cismet/cids/custom/wunda_blau/res/alkis/AdressPostfixStrings");
        if ((address.getHerkunftAdress() != null) && address.getHerkunftAdress().equals(ADRESS_HERKUNFT_KATASTERAMT)) {
            return bundle.getString("kataster");
        } else if ((address.getHerkunftAdress() != null)
                    && address.getHerkunftAdress().equals(ADRESS_HERKUNFT_GRUNDBUCHAMT)) {
            return bundle.getString("grundbuch");
        } else {
            String herkunft = address.getHerkunftAdress();
            if (herkunft == null) {
                herkunft = "-";
            }
            return String.format(bundle.getString("else"), herkunft);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   address  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String getAddressBoldOpenTag(final Address address) {
        if ((address.getHerkunftAdress() != null) && address.getHerkunftAdress().equals(ADRESS_HERKUNFT_KATASTERAMT)) {
            return "<b>";
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   address  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String getAddressBoldCloseTag(final Address address) {
        if ((address.getHerkunftAdress() != null) && address.getHerkunftAdress().equals(ADRESS_HERKUNFT_KATASTERAMT)) {
            return "</b>";
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   buchungsstelle  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static LandParcel[] getLandparcelFromBuchungsstelle(final Buchungsstelle buchungsstelle) {
        if ((buchungsstelle.getBuchungsstellen() == null) && (buchungsstelle.getLandParcel() == null)) {
            LOG.warn("getLandparcelFromBuchungsstelle returns null. Problem on landparcel with number:"
                        + buchungsstelle.getSequentialNumber());
            return new LandParcel[0];
        } else if (buchungsstelle.getBuchungsstellen() == null) {
            return buchungsstelle.getLandParcel();
        } else {
            LandParcel[] result = buchungsstelle.getLandParcel();
            for (final Buchungsstelle b : buchungsstelle.getBuchungsstellen()) {
                result = concatArrays(result, getLandparcelFromBuchungsstelle(b));
            }
            return result;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   a  DOCUMENT ME!
     * @param   b  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static LandParcel[] concatArrays(LandParcel[] a, LandParcel[] b) {
        if (a == null) {
            a = new LandParcel[0];
        }
        if (b == null) {
            b = new LandParcel[0];
        }
        return (LandParcel[])ArrayUtils.addAll(a, b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   land       DOCUMENT ME!
     * @param   gemarkung  DOCUMENT ME!
     * @param   flur       DOCUMENT ME!
     * @param   zaehler    DOCUMENT ME!
     * @param   nenner     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String generateLandparcelCode(final int land,
            final int gemarkung,
            final int flur,
            final int zaehler,
            final int nenner) {
        final String withoutNenner = generateLandparcelCode(land, gemarkung, flur, zaehler);
        final StringBuilder sb = new StringBuilder(withoutNenner);
        sb.append(String.format("/%04d", nenner));
        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   land       DOCUMENT ME!
     * @param   gemarkung  DOCUMENT ME!
     * @param   flur       DOCUMENT ME!
     * @param   zaehler    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String generateLandparcelCode(final int land,
            final int gemarkung,
            final int flur,
            final int zaehler) {
        return String.format("%02d%04d-%03d-%05d", land, gemarkung, flur, zaehler);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fullLandparcelCode  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String prettyPrintLandparcelCode(final String fullLandparcelCode) {
        final String[] tiles = fullLandparcelCode.split("-");
        switch (tiles.length) {
            case 1: {
                final String flurstueck = tiles[0];
                return _prettyPrintLandparcelCode(flurstueck);
            }
            case 2: {
                final String flurstueck = tiles[1];
                final String flur = tiles[0];
                final String result = _prettyPrintLandparcelCode(flurstueck, flur);
                return result;
            }
            case 3: {
                final String flurstueck = tiles[2];
                final String flur = tiles[1];
                final String gemarkung = tiles[0];
                return _prettyPrintLandparcelCode(flurstueck, flur, gemarkung);
            }
            default: {
                return fullLandparcelCode;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   strings    DOCUMENT ME!
     * @param   separator  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String arrayToSeparatedString(final String[] strings, final String separator) {
        if (strings != null) {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < strings.length; i++) {
                result.append(strings[i]);
                if ((i + 1) < strings.length) {
                    result.append(separator);
                }
            }
            return result.toString();
        }
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param   toEscape  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String escapeHtmlSpaces(String toEscape) {
        if (toEscape != null) {
            toEscape = toEscape.replace(" ", "%20");
        }
        return toEscape;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   level              DOCUMENT ME!
     * @param   namensnummerUuids  DOCUMENT ME!
     * @param   namensnummernMap   DOCUMENT ME!
     * @param   ownerHashMap       DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String buchungsblattOwnersToHtml(final int level,
            final List<String> namensnummerUuids,
            final HashMap<String, Namensnummer> namensnummernMap,
            final HashMap<String, Owner> ownerHashMap) {
        final String style;
        if (level > 0) {
            style = "style=\"border-left: 1px solid black\"";
        } else {
            style = "";
        }
        final StringBuffer sb = new StringBuffer(
                "<table cellspacing=\"0\" "
                        + style
                        + " cellpadding=\"10\" border=\"0\" align=\"left\" valign=\"top\">");

        final NamensnummerComparator nnComparator = new NamensnummerComparator(namensnummernMap);

        for (final String uuid : namensnummerUuids) {
            final Namensnummer namensnummer = namensnummernMap.get(uuid);

            sb.append("<tr>");
//            if (level > 0) {
//                sb.append("<td style=\"padding-left:0px; padding-right:0px;\">↳</td>");
//            }
            if (namensnummer.getArtRechtsgemeinschaft() != null) {
                final String artRechtsgemeinschaft = namensnummer.getArtRechtsgemeinschaft().trim();
                sb.append("<td width=\"80\">")
                        .append("ohne Nr.")
                        .append("</td><td><b>")
                        .append(artRechtsgemeinschaft.equals("Sonstiges") ? "Rechtsgemeinschaft"
                                                                          : artRechtsgemeinschaft)
                        .append(":</b> ")
                        .append((namensnummer.getBeschriebRechtsgemeinschaft() != null)
                                    ? namensnummer.getBeschriebRechtsgemeinschaft() : "")
                        .append(NEWLINE);

                final List<String> einzelGemeinschaftsUuids = Arrays.asList(namensnummer.getNamensnummernUUIds());
                Collections.sort(einzelGemeinschaftsUuids, nnComparator);

                sb.append(NEWLINE)
                        .append(buchungsblattOwnersToHtml(
                                    level
                                    + 1,
                                    einzelGemeinschaftsUuids,
                                    namensnummernMap,
                                    ownerHashMap))
                        .append("</td>");
            } else {
                final Owner owner = ownerHashMap.get(namensnummer.getEigentuemerUUId());
                sb.append(buchungsblattOwnerToHtml(namensnummer, owner));
            }

            sb.append("<td style=\"padding-left: 30px\">");
            if ((namensnummer.getZaehler() != null) && (namensnummer.getNenner() != null)) {
                final String part = namensnummer.getZaehler().intValue() + "/"
                            + namensnummer.getNenner().intValue();
                sb.append("<nobr>").append("zu ").append(part).append("</nobr>");
            }
            sb.append("</td></tr>");
        }

        sb.append("</table>");
        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   buchungsblatt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String buchungsblattOwnersToHtml(final Buchungsblatt buchungsblatt) {
        final HashMap<String, Owner> ownersMap = extractOwnersMap(buchungsblatt);
        final HashMap<String, Namensnummer> namensnummernMap = extractNamensnummernMap(buchungsblatt);
        final List<String> rootUuids = getSortedRootUuids(namensnummernMap);

        return buchungsblattOwnersToHtml(0, rootUuids, namensnummernMap, ownersMap);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   buchungsblatt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static HashMap<String, Owner> extractOwnersMap(final Buchungsblatt buchungsblatt) {
        final HashMap<String, Owner> ownersMap = new HashMap<>();
        for (final Owner owner : buchungsblatt.getOwners()) {
            ownersMap.put(owner.getOwnerId(), owner);
        }
        return ownersMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   namensnummernMap  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static List<String> getSortedRootUuids(final HashMap<String, Namensnummer> namensnummernMap) {
        final List<String> redirectedUuids = new ArrayList<>();
        for (final Namensnummer namensnummer : namensnummernMap.values()) {
            final String uuid = namensnummer.getUUId();
            namensnummernMap.put(uuid, namensnummer);
            final String[] namensnummerUuids = namensnummer.getNamensnummernUUIds();
            if (namensnummerUuids != null) {
                redirectedUuids.addAll(Arrays.asList(namensnummerUuids));
            }
        }

        final List<String> rootUuids = new ArrayList<>(namensnummernMap.keySet());
        rootUuids.removeAll(redirectedUuids);
        Collections.sort(rootUuids, new NamensnummerComparator(namensnummernMap));
        return rootUuids;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   buchungsblatt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static HashMap<String, Namensnummer> extractNamensnummernMap(final Buchungsblatt buchungsblatt) {
        final HashMap<String, Namensnummer> namensnummernMap = new HashMap<>();
        for (final Namensnummer namensnummer : buchungsblatt.getNamensnummern()) {
            namensnummernMap.put(namensnummer.getUUId(), namensnummer);
        }
        return namensnummernMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   namensnummer  DOCUMENT ME!
     * @param   owner         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String buchungsblattOwnerToHtml(final Namensnummer namensnummer, final Owner owner) {
        final StringBuffer sb = new StringBuffer();
        sb.append("<td width=\"60\">");
        if (owner.getNameNumber() != null) {
            sb.append(normalizeNameNumber(namensnummer.getLaufendeNummer()));
        }
        sb.append("</td><td><p>");
        if (owner.getSalutation() != null) {
            sb.append(owner.getSalutation()).append(" ");
        }
        if (owner.getForeName() != null) {
            sb.append(owner.getForeName()).append(" ");
        }
        if (owner.getSurName() != null) {
            sb.append(owner.getSurName());
        }
        if (owner.getDateOfBirth() != null) {
            Date date;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(owner.getDateOfBirth());
            } catch (final Exception ex) {
                date = null;
            }
            sb.append(", *").append(DF.format(date));
        }
        sb.append("</p>");
        if (owner.getNameOfBirth() != null) {
            sb.append("<p>").append("geb. ").append(owner.getNameOfBirth()).append("</p>");
        }
        final Address[] addresses = owner.getAddresses();
        if (addresses != null) {
            boolean first = true;
            for (final Address address : addresses) {
                if ((address != null) && (address.getHerkunftAdress() != null)
                            && address.getHerkunftAdress().equals(ADRESS_HERKUNFT_KATASTERAMT)) {
                    sb.append(first ? "" : NEWLINE).append("<p>").append(addressToString(address)).append("</p>");
                }
                first = false;
            }
            for (final Address address : addresses) {
                if ((address != null)
                            && ((address.getHerkunftAdress() == null)
                                || (!address.getHerkunftAdress().equals(ADRESS_HERKUNFT_KATASTERAMT)))) {
                    sb.append(first ? "" : NEWLINE).append("<p>").append(addressToString(address)).append("</p>");
                }
                first = false;
            }
        }

        sb.append("</td>");

        return sb.toString();
    }

    /**
     * ----------------private.
     *
     * @param   in  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String removeLeadingZeros(final String in) {
        return in.replaceAll("^0*", "");
    }

    /**
     * DOCUMENT ME!
     *
     * @param   nameNumber  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String normalizeNameNumber(final String nameNumber) {
        final String[] tokens = nameNumber.split("\\.");
        final StringBuilder result = new StringBuilder();
        for (String token : tokens) {
            token = removeLeadingZeros(token);
            if (token.length() > 0) {
                result.append(token).append(".");
            }
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } else {
            return "0";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   flurstueck  DOCUMENT ME!
     * @param   flur        DOCUMENT ME!
     * @param   gemarkung   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String _prettyPrintLandparcelCode(final String flurstueck,
            final String flur,
            final String gemarkung) {
        return _prettyPrintLandparcelCode(flurstueck, flur) + " - Gemarkung " + gemarkung;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   flurstueck  DOCUMENT ME!
     * @param   flur        DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String _prettyPrintLandparcelCode(final String flurstueck, final String flur) {
        return _prettyPrintLandparcelCode(flurstueck) + " - Flur " + removeLeadingZeros(flur);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   flurstueck  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String _prettyPrintLandparcelCode(final String flurstueck) {
        return "Flurstück " + prettyPrintFlurstueck(flurstueck);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fsZahlerNenner  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String prettyPrintFlurstueck(final String fsZahlerNenner) {
        final String[] tiles = fsZahlerNenner.split("/");
        if (tiles.length == 2) {
            return removeLeadingZeros(tiles[0]) + "/" + removeLeadingZeros(tiles[1]);
        } else if (tiles.length == 1) {
            return removeLeadingZeros(tiles[0]);
        }
        return fsZahlerNenner;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   blatt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String getBuchungsartFromBuchungsblatt(final Buchungsblatt blatt) {
        final Buchungsstelle[] buchungsstellen = blatt.getBuchungsstellen();
        if ((buchungsstellen != null) && (buchungsstellen.length > 0)) {
            final ArrayList<Buchungsstelle> alleStellen = new ArrayList<>();
            alleStellen.addAll(Arrays.asList(buchungsstellen));
            if (isListOfSameBuchungsart(alleStellen)) {
                return alleStellen.get(0).getBuchungsart();
            } else {
                return "diverse";
            }
        }
        return "";
    }

    /**
     * Check if in list of Buchungsstellen, all Buchungsstellen have the same Buchungsart. Return true if all have the
     * same Buchungsart, false otherwise. The check is realized with adding the buchungsart to a set. As soon the set
     * contains a second buchungsart, false can be returned.
     *
     * @param   buchungsstellen  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static boolean isListOfSameBuchungsart(final List<Buchungsstelle> buchungsstellen) {
        final Set<String> set = new HashSet<>();
        for (final Buchungsstelle o : buchungsstellen) {
            if (set.isEmpty()) {
                set.add(o.getBuchungsart());
            } else {
                if (set.add(o.getBuchungsart())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   aufteilungsnummer  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String prettyPrintAufteilungsnummer(final String aufteilungsnummer) {
        if (aufteilungsnummer != null) {
            return "ATP Nr. " + aufteilungsnummer;
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   fraction  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String prettyPrintFraction(final String fraction) {
        if (fraction != null) {
            return "Anteil " + fraction;
        }
        return "";
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class NamensnummerComparator implements Comparator<String> {

        //~ Instance fields ----------------------------------------------------

        private final HashMap<String, Namensnummer> namensnummernMap;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new NnComparator object.
         *
         * @param  namensnummernMap  DOCUMENT ME!
         */
        public NamensnummerComparator(final HashMap<String, Namensnummer> namensnummernMap) {
            this.namensnummernMap = namensnummernMap;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public int compare(final String einzelUuid1, final String einzelUuid2) {
            final Namensnummer n1 = namensnummernMap.get(einzelUuid1);
            final Namensnummer n2 = namensnummernMap.get(einzelUuid2);

            final String tmp1 = ((n1 == null) || (n1.getLaufendeNummer() == null)) ? "ZZZ" : n1.getLaufendeNummer();
            final String tmp2 = ((n2 == null) || (n2.getLaufendeNummer() == null)) ? "ZZZ" : n2.getLaufendeNummer();
            final String[] lfds1 = tmp1.split("\\.");
            final String[] lfds2 = tmp2.split("\\.");

            for (int i = 0; i < Math.max(lfds1.length, lfds2.length); i++) {
                String lfd1 = (i < lfds1.length) ? lfds1[i] : StringUtils.repeat("0", lfds2[i].length());
                String lfd2 = (i < lfds2.length) ? lfds2[i] : StringUtils.repeat("0", lfds1[i].length());

                if (lfd1.length() < lfd2.length()) {
                    lfd1 = StringUtils.repeat("0", lfd2.length() - lfd1.length()) + lfd1;
                } else {
                    lfd2 = StringUtils.repeat("0", lfd1.length() - lfd2.length()) + lfd2;
                }

                final int compare = lfd1.compareTo(lfd2);
                if (compare != 0) {
                    return compare;
                }
            }

            return 0;
        }
    }
}
