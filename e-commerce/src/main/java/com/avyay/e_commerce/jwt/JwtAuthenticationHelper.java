package com.avyay.e_commerce.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtAuthenticationHelper {
    public final String SECRET = "thisisanotherexampleofasecretkeyforspringsecurityjsonwebtokenauthenticationwithuniquecontent";
    private static final long JWT_TOKEN_VALIDITY = 60 * 60;

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET.getBytes()).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // public Long getUserIdFromToken(String token) {
    //     return getClaimsFromToken(token).get("userId", Long.class);
    // }

    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);

        Date expDate = claims.getExpiration();
        return expDate.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS512.getJcaName()),
                        SignatureAlgorithm.HS512)
                .compact();
    }
}
