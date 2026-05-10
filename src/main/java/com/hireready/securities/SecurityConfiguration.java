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

@Configuration
public class SecurityConfiguration {

    private static final String[] AUTH_WHITELIST ={

            // -- Login
            "/hireready/login/**",

            // -- Registro de nuevo usuarios
            "/hireready/companies/**",
            "/hireready/applicants/**",

    };

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*





    */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                (auth) -> auth
                // .anyRequest().permitAll()
                .requestMatchers(AUTH_WHITELIST).permitAll()

                        .requestMatchers(HttpMethod.GET,"/hireready/applicants/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET,"/hireready/applicants/dashboard/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT,"/hireready/applicants/**").hasAnyAuthority("ROLE_ADMIN","ROLE_ASSIST")

                        .requestMatchers(HttpMethod.POST,"/hireready/applicants/**").hasAnyAuthority("ROLE_ADMIN")


                        .requestMatchers(HttpMethod.GET,"/hireready/companies/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET,"/hireready/companies/dashboard/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT,"/hireready/companies/**").hasAnyAuthority("ROLE_ADMIN","ROLE_ASSIST")

                        .requestMatchers(HttpMethod.POST,"/hireready/companies/**").hasAnyAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()

        );

        // Spring Security no va a crear ni utilizar sesiones HTTP para gestionar el estado de autenticación de los usuarios}
        http.sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }
}
