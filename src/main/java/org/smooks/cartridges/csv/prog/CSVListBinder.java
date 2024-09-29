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

import org.smooks.Smooks;
import org.smooks.assertion.AssertArgument;
import org.smooks.cartridges.csv.CSVRecordParserConfigurator;
import org.smooks.cartridges.flatfile.Binding;
import org.smooks.cartridges.flatfile.BindingType;
import org.smooks.io.sink.JavaSink;
import org.smooks.io.source.ReaderSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.UUID;

/**
 * CSV {@link List} Binder class.
 * <p/>
 * Simple CSV to Object {@link List} binding class.
 * <p/>
 * Exmaple usage:
 * <pre>
 * public class PeopleBinder {
 *     // Create and cache the binder instance..
 *     private CSVListBinder binder = new CSVListBinder("firstname,lastname,gender,age,country", Person.class);
 *
 *     public List&lt;Person&gt; bind(Reader csvStream) {
 *         return binder.bind(csvStream);
 *     }
 * }
 * </pre>
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class CSVListBinder {

    private String beanId = UUID.randomUUID().toString();
    private Smooks smooks;

    public CSVListBinder(String fields, Class recordType) {
        AssertArgument.isNotNullAndNotEmpty(fields, "fields");
        AssertArgument.isNotNull(recordType, "recordType");

        smooks = new Smooks();
        smooks.setReaderConfig(new CSVRecordParserConfigurator(fields)
                .setBinding(new Binding(beanId, recordType, BindingType.LIST)));
    }

    public List bind(Reader csvStream) {
        AssertArgument.isNotNull(csvStream, "csvStream");

        JavaSink javaSink = new JavaSink();

        smooks.filterSource(new ReaderSource<>(csvStream), javaSink);

        return (List) javaSink.getBean(beanId);
    }

    public List bind(InputStream csvStream) {
        return bind(new InputStreamReader(csvStream));
    }
}
