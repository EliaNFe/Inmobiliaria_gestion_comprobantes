package com.inmobiliaria.comprobante.gestion_comprobantes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomSuccessHandler successHandler) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()

                        // --- RESTRICCIONES POR ROL ---
                        // Solo el admin entra a la gestión de usuarios y base de datos
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Empleados y Admin pueden ver clientes y generar recibos
                        .requestMatchers("/empleado/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers("/clientes/**", "/contratos/**").hasAnyRole("ADMIN", "EMPLEADO")

                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // Usamos el successHandler en lugar de defaultSuccessUrl
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("lili123"))
                .roles("ADMIN")
                .build();

        UserDetails empleado = User.builder()
                .username("empleado")
                .password(encoder.encode("emp123"))
                .roles("EMPLEADO")
                .build();

        return new InMemoryUserDetailsManager(admin, empleado);
    }
}