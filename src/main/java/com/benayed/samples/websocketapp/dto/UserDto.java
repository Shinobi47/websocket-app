package com.benayed.samples.websocketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class UserDto {
	
	private Long id;
	private String name;
	private String email;
	
}
