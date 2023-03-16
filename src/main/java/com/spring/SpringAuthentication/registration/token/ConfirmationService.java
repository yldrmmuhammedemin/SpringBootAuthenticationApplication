package com.spring.SpringAuthentication.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationService {
    private final ConfimationTokenRepository confimationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confimationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confimationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token){
        return confimationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
