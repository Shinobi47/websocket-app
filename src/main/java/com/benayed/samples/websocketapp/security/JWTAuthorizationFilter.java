package com.benayed.samples.websocketapp.security;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("Filter invoked");
		System.out.println(request.getProtocol() + "   " + request.getUserPrincipal() + "   " + request.getRequestURI());
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
		response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization");
		if(request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
		}

		else {
			String authorizationHeaderValue = request.getHeader("Authorization");
			System.out.println("Extracted authorisation header : " + authorizationHeaderValue);
			if(!StringUtils.hasText(authorizationHeaderValue) || !authorizationHeaderValue.startsWith("Bearer ")) {
				filterChain.doFilter(request, response); // Causes the next filter in the chain to be invoked
				return;
			}

			String jwtToken =  authorizationHeaderValue.replace("Bearer ","");
			String jwtSecret = "superSecret";
			
			Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(jwtToken);	
            String username = decodedJWT.getSubject();
            System.out.println(decodedJWT);
            
            System.out.println("username : " + username);
            Collection<GrantedAuthority> authorities = decodedJWT.getClaim("authorities").asList(String.class).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            
            System.out.println(authorities);

			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
			
			filterChain.doFilter(request, response);
		}

		
	}

}
