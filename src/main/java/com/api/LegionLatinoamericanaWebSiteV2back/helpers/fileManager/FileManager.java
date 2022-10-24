package com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.strings.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class FileManager {
    private final String loggerHeader = ">> DatabaseConf:\n\t";
    private Logger logger = LoggerFactory.getLogger(FileManager.class);

    public boolean createFileSecret(String fileName) throws FileManagerExceptions {
        File folder = new File(Constants.folderSecretPath);
        if (!folder.exists()) folder.mkdir();
        File file = new File(folder.getAbsolutePath() + File.separator + fileName);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException ex) {
                logger.error(loggerHeader + ex.getStackTrace());
                throw new FileManagerExceptions(FileManagerExceptions.FileManagerExceptionMessage.cantBeCreatedTheSecretFile);
            }
        } else {
            return true;
        }
    }

    public Properties getFilePropertiesSecret(String fileName) throws FileManagerExceptions {
        Properties properties = new Properties();
        File folder = new File(Constants.folderSecretPath);
        try (InputStream inputStream = new FileInputStream(folder.getAbsolutePath() + File.separator + fileName)) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw new FileManagerExceptions(FileManagerExceptions.FileManagerExceptionMessage.cantBeReadThePropertiesSecretFile);
        }
        return properties;
    }

    public void saveFilePropertiesSecret(Properties properties, String fileName) throws FileManagerExceptions {
        File folder = new File(Constants.folderSecretPath);
        try (OutputStream outputStream = new FileOutputStream(folder.getAbsolutePath() + File.separator + fileName)) {
            properties.store(outputStream, null);
        } catch (IOException ex) {
            throw new FileManagerExceptions(FileManagerExceptions.FileManagerExceptionMessage.cantBeSaveThePropertiesSecretFile);
        }

    }
}
