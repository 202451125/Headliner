package headliner;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:headliner.db";
    
    public DatabaseHelper() {
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                try (Statement st = conn.createStatement()) {
                    st.execute("""
                        CREATE TABLE IF NOT EXISTS users(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT UNIQUE NOT NULL,
                            email TEXT,
                            password_hash TEXT NOT NULL,
                            profile_pic TEXT DEFAULT 'default',
                            theme TEXT DEFAULT 'light',
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                        )
                    """);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    public boolean registerUser(String username, String rawPassword) {
        return registerUserWithEmail(username, null, rawPassword);
    }

    public boolean registerUserWithEmail(String username, String email, String rawPassword) {
        if (username == null || username.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            return false;
        }
        
        String sql = "INSERT INTO users(username, email, password_hash) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, email != null ? email.trim() : null);
            ps.setString(3, sha256(rawPassword));
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean validateUser(String username, String rawPassword) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String stored = rs.getString("password_hash");
                    return stored.equals(sha256(rawPassword));
                }
            }
        } catch (SQLException e) {
            System.err.println("Login check failed: " + e.getMessage());
        }
        return false;
    }

    public String getUserEmail(String username) {
        String sql = "SELECT email FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            System.err.println("Get email failed: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUserProfile(String username, String profilePic, String theme) {
        String sql = "UPDATE users SET profile_pic = ?, theme = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profilePic);
            ps.setString(2, theme);
            ps.setString(3, username.trim());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Update profile failed: " + e.getMessage());
            return false;
        }
    }

    public String getUserTheme(String username) {
        String sql = "SELECT theme FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("theme");
                }
            }
        } catch (SQLException e) {
            System.err.println("Get theme failed: " + e.getMessage());
        }
        return "light";
    }

    public String getUserProfilePic(String username) {
        String sql = "SELECT profile_pic FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("profile_pic");
                }
            }
        } catch (SQLException e) {
            System.err.println("Get profile pic failed: " + e.getMessage());
        }
        return "default";
    }

    private static String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : out) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}