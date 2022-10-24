package com.api.LegionLatinoamericanaWebSiteV2back.helpers;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.database.DatabaseConf;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.database.DatabaseConfExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalManager {
    @Autowired
    public GlobalManager(DatabaseConf databaseConf) {
        try {
            databaseConf.createConnection();
        } catch (DatabaseConfExceptions e) {
            throw new RuntimeException(e);
        }
    }
}
