package com.api.LegionLatinoamericanaWebSiteV2back.models.implement;

import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.exceptions.ConnectionPoolException;
import com.api.LegionLatinoamericanaWebSiteV2back.models.interfaces.ConnectionPoolInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConnectionPoolImp implements ConnectionPoolInt {

    private String url;
    private String username;
    private String password;
    private int initConnections;
    private int maxConnections;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(ConnectionPoolImp.class);
    private final String loggerHeader = ">> Connection Pool Imp:\n\t";

    @Override
    public void createConnectionPool(String url, String username, String password, int initConnections, int maxConnections) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.initConnections = initConnections;
        this.maxConnections = maxConnections;

        connectionPool = new ArrayList<>(initConnections);
        for (int i = 0; i < initConnections; i++) {
            try {
                connectionPool.add(createConnection());
            } catch (ConnectionPoolException e) {
                printError(e);
            }
        }

        logger.info(loggerHeader + "Was created the pool connection successfully. Num Connections: " + getSize());
    }

    private Connection createConnection() throws ConnectionPoolException {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            printError(ex);
            throw new ConnectionPoolException(ConnectionPoolException.ConnectionPoolExceptionMessage.errorToCreatingTheConnectionForThePool);
        }
    }

    @Override
    public Connection getConnection() {
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    @Override
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    private void printError(Exception ex) {
        logger.error(loggerHeader + ex);
    }
}
