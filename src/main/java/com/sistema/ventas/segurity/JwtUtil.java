package com.sistema.ventas.segurity;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sistema.ventas.exceptions.BOException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

    private String secret = "javatechie";

    public String extractUsername(String token) throws BOException {
       return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws BOException {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws BOException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws BOException {
    	Claims claims = null ;
    	 try {
        	 claims=Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    	 }catch(Exception e){
    		 throw new BOException("ven.warn.tokenInvalido");
    	 }
    	 
    	 return claims;
    }

    private Boolean isTokenExpired(String token) throws BOException {
    	
    	System.out.print("Fecha: Expiracion: "+extractExpiration(token));
    	System.out.print("Fecha: Actual: "+new Date());
    	
    	if(extractExpiration(token).before(new Date())) 
    		 throw new ExpiredJwtException(null,null,"Token caducado");
    	
        return false;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {

    	System.out.print("Fecha Inicio: "+new Date(System.currentTimeMillis()));
    	System.out.print("Fecha Expiracion: "+new Date(System.currentTimeMillis() +3600000 ));
    	
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000 ))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws BOException {
    	
    	System.out.print("Validar Token: "+!isTokenExpired(token));
    	
        final String username = extractUsername(token);
        
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        
    }
}