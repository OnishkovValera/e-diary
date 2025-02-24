package onishkoff.backend.configure.securityConfiguration;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import onishkoff.backend.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtTokenUtils;

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChainForAuthentication(HttpSecurity http) throws Exception{
        return getHttpBasicConfiguration(http)
                .securityMatcher("/auth/login", "/auth/reg")
                .build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return getHttpBasicConfiguration(http)
                .securityMatcher("/**")
                .addFilterBefore(new JwtFilter(jwtTokenUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public HttpSecurity getHttpBasicConfiguration(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))

                .httpBasic(AbstractHttpConfigurer::disable)

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(
                                (request, response, exception) ->
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        )
                        .accessDeniedHandler(
                                (request, response, exception) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN)))
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/auth/login", "/auth/reg", "/preview").permitAll()
                        .anyRequest().authenticated()
                );
        return http;
    }
}
