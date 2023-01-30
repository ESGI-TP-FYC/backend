package fr.esgi.ecommerce.service;

import fr.esgi.ecommerce.domain.User;

import java.util.Map;

public interface AuthenticationService {

    Map<String, String> login(String email, String password);

    String registerUser(User user, String captcha, String password2);

    String activateUser(String code);

    User findByPasswordResetCode(String code);

    String sendPasswordResetCode(String email);

    String passwordReset(String email, String password, String password2);
}
