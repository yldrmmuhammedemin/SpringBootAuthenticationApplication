package com.spring.SpringAuthentication.appuser;

import com.spring.SpringAuthentication.registration.token.ConfirmationService;
import com.spring.SpringAuthentication.registration.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "User %s not found !";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserRepository appUserRepository;
    private final ConfirmationService confirmationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser){
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if (userExists){
            throw new IllegalStateException("Email already taken.");
        }
        String encodePassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodePassword);
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationService.saveConfirmationToken(confirmationToken);


        return "it works";
    }
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
