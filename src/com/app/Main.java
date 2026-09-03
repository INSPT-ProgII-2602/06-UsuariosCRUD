package com.app;

import com.app.controller.UserController;
import com.app.repository.UserRepository;
import com.app.repository.impl.UserRepositoryAL;
import com.app.service.UserService;
import com.app.view.UserConsoleView;

/**
 * Punto de entrada de la aplicación.
 * Ensambla manualmente las capas del CRUD y delega el inicio al controlador.
 */
public class Main {
    public static void main(String[] args) {
        UserRepository repository = new UserRepositoryAL();
        UserService service = new UserService(repository);
        UserConsoleView view = new UserConsoleView();
        UserController controller = new UserController(service, view);
        controller.start();
    }
}