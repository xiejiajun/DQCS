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
package org.datacleaner.job.output;

import org.apache.metamodel.query.Query;
import org.datacleaner.api.HasOutputDataStreams;
import org.datacleaner.api.OutputDataStream;

/**
 * Convenience/utility methods related to handling and building
 * {@link OutputDataStream} objects.
 * 与处理和构建{@link OutputDataStream}对象有关的便利/实用方法。
 */
public class OutputDataStreams {

    private OutputDataStreams() {
        // prevent instantiation
    }

    /**
     * Creates an {@link OutputDataStreamBuilder} for push-based dispatching of
     * records (meaning that the {@link HasOutputDataStreams} component will
     * push records to the output data stream without supporting {@link Query}
     * optimization).
     *
     * @param name
     * @return
     */
    public static OutputDataStreamBuilder pushDataStream(final String name) {
        return new OutputDataStreamBuilderImpl(name);
    }
}
