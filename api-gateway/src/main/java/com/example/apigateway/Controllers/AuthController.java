package com.example.apigateway.Controllers;

import com.example.apigateway.Clients.UserClient;
import com.example.apigateway.Requests.LoginRequest;
import com.example.apigateway.Requests.RegisterRequest;
import com.example.generated.ApiResponse;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/auth")
@PermitAll
public class AuthController {
    @PermitAll
    @PostMapping("/login")
    public ApiResponse login(@Validated @RequestBody LoginRequest request) {
        return UserClient.login(request.email, request.password);
    }

    @PermitAll
    @PostMapping("/register")
    public ApiResponse register(@Validated @RequestBody RegisterRequest request) {
        return UserClient.register(request.getEmail(),request.getPassword(), request.getName());
    }

    @PermitAll
    @GetMapping("/generate")
    public String generateToken() {
        String userId = "1";  // Assuming username is the user ID
        String secret = "awdawdawdawdawdbfsuboseocosiencoienvoseosefnluBsivusvsoievbyspieufbpisuebfpiesub";

        // Create claims for the JWT token
        JwtClaimsSet claims = JwtClaimsSet.builder()// Issuer of the token// The time the token was issued
                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS)) // Expiry time of the token
                .claim("sub", userId) // You can add custom claims if necessary
                .build();

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
}
