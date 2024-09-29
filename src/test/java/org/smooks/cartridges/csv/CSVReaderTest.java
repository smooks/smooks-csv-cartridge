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
package org.smooks.cartridges.csv;

import org.junit.Test;
import org.smooks.Smooks;
import org.smooks.api.ExecutionContext;
import org.smooks.api.SmooksConfigException;
import org.smooks.api.SmooksException;
import org.smooks.cartridges.flatfile.Binding;
import org.smooks.cartridges.flatfile.BindingType;
import org.smooks.io.sink.JavaSink;
import org.smooks.io.sink.StringSink;
import org.smooks.io.source.StreamSource;
import org.smooks.support.SmooksUtil;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author tfennelly
 */
@SuppressWarnings("unchecked")
public class CSVReaderTest {

    @Test
    public void test_04() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-04.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-03.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_05() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-05.xml"));

        ExecutionContext context = smooks.createExecutionContext("A");

        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-03.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result);

        context = smooks.createExecutionContext("B");

        result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-04.csv"), smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_06() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-06.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-03.csv"),
                smooks);
        assertEquals(
                "<customers><customer number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></customer><customer number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></customer></customers>",
                result);
    }

    @Test
    public void test_07() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,gender,age,country"));

        StringSink sink = new StringSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-01.csv")), sink);

        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                sink.getResult());
    }

    @Test
    public void test_08() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,gender,age,country")
                .setSeparatorChar('|').setQuoteChar('\'').setSkipLineCount(1).setRootElementName("customers")
                .setRecordElementName("customer"));

        StringSink sink = new StringSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-03.csv")), sink);

        assertEquals(
                "<customers><customer number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></customer><customer number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></customer></customers>",
                sink.getResult());
    }

    @Test
    public void test_09_1() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-07.xml"));

        JavaSink sink = new JavaSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);

        Person person = (Person) sink.getBean("person");
        assertEquals("(Linda, Coughlan, Ireland, Female, 22)", person.toString());
    }

    @Test
    public void test_09_2() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-08.xml"));

        JavaSink sink = new JavaSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);

        List<Person> people = (List<Person>) sink.getBean("people");
        assertEquals(
                "[(Tom, Fennelly, Ireland, Male, 4), (Mike, Fennelly, Ireland, Male, 2), (Linda, Coughlan, Ireland, Female, 22)]",
                people.toString());
    }

    @Test
    public void test_10() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("people", Person.class, BindingType.LIST)));

        JavaSink sink = new JavaSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);

        List<Person> people = (List<Person>) sink.getBean("people");
        assertEquals(
                "[(Tom, Fennelly, Ireland, Male, 4), (Mike, Fennelly, Ireland, Male, 2), (Linda, Coughlan, Ireland, Female, 22)]",
                people.toString());
    }

    @Test
    public void test_11() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("person", Person.class, BindingType.SINGLE)));

        JavaSink sink = new JavaSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);

        Person person = (Person) sink.getBean("person");
        assertEquals("(Linda, Coughlan, Ireland, Female, 22)", person.toString());
    }

    @Test
    public void test_12() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("people", HashMap.class, BindingType.LIST)));

        JavaSink sink = new JavaSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);

        List<Map> people = (List<Map>) sink.getBean("people");
        Map person;

        assertEquals(3, people.size());

        person = people.get(0);
        assertEquals("Tom", person.get("firstname"));
        assertEquals("Fennelly", person.get("lastname"));
        assertEquals("Male", person.get("gender"));
        assertEquals("4", person.get("age"));
        assertEquals("Ireland", person.get("country"));

        person = people.get(1);
        assertEquals("Mike", person.get("firstname"));
        assertEquals("Fennelly", person.get("lastname"));
        assertEquals("Male", person.get("gender"));
        assertEquals("2", person.get("age"));
        assertEquals("Ireland", person.get("country"));

        person = people.get(2);
        assertEquals("Linda", person.get("firstname"));
        assertEquals("Coughlan", person.get("lastname"));
        assertEquals("Female", person.get("gender"));
        assertEquals("22", person.get("age"));
        assertEquals("Ireland", person.get("country"));
    }


    @Test
    public void test_13_xml() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-09.xml"));
        test_13(smooks);
    }

    @Test
    public void test_13_programmatic() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("people", Person.class, BindingType.MAP).setKeyField("age")));

        test_13(smooks);
    }

    private void test_13(Smooks smooks) {
        JavaSink sink = new JavaSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);

        Map<Integer, Person> people = (Map<Integer, Person>) sink.getBean("people");
        Person person;

        person = people.get(4);
        assertEquals("(Tom, Fennelly, Ireland, Male, 4)", person.toString());
        person = people.get(2);
        assertEquals("(Mike, Fennelly, Ireland, Male, 2)", person.toString());
        person = people.get(22);
        assertEquals("(Linda, Coughlan, Ireland, Female, 22)", person.toString());
    }

    @Test
    public void test_14_xml() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-10.xml"));
        test_14(smooks);
    }

    @Test
    public void test_14_programmatic() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("people", HashMap.class, BindingType.MAP).setKeyField("firstname")));

        test_14(smooks);
    }

    private void test_14(Smooks smooks) {
        JavaSink sink = new JavaSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);

        Map<String, Map> people = (Map<String, Map>) sink.getBean("people");
        Map person;

        person = people.get("Tom");
        assertEquals("Tom", person.get("firstname"));
        assertEquals("Fennelly", person.get("lastname"));
        assertEquals("Male", person.get("gender"));
        assertEquals("4", person.get("age"));
        assertEquals("Ireland", person.get("country"));

        person = people.get("Mike");
        assertEquals("Mike", person.get("firstname"));
        assertEquals("Fennelly", person.get("lastname"));
        assertEquals("Male", person.get("gender"));
        assertEquals("2", person.get("age"));
        assertEquals("Ireland", person.get("country"));

        person = people.get("Linda");
        assertEquals("Linda", person.get("firstname"));
        assertEquals("Coughlan", person.get("lastname"));
        assertEquals("Female", person.get("gender"));
        assertEquals("22", person.get("age"));
        assertEquals("Ireland", person.get("country"));
    }

    @Test
    public void test_15() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-11.xml"));

        JavaSink sink = new JavaSink();
        try {
            smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);
            fail("Expected SmooksConfigurationException");
        } catch (SmooksConfigException e) {
            assertEquals("Invalid field name 'xxxx'.  Valid names: [firstname, lastname, gender, age, country].",
                    e.getMessage());
        }
    }

    @Test
    public void test_16() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-12.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-01.csv"),
                smooks);

        assertEquals(
                "<main-set><record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></record><record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></record></main-set>",
                result);
    }

    @Test
    public void test_17() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-13.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-13.csv"),
                smooks);

        assertEquals(
                "<main-set><record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></record><record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></record></main-set>",
                result);

        smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-12.xml"));

        context = smooks.createExecutionContext();
        result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-13.csv"), smooks);

        assertNotSame(
                "<main-set><record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></record><record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></record></main-set>",
                result);

    }

    @Test
    public void test_17_wildcard() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-13-wildcard.xml"));
        StringSink sink = new StringSink();

        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-13.csv")), sink);

        assertEquals(
                "<main-set><record number=\"1\"><field_0>Tom</field_0><field_1>Fennelly</field_1><field_2>Male</field_2><field_3>A</field_3><field_4>B</field_4><field_5>C</field_5><field_6>4</field_6><field_7>IR</field_7><field_8>Ireland</field_8><field_9>2</field_9><field_10>3</field_10></record><record number=\"2\"><field_0>Mike</field_0><field_1>Fennelly</field_1><field_2>Male</field_2><field_3>D</field_3><field_4>F</field_4><field_5>G</field_5><field_6>2</field_6><field_7>IR</field_7><field_8>Ireland</field_8><field_9>4</field_9></record></main-set>",
                sink.toString());
    }

    @Test
    public void test_18() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname?upper_case,lastname?uncap_first,$ignore$5")
                .setBinding(new Binding("people", HashMap.class, BindingType.LIST)));

        JavaSink sink = new JavaSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-05.csv")), sink);

        List<Map> people = (List<Map>) sink.getBean("people");
        Map person;

        person = people.get(0);
        assertEquals("TOM", person.get("firstname"));
        assertEquals("fennelly", person.get("lastname"));

        person = people.get(1);
        assertEquals("MIKE", person.get("firstname"));
        assertEquals("fennelly", person.get("lastname"));

        person = people.get(2);
        assertEquals("LINDA", person.get("firstname"));
        assertEquals("coughlan", person.get("lastname"));
    }

    @Test
    public void test_19() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-13.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-15.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Collins</lastname><address>Victoria Ave</address><age>32</age></csv-record><csv-record number=\"2\"><firstname>Fred</firstname><lastname>Cook</lastname><address>Mainstreet 12</address><age>40</age></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_20() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-14.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-15.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Collins</lastname><address>Victoria Ave</address><age>32</age></csv-record><csv-record number=\"2\"><firstname>Fred</firstname><lastname>Cook</lastname><address>Mainstreet 12</address><age>40</age></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_21() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-extended-config-15.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-16.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Collins</lastname><address>Victoria Ave</address><age>32</age></csv-record><csv-record number=\"2\"><firstname>Fred</firstname><lastname>Cook</lastname><address>Mainstreet 12</address><age>40</age></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_null_field_values() throws SmooksException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,gender,age,country"));

        StringSink sink = new StringSink();
        smooks.filterSource(new StreamSource<>(getClass().getResourceAsStream("/input-message-null-field-values.csv")), sink);

        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record><csv-record number=\"3\"><firstname>Joel</firstname><lastname>Pearson</lastname><gender>Male</gender><age></age><country>Australia</country></csv-record></csv-set>",
                sink.getResult());
    }
}
