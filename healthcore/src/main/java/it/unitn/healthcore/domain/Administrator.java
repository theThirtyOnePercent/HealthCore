
package it.unitn.healthcore.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="administrators")
@DiscriminatorValue("Administrator")
public class Administrator extends User{

    public Administrator(){

    }

    public Administrator(String name, String surname, String email, String password) {
        super(name, surname, email, password);
    }
}
