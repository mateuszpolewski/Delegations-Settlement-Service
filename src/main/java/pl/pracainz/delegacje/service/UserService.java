package pl.pracainz.delegacje.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pracainz.delegacje.model.Password;
import pl.pracainz.delegacje.model.Role;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.repository.PasswordRepository;
import pl.pracainz.delegacje.repository.RoleRepository;
import pl.pracainz.delegacje.repository.UserRepository;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private EmailService emailService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) throws MessagingException {
        Password randomPassword = passwordService.generateRandomPasswordObject();
        String plainTextPassword = randomPassword.getPasswordHash();

        randomPassword.setPasswordHash(bCryptPasswordEncoder.encode(plainTextPassword));

        Role userRole = roleRepository.findByRole(user.getChosenRole());
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        user.setPassword(randomPassword);
        user.setActive(true);

        emailService.sendEmail(user.getEmail(), "Nowy użytkownik",
                "email/register-email.html", plainTextPassword);

        return userRepository.save(user);

    }


    //to tylko testowa metoda
    public User saveAdmin(User user) {
        Password randomPassword = passwordService.generateRandomPasswordObject();
        randomPassword.setPasswordHash(bCryptPasswordEncoder.encode("abcdefg12@"));
        randomPassword.setTemporary(false);
        user.setPassword(randomPassword);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setActive(true);
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findUserById(int id) { return userRepository.findById(id); }

    public String getRoleNameByUserId(int id) {
        return userRepository.findById(id).getRoles().stream().findFirst().get().getRole();
    }
    public List<User> findUsersListById(List<Long> indexes) {
        return userRepository.findAllById(indexes);
    }
    public User getLoggedInUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String email = loggedInUser.getName();
        return findUserByEmail(email);
    }

    public Page<User> listAll(int pageNumber, String sortField, String sortDir, String keyword, String role) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, 6, sort);
        System.out.println(role);
        System.out.println(role);
        if(keyword != null) {
            if(role.equals("ACCOUNTANT"))
                return userRepository.findAllEmployees(keyword, pageable);

            return userRepository.findAll(keyword, pageable);
        }
        if(role.equals("ACCOUNTANT"))
            return userRepository.findAllEmployees("", pageable);

        return userRepository.findAll(pageable);
    }


}
