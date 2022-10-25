package com.api.LegionLatinoamericanaWebSiteV2back.services;

import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.ConnectionPoolImp;
import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.database.GetUserIdByName;
import com.api.LegionLatinoamericanaWebSiteV2back.models.interfaces.ConnectionPoolInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class QueriesServices {

    private final ConnectionPoolImp connectionPool;
    private final Logger logger = LoggerFactory.getLogger(QueriesServices.class);
    private final String loggerHeader = ">> QueriesServices:\n\t";

    @Autowired
    public QueriesServices(ConnectionPoolImp connectionPool) {
        this.connectionPool = connectionPool;
    }

    public long isUserExists(String username) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String errMsg;
        long userExits = -1;
        try {
            conn = connectionPool.getConnection();
            preparedStatement = conn.prepareStatement("CALL legion_latinoamericana_db.IS_USER_EXISTS(?);");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) userExits = resultSet.getInt("RESULT");
        } catch (SQLException e) {
            errMsg = loggerHeader + e;
            logger.error(errMsg);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                errMsg = loggerHeader + e;
                logger.error(errMsg);
            }
            connectionPool.releaseConnection(conn);
        }
        return userExits;
    }

    public long createUser(String username, String password, String email) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String errMsg;
        long userId = -1;
        try {
            conn = connectionPool.getConnection();
            preparedStatement = conn.prepareStatement("CALL legion_latinoamericana_db.CREATE_USER(?, ?, ?);");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) userId = resultSet.getLong("USER_ID");
        } catch (SQLException e) {
            errMsg = loggerHeader + e;
            logger.error(errMsg);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                errMsg = loggerHeader + e;
                logger.error(errMsg);
            }
            connectionPool.releaseConnection(conn);
        }
        return userId;
    }
}
