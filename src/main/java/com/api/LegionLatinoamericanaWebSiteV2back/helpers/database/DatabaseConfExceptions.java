package com.api.LegionLatinoamericanaWebSiteV2back.helpers.database;

public class DatabaseConfExceptions extends Exception {

    public DatabaseConfExceptions(String message) {
        super(message);
    }

    public static class DatabaseConfExceptionMessage {
        public static final String
            cantReadTheEnvProfile = "Can't be read the active profile.",
            cantBeCreatedFileProperties = "Can't be created file properties database.",
            errorToCreateThePoolConnection = "Was generate a error when try to create the pool connection.",
            errorToCreateThePoolConnection_validateProperties = "The database properties file is not change initials values.",
            errorToCreateThePoolConnection_parsingValues = "Was generate a error to parse the values from database properties file.",
            errorToCreateThePoolConnection_theMaxValueCantBeLowerToInitial = "Was detected that value \"maxConnections\" is lower to \"initConnections\" on database file properties.";
    }
}
