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

import java.util.List;

import org.smooks.assertion.AssertArgument;
import org.smooks.cdr.ResourceConfig;
import org.smooks.cartridges.flatfile.variablefield.VariableFieldRecordParserConfigurator;

/**
 * CSV Record Parser configurator.
 * <p/>
 * Supports programmatic configuration of {@link CSVRecordParserFactory} and
 * {@link CSVRecordParser}.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class CSVRecordParserConfigurator extends VariableFieldRecordParserConfigurator {

    private String csvFields;
    private char separatorChar = ',';
    private char quoteChar = '"';
	private char escapeChar = '\\';
    private int skipLineCount = 0;
    private String rootElementName = "csv-set";
    private String recordElementName = "csv-record";

    public CSVRecordParserConfigurator(String csvFields) {
        super(CSVRecordParserFactory.class);
        AssertArgument.isNotNullAndNotEmpty(csvFields, "csvFields");
        this.csvFields = csvFields;
    }

    public CSVRecordParserConfigurator setSeparatorChar(char separatorChar) {
        AssertArgument.isNotNull(separatorChar, "separatorChar");
        this.separatorChar = separatorChar;
        return this;
    }

    public CSVRecordParserConfigurator setQuoteChar(char quoteChar) {
        AssertArgument.isNotNull(quoteChar, "quoteChar");
        this.quoteChar = quoteChar;
        return this;
    }
	
	public CSVRecordParserConfigurator setEscapeChar(char escapeChar) {
        AssertArgument.isNotNull(escapeChar, "escapeChar");
        this.escapeChar = escapeChar;
        return this;
    }

    public CSVRecordParserConfigurator setSkipLineCount(int skipLineCount) {
        AssertArgument.isNotNull(skipLineCount, "skipLineCount");
        this.skipLineCount = skipLineCount;
        return this;
    }

    public CSVRecordParserConfigurator setRootElementName(String csvRootElementName) {
        AssertArgument.isNotNullAndNotEmpty(csvRootElementName, "rootElementName");
        this.rootElementName = csvRootElementName;
        return this;
    }

    public CSVRecordParserConfigurator setRecordElementName(String csvRecordElementName) {
        AssertArgument.isNotNullAndNotEmpty(csvRecordElementName, "recordElementName");
        this.recordElementName = csvRecordElementName;
        return this;
    }

    public List<ResourceConfig> toConfig() {
        getParameters().setProperty("fields", csvFields);
        getParameters().setProperty("separator", Character.toString(separatorChar));
        getParameters().setProperty("quote-char", Character.toString(quoteChar));
		getParameters().setProperty("escape-char", Character.toString(escapeChar));
        getParameters().setProperty("skip-line-count", Integer.toString(skipLineCount));
        getParameters().setProperty("rootElementName", rootElementName);
        getParameters().setProperty("recordElementName", recordElementName);

        return super.toConfig();
    }
}
