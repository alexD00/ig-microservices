package com.alex.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${secret.secret-key}")
    private String SECRET_KEY;

    public void validateToken(final String token){
        try {
            Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token);
        }catch (JwtException e){
            throw new RuntimeException("Invalid token");
        }
    }

    public Claims getClaimsFromToken(final String token){
        return Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
