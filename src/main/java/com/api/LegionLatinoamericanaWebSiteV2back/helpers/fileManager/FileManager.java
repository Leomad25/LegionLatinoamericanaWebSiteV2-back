package com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.strings.Constants;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class FileManager {
    public boolean createFileSecret(String fileName) throws FileManagerException {
        File folder = new File(Constants.FOLDER_SECRET_PATH);
        if (!folder.exists()) folder.mkdir();
        File file = new File(folder.getAbsolutePath() + File.separator + fileName);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException ex) {
                throw new FileManagerException(FileManagerException.FileManagerExceptionMessage.CANT_BE_CREATED_THE_SECRET_FILE);
            }
        } else {
            return true;
        }
    }

    public Properties getFilePropertiesSecret(String fileName) throws FileManagerException {
        Properties properties = new Properties();
        File folder = new File(Constants.FOLDER_SECRET_PATH);
        try (InputStream inputStream = new FileInputStream(folder.getAbsolutePath() + File.separator + fileName)) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw new FileManagerException(FileManagerException.FileManagerExceptionMessage.CANT_BE_READ_THE_PROPERTIES_SECRET_FILE);
        }
        return properties;
    }

    public void saveFilePropertiesSecret(Properties properties, String fileName) throws FileManagerException {
        File folder = new File(Constants.FOLDER_SECRET_PATH);
        try (OutputStream outputStream = new FileOutputStream(folder.getAbsolutePath() + File.separator + fileName)) {
            properties.store(outputStream, null);
        } catch (IOException ex) {
            throw new FileManagerException(FileManagerException.FileManagerExceptionMessage.CANT_BE_SAVE_THE_PROPERTIES_SECRET_FILE);
        }
    }
}
