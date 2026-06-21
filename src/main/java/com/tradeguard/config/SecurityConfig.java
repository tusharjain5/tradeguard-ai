package com.tradeguard.config;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tradeguard.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	  private final CustomUserDetailsService userDetailsService;
	    
	    public SecurityConfig(CustomUserDetailsService userDetailsService) {
	        this.userDetailsService = userDetailsService;
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService);
	        authProvider.setPasswordEncoder(passwordEncoder());
	        return authProvider;
	    }
	    
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	        return authConfig.getAuthenticationManager();
	    }
	    
	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http
	            .authenticationProvider(authenticationProvider())
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/", "/home", "/login", "/register", 
	                               "/verify", "/payment/**", "/api/**", "/css/**", "/js/**", 
	                               "/images/**", "/favicon.ico").permitAll()
	                .anyRequest().authenticated()
	            )
	            .formLogin(form -> form
	                .loginPage("/login")
	                .loginProcessingUrl("/login")
	                .defaultSuccessUrl("/dashboard", true)
	                .failureUrl("/login?error=true")
	                .usernameParameter("username")
	                .passwordParameter("password")
	                .permitAll()
	            )
	            .logout(logout -> logout
	                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                .logoutSuccessUrl("/")
	                .invalidateHttpSession(true)
	                .deleteCookies("JSESSIONID")
	                .permitAll()
	            )
	            .csrf(csrf -> csrf.disable());

	        return http.build();
	    }
	}
