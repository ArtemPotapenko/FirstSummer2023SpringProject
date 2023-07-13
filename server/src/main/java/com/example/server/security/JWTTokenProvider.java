package com.example.server.security;

import com.example.server.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.sql.Date;
@Component
@Log4j2
public class JWTTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expDate = new Date(now.getTime() + SecurityConstant.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();

        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("firstname", user.getName());
        claimsMap.put("lastname", user.getLastname());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.ES512, SecurityConstant.SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstant.SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException | UnsupportedJwtException
                | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.SECRET)
                .parseClaimsJws(token)
                .getBody();
         return Long.parseLong((String) claims.get("id"));

    }
}
