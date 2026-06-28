package com.hireready.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // nuestro filtro JWT
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        // reglas de autorización
        // US04
        http.authorizeHttpRequests(auth -> auth
                //WHITELIST
                .requestMatchers(HttpMethod.POST, "/hireready/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/hireready/applicants").permitAll() //para registro (2)
                .requestMatchers(HttpMethod.POST, "/hireready/companies").permitAll()

                //POR ROL
                .requestMatchers("/hireready/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/hireready/applicants/**").hasAuthority("ROLE_APPLICANT")
                .requestMatchers("/hireready/companies/**").hasAuthority("ROLE_COMPANY")

                //cualquier otra
                .anyRequest().authenticated()
        );

        http.sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }
}
