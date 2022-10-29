package com.api.LegionLatinoamericanaWebSiteV2back.helpers.security.jwt;

import com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager.FileManager;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.fileManager.FileManagerException;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.security.dto.SecureUserDTO;
import com.api.LegionLatinoamericanaWebSiteV2back.helpers.strings.Constants;
import com.api.LegionLatinoamericanaWebSiteV2back.services.QueriesServices;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Date;

@Component
public class JwtProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);
    private final FileManager fileManager;
    @Autowired
    private QueriesServices queriesServices;
    private Date issuedAt;
    private String secret;
    private long expiration;

    @Autowired
    public JwtProvider(FileManager fileManager, QueriesServices queriesServices) {
        this.fileManager = fileManager;
    }

    @PostConstruct
    public void postJwtProvider() throws FileManagerException {
        secret = fileManager.getPropertyByKey(Constants.PROPERTIES_JWT_NAME, "secret");
        issuedAt = new Date(0); //queriesServices.getCurrentDate();
        expiration = issuedAt.getTime() + Long.parseLong(fileManager.getPropertyByKey(Constants.PROPERTIES_JWT_NAME, "expirationTime")) * 1000;
    }

    public String generateToken(Authentication authentication) {
        SecureUserDTO secureUserDTO = (SecureUserDTO) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(secureUserDTO.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        String errMsg;
        String header = ">> JwtProvider\n\t";
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            errMsg = header + "Token is Malformed\n" + e;
            LOGGER.error(errMsg);
        } catch (UnsupportedJwtException e) {
            errMsg = header + "Token is Unsupported\n" + e;
            LOGGER.error(errMsg);
        } catch (ExpiredJwtException e) {
            errMsg = header + "Token expired\n" + e;
            LOGGER.error(errMsg);
        } catch (IllegalArgumentException e) {
            errMsg = header + "Token is empty\n" + e;
            LOGGER.error(errMsg);
        } catch (SignatureException e) {
            errMsg = header + "Signature error\n" + e;
            LOGGER.error(errMsg);
        }
        return false;
    }
}
