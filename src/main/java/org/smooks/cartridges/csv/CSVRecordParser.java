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

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.smooks.cartridges.flatfile.variablefield.VariableFieldRecordParser;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * CSV record parser.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class CSVRecordParser<T extends CSVRecordParserFactory> extends VariableFieldRecordParser<T> {

    private com.opencsv.CSVReader csvLineReader;

    /**
     * {@inheritDoc}
     */
    public void setDataSource(InputSource source) {
        Reader reader = source.getCharacterStream();

        if (reader == null) {
            throw new IllegalStateException(
                    "Invalid InputSource type supplied to CSVRecordParser.  Must contain a Reader instance.");
        }

        // Create the CSV line reader...
        T factory = getFactory();
        csvLineReader = new CSVReaderBuilder(reader).withCSVParser(new CSVParserBuilder().withSeparator(factory.getSeparator()).withQuoteChar(factory.getQuoteChar()).withEscapeChar(factory.getEscapeChar()).build()).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> nextRecordFieldValues() throws IOException {
        String[] csvRecord;
        try {
            csvRecord = csvLineReader.readNext();
        } catch (CsvValidationException e) {
            throw new IOException(e);
        }

        if (csvRecord == null) {
            return null;
        }

        return Arrays.asList(csvRecord);
    }

    @Override
    protected void validateHeader(List<String> headers) {
        // For backward compatibility with pre v1.5....
        try {
            super.validateHeader(headers);
        } catch (IOException e) {
            throw new CSVHeaderValidationException(getFactory().getRecordMetaData().getFieldNames(), headers);
        }
    }
}
