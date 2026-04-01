package xeubiart.com.domain.identity.providers.local.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xeubiart.com.domain.identity.providers.local.dto.VerificationLocalOutputDTO;
import xeubiart.com.domain.identity.providers.local.entity.IdentityLocal;
import xeubiart.com.domain.identity.providers.local.entity.VerificationLocal;
import xeubiart.com.domain.identity.providers.local.mapper.VerificationLocalMapper;
import xeubiart.com.domain.identity.providers.local.repository.VerificationLocalRepository;
import xeubiart.com.infra.emailSender.EmailSenderService;
import xeubiart.com.infra.emailSender.dto.EmailVerificationCodeDTO;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationLocalService {
    @Autowired
    private VerificationLocalRepository repository;
    @Autowired
    private VerificationLocalMapper mapper;

    @Autowired
    private EmailSenderService emailSender;
    // TODO: generate HTML to email by the frontend engine

    @Value("${app.feature.send-emails:false}")
    private boolean isEmailEnabled;

    @Transactional
    public UUID createVerification(IdentityLocal identity){
        String code = this.generateSixDigitCode();

        VerificationLocal entity = new VerificationLocal();
            entity.setCode(code);
            entity.setIdentity(identity);
            entity.setExpiry(LocalDateTime.now().plusMinutes(15));

        VerificationLocal savedEntity = this.repository.save(entity);

        if(!isEmailEnabled){
            System.out.println(code);
            return savedEntity.getId();
        }

        try{
            this.emailSender.sendConfirmationEmail(new EmailVerificationCodeDTO(
                    code,
                    identity.getEmail(),
                    identity.getUser().getName()
            ));
        } catch (MessagingException e) {
            // TODO re-try send email again
            throw new RuntimeException(e);
        }

        return savedEntity.getId();
    }

    private String generateSixDigitCode(){
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }

    @Transactional
    public void verify(String sessionToken, String code){
        VerificationLocal verification = this.repository.findByIdAndCode(UUID.fromString(sessionToken), code)
                .orElseThrow(() -> new RuntimeException("Verification code not found!"));

        if(!verification.getCode().equals(code)){
            throw new RuntimeException("Verification code not match!");
        }

        if(verification.getExpiry().isBefore(LocalDateTime.now())){
            // Remove the Expired verification registry
            this.repository.delete(verification);
            throw new RuntimeException("Verification expired!");
        }

        // Since the verification has a reference to Identity it belongs to, just active from here
        verification.getIdentity().setActive(true);

        // Remove the Verification registry
        this.repository.delete(verification);
    }

    @Transactional
    public void generateNewCode(String sessionToken){
        VerificationLocal verification = this.repository.findById(UUID.fromString(sessionToken))
                .orElseThrow(() -> new RuntimeException("Verification code not found!"));

        if(verification.getExpiry().isBefore(LocalDateTime.now())){
            // Remove the Expired verification registry
            this.repository.delete(verification);
            throw new RuntimeException("Verification expired!");
        }

        String code = this.generateSixDigitCode();
        verification.setCode(code);

        if(!isEmailEnabled){
            System.out.println(code);
            return;
        }

        try{
            this.emailSender.sendConfirmationEmail(new EmailVerificationCodeDTO(
                    code,
                    verification.getIdentity().getEmail(),
                    verification.getIdentity().getUser().getName()
            ));
        } catch (MessagingException e) {
            // TODO re-try send email again
            throw new RuntimeException(e);
        }
    }
}
