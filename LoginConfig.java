package com.example.demo.app.login;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class LoginConfig{
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.formLogin(login -> login
				.loginProcessingUrl("/login")
				.loginPage("/login/loginForm")
				.defaultSuccessUrl("/?flg=1", true)
				.failureUrl("/loginForm?error=true")
				.usernameParameter("username")
				.passwordParameter("password")
				.permitAll()
			).logout(logout -> logout
				.logoutSuccessUrl("/login/loginForm").permitAll()
			).authorizeHttpRequests(authz -> authz
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
					.permitAll()
				.requestMatchers("/login/loginForm").permitAll()
				.anyRequest().authenticated()
				);

		http.csrf().disable();
		return http.build();
	}
}
