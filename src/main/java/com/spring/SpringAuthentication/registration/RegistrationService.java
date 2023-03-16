package com.spring.SpringAuthentication.registration;

import com.spring.SpringAuthentication.appuser.AppUser;
import com.spring.SpringAuthentication.appuser.AppUserModel;
import com.spring.SpringAuthentication.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstname(),
                        request.getEmail(),
                        request.getLastname(),
                        request.getPassword(),
                        AppUserModel.USER
                )
        );
    }
}
