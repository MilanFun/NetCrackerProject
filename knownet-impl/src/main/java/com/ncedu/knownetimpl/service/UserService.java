package com.ncedu.knownetimpl.service;

import com.ncedu.knownetimpl.model.User;
import com.ncedu.knownetimpl.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findByLogin(String login) {
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
        return userRepository.findById(id);
    }
    
    public boolean deleteByLogin(String login) {
        boolean exists = userRepository.existsByLogin(login);
        if (exists) {
            userRepository.deleteByLogin(login);
        }
        return exists;
    }
    
    public boolean create(User user) {
        boolean exists = userRepository.existsByLogin(user.getLogin());
        if (!exists) {
            userRepository.save(user);
        }
        return !exists;
    }
    
    public boolean update(User user) {
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
    
    
}