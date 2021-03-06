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
package org.datacleaner.widgets.database;

/**
 * {@link DatabaseConnectionPresenter} for MySQL database connections
 * {@link DatabaseConnectionPresenter}用于MySQL数据库连接
 */
public class MysqlDatabaseConnectionPresenter extends UrlTemplateDatabaseConnectionPresenter {

    public MysqlDatabaseConnectionPresenter() {
        super("jdbc:mysql://HOSTNAME:PORT/DATABASE?defaultFetchSize=" + Integer.MIN_VALUE
                + "&largeRowSizeThreshold=1024");
    }

    @Override
    protected String getJdbcUrl(final String hostname, final int port, final String database, final String param1,
            final String param2, final String param3, final String param4) {
        return "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?defaultFetchSize=" + Integer.MIN_VALUE
                + "&largeRowSizeThreshold=1024";
    }

    @Override
    protected int getDefaultPort() {
        return 3306;
    }

}
