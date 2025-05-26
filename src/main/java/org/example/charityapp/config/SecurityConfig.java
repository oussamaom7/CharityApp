package org.example.charityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    // 1. API Security (for /api/**) -- DECLARE THIS FIRST!
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Applies only to /api/
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 2. Web Security (for normal pages)
    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // Applies to all non-API requests
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(org.springframework.http.HttpMethod.GET, 
                            "/register", "/Register", 
                            "/register/organization",
                            "/login", "/forgot-password", "/reset-password", "/error").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, 
                            "/register", "/Register", 
                            "/register/organization",
                            "/login", "/forgot-password", "/reset-password").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/charity.png").permitAll()
                        .requestMatchers("/", "/home", "/about", "/programs", "/getinvolved", "/blog").permitAll()
                        .requestMatchers("/organizations", "/campaigns").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        
                        // Organization endpoints
                        .requestMatchers("/organization/dashboard").hasRole("ORGANIZATION")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/organization/charity-actions/new").hasRole("ORGANIZATION")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/organization/charity-actions").hasRole("ORGANIZATION")
                        
                        // Admin endpoints
                        .requestMatchers("/admin/manage", "/admin/approve/**").hasRole("SUPER_ADMIN")
                        .requestMatchers(
                            "/admin/charity-actions/approve", 
                            "/admin/charity-actions/reject",
                            "/admin/organizations/approve",
                            "/admin/organizations/reject"
                        ).hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        
                        // Profile endpoints
                        .requestMatchers("/profile/**").authenticated()
                        
                        // Donation endpoints
                        .requestMatchers("/donate/**").authenticated()
                        
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/login?expired")
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
