package org.example.shop.security;

import org.example.shop.config.ShopProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class TokenService {
    private final JwtEncoder encoder;
    private final Duration validity;

    public TokenService(JwtEncoder encoder, ShopProperties props) {
        this.encoder = encoder;
        this.validity = props.security().tokenValidity();
    }

    public LoginResponse issue(Authentication authentication) {
        Instant now = Instant.now();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.replaceFirst("^ROLE_", ""))
                .toList();

        JwtClaimsSet claim = JwtClaimsSet.builder()
                .issuer("shop")
                .issuedAt(now)
                .expiresAt(now.plus(validity))
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claim)).getTokenValue();
        return new LoginResponse(token, "Bearer", validity.toSeconds());
    }
}
