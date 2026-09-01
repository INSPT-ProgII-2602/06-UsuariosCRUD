// Paquete: com.app.repository.impl
package com.app.repository.impl;

import com.app.model.User;
import com.app.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    
    private final List<User> database = new ArrayList<>();
    private Long autoIncrementId = 1L;

    @Override
    public User save(User user) {
        User savedUser = user;
        savedUser.setId(autoIncrementId++);
        database.add(savedUser);
        return savedUser; // Single return
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> result = Optional.empty();
        for (User u : database) {
            if (u.getId().equals(id)) {
                result = Optional.of(u);
                break;
            }
        }
        return result; // Single return
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>(database);
        return result; // Single return (retorna una copia para proteger el encapsulamiento)
    }

    @Override
    public boolean deleteById(Long id) {
        boolean isDeleted = false;
        Optional<User> userOptional = findById(id);
        
        if (userOptional.isPresent()) {
            database.remove(userOptional.get());
            isDeleted = true;
        }
        
        return isDeleted; // Single return
    }

    @Override
    public User update(User user) {
        User updatedUser = null;
        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getId().equals(user.getId())) {
                database.set(i, user);
                updatedUser = user;
                break;
            }
        }
        return updatedUser; // Single return
    }
}
