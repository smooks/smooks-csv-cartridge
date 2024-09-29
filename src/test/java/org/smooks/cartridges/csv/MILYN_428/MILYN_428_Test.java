/*-
 * ========================LICENSE_START=================================
 * smooks-csv-cartridge
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 *
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 *
 * ======================================================================
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ======================================================================
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.cartridges.csv.MILYN_428;

import org.junit.Test;
import org.smooks.Smooks;
import org.smooks.api.SmooksException;
import org.smooks.api.io.Source;
import org.smooks.cartridges.csv.CSVHeaderValidationException;
import org.smooks.io.source.StreamSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Clemens Fuchslocher
 */
public class MILYN_428_Test {

    // No errors.
    @Test
    public void test01() throws IOException, SAXException {
        try (Smooks smooks = new Smooks(getConfig("/MILYN_428/test-01-config.xml"))) {
            smooks.filterSource(getSource("/MILYN_428/test-01-data.csv"));
        } catch (SmooksException exception) {
            fail(getStackTrace(exception));
        }
    }

    // Ignored fields.
    @Test
    public void test04() throws IOException, SAXException {
        Smooks smooks = null;
        try {
            smooks = new Smooks(getConfig("/MILYN_428/test-04-config.xml"));
            smooks.filterSource(getSource("/MILYN_428/test-04-data.csv"));
        } catch (SmooksException exception) {
            fail(getStackTrace(exception));
        } finally {
            if (smooks != null) {
                smooks.close();
            }
        }
    }

    // String manipulation functions.
    @Test
    public void test05() throws IOException, SAXException {
        Smooks smooks = null;
        try {
            smooks = new Smooks(getConfig("/MILYN_428/test-05-config.xml"));
            smooks.filterSource(getSource("/MILYN_428/test-05-data.csv"));
        } catch (SmooksException exception) {
            fail(getStackTrace(exception));
        } finally {
            if (smooks != null) {
                smooks.close();
            }
        }
    }

    // No header validation.
    @Test
    public void test08() throws IOException, SAXException {
        Smooks smooks = null;
        try {
            smooks = new Smooks(getConfig("/MILYN_428/test-08-config.xml"));
            smooks.filterSource(getSource("/MILYN_428/test-08-data.csv"));
        } catch (SmooksException exception) {
            fail(getStackTrace(exception));
        } finally {
            if (smooks != null) {
                smooks.close();
            }
        }
    }

    // Disabled header validation.
    @Test
    public void test09() throws IOException, SAXException {
        Smooks smooks = null;
        try {
            smooks = new Smooks(getConfig("/MILYN_428/test-09-config.xml"));
            smooks.filterSource(getSource("/MILYN_428/test-09-data.csv"));
        } catch (SmooksException exception) {
            fail(getStackTrace(exception));
        } finally {
            if (smooks != null) {
                smooks.close();
            }
        }
    }

    private InputStream getConfig(final String file) {
        return getClass().getResourceAsStream(file);
    }

    private Source getSource(final String file) {
        return new StreamSource<>(getClass().getResourceAsStream(file));
    }

    public static String getStackTrace(final Throwable throwable) {
        Writer writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    private void assertException(final Exception exception, final String[] expected, final String[] found, final String message) {
        Throwable cause = exception.getCause();
        if (cause == null) {
            fail("cause == null");
        }

        if (!(cause instanceof CSVHeaderValidationException)) {
            fail("!(cause instanceof CsvHeaderValidationException)");
        }

        CSVHeaderValidationException validation = (CSVHeaderValidationException) cause;
        assertEquals(Arrays.asList(expected), validation.getExpected());
        assertEquals(Arrays.asList(found), validation.getFound());
        assertEquals(message, validation.getMessage());
    }
}
