package com.spring.SpringAuthentication.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationService {
    private final ConfimationTokenRepository confimationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confimationTokenRepository.save(token);
    }
}
