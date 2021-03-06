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

/**
 * Interface for components whose distribution property (normally set via
 * {@link Distributed}) is dynamically adjusted depending on its configuration.
 * 组件的接口，其分配属性（通常通过{@link Distributed}设置）根据其配置动态调整。
 */
public interface HasDistributionAdvice {

    /**
     * Determines if the component is distributable with its current
     * configuration.
     *
     * @return
     */
    boolean isDistributable();
}
