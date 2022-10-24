package com.api.LegionLatinoamericanaWebSiteV2back.services;

import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.ConnectionPoolImp;
import com.api.LegionLatinoamericanaWebSiteV2back.models.interfaces.ConnectionPoolInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    private ConnectionPoolInt connectionPool;

    @Autowired
    public AuthServices(ConnectionPoolImp connectionPool) {
        this.connectionPool = connectionPool;
    }
}
