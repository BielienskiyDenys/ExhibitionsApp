package org.example.exhibitionsapp.service;

import org.example.exhibitionsapp.entity.User;
import org.example.exhibitionsapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    //  ПРИМЕР связи через конструктор, а не через аннотацию @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).get();
    }

    public List<User> findAllUsers() {return userRepository.findAll();}

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public boolean addNewUser(User user) {
        Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb.isPresent()) {
            logger.debug("Attempt to create user with existing email: " + user.getEmail());
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        userRepository.save(user);
        logger.info("New user registered:" + user);
        return true;
    }
}
