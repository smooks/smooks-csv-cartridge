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
package org.smooks.cartridges.csv.MILYN_642;

import org.junit.Test;
import org.smooks.Smooks;
import org.smooks.api.ExecutionContext;
import org.smooks.api.SmooksException;
import org.smooks.cartridges.csv.CSVRecordParserConfigurator;
import org.smooks.support.SmooksUtil;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Ken Hill
 */
public class MILYN_642_Test {
	// Default escape char
    @Test
    public void test_01() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/MILYN_642/test-01-config.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/MILYN_642/test-01-data.csv"), smooks);

        assertEquals(
            "<csv-set><csv-record number=\"1\"><name>Erika Mustermann</name><email>e.m@ex.org</email></csv-record><csv-record number=\"2\"><name>Max \"The Man\" Mustermann</name><email>m.m@ex.org</email></csv-record></csv-set>",
            result);
    }

	// Custom escape char, xml config
    @Test
    public void test_02() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/MILYN_642/test-02-config.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/MILYN_642/test-02-data.csv"), smooks);

        assertEquals(
            "<csv-set><csv-record number=\"1\"><name>Erika Mustermann</name><email>e.m@ex.org</email></csv-record><csv-record number=\"2\"><name>Max \"The Man\" Mustermann</name><email>m.m@ex.org</email></csv-record></csv-set>",
            result);
    }
	
	// Custom escape, programmatic config
    @Test
    public void test_03() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();
		
        smooks.setReaderConfig(
            new CSVRecordParserConfigurator("name,email")
                .setEscapeChar('~'));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/MILYN_642/test-02-data.csv"), smooks);
		
        assertEquals(
            "<csv-set><csv-record number=\"1\"><name>Erika Mustermann</name><email>e.m@ex.org</email></csv-record><csv-record number=\"2\"><name>Max \"The Man\" Mustermann</name><email>m.m@ex.org</email></csv-record></csv-set>",
            result);
    }
}
