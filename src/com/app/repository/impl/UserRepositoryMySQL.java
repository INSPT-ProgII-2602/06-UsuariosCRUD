package com.app.repository.impl;

import com.app.config.DatabaseConfig;
import com.app.model.User;
import com.app.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC del repositorio de usuarios.
 * Usa PreparedStatement para prevenir SQL Injection.
 * Maneja transacciones manualmente cuando es necesario.
 * Envuelve SQLException en RuntimeException.
 */
public class UserRepositoryMySQL implements UserRepository {
    
    static {
        DatabaseConfig.loadDriver();
    }
    
    // ============================================
    // SQL CONSTANTS
    // ============================================
    private static final String SQL_SAVE = 
        "INSERT INTO usuario (nombre, mail) VALUES (?, ?)";
    
    private static final String SQL_FIND_BY_ID = 
        "SELECT id, nombre, mail FROM usuario WHERE id = ?";
    
    private static final String SQL_FIND_ALL = 
        "SELECT id, nombre, mail FROM usuario ORDER BY id";
    
    private static final String SQL_UPDATE = 
        "UPDATE usuario SET nombre = ?, mail = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM usuario WHERE id = ?";
    
    private static final String SQL_EXISTS = 
        "SELECT 1 FROM usuario WHERE id = ?";
    
    // ============================================
    // CONEXIÓN Y UTILIDADES
    // ============================================
    
    private Connection getConnection() throws SQLException {
        return DatabaseConfig.getConnection();
    }
    
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3)
        );
    }
    
    // ============================================
    // CRUD - AUTO-COMMIT (Simple, una operación = una transacción)
    // ============================================
    
    @Override
    public User save(User user) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Inserción falló, ninguna fila afectada");
            }
            
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                } else {
                    throw new RuntimeException("No se generó ID auto-incremental");
                }
            }
            return user;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Optional<User> findById(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<User> findAll() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapRow(rs));
            }
            return users;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean deleteById(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            
            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public User update(User user) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setLong(3, user.getId());
            
            int affected = ps.executeUpdate();
            if (affected == 0) {
                return null;  // No encontrado
            }
            return user;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    // ============================================
    // CRUD - TRANSACCIONES MANUALES (Operaciones compuestas)
    // ============================================
    
    /**
     * Ejemplo: Transferir usuario entre sistemas (operación atómica múltiple).
     * Requiere deshabilitar auto-commit y manejar commit/rollback manual.
     */
    public User saveWithAudit(User user, String auditAction) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);  // INICIAR TRANSACCIÓN
            
            // 1. Insertar usuario
            Long generatedId;
            try (PreparedStatement ps = conn.prepareStatement(
                    SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.executeUpdate();
                
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        generatedId = keys.getLong(1);
                        user.setId(generatedId);
                    } else {
                        throw new RuntimeException("No se generó ID");
                    }
                }
            }
            
            // 2. Insertar auditoría (tabla hipotética audit_log)
            String sqlAudit = "INSERT INTO audit_log (action, entity_id, entity_type) VALUES (?, ?, 'USER')";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudit)) {
                ps.setString(1, auditAction);
                ps.setLong(2, generatedId);
                ps.executeUpdate();
            }
            
            conn.commit();  // CONFIRMAR TRANSACCIÓN
            return user;
            
        } catch (SQLException e) {
            rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
        }
    }
    
    /**
     * Ejemplo: Actualizar usuario + log de cambios (transacción manual).
     */
    public boolean updateWithLog(User user, String changedBy) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // 1. Actualizar usuario
            int affected;
            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setLong(3, user.getId());
                affected = ps.executeUpdate();
            }
            
            if (affected == 0) {
                conn.rollback();
                return false;
            }
            
            // 2. Log de cambios
            String sqlLog = "INSERT INTO user_changes (user_id, changed_by, change_date) VALUES (?, ?, NOW())";
            try (PreparedStatement ps = conn.prepareStatement(sqlLog)) {
                ps.setLong(1, user.getId());
                ps.setString(2, changedBy);
                ps.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
        }
    }
    
    // ============================================
    // HELPERS PARA TRANSACCIONES MANUALES
    // ============================================
    
    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
        }
    }
    
    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try { 
                conn.setAutoCommit(true);  // Restaurar default
                conn.close(); 
            } catch (SQLException ignored) {}
        }
    }
}