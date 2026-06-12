
package it.unitn.healthcore.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
/**
 * @class Administrator
 * @brief Administrator entity mapped to the "administrators" table, extends User.
 * @detail This class represents an administrator in the system, with all the properties and behaviors of a User. 
 * @detail Administrators have elevated privileges for managing the system.
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Entity
@Table(name="administrators")
@DiscriminatorValue("Administrator")
public class Administrator extends User{
    /** @brief Default constructor required by JPA. */
    public Administrator(){

    }
    /**
     * @brief Creates an Administrator with full credentials.
     * @param name     First name.
     * @param surname  Last name.
     * @param email    Email address.
     * @param password Account password.
     */
    public Administrator(String name, String surname, String email, String password) {
        super(name, surname, email, password);
    }
}
