package com.example.user.helpers;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJWT(String userId) {

        // Create claims for the JWT token
        JwtClaimsSet claims = JwtClaimsSet.builder()// Issuer of the token// The time the token was issued
                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS)) // Expiry time of the token
                .claim("sub", userId) // You can add custom claims if necessary
                .build();

        System.out.println(claims.getClaims());
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();

        // Define the secret key for signing the JWT token
        SecretKey key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(key);

        // Initialize the JWT encoder with the signing key
        JwtEncoder encoder = new NimbusJwtEncoder(jwkSource);

        // Create and return the encoded JWT token
        Jwt jwt = encoder.encode(JwtEncoderParameters.from(jwsHeader, claims));

        return jwt.getTokenValue();
    }
    public String extractUserId(String token) {
        SecretKey key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS512).build();

        Jwt decodedJwt = jwtDecoder.decode(token);
        return decodedJwt.getClaim("userId");

    }
}
