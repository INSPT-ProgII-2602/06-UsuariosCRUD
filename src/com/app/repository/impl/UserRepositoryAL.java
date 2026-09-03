// Paquete: com.app.repository.impl
package com.app.repository.impl;

import com.app.model.User;
import com.app.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio en memoria usando ArrayList.
 * Simula una base de datos en la capa de persistencia.
 */
public class UserRepositoryAL implements UserRepository {
    
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
    // Se retorna Optional<User> para indicar de forma segura si el usuario existe o no,
    // en lugar de retornar el usuario directamente o null.
    public Optional<User> findById(Long id) {
        Optional<User> result = Optional.empty();
        int i = 0;
        while (i < database.size() && result.isEmpty()) {
            if (database.get(i).getId().equals(id)) {
                result = Optional.of(database.get(i));
            }
            i++;
        }
        return result; // Single return (búsqueda lineal con while)
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
        int i = 0;
        while (i < database.size() && updatedUser == null) {
            if (database.get(i).getId().equals(user.getId())) {
                database.set(i, user);
                updatedUser = user;
            }
            i++;
        }
        return updatedUser; // Single return (búsqueda lineal con while)
    }
}
