// Paquete: com.app.repository
package com.app.repository;
import com.app.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    boolean deleteById(Long id);
    User update(User user);
}
