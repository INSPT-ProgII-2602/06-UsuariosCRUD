// Paquete: com.app.service
package com.app.service;
import com.app.model.User;
import java.util.List;

public interface UserService {
    User createUser(String name, String email);
    User getUser(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, String newName, String newEmail);
    boolean deleteUser(Long id);
}
