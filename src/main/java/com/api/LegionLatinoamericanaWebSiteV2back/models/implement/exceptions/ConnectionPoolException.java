package com.api.LegionLatinoamericanaWebSiteV2back.models.implement.exceptions;

public class ConnectionPoolException extends Exception {
    public ConnectionPoolException(String message) {
        super(message);
    }

    public static class ConnectionPoolExceptionMessage {
        public final static String
            errorToCreatingTheConnectionForThePool = "Was throw a error to creating a new connection for the pool.";
    }
}
