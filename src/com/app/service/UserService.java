// Paquete: com.app.service
package com.app.service;

import com.app.exception.UserNotFoundException;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.util.Constants;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio.
 * Aplica reglas y validaciones antes de delegar en el repositorio.
 */
public class UserService {

    // Inyección de dependencias a través del constructor
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email) {
        User result = null;
        User newUser = new User(null, name, email);
        result = userRepository.save(newUser);
        return result; // Single return
    }

    public User getUser(Long id) {
        User result;
        // Se usa Optional para evitar retornar null y hacer explícito que el usuario puede no existir.
        // Esto obliga al llamador a manejar la ausencia de valor de forma controlada.
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            result = userOpt.get();
        } else {
            throw new UserNotFoundException(Constants.MSG_USER_NOT_FOUND);
        }

        return result; // Single return
    }

    public List<User> getAllUsers() {
        List<User> result = userRepository.findAll();
        return result; // Single return
    }

    public User updateUser(Long id, String newName, String newEmail) {
        User result = null;
        User existingUser = getUser(id);

        existingUser.setName(newName);
        existingUser.setEmail(newEmail);
        result = userRepository.update(existingUser);

        return result; // Single return
    }

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
