package com.app.controller;

import com.app.exception.UserNotFoundException;
import com.app.model.User;
import com.app.service.UserService;
import com.app.view.UserConsoleView;
import java.util.List;

/**
 * Controlador. Orquesta las operaciones entre la Vista y el Servicio,
 * captura excepciones y coordina el flujo de la aplicación.
 */
public class UserController {
    private final UserService userService;
    private final UserConsoleView view;

    public UserController(UserService userService, UserConsoleView view) {
        this.userService = userService;
        this.view = view;
    }

    public void start() {
        boolean running = true;
        while (running) {
            view.displayMenu();
            String option = view.readOption();
            switch (option) {
                case "1": handleCreate(); break;
                case "2": handleFindAll(); break;
                case "3": handleUpdate(); break;
                case "4": handleDelete(); break;
                case "5": running = false; view.displayGoodbye(); break;
                default: view.displayMessage("Opción no válida.");
            }
        }
    }

    private void handleCreate() {
        try {
            String name = view.readName();
            String email = view.readEmail();
            User user = userService.createUser(name, email);
            view.displayMessage("Éxito: Usuario creado con ID " + user.getId());
        } catch (IllegalArgumentException e) {
            view.displayMessage(e.getMessage());
        }
    }

    private void handleFindAll() {
        view.displayMessage("\n--- Lista de Usuarios ---");
        List<User> users = userService.getAllUsers();
        view.displayUsers(users);
    }

    private void handleUpdate() {
        try {
            Long idToUpdate = view.readId("Ingrese ID del usuario a actualizar: ");
            String newName = view.readName();
            String newEmail = view.readEmail();
            userService.updateUser(idToUpdate, newName, newEmail);
            view.displayMessage("Éxito: Usuario actualizado.");
        } catch (UserNotFoundException | IllegalArgumentException e) {
            view.displayMessage(e.getMessage());
        }
    }

    private void handleDelete() {
        try {
            Long idToDelete = view.readId("Ingrese ID del usuario a eliminar: ");
            userService.deleteUser(idToDelete);
            view.displayMessage("Éxito: Usuario eliminado.");
        } catch (UserNotFoundException e) {
            view.displayMessage(e.getMessage());
        }
    }
}
