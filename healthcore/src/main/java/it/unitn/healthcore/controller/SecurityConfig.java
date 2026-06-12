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
 * @detail The SecurityConfig class is responsible for setting up the security configuration for the application. 
 * It defines a PasswordEncoder bean that uses BCrypt for hashing passwords, an authentication provider that integrates with Spring Security's authentication mechanism, and a security filter chain that specifies which endpoints are publicly accessible and which require authentication.
 *  The filter chain also configures form-based login and logout behavior. 
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
/**
    * @brief Defines a bean for password encoding using BCrypt algorithm with a strength of 10. This bean is used by the authentication provider to encode and verify user passwords securely.
    * @return A PasswordEncoder instance that uses BCrypt for password hashing.
    */  
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    /**
     * @brief Configures the security filter chain for the application. It specifies which endpoints are publicly accessible and which require authentication, as well as the login and logout behavior.
     * @param http The HttpSecurity object used to configure web-based security for specific HTTP requests.
     * @return A SecurityFilterChain that defines the security configuration for the application.
     * @throws Exception If an error occurs while configuring the security filter chain.
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
     * @brief Defines a bean for the DaoAuthenticationProvider, which integrates with Spring Security's authentication mechanism. It sets the UserDetailsService and PasswordEncoder to be used for authentication.
     * @param userDetailsService The UserDetailsService that provides user details for authentication.
     * @param passwordEncoder The PasswordEncoder that is used to encode and verify passwords during authentication.
     * @return A DaoAuthenticationProvider configured with the specified UserDetailsService and PasswordEncoder.
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
