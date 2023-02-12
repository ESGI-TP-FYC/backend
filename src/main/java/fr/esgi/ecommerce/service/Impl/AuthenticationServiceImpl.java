package fr.esgi.ecommerce.service.Impl;

import fr.esgi.ecommerce.domain.AuthProvider;
import fr.esgi.ecommerce.domain.Role;
import fr.esgi.ecommerce.domain.User;
import fr.esgi.ecommerce.dto.CaptchaResponse;
import fr.esgi.ecommerce.exception.ApiRequestException;
import fr.esgi.ecommerce.exception.EmailException;
import fr.esgi.ecommerce.exception.PasswordConfirmationException;
import fr.esgi.ecommerce.exception.PasswordException;
import fr.esgi.ecommerce.repository.UserRepository;
import fr.esgi.ecommerce.security.JwtProvider;
import fr.esgi.ecommerce.service.AuthenticationService;
import fr.esgi.ecommerce.service.email.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;
    private final MailSenderService mailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${hostname}")
    private String hostname;

    @Value("${recaptcha.secret}")
    private String secret;

    @Value("${recaptcha.url}")
    private String captchaUrl;

    @Override
    public Map<String, String> login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userRepository.findByEmail(email);
            String userRole = user.getRoles().iterator().next().name();
            String token = jwtProvider.createToken(email, userRole);
            Map<String, String> response = new HashMap<>();
            response.put("email", email);
            response.put("token", token);
            response.put("userRole", userRole);
            return response;
        } catch (AuthenticationException e) {
            throw new ApiRequestException("Incorrect password or email", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public String registerUser(User user, String captcha, String password2) {
        /*String url = String.format(captchaUrl, secret, captcha);
        restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponse.class);*/

        if (user.getPassword() != null && !user.getPassword().equals(password2)) {
            throw new PasswordException("Passwords do not match.");
        }
        User userFromDb = userRepository.findByEmail(user.getEmail());

        if (userFromDb != null) {
            throw new EmailException("Email is already used.");
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setProvider(AuthProvider.LOCAL);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String subject = "Activation code";
        String template = "registration-template";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstName());
        attributes.put("registrationUrl", "http://" + hostname + "/activate/" + user.getActivationCode());
        mailSenderService.sendMessageHtml(user.getEmail(), subject, template, attributes);
        return "User successfully registered.";
    }

    @Override
    public User findByPasswordResetCode(String code) {
        User user = userRepository.findByPasswordResetCode(code);

        if (user == null) {
            throw new ApiRequestException("Password reset code is invalid!", HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    @Override
    public String sendPasswordResetCode(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ApiRequestException("Email not found", HttpStatus.BAD_REQUEST);
        }
        user.setPasswordResetCode(UUID.randomUUID().toString());
        userRepository.save(user);

        String subject = "Password reset";
        String template = "password-reset-template";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstName());
        attributes.put("resetUrl", "http://" + hostname + "/reset/" + user.getPasswordResetCode());
        mailSenderService.sendMessageHtml(user.getEmail(), subject, template, attributes);
        return "Reset password code is send to your E-mail";
    }

    @Override
    public String passwordReset(String email, String password, String password2) {
        if (StringUtils.isEmpty(password2)) {
            throw new PasswordConfirmationException("Password confirmation cannot be empty.");
        }
        if (password != null && !password.equals(password2)) {
            throw new PasswordException("Passwords do not match.");
        }
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordResetCode(null);
        userRepository.save(user);
        return "Password successfully changed!";
    }

    @Override
    public String activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            throw new ApiRequestException("Activation code not found.", HttpStatus.NOT_FOUND);
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        return "User successfully activated.";
    }
}
