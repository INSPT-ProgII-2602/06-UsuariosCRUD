package com.app;

import com.app.config.DatabaseConfig;
import com.app.controller.UserController;
import com.app.repository.UserRepository;
import com.app.repository.impl.UserRepositoryAL;
import com.app.repository.impl.UserRepositoryMySQL;
import com.app.service.UserService;
import com.app.view.UserConsoleView;
import java.sql.SQLException;

/**
 * Punto de entrada de la aplicación.
 * Ensambla manualmente las capas del CRUD y delega el inicio al controlador.
 */
public class Main {
    public static void main(String[] args) {
        
        try {
            DatabaseConfig.loadDriver();
            DatabaseConfig.getConnection();
            
            UserRepository repository = new UserRepositoryMySQL();
            UserService service = new UserService(repository);
            UserConsoleView view = new UserConsoleView();
            UserController controller = new UserController(service, view);
            controller.start();
        } catch (SQLException ex) {
            System.getLogger(Main.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}