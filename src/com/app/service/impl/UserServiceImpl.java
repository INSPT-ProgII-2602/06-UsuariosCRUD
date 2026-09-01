// Paquete: com.app.service.impl
package com.app.service.impl;

import com.app.exception.UserNotFoundException;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import com.app.util.Constants;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    // Inyección de dependencias a través del constructor
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, String email) {
        User result = null;
        if (name != null && !name.trim().isEmpty() && email != null) {
            User newUser = new User(null, name, email);
            result = userRepository.save(newUser);
        } else {
            throw new IllegalArgumentException(Constants.MSG_INVALID_DATA);
        }
        return result; // Single return
    }

    @Override
    public User getUser(Long id) {
        User result = null;
        Optional<User> userOpt = userRepository.findById(id);
        
        if (userOpt.isPresent()) {
            result = userOpt.get();
        } else {
            throw new UserNotFoundException(Constants.MSG_USER_NOT_FOUND);
        }
        
        return result; // Single return
    }

    @Override
    public List<User> getAllUsers() {
        List<User> result = userRepository.findAll();
        return result; // Single return
    }

    @Override
    public User updateUser(Long id, String newName, String newEmail) {
        User result = null;
        User existingUser = getUser(id); // Reutiliza validación y excepción de getUser
        
        existingUser.setName(newName);
        existingUser.setEmail(newEmail);
        result = userRepository.update(existingUser);
        
        return result; // Single return
    }

    @Override
    public boolean deleteUser(Long id) {
        boolean result = false;
        if (userRepository.deleteById(id)) {
            result = true;
        } else {
            throw new UserNotFoundException(Constants.MSG_USER_NOT_FOUND);
        }
        return result; // Single return
    }
}