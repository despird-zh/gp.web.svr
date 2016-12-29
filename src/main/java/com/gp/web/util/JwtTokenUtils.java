package com.gp.web.util;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gp.common.JWTPayload;  
  

public class JwtTokenUtils {
  
    /** 
     * get jwt String of object 
     * @param object 
     *            the POJO object 
     * @param maxAge 
     *            the milliseconds of life time 
     * @return the jwt token 
     */  
    public static String signHS256(String secret, JWTPayload payload) {  
        try {  
        	JWTCreator.Builder build = JWT.create();
        	build.withIssuer(payload.getIssuer());
        	build.withAudience(payload.getAudience());
        	build.withSubject(payload.getSubject());
        	build.withIssuedAt(payload.getIssueAt());
        	build.withNotBefore(payload.getNotBefore());
        	build.withExpiresAt(payload.getExpireTime());
        	build.withJWTId(payload.getJwtId());
        	
            if(MapUtils.isNotEmpty(payload.getClaims())){
            	
            	for(Map.Entry<String, Object> entry: payload.getClaims().entrySet()){
            		build.withClaim(entry.getKey(), entry.getValue());
            	}
            }
            
            return build.sign(Algorithm.HMAC256(secret)); 
            
        } catch(Exception e) {  
            return null;  
        }  
    }  

    /** 
     * get the object of jwt if not expired 
     * @param jwt 
     * @return POJO object 
     */  
    public static boolean verifyHS256(String secret, String jwtToken, JWTPayload payload) {  
    	
    	try {
    		
    	    JWTVerifier.Verification verification = JWT.require(Algorithm.HMAC256(secret))
    	    .withIssuer(payload.getIssuer())
    	    .withAudience(payload.getAudience())
    	    .withSubject(payload.getSubject())
    	    .withJWTId(payload.getJwtId());
    	    if(MapUtils.isNotEmpty(payload.getClaims())){
            	
            	for(Map.Entry<String, Object> entry: payload.getClaims().entrySet()){
            		verification.withClaim(entry.getKey(), entry.getValue());
            	}
            }
    	    JWTVerifier verifier = verification.build();
    	    
    	    verifier.verify(jwtToken);
    	    
    	    return true;
    	} catch (JWTVerificationException | IllegalArgumentException | UnsupportedEncodingException exception){
    	    return false;
    	}
    	
    } 

}
