package it.unitn.healthcore.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @class  SecurityConfig
 * @brief This class configures the security settings for the application using Spring Security. It defines password encoding, authentication provider, and security filter chain to manage access control and authentication for the application's endpoints.  
 * @see   it.unitn.healthcore.controller.UserController     
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
/**
    * @brief Encodes Password
    * @param
    * @return 
    */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    /**
    * @brief 
    * @param
    * @return 
    */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/error", "/registration", "/allUsers", "/passwordRecovery", "/passwordRecovery/request", "/passwordRecovery/otp").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/otp", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"))
                .build();
    }

        /**
    * @brief 
    * @param
    * @return 
    */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
