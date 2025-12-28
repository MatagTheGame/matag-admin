package com.matag.admin;

import com.matag.admin.auth.logout.MatagLogoutSuccessHandler;
import com.matag.admin.session.AuthSessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import java.util.HashMap;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MatagAdminWebSecurityConfiguration {
    @Autowired
    private MatagLogoutSuccessHandler matagLogoutSuccessHandler;

    @Autowired
    private AuthSessionFilter authSessionFilter;

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/js/**", "/img/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(authSessionFilter, SecurityContextPersistenceFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL)
                        ))
                        .logoutSuccessHandler(matagLogoutSuccessHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/test/**", "/ui/**", "/stats", "/config",
                                "/auth/login", "/auth/logout", "/auth/register", "/auth/verify").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        var defaultEncodingId = "argon2";
        var encoders = new HashMap<String, PasswordEncoder>();
        encoders.put(defaultEncodingId, new Argon2PasswordEncoder(16, 32, 8, 1 << 16, 4));
        return new DelegatingPasswordEncoder(defaultEncodingId, encoders);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            throw new AuthenticationServiceException("Cannot authenticate " + authentication);
        };
    }
}
