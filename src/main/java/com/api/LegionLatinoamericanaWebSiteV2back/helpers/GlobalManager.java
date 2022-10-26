package com.api.LegionLatinoamericanaWebSiteV2back.helpers;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.database.DatabaseConf;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.database.DatabaseConfException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalManager {
    @Autowired
    public GlobalManager(DatabaseConf databaseConf) throws GlobalManagerException {
        try {
            databaseConf.createConnection();
        } catch (DatabaseConfException e) {
            throw new GlobalManagerException(e.getMessage());
        }
    }
}
