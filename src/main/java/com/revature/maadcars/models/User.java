package com.revature.maadcars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Foreign keys:
 * - One to many: One user, many vehicles
 */
@Entity
@Table(name="users")
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @NotBlank(message = "Username is mandatory")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Size(min = 5, max = 200, message = "password length needs to be between 5 and 200")
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9]{5,200}$", message = "Password must include at least one upper case character and one lower case character and one digit")
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Vehicle> vehicles;
}
