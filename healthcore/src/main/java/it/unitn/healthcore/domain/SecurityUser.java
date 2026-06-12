package it.unitn.healthcore.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @class SecurityUser
 * @brief Adapter class that implements UserDetails to integrate our User entity with Spring Security.
 * @detail It provides the necessary user information to Spring Security for authentication and authorization purposes.
 * @detail The getAuthorities method maps the user's role to a GrantedAuthority, which Spring Security uses to manage access control.
 * @see User
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public class SecurityUser implements UserDetails {

    private final User user;

    public SecurityUser(User user){
        this.user = user;
    }

    /** @brief Returns the authorities granted to the user. In this implementation, it maps the user's role to a Spring Security authority.
     * @detail The role is prefixed with "ROLE_" to conform to Spring Security's convention for role names.
     * @return A collection of GrantedAuthority representing the user's roles and permissions.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        //String role = user.getRole();
        //String prefixedRole = role.startsWith("ROLE_") ? role: "ROLE_" + role;
        String role = "ROLE_" + user.getClass().getSimpleName().toUpperCase();
        return List.of(new SimpleGrantedAuthority(role));
    }

    /** @brief Returns the password used to authenticate the user. In this implementation, it retrieves the password from the underlying User entity.
     * @return The user's password as a String.
     */
    @Override
    public String getPassword(){
        return user.getPassword();
    }

    /** @brief Returns the username used to authenticate the user. In this implementation, it retrieves the email from the underlying User entity as the username.
     * @return The user's email as a String, which serves as the username for authentication purposes.
     */
    @Override
    public String getUsername(){
        return user.getEmail();
    }

    /** @brief Indicates whether the user's account has expired. In this implementation, it always returns true, indicating that accounts do not expire.
     * @return true, indicating that the user's account is non-expired.
     */

    @Override public boolean isEnabled() { return true; }
}
