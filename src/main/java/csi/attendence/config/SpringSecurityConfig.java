package csi.attendence.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import csi.attendence.filter.JwtTokenValidatorFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {

	private final JwtTokenValidatorFilter jwtTokenValidatorFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(x -> x.disable());
		http.authorizeHttpRequests(r -> {
			r.requestMatchers("/api/v1/login", "/api/v1/refresh-token","/api/v1/student/register","/verify-email","/api/v1/me").permitAll();
			r.requestMatchers("/api/me","/api/user/**").authenticated();
		});
		http.formLogin(fl -> fl.disable());
//		http.authorizeHttpRequests(x->x.anyRequest().authenticated());
		http.httpBasic(t -> {
			t.authenticationEntryPoint(new NoPopupBasicAuthenticationEntryPoint());
		});
		http.sessionManagement(sm -> {
			sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		});
		http.rememberMe(rem -> {
			rem.disable();
		});
		http.addFilterBefore(jwtTokenValidatorFilter, UsernamePasswordAuthenticationFilter.class);
		http.headers(headers -> headers.frameOptions(x -> {
			x.sameOrigin();
		}));
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("*"));
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}

class NoPopupBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
	}

}