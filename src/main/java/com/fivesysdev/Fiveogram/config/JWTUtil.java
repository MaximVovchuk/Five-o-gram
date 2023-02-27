package com.fivesysdev.Fiveogram.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fivesysdev.Fiveogram.roles.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String username, Collection<Role> roles) {
//        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(1).toInstant());
        String role = roles.toArray()[0].toString();
        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withClaim("role",role)
                .withIssuedAt(new Date())
                .withIssuer("Five-o-gram")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String getUsername(String token) {
        String jwt = token.substring(7).trim();
        return validateTokenAndRetrieveUsername(jwt);
    }

    public String validateTokenAndRetrieveUsername(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("Five-o-gram")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}