package fr.esgi.ecommerce.controller;

import fr.esgi.ecommerce.dto.PasswordResetRequest;
import fr.esgi.ecommerce.dto.auth.AuthenticationRequest;
import fr.esgi.ecommerce.dto.auth.AuthenticationResponse;
import fr.esgi.ecommerce.dto.user.UserResponse;
import fr.esgi.ecommerce.exception.InputFieldException;
import fr.esgi.ecommerce.mapper.AuthenticationMapper;
import fr.esgi.ecommerce.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationMapper authenticationMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationMapper.login(request));
    }

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest passwordReset) {
        return ResponseEntity.ok(authenticationMapper.sendPasswordResetCode(passwordReset.getEmail()));
    }

    @GetMapping("/reset/{code}")
    public ResponseEntity<UserResponse> getPasswordResetCode(@PathVariable String code) {
        return ResponseEntity.ok(authenticationMapper.findByPasswordResetCode(code));
    }

    @PostMapping("/reset")
    public ResponseEntity<String> passwordReset(@RequestBody PasswordResetRequest passwordReset) {
        return ResponseEntity.ok(authenticationMapper.passwordReset(passwordReset.getEmail(), passwordReset));
    }

    @PutMapping("/edit/password")
    public ResponseEntity<String> updateUserPassword(@AuthenticationPrincipal UserPrincipal user,
                                                     @Valid @RequestBody PasswordResetRequest passwordReset,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            return ResponseEntity.ok(authenticationMapper.passwordReset(user.getEmail(), passwordReset));
        }
    }
}
