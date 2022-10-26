package com.api.LegionLatinoamericanaWebSiteV2back.helpers.database;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager.FileManager;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager.FileManagerException;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.strings.Constants;
import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.ConnectionPoolImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class DatabaseConf {
    private final Environment env;
    private final FileManager fileManager;
    private final Logger logger = LoggerFactory.getLogger(DatabaseConf.class);
    private final ConnectionPoolImp connectionPool;
    private Properties databaseSecretProperties = null;
    private String fileSecretName;

    @Autowired
    public DatabaseConf(Environment env, FileManager fileManager, ConnectionPoolImp connectionPool) {
        this.env = env;
        this.fileManager = fileManager;
        this.connectionPool = connectionPool;
    }

    public void createConnection() throws DatabaseConfException {
        String errMsg;
        String[] profile = env.getActiveProfiles();
        if (profile.length > 0) {
            String loggerHeader = ">> DatabaseConf:\n\t";
            if (profile[0].equals("prod")) {
                errMsg = loggerHeader + "Was detected production environment.";
                logger.info(errMsg);
                fileSecretName = "database.prod.properties";
            } else {
                errMsg = loggerHeader + "Was detected a environment different to production.\n\tWas setting for default dev environment.";
                logger.info(errMsg);
                fileSecretName = "database.dev.properties";
            }
            try {
                if (fileManager.createFileSecret(fileSecretName)) {
                    errMsg = loggerHeader + "The file " + fileSecretName + " was created/founded successfully.";
                    logger.info(errMsg);
                    databaseSecretProperties = fileManager.getFilePropertiesSecret(fileSecretName);
                    createPoolConnection(
                            getPropertyByKey("url"),
                            getPropertyByKey("username"),
                            getPropertyByKey("password"),
                            getPropertyByKey("initConnections"),
                            getPropertyByKey("maxConnections")
                    );
                } else
                    throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.CANT_BE_CREATED_FILE_PROFILE);
            } catch (FileManagerException e) {
                throw new DatabaseConfException(e.getMessage());
            }
        } else
            throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.CANT_READ_THE_ENV_PROFILE);
    }

    private String getPropertyByKey(String key) throws DatabaseConfException {
        if (databaseSecretProperties != null) {
            String value = databaseSecretProperties.getProperty(key);
            try {
                if (value == null || value.length() == 0) {
                    databaseSecretProperties.setProperty(key, Constants.PROPERTY_FILE_DEFAULT_VALUE);
                    fileManager.saveFilePropertiesSecret(databaseSecretProperties, fileSecretName);
                    return Constants.PROPERTY_FILE_DEFAULT_VALUE;
                }
                return value;
            } catch (FileManagerException e) {
                throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.CANT_BE_READ_THE_PROPERTY);
            }
        }
        return Constants.PROPERTY_FILE_DEFAULT_VALUE;
    }

    private void createPoolConnection(String url, String username, String password, String initConnectionsStr, String maxConnectionsStr) throws DatabaseConfException {
        int initConnections = 0;
        int maxConnections = 0;
        if (
                url.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE)
                || username.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE)
                || password.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE)
                || initConnectionsStr.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE)
                || maxConnectionsStr.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE)
        ) throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.ERROR_TO_CREATE_THE_POOL_CONNECTION_VALIDATE_PROPERTIES);
        try {
            initConnections = Integer.parseInt(initConnectionsStr);
            maxConnections = Integer.parseInt(maxConnectionsStr);
        } catch (NumberFormatException ex) {
            throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.ERROR_TO_CREATE_THE_POOL_CONNECTION_PARSING_VALUES);
        }
        if (maxConnections < initConnections) throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.ERROR_TO_CREATE_THE_POOL_CONNECTION_PARSING_VALUES);
        connectionPool.createConnectionPool(url, username, password, initConnections, maxConnections);
    }
}
