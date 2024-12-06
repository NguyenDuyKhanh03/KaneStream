package com.example.KaneStream.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret_key}")
    private String secret;

    public String generateToken(String username) {
        Map<String, Object> claim = new HashMap<>();

        return Jwts.builder()
                .claims(claim)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey())
                .compact();
    }
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String email = extractUsername(token);
        return !isTokenExpired(token) && email.equals(userDetails.getUsername());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims= extractAllClaim(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaim(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }




    private SecretKey getKey(){
        byte[] key = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(key);
    }
}
