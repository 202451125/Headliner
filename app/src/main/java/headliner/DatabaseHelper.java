package headliner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
                    
                    st.execute("""
                        CREATE TABLE IF NOT EXISTS bookmarks(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            user_id INTEGER NOT NULL,
                            title TEXT,
                            description TEXT,
                            url TEXT NOT NULL UNIQUE,
                            imageUrl TEXT,
                            source TEXT,
                            publishedAt TEXT,
                            category TEXT,
                            language TEXT,
                            bookmarked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(id)
                        )
                    """);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    public boolean addBookmark(int userId, Article article) {
        String sql = "INSERT OR IGNORE INTO bookmarks(user_id, title, description, url, imageUrl, source, publishedAt, category, language) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, article.getTitle());
            ps.setString(3, article.getDescription());
            ps.setString(4, article.getUrl());
            ps.setString(5, article.getImageUrl());
            ps.setString(6, article.getSource());
            ps.setString(7, article.getPublishedAt());
            ps.setString(8, article.getCategory());
            ps.setString(9, article.getLanguage());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding bookmark: " + e.getMessage());
            return false;
        }
    }

    public boolean removeBookmark(int userId, String articleUrl) {
        String sql = "DELETE FROM bookmarks WHERE user_id = ? AND url = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, articleUrl);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing bookmark: " + e.getMessage());
            return false;
        }
    }

    public boolean isBookmarked(int userId, String articleUrl) {
        String sql = "SELECT 1 FROM bookmarks WHERE user_id = ? AND url = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, articleUrl);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking bookmark: " + e.getMessage());
            return false;
        }
    }

    public List<Article> getBookmarks(int userId) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM bookmarks WHERE user_id = ? ORDER BY bookmarked_at DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                articles.add(new Article(
                    rs.getString("title"), rs.getString("description"), rs.getString("url"),
                    rs.getString("imageUrl"), rs.getString("source"), rs.getString("publishedAt"),
                    rs.getString("category"), rs.getString("language")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookmarks: " + e.getMessage());
        }
        return articles;
    }

    public int getUserId(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Get user ID failed: " + e.getMessage());
        }
        return -1;
    }

    // --- Original User Methods Below ---
    public boolean registerUser(String username, String rawPassword) { /* ... same as before ... */ return false; }
    public boolean registerUserWithEmail(String username, String email, String rawPassword) { /* ... same as before ... */ return false; }
    public boolean validateUser(String username, String rawPassword) { /* ... same as before ... */ return false; }
    public String getUserEmail(String username) { /* ... same as before ... */ return null; }
    public boolean updateUserProfile(String username, String profilePic, String theme) { /* ... same as before ... */ return false; }
    public String getUserTheme(String username) { /* ... same as before ... */ return "light"; }
    public String getUserProfilePic(String username) { /* ... same as before ... */ return "default"; }
    private static String sha256(String s) { /* ... same as before ... */ return ""; }
}