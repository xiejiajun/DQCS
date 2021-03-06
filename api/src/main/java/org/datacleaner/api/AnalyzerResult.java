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
package org.datacleaner.api;

import java.io.Serializable;

/**
 * An {@link AnalyzerResult} represents the result of an {@link Analyzer}s
 * execution.
 * {@link AnalyzerResult}表示执行{@link Analyzer}的结果。
 *
 * It is advised that {@link AnalyzerResult} implementations expose a number of
 * result metrics. This is done by having getter methods annotated with the
 * {@link Metric} annotation.
 * 建议{@link AnalyzerResult}实现实现多个结果指标。
 * 这可以通过使用带有{@link Metric}批注的getter方法来完成。
 *
 * @see Metric
 */
public interface AnalyzerResult extends Renderable, Serializable {

}
