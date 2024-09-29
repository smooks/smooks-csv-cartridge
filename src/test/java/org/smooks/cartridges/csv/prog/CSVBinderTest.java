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
package org.smooks.cartridges.csv.prog;

import org.junit.Test;
import org.smooks.cartridges.csv.Person;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author
 */
@SuppressWarnings("unchecked")
public class CSVBinderTest {

    @Test
    public void test_CSVListBinder() {
        InputStream csvStream = getClass().getResourceAsStream("/input-message-01.csv");

        CSVListBinder binder = new CSVListBinder("firstname,lastname,gender,age,country", Person.class);
        List<Person> people = (List<Person>) binder.bind(csvStream);

        assertEquals(2, people.size());
        assertEquals("[(Tom, Fennelly, Ireland, Male, 4), (Mike, Fennelly, Ireland, Male, 2)]", people.toString());
    }

    @Test
    public void test_CSVMapBinder() {
        InputStream csvStream = getClass().getResourceAsStream("/input-message-01.csv");

        CSVMapBinder binder = new CSVMapBinder("firstname,lastname,gender,age,country", Person.class, "firstname");
        Map<String, Person> people = binder.bind(csvStream);

        assertEquals(2, people.size());
        assertEquals("(Tom, Fennelly, Ireland, Male, 4)", people.get("Tom").toString());
        assertEquals("(Mike, Fennelly, Ireland, Male, 2)", people.get("Mike").toString());
    }
}
