package com.ncedu.knownetimpl.service;

import com.ncedu.knownetimpl.model.entity.User;
import com.ncedu.knownetimpl.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findByLogin(String login) {
        if (login == null) {
            log.warn("requested user with null login");
            return Optional.empty();
        }
        List<User> users = userRepository.findByLogin(login);
        if (users.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(users.get(0));
        }
    }
    
    public List<User> findByGroup(String group) {
        return userRepository.findByGroup(group);
    }
    
    public Optional<User> findById(Long id) {
        if (id != null) {
            return userRepository.findById(id);
        } else {
            log.warn("requested user with null id");
            return Optional.empty();
        }
    }
    
    @Transactional
    public boolean deleteByLogin(String login) {
        if (login == null) {
            log.warn("deleting user with null login");
            return false;
        }
        return userRepository.deleteByLogin(login) != 0;
    }
    
    public boolean deleteById(Long id) {
        if (id == null) {
            log.warn("deleting user with null id");
            return false;
        }
        boolean exists = userRepository.existsById(id);
        if (exists) {
            userRepository.deleteById(id);
        }
        return exists;
    }
    
    public boolean create(User user) {
        if (user.getLogin() == null) {
            throw new IllegalArgumentException("user mustn't have null login");
        }
        if (user.getLogin().length() < 4) {
            throw new IllegalArgumentException("login must have at least 4 symbols");
        }

        if (userRepository.existsByLogin(user.getLogin())) {
            return false;
        }

        if (user.getPassword() == null) {
            throw new IllegalArgumentException("user mustn't have null password");
        }
        if (user.getPassword().length() < 4) {
            throw new IllegalArgumentException("password must have at least 4 symbols");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    
    public boolean update(User user) {
        if (user.getLogin() == null) {
            log.warn("updating user with null login");
            return false;
        }

        Optional<User> oldUserOpt = findByLogin(user.getLogin());
        if (oldUserOpt.isPresent()) {
            User oldUser = oldUserOpt.get();

            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());
            oldUser.setGroup(user.getGroup());
            oldUser.setVkLink(user.getVkLink());
            oldUser.setTelegramLink(user.getTelegramLink());
            oldUser.setEmail(user.getEmail());
            oldUser.setPhoneNumber(user.getPhoneNumber());
            userRepository.save(oldUser);
        }
        return oldUserOpt.isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(String.format("user with login \"%s\" not found", login)));
    }
    
}
