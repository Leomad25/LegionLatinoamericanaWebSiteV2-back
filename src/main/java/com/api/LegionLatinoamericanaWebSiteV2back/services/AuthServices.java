package com.api.LegionLatinoamericanaWebSiteV2back.services;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.strings.GeneralResponsiveMessage;
import com.api.LegionLatinoamericanaWebSiteV2back.models.GeneralResponsiveModel;
import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.httpRequest.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    @Autowired
    private QueriesServices queriesServices;

    public void createNewUserAccount(GeneralResponsiveModel responsive, UserModel user) {
        if (isUserDataEmpty(responsive, user) || isUserExist(responsive, queriesServices.isUserExists(user.getUsername()))) return;
        createUser(responsive, queriesServices.createUser(user.getUsername(), user.getPassword(), user.getEmail()));
    }

    private boolean isUserDataEmpty(@NonNull GeneralResponsiveModel responsive, UserModel user) {
        if (
                user.getUsername() != null
                && user.getPassword() != null
                && user.getUsername().length() > 0
                && user.getPassword().length() > 0
        ) return false;
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(0);
        responsive.setMessage(GeneralResponsiveMessage.USER_DATA_IS_NULL);
        responsive.setData(null);
        return true;
    }

    private boolean isUserExist (@NonNull GeneralResponsiveModel responsive, long consult) {
        boolean userExist = consult > 0;
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(0);
        responsive.setMessage(GeneralResponsiveMessage.USER_DOES_NOT_EXIST);
        if (userExist) responsive.setMessage(GeneralResponsiveMessage.USER_ALREADY_EXISTS);
        responsive.setData(null);
        return userExist;
    }

    private void createUser(@NonNull GeneralResponsiveModel responsive, long consult) {
        if (consult > 0) {
            responsive.setStatus(HttpStatus.OK);
            responsive.setCode(1);
            responsive.setMessage(GeneralResponsiveMessage.USER_CREATED_SUCCESSFULLY);
            responsive.setData(null);
        }
    }
}
