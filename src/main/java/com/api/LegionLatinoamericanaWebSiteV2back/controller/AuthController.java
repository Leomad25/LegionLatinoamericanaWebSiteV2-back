package com.api.LegionLatinoamericanaWebSiteV2back.controller;

import com.api.LegionLatinoamericanaWebSiteV2back.models.GeneralResponsiveModel;
import com.api.LegionLatinoamericanaWebSiteV2back.services.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServices services;

    @PostMapping("/register")
    public GeneralResponsiveModel createAccount() {
        GeneralResponsiveModel responsive = new GeneralResponsiveModel();
        return responsive;
    }
}
