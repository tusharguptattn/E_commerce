package com.ecommerce.ecommerce.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    return http
        .csrf(csrf -> csrf.disable())

        .authorizeHttpRequests(auth -> auth

            // ========= PUBLIC APIs =========
            .requestMatchers(
                "/api/auth/login",
                "/api/auth/forgot-password",
                "/api/auth/reset-password",
                "/api/register/**",

                "/api/products/view/**",
                "/api/products/ViewProductByIdCustomer/**",
                "/api/products/ViewAllProductByIdCustomer/**",
                "/api/products/ViewAllSimilarProducts/**",

                "/api/categories/public/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**"
            ).permitAll()

            // ========= CUSTOMER APIs =========
            .requestMatchers(
                "/api/customers/**",
                "/api/cart/**",
                "/api/orders/customer/**",
                "/api/categories/customer/**"
            ).hasRole("CUSTOMER")

            // ========= ADMIN APIs =========
            .requestMatchers(
                "/api/admin/**",
                "/api/categories/admin/**",
                "/api/orders/admin/**",
                "/api/products/activateProduct/**",
                "/api/products/deactivateProduct/**"
            ).hasRole("ADMIN")

            // ========= SELLER APIs =========
            .requestMatchers(
                "/api/sellers/**",
                "/api/products/**",
                "/api/variations/**",
                "/api/orders/seller/**",
                "/api/orders/changeOrderStatus/**",
                "/api/categories/seller/**"
            ).hasRole("SELLER")




            // ========= EVERYTHING ELSE =========
            .anyRequest().authenticated()
        )

        .sessionManagement(sess ->
            sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        .build();
  }





    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

