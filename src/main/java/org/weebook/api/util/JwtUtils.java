package org.weebook.api.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Component
public class JwtUtils {
    private static final String AUTHORITIES_CLAIM_NAME = "authorities";
    private static final int JWT_TOKEN_VALIDITY = 30;
    private final JwsAlgorithm algorithm;
    private final JwtEncoder encoder;

    public JwtUtils(JwtEncoder encoder) {
        this.algorithm = SignatureAlgorithm.RS256;
        this.encoder = encoder;
    }

    public String generateToken(Authentication authentication) {
        final Instant now = Instant.now();

        var authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var header = JwsHeader.with(algorithm).build();

        var claims = JwtClaimsSet.builder()
                .issuer("ZhuoFan")
                .audience(Collections.singletonList("web"))
                .subject(authentication.getName())
                .claim(AUTHORITIES_CLAIM_NAME, authorities)
                .issuedAt(now)
                .expiresAt(now.plus(JWT_TOKEN_VALIDITY, ChronoUnit.DAYS))
                .build();

        var parameters = JwtEncoderParameters.from(header, claims);
        return encoder.encode(parameters).getTokenValue();
    }
}
