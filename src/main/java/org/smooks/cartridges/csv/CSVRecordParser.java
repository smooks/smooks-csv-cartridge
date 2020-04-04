/*
 * Milyn - Copyright (C) 2006 - 2010
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (version 2.1) as published by the Free Software
 * Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details:
 * http://www.gnu.org/licenses/lgpl.txt
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
