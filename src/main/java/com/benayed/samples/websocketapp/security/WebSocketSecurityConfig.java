package com.benayed.samples.websocketapp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

//Very useful: https://docs.spring.io/spring-security/reference/6.0/servlet/integrations/websocket.html
@SuppressWarnings("deprecation")
@Configuration
// Can't do it using @EnableWebSocketSecurity and @Bean because to this date that "new" implementation doesn't provide a sameOriginDisabled() equivalent. 
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected boolean sameOriginDisabled() { // Leads to CSRF error if not present
        return true;
    }

	@Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
    	message
    	.simpDestMatchers("/app/e1").hasAuthority("ROOT") // OR @PreAuthorize("hasAuthority('ROOT')")
    	.anyMessage().permitAll();
    }
    
}
