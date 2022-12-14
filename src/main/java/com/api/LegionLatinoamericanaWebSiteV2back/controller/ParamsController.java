package com.api.LegionLatinoamericanaWebSiteV2back.controller;

import com.api.LegionLatinoamericanaWebSiteV2back.models.GeneralResponsiveModel;
import com.api.LegionLatinoamericanaWebSiteV2back.services.ParamsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/params")
public class ParamsController {

    @Autowired
    private ParamsServices services;

    @GetMapping()
    public GeneralResponsiveModel getParameter(@RequestParam("key") String key) {
        GeneralResponsiveModel responsive = new GeneralResponsiveModel();
        services.getValueParam(responsive, key);
        return responsive;
    }
}
