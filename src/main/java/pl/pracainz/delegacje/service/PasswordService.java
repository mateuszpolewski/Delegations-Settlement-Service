package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pracainz.delegacje.model.Password;
import pl.pracainz.delegacje.repository.PasswordRepository;

@Service
public class PasswordService {
    private final int PASSWORD_LENGTH = 10;

    @Autowired
    PasswordRepository passwordRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Password getById(int id) {return (Password) passwordRepository.findById(id); }

    public Password generateRandomPasswordObject() {
        Password password = new Password();
        String randomPasswordText = PasswordGenerator.generateRandomPassword();
        password.setPasswordHash(randomPasswordText);
        password.setTemporary(true);
        System.out.println(randomPasswordText);
        System.out.println(randomPasswordText);
        System.out.println(randomPasswordText);
        return password;
    }

}
