package com.benayed.samples.websocketapp;

import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class MetricsController {

	private WebSocketMessageBrokerStats webSocketMessageBrokerStats;
    private final SimpUserRegistry simpUserRegistry;
    private SubProtocolWebSocketHandler subProtocolWebSocketHandler;
    
	@GetMapping("/test")
//	@PreAuthorize("hasAuthority('ROOT')")
	public void test() {
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.toString());
		System.out.println(webSocketMessageBrokerStats.getClientInboundExecutorStatsInfo());
		System.out.println(webSocketMessageBrokerStats.getClientOutboundExecutorStatsInfo());
		System.out.println(webSocketMessageBrokerStats.getLoggingPeriod());
		System.out.println(webSocketMessageBrokerStats.getSockJsTaskSchedulerStatsInfo());
		System.out.println(webSocketMessageBrokerStats.getStompBrokerRelayStatsInfo());
		System.out.println(webSocketMessageBrokerStats.getStompSubProtocolStatsInfo());
		System.out.println(webSocketMessageBrokerStats.getWebSocketSessionStatsInfo());
		System.out.println(webSocketMessageBrokerStats);
		
		System.out.println(subProtocolWebSocketHandler.getProtocolHandlers());
		System.out.println(subProtocolWebSocketHandler.getStats()); // See Open sessions
		
	}
	
	
	
	@GetMapping("/test2")
	public void test2(@RequestParam("topic") String topic) {

		System.out.println(topic);
		System.out.println(simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(topic)));
		System.out.println(simpUserRegistry.getUserCount());
		System.out.println(simpUserRegistry.getUsers());
	}
}
