package com.api.LegionLatinoamericanaWebSiteV2back.models.interfaces;

import java.sql.Connection;

public interface ConnectionPoolInt {
    void createConnectionPool(String url, String username, String password, int initConnections, int maxConnections);
    Connection getConnection();
    boolean releaseConnection(Connection connection);
    int getSize();
}
