// Paquete: com.app.repository
package com.app.repository;
import com.app.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio. Define el contrato de persistencia de usuarios.
 * Se usa Optional en findById para representar explícitamente la ausencia de un usuario,
 * evitando retornar null y reduciendo el riesgo de NullPointerException en las capas superiores.
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    boolean deleteById(Long id);
    User update(User user);
}
