package com.benayed.samples.websocketapp.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic"); //enable a simple memory-based message broker to carry the messages back to the client on destinations prefixed with /topic
		config.setApplicationDestinationPrefixes("/app");
		
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/topic-channel")
//		.addInterceptors(new HttpHandshakeInterceptor())
		.setAllowedOriginPatterns("*");
//		.withSockJS();
		
		registry.addEndpoint("/user-channel")
		.setHandshakeHandler(new CustomHandshaker())
		.setAllowedOriginPatterns("*");
//		.withSockJS();

	}

	@Override
	//https://docs.spring.io/spring-framework/reference/web/websocket/stomp/authentication-token-based.html
	public void configureClientInboundChannel(ChannelRegistration registration) {

	    registration.interceptors(new ChannelInterceptor() {
	        @Override
	        public Message<?> preSend(Message<?> message, MessageChannel channel) {
	        	System.out.println("message received: " + message);
	            StompHeaderAccessor accessor =
	                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
	            
	            
	            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
	            	
	            	System.out.println();

	                List<String> authorization = accessor.getNativeHeader("Authorization");
	                System.out.println("Authorization: " + authorization);

	                String jwtToken =  authorization.get(0).replace("Bearer ","");
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

	                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
	                
	                accessor.setUser(authentication);
	            }
	            
	            
	            return message;
	        }
	    });
	}
	 

}
