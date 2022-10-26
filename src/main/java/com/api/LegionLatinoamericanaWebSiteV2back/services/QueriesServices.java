package com.api.LegionLatinoamericanaWebSiteV2back.services;

import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.ConnectionPoolImp;
import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.exceptions.ConnectionPoolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class QueriesServices {

    private final ConnectionPoolImp connectionPool;
    private final Logger logger = LoggerFactory.getLogger(QueriesServices.class);

    @Autowired
    public QueriesServices(ConnectionPoolImp connectionPool) {
        this.connectionPool = connectionPool;
    }

    public String getGlobalParam(String key) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String rtn = null;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return null;
            preparedStatement = conn.prepareStatement("CALL legion_latinoamericana_db.GET_PARAM(?);");
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) rtn = resultSet.getString("PARAM_VALUE");
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return rtn;
    }

    public long isUserExists(String username) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        long userExits = -1;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return userExits;
            preparedStatement = conn.prepareStatement("CALL legion_latinoamericana_db.IS_USER_EXISTS(?);");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) userExits = resultSet.getInt("RESULT");
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return userExits;
    }

    public long createUser(String username, String password, String email) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        long userId = -1;
        try {
            conn = connectionPool.getConnection();
            if (conn == null) return userId;
            preparedStatement = conn.prepareStatement("CALL legion_latinoamericana_db.CREATE_USER(?, ?, ?);");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) userId = resultSet.getLong("USER_ID");
        } catch (SQLException | ConnectionPoolException e) {
            printError(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                printError(e);
            }
            connectionPool.releaseConnection(conn);
        }
        return userId;
    }

    private void printError(Exception ex) {
        String errMsg = ">> QueriesServices:\n" + ex;
        logger.error(errMsg);
    }
}
