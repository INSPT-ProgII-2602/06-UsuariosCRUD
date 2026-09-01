// Paquete: com.app.controller
package com.app.controller;

import com.app.exception.UserNotFoundException;
import com.app.model.User;
import com.app.service.UserService;
import java.util.List;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public String handleCreate(String name, String email) {
        String response = "";
        try {
            User user = userService.createUser(name, email);
            response = "Éxito: Usuario creado con ID " + user.getId();
        } catch (IllegalArgumentException e) {
            response = e.getMessage();
        }
        return response; // Single return
    }

    public String handleFindAll() {
        String response = "";
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            response = "No hay usuarios registrados.";
        } else {
            StringBuilder sb = new StringBuilder();
            for (User u : users) {
                sb.append(u.toString()).append("\n");
            }
            response = sb.toString();
        }
        return response; // Single return
    }

    public String handleUpdate(Long id, String name, String email) {
        String response = "";
        try {
            userService.updateUser(id, name, email);
            response = "Éxito: Usuario actualizado.";
        } catch (UserNotFoundException e) {
            response = e.getMessage();
        }
        return response; // Single return
    }

    public String handleDelete(Long id) {
        String response = "";
        try {
            userService.deleteUser(id);
            response = "Éxito: Usuario eliminado.";
        } catch (UserNotFoundException e) {
            response = e.getMessage();
        }
        return response; // Single return
    }
}
