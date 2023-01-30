package fr.esgi.ecommerce.dto.user;

import fr.esgi.ecommerce.domain.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private String address;
    private String phoneNumber;
    private String postIndex;
    private String provider;
    private boolean active;
    private String activationCode;
    private String passwordResetCode;
    private Set<Role> roles;
}
