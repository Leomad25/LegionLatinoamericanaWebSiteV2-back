package com.api.LegionLatinoamericanaWebSiteV2back.helpers.security;

public class SecurityGeneralException extends Exception {
    public SecurityGeneralException(String message) {
        super(message);
    }

    public static class SecurityGeneralExceptionMessage {
        private SecurityGeneralExceptionMessage() {
            throw new IllegalStateException("SecurityGeneralExceptionMessage - Utility class");
        }

        public static final String ERROR_TO_VALIDATE_PROPERTIES = "You have change the initial value of JWT properties file on secret folder.";
        public static final String ERROR_TO_PARSING_VALUES = "Was generate a error to parse the values from JWT properties file.";
    }
}
