package com.xeubiart.verification.service;

import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.verification.entity.VerificationSession;
import com.xeubiart.verification.exceptions.BadVerificationException;
import com.xeubiart.verification.exceptions.VerificationAttemptsExceededException;
import com.xeubiart.verification.exceptions.VerificationReSendCooldownException;
import com.xeubiart.verification.repository.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VerificationServiceImpl implements VerificationService{
    private VerificationRepository verificationRepository;

    @Override
    public String generate(UUID accountId, IdentityType provider) {
        String sessionToken = UUID.randomUUID().toString();

        SecureRandom random = new SecureRandom();
        int number = 100000 + random.nextInt(900000);
        String code = String.valueOf(number);

        // TODO turn this into a mail sender service
        System.out.println(code);

        VerificationSession session = VerificationSession.builder()
                .sessionToken(sessionToken)
                .code(code)
                .provider(provider)
                .accountId(accountId)
                .build();

        this.verificationRepository.save(session);

        return sessionToken;
    }

    @Override
    @Transactional
    public VerificationSession verify(String sessionToken, String code) throws BadVerificationException, VerificationAttemptsExceededException {
        VerificationSession session = this.verificationRepository.findById(sessionToken)
                .orElseThrow(() -> new BadVerificationException("Session not found"));

        if(session.getCode().equals(code)){
            this.erase(sessionToken);
            return session;
        }

        session.registerWrongAttempt();
        if(session.isAttemptsExceeded()){
            this.erase(sessionToken);
            throw new VerificationAttemptsExceededException();
        }
        this.verificationRepository.save(session);

        return null;
    }

    @Override
    public void erase(String sessionToken) throws BadVerificationException {
        this.verificationRepository.deleteById(sessionToken);
    }

    @Override
    @Transactional
    public void generateNew(String sessionToken) throws BadVerificationException, VerificationReSendCooldownException {
        VerificationSession session = this.verificationRepository.findById(sessionToken)
                .orElseThrow(() -> new BadVerificationException("Session not found"));

        if(!session.canSendNewCode()) throw new VerificationReSendCooldownException();

        session.setCode(UUID.randomUUID().toString().substring(0, 6));
        session.setAttempts(0);

        this.verificationRepository.save(session);
    }
}
