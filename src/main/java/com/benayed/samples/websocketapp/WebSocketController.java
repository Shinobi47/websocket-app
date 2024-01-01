package com.benayed.samples.websocketapp;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.benayed.samples.websocketapp.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebSocketController {
	
	private SimpMessagingTemplate simpMessagingTemplate;

	public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}
	
	@MessageMapping("/e1") // input. config.setApplicationDestinationPrefixes("/app"); <=> input path is /app/hello
	@SendTo("/topic/t1") // output. The return value is broadcast to all subscribers of /topic/user
//	@PreAuthorize("hasAuthority('ROOT')")
	public ResponseEntity<?> test(UserDto inputUser, @AuthenticationPrincipal Principal principal) throws InterruptedException {
		log.info(principal.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.toString());

		log.info(inputUser.toString());
		Thread.sleep(3000); // simulated delay
		inputUser.setId(1L);
		inputUser.setEmail(inputUser.getName() + "@benayedcorp.com");
		return ResponseEntity.ok(inputUser);
	}
	
	
	// Same as method above nut using  simpMessagingTemplate.convertAndSend instead of returning ResponseEntity
	@MessageMapping("/hello")
	public void user(@Payload Message<UserDto> message) throws InterruptedException {
		log.info(message.getPayload().toString());
		log.info(message.getHeaders().toString());
		
		UserDto user = message.getPayload();
//		Thread.sleep(3000); // simulated delay
		user.setId(1L);
		user.setEmail(user.getName() + "@benayedcorp.com");
		this.simpMessagingTemplate.convertAndSend("/topic/t1", user);
		
	}
	
	//Leveraging user channel (using .setHandshakeHandler(new CustomHandshaker() in )
	@MessageMapping("/e2")
	@SendToUser("/topic/u1")
	public ResponseEntity<?> user(UserDto inputUser, final Principal principal) throws InterruptedException {
		log.info(principal.toString());
		log.info(inputUser.toString());

		inputUser.setId(1L);
		inputUser.setEmail(inputUser.getName() + "@benayedcorp-private.com");
//		this.simpMessagingTemplate.convertAndSendToUser(null, null, user)
		return ResponseEntity.ok(inputUser);
		
	}
}
