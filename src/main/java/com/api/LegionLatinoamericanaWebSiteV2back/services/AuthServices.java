package com.api.LegionLatinoamericanaWebSiteV2back.services;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.security.dto.JwtDTO;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.security.jwt.JwtProvider;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.strings.GeneralResponsiveMessage;
import com.api.LegionLatinoamericanaWebSiteV2back.models.GeneralResponsiveModel;
import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.httpRequest.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    @Autowired
    private QueriesServices queriesServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    public void createNewUserAccount(GeneralResponsiveModel responsive, UserModel.UserRegisterModel user) {
        if (isUserRegisterDataEmpty(responsive, user) || isUserExist(responsive, queriesServices.isUserExists(user.getUsername()))) return;
        createUser(responsive, queriesServices.createUser(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getEmail()));
    }

    private boolean isUserRegisterDataEmpty(@NonNull GeneralResponsiveModel responsive, UserModel.UserRegisterModel user) {
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

    public void loginUserAccount(GeneralResponsiveModel responsive, UserModel.UserLoginModel user) {
        if (isUserLoginDataEmpty(responsive, user) || !isUserExist(responsive, queriesServices.isUserExists(user.getUsername()))) return;
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDTO jwtDTO = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        // Http response
        responsive.setStatus(HttpStatus.OK);
        responsive.setCode(1);
        responsive.setMessage(GeneralResponsiveMessage.USER_LOGIN_SUCCESSFULLY);
        responsive.setData(jwtDTO);
    }

    private boolean isUserLoginDataEmpty(@NonNull GeneralResponsiveModel responsive, UserModel.UserLoginModel user) {
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
}
