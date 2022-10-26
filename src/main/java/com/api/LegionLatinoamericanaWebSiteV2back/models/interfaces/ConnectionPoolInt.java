package com.api.LegionLatinoamericanaWebSiteV2back.models.interfaces;

import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.exceptions.ConnectionPoolException;

import java.sql.Connection;

public interface ConnectionPoolInt {
    void createConnectionPool(String url, String username, String password, int initConnections, int maxConnections);
    Connection getConnection() throws ConnectionPoolException;
    boolean releaseConnection(Connection connection);
    int getSize();
}
