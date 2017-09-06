package com.gp.web;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

import com.gp.common.AccessPoint;
import com.gp.common.GPrincipal;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.JwtPayload;
import com.gp.common.SystemOptions;
import com.gp.core.MasterFacade;
import com.gp.core.SecurityFacade;
import com.gp.dao.info.SysOptionInfo;
import com.gp.dao.info.TokenInfo;
import com.gp.exception.CoreException;
import com.gp.info.InfoId;
import com.gp.svc.SecurityService;
import com.gp.util.JwtTokenUtils;
import com.gp.web.servlet.ServiceTokenFilter;

public class JwtAuthenProvider implements AuthenticationProvider{

	static Logger LOGGER = LoggerFactory.getLogger(UserPasswordAuthenProvider.class);
	
	@Autowired
	SecurityService securitysvc;
	
	@Override
	public Authentication authenticate(Authentication arg0) throws AuthenticationException {
		JwtAuthenToken jwtToken = (JwtAuthenToken) arg0;
		String token = jwtToken.getToken();
		if(StringUtils.startsWith(token, ServiceTokenFilter.TOKEN_PREFIX)) {
			token = StringUtils.substringAfter(jwtToken.getToken(), ServiceTokenFilter.TOKEN_PREFIX);
		}
		try{
			AccessPoint accesspoint = new AccessPoint("sync","blind");
			JwtPayload jwtPayload = JwtTokenUtils.parsePayload(token);
			InfoId<Long> tokenId = IdKeys.getInfoId(IdKey.GP_TOKENS, NumberUtils.toLong(jwtPayload.getJwtId()));
			TokenInfo tokenInfo = SecurityFacade.findToken(accesspoint, tokenId);
			// check if the token record exists
			if(tokenInfo == null){
				// not find any token in db
				throw new BadCredentialsException("Invalid Token");
			}else{
				SysOptionInfo secret = MasterFacade.findSystemOption(accesspoint, GroupUsers.PSEUDO_USER, SystemOptions.SECURITY_JWT_SECRET);
				
				if(!StringUtils.equals(tokenInfo.getJwtToken(), token)){
					
					throw new BadCredentialsException("Invalid Token");
				}
				else{
					int valid =  JwtTokenUtils.verifyHS256(secret.getOptionValue(), token, jwtPayload);
					if(valid < 0){
						
						throw new BadCredentialsException("Invalid Token");
					}
					else if(valid == JwtTokenUtils.EXPIRED){
						
						throw new CredentialsExpiredException("Expired Token");
					}
					else{

						GPrincipal principal = SecurityFacade.findPrincipal(accesspoint, null, jwtPayload.getSubject(), null);
						UserPasswordAuthenToken rtv =  new UserPasswordAuthenToken(jwtPayload.getSubject(), "blind",
				                AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
						
						rtv.setDetails(principal);
						return rtv;
					}
				}
			}
		}catch(CoreException ce){
			throw new AuthenticationServiceException(ce.getMessage());
		}
	}

	@Override
	public boolean supports(Class<?> tokenClazz) {
		return tokenClazz.equals(JwtAuthenToken.class);
	}

}
