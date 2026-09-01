// Paquete: com.app.view
package com.app.view;

import com.app.controller.UserController;
import com.app.util.Constants;
import java.util.Scanner;

public class UserConsoleView {

    private final UserController controller;

    // Inyectamos el controlador en la vista
    public UserConsoleView(UserController controller) {
        this.controller = controller;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println(Constants.MSG_MENU_HEADER);
            System.out.println("1. Crear Usuario");
            System.out.println("2. Listar Usuarios");
            System.out.println("3. Actualizar Usuario");
            System.out.println("4. Eliminar Usuario");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    System.out.print("Ingrese nombre: ");
                    String name = scanner.nextLine();
                    System.out.print("Ingrese email: ");
                    String email = scanner.nextLine();
                    System.out.println(controller.handleCreate(name, email));
                    break;
                case "2":
                    System.out.println("\n--- Lista de Usuarios ---");
                    System.out.println(controller.handleFindAll());
                    break;
                case "3":
                    System.out.print("Ingrese ID del usuario a actualizar: ");
                    Long idToUpdate = Long.parseLong(scanner.nextLine());
                    System.out.print("Ingrese nuevo nombre: ");
                    String newName = scanner.nextLine();
                    System.out.print("Ingrese nuevo email: ");
                    String newEmail = scanner.nextLine();
                    System.out.println(controller.handleUpdate(idToUpdate, newName, newEmail));
                    break;
                case "4":
                    System.out.print("Ingrese ID del usuario a eliminar: ");
                    Long idToDelete = Long.parseLong(scanner.nextLine());
                    System.out.println(controller.handleDelete(idToDelete));
                    break;
                case "5":
                    running = false;
                    System.out.println("Cerrando aplicación...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        // No cerramos el Scanner para proteger System.in
    }
}
