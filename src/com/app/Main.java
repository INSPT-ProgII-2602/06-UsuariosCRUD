// Paquete: com.app
package com.app;

import com.app.controller.UserController;
import com.app.repository.UserRepository;
import com.app.repository.impl.UserRepositoryImpl;
import com.app.service.UserService;
import com.app.service.impl.UserServiceImpl;
import com.app.view.UserConsoleView;

public class Main {
    public static void main(String[] args) {
        // 1. Instanciar la capa de Datos
        UserRepository repository = new UserRepositoryImpl();
        
        // 2. Instanciar la capa de Negocio (Inyectando Datos)
        UserService service = new UserServiceImpl(repository);
        
        // 3. Instanciar la capa de Control (Inyectando Negocio)
        UserController controller = new UserController(service);
        
        // 4. Instanciar la capa de Vista (Inyectando Control)
        UserConsoleView view = new UserConsoleView(controller);
        
        // 5. Arrancar la aplicación
        view.start();
    }
}