package org.crops.fitserver.global.config;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.filter.JwtAccessDeniedHandler;
import org.crops.fitserver.global.filter.JwtAuthenticationFilter;
import org.crops.fitserver.global.filter.JwtExceptionFilter;
import org.crops.fitserver.global.http.HeaderTokenExtractor;
import org.crops.fitserver.global.jwt.JwtResolver;
import org.crops.fitserver.global.security.PrincipalDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtExceptionFilter jwtExceptionFilter;
	private final JwtAccessDeniedHandler customAccessDeniedHandler;
	private final HeaderTokenExtractor headerTokenExtractor;
	private final PrincipalDetailsService principalDetailsService;
	private final JwtResolver jwtResolver;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter =
				new JwtAuthenticationFilter(principalDetailsService, headerTokenExtractor, jwtResolver);
		http
				.addFilterBefore(jwtAuthenticationFilter,
						UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtExceptionFilter,
						JwtAuthenticationFilter.class)
				.exceptionHandling()
				.accessDeniedHandler(customAccessDeniedHandler)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.httpBasic().disable()
				.csrf().disable()
				.cors().configurationSource(corsConfigurationSource());

		http.authorizeHttpRequests()
				.requestMatchers(new AntPathRequestMatcher("/manager/**"))
				.hasAnyAuthority("MANAGER", "ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/admin/**"))
				.hasAuthority("ADMIN")
				.anyRequest()
				.hasAnyAuthority("USER", "MANAGER", "ADMIN");
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> {
			web.ignoring()
					.antMatchers("/actuator/**")
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
					.requestMatchers(
							new AntPathRequestMatcher(
									"/v1/auth/social/**",
									HttpMethod.GET.name()),
							new AntPathRequestMatcher("/h2-console/**")
					);
		};
	}

	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", getDefaultCorsConfiguration());
		return source;
	}

	private CorsConfiguration getDefaultCorsConfiguration() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("https://api.f-it.com"));
		configuration.setAllowedMethods(
				Arrays.asList(
						HttpMethod.GET.name(),
						HttpMethod.POST.name(),
						HttpMethod.PUT.name(),
						HttpMethod.PATCH.name(),
						HttpMethod.DELETE.name()));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("Refresh-Token"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);
		return configuration;
	}
}
