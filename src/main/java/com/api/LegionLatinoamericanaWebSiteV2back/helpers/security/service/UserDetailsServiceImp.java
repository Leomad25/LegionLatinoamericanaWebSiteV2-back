package com.api.LegionLatinoamericanaWebSiteV2back.helpers.security.service;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.security.dto.SecureUserDTO;
import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.dto.database.GetUserDTO;
import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.dto.database.GetUserPassDTO;
import com.api.LegionLatinoamericanaWebSiteV2back.services.QueriesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    private QueriesServices queriesServices;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GetUserDTO> userList = queriesServices.getUserByUserName(username);
        if (!userList.isEmpty()) {
            System.out.println("> userList: " + userList.get(0));
            List<GetUserPassDTO> passList = queriesServices.getUserPass(userList.get(0).getUserId());
            if (!passList.isEmpty()) {
                return SecureUserDTO.build(
                        userList.get(0),
                        passList.get(0),
                        queriesServices.getPermissions()
                );
            }
        }
        return null;
    }
}
