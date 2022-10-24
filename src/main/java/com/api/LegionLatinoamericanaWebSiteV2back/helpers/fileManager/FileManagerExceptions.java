package com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager;

public class FileManagerExceptions extends Exception {

    public FileManagerExceptions(String message) {
        super(message);
    }

    public static class FileManagerExceptionMessage {
        public static final String
            cantBeCreatedTheSecretFile = "Can't be created the secret file.",
            cantBeReadThePropertiesSecretFile = "can't be read the properties secret file.",
            cantBeSaveThePropertiesSecretFile = "can't be save the properties secret file.";
    }
}
