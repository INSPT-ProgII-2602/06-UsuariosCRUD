package com.app.view;

import com.app.model.User;
import com.app.util.Constants;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola. Responsable de la interacción con el usuario:
 * muestra el menú, lee entradas y presenta mensajes.
 */
public class UserConsoleView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println(Constants.MSG_MENU_HEADER);
        System.out.println("1. Crear Usuario");
        System.out.println("2. Listar Usuarios");
        System.out.println("3. Actualizar Usuario");
        System.out.println("4. Eliminar Usuario");
        System.out.println("5. Salir");
    }

    public String readOption() {
        System.out.print("Seleccione una opción: ");
        return scanner.nextLine();
    }

    public String readName() {
        System.out.print("Ingrese nombre: ");
        return scanner.nextLine();
    }

    public String readEmail() {
        System.out.print("Ingrese email: ");
        return scanner.nextLine();
    }

    public Long readId(String prompt) {
        System.out.print(prompt);
        return Long.parseLong(scanner.nextLine());
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayUsers(List<User> users) {
        if (users.isEmpty()) {
            displayMessage("No hay usuarios registrados.");
        } else {
            for (User u : users) {
                displayMessage(u.toString());
            }
        }
    }

    public void displayGoodbye() {
        System.out.println("Cerrando aplicación...");
    }
}
