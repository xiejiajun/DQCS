/**
 * DataCleaner (community edition)
 * Copyright (C) 2014 Free Software Foundation, Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.datacleaner.data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.metamodel.DataContext;
import org.apache.metamodel.MetaModelException;
import org.apache.metamodel.QueryPostprocessDataContext;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.EmptyDataSet;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.query.SelectItem;
import org.apache.metamodel.schema.AbstractSchema;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;
import org.datacleaner.api.OutputDataStream;

/**
 * A virtual {@link DataContext} that represents/wraps a
 * {@link OutputDataStream}.
 * 表示/包装{@link OutputDataStream}的虚拟{@link DataContext}。
 */
public class OutputDataStreamDataContext extends QueryPostprocessDataContext {

    private final OutputDataStream _outputDataStream;

    public OutputDataStreamDataContext(final OutputDataStream outputDataStream) {
        super(false);
        _outputDataStream = outputDataStream;
    }

    @Override
    protected Schema getMainSchema() throws MetaModelException {
        return new AbstractSchema() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getQuote() {
                return null;
            }

            @Override
            public List<Table> getTables() {
                return Collections.singletonList(_outputDataStream.getTable());
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    @Override
    protected String getMainSchemaName() throws MetaModelException {
        return _outputDataStream.getName();
    }

    @Override
    protected DataSet materializeMainSchemaTable(final Table table, final List<Column> columns, final int maxRows) {
        final  List<SelectItem> selectItems = columns.stream().map(c -> new SelectItem(c)).collect(Collectors.toList());
        return new EmptyDataSet(selectItems);
    }

    @Override
    protected Number executeCountQuery(final Table table, final List<FilterItem> whereItems,
            final boolean functionApproximationAllowed) {
        return -1;
    }
}
