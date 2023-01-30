package fr.esgi.ecommerce.dto.auth;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String email;
    private String token;
    private String userRole;
}
