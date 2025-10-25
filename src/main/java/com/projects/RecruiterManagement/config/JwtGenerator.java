package com.projects.RecruiterManagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtGenerator {
    private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);
        String jwt = Jwts.builder()
                .setSubject(auth.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 84600000))
                .claim("email", auth.getName())
                .claim("authorities", roles
                )
                .signWith(key)
                .compact();

        return jwt;
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();

        for(GrantedAuthority authority: authorities){
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

    public String getEmailFromJwt(String jwt){
        if(jwt!=null && jwt.startsWith("Bearer ")){
            jwt = jwt.substring( 7);
        }
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = claims.getSubject();
        System.out.println("email recieved from jwt:"+jwt);
        return email;
    }

}
