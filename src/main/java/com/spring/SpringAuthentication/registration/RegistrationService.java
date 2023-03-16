package com.spring.SpringAuthentication.registration;

import com.spring.SpringAuthentication.appuser.AppUser;
import com.spring.SpringAuthentication.appuser.AppUserModel;
import com.spring.SpringAuthentication.appuser.AppUserService;
import com.spring.SpringAuthentication.appuser.mail.EmailHtml;
import com.spring.SpringAuthentication.appuser.mail.EmailSender;
import com.spring.SpringAuthentication.appuser.mail.EmailService;
import com.spring.SpringAuthentication.registration.token.ConfirmationService;
import com.spring.SpringAuthentication.registration.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationService confirmationService;
    private final EmailSender emailSender;
    private final EmailHtml emailHtml;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        String token = appUserService.signUpUser(
                new AppUser(
                        request.getFirstname(),
                        request.getEmail(),
                        request.getLastname(),
                        request.getPassword(),
                        AppUserModel.USER
                )
        );
        String link = "http://localhost:8080/api/v1/registration/confirm?token="+token;
        emailSender.send(request.getEmail(),emailHtml.buildEmail(request.getFirstname(),link));
        return token;
    }
    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationService
                .getToken(token)
                .orElseThrow(()-> new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }
        confirmationService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }
}
