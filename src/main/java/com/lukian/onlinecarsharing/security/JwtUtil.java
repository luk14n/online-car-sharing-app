package com.lukian.onlinecarsharing.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final String ALGORITHM_NAME = "HmacSHA256";
    private Key secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secretString) {
        this.secret =
                new SecretKeySpec(secretString.getBytes(StandardCharsets.UTF_8), ALGORITHM_NAME);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secret)
                .compact();
    }

    public boolean isValidToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public String getUserName(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolved) {
        final Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolved.apply(claims);
    }
}
