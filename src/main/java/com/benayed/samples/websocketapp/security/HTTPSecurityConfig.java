package com.benayed.samples.websocketapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class HTTPSecurityConfig { //HTTP part of the security

//	private UserDetailsService userDetailsService;
//	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable()).authorizeHttpRequests(req -> req.anyRequest().permitAll());
    	
//        http
//        	.csrf(csrf -> csrf.disable())
//        	.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        	.authenticationProvider(authenticationProvider())
//        	.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//
        return http.build();
    }
//    
//    @Bean
//    public JWTAuthorizationFilter jwtAuthorizationFilter() {
//    	return new JWTAuthorizationFilter();
//    }
//    
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//     
//        return authProvider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//      return new BCryptPasswordEncoder();
//    }
}
