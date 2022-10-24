package com.api.LegionLatinoamericanaWebSiteV2back.helpers.database;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager.FileManager;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager.FileManagerExceptions;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.strings.Constants;
import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.ConnectionPoolImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Properties;

@Component
public class DatabaseConf {

    private final String loggerHeader = ">> DatabaseConf:\n\t";

    private Environment env;
    private FileManager fileManager;
    private Logger logger = LoggerFactory.getLogger(DatabaseConf.class);
    private Properties databaseSecretProperties = null;
    private String fileSecretName;
    private ConnectionPoolImp connectionPool;

    @Autowired
    public DatabaseConf(Environment env, FileManager fileManager, ConnectionPoolImp connectionPool) {
        this.env = env;
        this.fileManager = fileManager;
        this.connectionPool = connectionPool;
    }

    public void createConnection() throws DatabaseConfExceptions {
        String[] profile = env.getActiveProfiles();
        if (profile.length > 0) {
            if (profile[0].equals("prod")) {
                logger.info(loggerHeader + "Was detected production environment.");
                fileSecretName = "database.prod.properties";
            } else {
                logger.info(loggerHeader + "Was detected a environment different to production.\n\tWas setting for default dev environment.");
                fileSecretName = "database.dev.properties";
            }
            try {
                if (fileManager.createFileSecret(fileSecretName)) {
                    logger.info(loggerHeader + "The file " + fileSecretName + " was created/founded successfully.");
                    databaseSecretProperties = fileManager.getFilePropertiesSecret(fileSecretName);
                    createPoolConnection(
                            getPropertyByKey("url"),
                            getPropertyByKey("username"),
                            getPropertyByKey("password"),
                            getPropertyByKey("initConnections"),
                            getPropertyByKey("maxConnections")
                    );
                } else
                    throw new DatabaseConfExceptions(DatabaseConfExceptions.DatabaseConfExceptionMessage.cantBeCreatedFileProperties);
            } catch (FileManagerExceptions e) {
                throw new RuntimeException(e);
            }
        } else
            throw new DatabaseConfExceptions(DatabaseConfExceptions.DatabaseConfExceptionMessage.cantReadTheEnvProfile);
    }

    private String getPropertyByKey(String key) {
        if (databaseSecretProperties != null) {
            String value = databaseSecretProperties.getProperty(key);
            try {
                if (value == null || value.length() == 0) {
                    databaseSecretProperties.setProperty(key, Constants.propertyFileDefaultValue);
                    fileManager.saveFilePropertiesSecret(databaseSecretProperties, fileSecretName);
                }
                return value;
            } catch (FileManagerExceptions e) {
                throw new RuntimeException(e);
            }
        }
        return Constants.propertyFileDefaultValue;
    }

    private void createPoolConnection(String url, String username, String password, String initConnectionsStr, String maxConnectionsStr) throws DatabaseConfExceptions {
        int initConnections = 0, maxConnections = 0;
        if (
                url.equals(Constants.propertyFileDefaultValue)
                || username.equals(Constants.propertyFileDefaultValue)
                || password.equals(Constants.propertyFileDefaultValue)
                || initConnectionsStr.equals(Constants.propertyFileDefaultValue)
                || maxConnectionsStr.equals(Constants.propertyFileDefaultValue)
        ) throw new DatabaseConfExceptions(DatabaseConfExceptions.DatabaseConfExceptionMessage.errorToCreateThePoolConnection_validateProperties);
        try {
            initConnections = Integer.parseInt(initConnectionsStr);
            maxConnections = Integer.parseInt(maxConnectionsStr);
        } catch (NumberFormatException ex) {
            logger.debug(ex.getMessage() + "\n" + ex.getStackTrace().toString());
            throw new DatabaseConfExceptions(DatabaseConfExceptions.DatabaseConfExceptionMessage.errorToCreateThePoolConnection_parsingValues);
        }
        if (maxConnections < initConnections) throw new DatabaseConfExceptions(DatabaseConfExceptions.DatabaseConfExceptionMessage.errorToCreateThePoolConnection_theMaxValueCantBeLowerToInitial);
        connectionPool.createConnectionPool(url, username, password, initConnections, maxConnections);
    }
}
