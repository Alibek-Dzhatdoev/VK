package com.dzhatdoev.vk.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.util.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;

    public String generateToken (String username) {
         Date expiration = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

         return JWT.create().withSubject("User details").withClaim("username", username)
                 .withIssuedAt(new Date()).withIssuer("VKapp").withExpiresAt(expiration).sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim (String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("VKapp")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
