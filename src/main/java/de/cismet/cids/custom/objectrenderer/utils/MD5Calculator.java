/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 srichter
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
package de.cismet.cids.custom.objectrenderer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.math.BigInteger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class MD5Calculator {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MD5Calculator object.
     *
     * @throws  AssertionError  DOCUMENT ME!
     */
    private MD5Calculator() {
        throw new AssertionError("Epic fail.");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   inputFile  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NoSuchAlgorithmException  DOCUMENT ME!
     * @throws  IOException               DOCUMENT ME!
     * @throws  RuntimeException          DOCUMENT ME!
     */
    public static String generateMD5(final File inputFile) throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = MessageDigest.getInstance("MD5");
        final InputStream is = new FileInputStream(inputFile);
        final byte[] buffer = new byte[8192];
        int read = 0;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            final byte[] md5sum = digest.digest();
            final BigInteger result = new BigInteger(1, md5sum);
            return result.toString(16);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   filename  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NoSuchAlgorithmException  DOCUMENT ME!
     * @throws  IOException               DOCUMENT ME!
     */
    public static String generateMD5(final String filename) throws NoSuchAlgorithmException, IOException {
        return generateMD5(new File(filename));
    }
}
