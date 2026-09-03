// Paquete: com.app.model
package com.app.model;

import com.app.util.Constants;

/**
 * Entidad del modelo que representa un Usuario.
 * Responsable de mantener la consistencia de sus datos mediante validaciones en setters.
 */
public class User {
    private Long id;
    private String name;
    private String email;

    /**
     * Constructor que inicializa el usuario delegando la validación a cada setter.
     */
    public User(Long id, String name, String email) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.MSG_INVALID_NAME);
        }
        this.name = name;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.MSG_INVALID_EMAIL);
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "User[ID=" + id + ", Nombre=" + name + ", Email=" + email + "]";
    }
}
