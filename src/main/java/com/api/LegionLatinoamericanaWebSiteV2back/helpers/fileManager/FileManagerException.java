package com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager;

public class FileManagerException extends Exception {

    public FileManagerException(String message) {
        super(message);
    }

    public static class FileManagerExceptionMessage {
        private FileManagerExceptionMessage() {
            throw new IllegalStateException("FileManagerExceptionMessage - Utility class");
        }

        public static final String CANT_BE_CREATED_THE_SECRET_FILE = "Can't be created the secret file.";
        public static final String CANT_BE_READ_THE_PROPERTIES_SECRET_FILE = "can't be read the properties secret file.";
        public static final String CANT_BE_SAVE_THE_PROPERTIES_SECRET_FILE = "can't be save the properties secret file.";
    }
}
