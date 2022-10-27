package com.api.LegionLatinoamericanaWebSiteV2back.helpers.security;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager.FileManager;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager.FileManagerException;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.strings.Constants;
import com.api.LegionLatinoamericanaWebSiteV2back.services.QueriesServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JWTUtils {
    private final FileManager fileManager;
    private final QueriesServices queriesServices;
    private final Logger logger = LoggerFactory.getLogger(JWTUtils.class);
    public static final String SECRET_TAG = "secret";
    private static final String EXPIRATION_TIME_TAG = "expirationTime";
    public String secret;
    public long expirationTime;

    @Autowired
    public JWTUtils(FileManager fileManager, QueriesServices queriesServices) {
        this.fileManager = fileManager;
        this.queriesServices = queriesServices;
    }

    @PostConstruct
    public void postJWTUtils() throws FileManagerException, SecurityGeneralException {
        String errMsg;
        String loggerHeader = ">> JWTUtils: \n\t";
        try {
            if (fileManager.createFileSecret(Constants.PROPERTIES_JWT_NAME)) {
                errMsg = loggerHeader + "The file " + Constants.PROPERTIES_JWT_NAME + " was created/founded successfully.";
                logger.info(errMsg);
                validateDataFromProperties(
                        fileManager.getPropertyByKey(Constants.PROPERTIES_JWT_NAME, SECRET_TAG),
                        fileManager.getPropertyByKey(Constants.PROPERTIES_JWT_NAME, EXPIRATION_TIME_TAG),
                        false
                );
            }
        } catch (FileManagerException e) {
            throw new FileManagerException(e.getMessage());
        } catch (SecurityGeneralException e) {
            throw new SecurityGeneralException(e.getMessage());
        }
    }

    private void validateDataFromProperties(String secret, String expirationTime, boolean secondTry) throws SecurityGeneralException, FileManagerException {
        if (
                secret.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE) ||
                expirationTime.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE)
        ) {
            if (!secondTry) {
                fileManager.setPropertyByKey(Constants.PROPERTIES_JWT_NAME, SECRET_TAG, Constants.getRandomUUID());
                fileManager.setPropertyByKey(Constants.PROPERTIES_JWT_NAME, EXPIRATION_TIME_TAG, "604800"); // Set default 7 days like util life.
                validateDataFromProperties(
                        fileManager.getPropertyByKey(Constants.PROPERTIES_JWT_NAME, SECRET_TAG),
                        fileManager.getPropertyByKey(Constants.PROPERTIES_JWT_NAME, EXPIRATION_TIME_TAG),
                        true
                );
            } else throw new SecurityGeneralException(SecurityGeneralException.SecurityGeneralExceptionMessage.ERROR_TO_VALIDATE_PROPERTIES);
        } else {
            try {
                long expirationTimeToLong = Long.parseLong(expirationTime);
                loadDataFromProperties(secret, expirationTimeToLong);
            } catch (NumberFormatException | NullPointerException ex) {
                throw new SecurityGeneralException(SecurityGeneralException.SecurityGeneralExceptionMessage.ERROR_TO_PARSING_VALUES);
            }
        }
    }

    private void loadDataFromProperties(String secret, long expirationTime) throws NumberFormatException, NullPointerException {
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    public String createToken() {
        return "";
    }
}
