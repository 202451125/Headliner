package headliner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiHelper {
    private static final String BASE_URL = "https://newsapi.org/v2";
<<<<<<< HEAD
    private static final String API_KEY = "5293e2138f024b1a9e68eab6a2035e0d";
=======
    // SECURITY: API key from environment variable with fallback
    private static final String API_KEY = System.getenv().getOrDefault("NEWS_API_KEY", "5293e2138f024b1a9e68eab6a2035e0d");
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612

    // Temporary user storage
    private static Map<String, String> tempUsers = new HashMap<>();
    private static final String USER_FILE = "users.txt";

    // --- LOGIN ---
    public static boolean login(String username, String password) {
        System.out.println("Login attempt: " + username);
        
        // SECURITY: Input validation
        if (username == null || username.length() < 3 || password == null || password.length() < 6) {
            System.out.println("❌ Invalid username or password format");
            return false;
        }
        
        // First try database
        try {
            DatabaseHelper db = new DatabaseHelper();
            boolean dbResult = db.validateUser(username, password);
            if (dbResult) {
                System.out.println("✅ Login successful via database");
                return true;
            }
        } catch (Exception e) {
            System.out.println("❌ Database login failed: " + e.getMessage());
        }
        
        // Try to load from file
        try {
            loadUsersFromFile();
        } catch (IOException e) {
            System.out.println("❌ Could not load users from file: " + e.getMessage());
        }
        
        // Check in-memory storage
        if (tempUsers.containsKey(username) && tempUsers.get(username).equals(password)) {
            System.out.println("✅ Login successful via memory storage");
            return true;
        }
        
        System.out.println("❌ Login failed - user not found");
        return false;
    }

    // --- REGISTER ---
    public static boolean register(String username, String password) {
        return registerWithEmail(username, null, password);
    }

    public static boolean registerWithEmail(String username, String email, String password) {
        System.out.println("Registration attempt: " + username + " Email: " + email);
        
<<<<<<< HEAD
        // First try database with email
=======
        // SECURITY: Input validation
        if (username == null || username.length() < 3 || password == null || password.length() < 6) {
            System.out.println("❌ Invalid username or password format");
            return false;
        }
        
        // First try database
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612
        try {
            DatabaseHelper db = new DatabaseHelper();
            boolean dbResult = db.registerUserWithEmail(username, email, password);
            if (dbResult) {
                System.out.println("✅ Registration successful via database with email");
                return true;
            }
        } catch (Exception e) {
            System.out.println("❌ Database registration failed: " + e.getMessage());
        }
        
        // Fallback to file-based storage
        try {
            // Save to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE, true))) {
                writer.println(username + ":" + password);
            }
            
            // Also store in memory
            tempUsers.put(username, password);
            
            System.out.println("✅ Registration successful via file storage");
            return true;
        } catch (IOException e) {
            System.out.println("❌ File registration failed: " + e.getMessage());
            
            // Final fallback: in-memory only
            tempUsers.put(username, password);
            System.out.println("✅ Registration successful via memory storage");
            return true;
        }
    }

    // Helper method to load users from file
    private static void loadUsersFromFile() throws IOException {
        File file = new File(USER_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        tempUsers.put(parts[0], parts[1]);
                    }
                }
            }
        }
    }

    // --- FETCH TOP HEADLINES ---
    public static List<Article> fetchTopHeadlines(String country, String category) {
        List<Article> articles = new ArrayList<>();
        try {
            // Input validation for API parameters
            if (country == null || country.length() != 2) {
                country = "us"; // Default to US
            }
            
            String urlString = String.format("%s/top-headlines?country=%s&category=%s&apiKey=%s", 
                                           BASE_URL, country, category, API_KEY);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(10000); // Timeout after 10 seconds

            int code = conn.getResponseCode();
            if (code == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    JSONObject json = new JSONObject(response.toString());
                    JSONArray jsonArticles = json.getJSONArray("articles");

                    for (int i = 0; i < jsonArticles.length(); i++) {
                        JSONObject obj = jsonArticles.getJSONObject(i);

                        String title = obj.optString("title", "No Title");
                        String description = obj.optString("description", "");
                        String urlToArticle = obj.optString("url", "");
                        String imageUrl = obj.optString("urlToImage", "");
                        String source = obj.getJSONObject("source").optString("name", "");
                        String publishedAt = obj.optString("publishedAt", "");

                        Article article = new Article(title, description, urlToArticle, imageUrl, source, publishedAt, category, "en");
                        articles.add(article);
                    }
                }
            } else {
                System.out.println("❌ API Error: HTTP " + code);
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching news: " + e.getMessage());
            // Add sample data for demo
            articles.add(new Article("Sample News 1", "This is a sample news description", "https://example.com", 
                                   "", "Sample Source", "2023-01-01", "general", "en"));
            articles.add(new Article("Sample News 2", "Another sample news description", "https://example.com", 
                                   "", "Sample Source", "2023-01-01", "general", "en"));
        }
        return articles;
    }

    // --- SEARCH ARTICLES --- (ADD THIS METHOD)
    public static List<Article> searchArticles(String query, String language) {
        List<Article> articles = new ArrayList<>();
        try {
            String urlString = String.format("%s/everything?q=%s&language=%s&sortBy=publishedAt&apiKey=%s", 
                                           BASE_URL, query, language, API_KEY);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            if (code == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    JSONObject json = new JSONObject(response.toString());
                    JSONArray jsonArticles = json.getJSONArray("articles");

                    for (int i = 0; i < jsonArticles.length(); i++) {
                        JSONObject obj = jsonArticles.getJSONObject(i);

                        String title = obj.optString("title", "No Title");
                        String description = obj.optString("description", "");
                        String urlToArticle = obj.optString("url", "");
                        String imageUrl = obj.optString("urlToImage", "");
                        String source = obj.getJSONObject("source").optString("name", "");
                        String publishedAt = obj.optString("publishedAt", "");

                        Article article = new Article(title, description, urlToArticle, imageUrl, source, publishedAt, "search", language);
                        articles.add(article);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching news: " + e.getMessage());
            // Return empty list instead of sample data for search
        }
        return articles;
    }

    // --- NEW METHODS FOR SESSION MANAGEMENT ---
    public static boolean isUserLoggedIn() {
        return UserSessionManager.isUserLoggedIn();
    }

    public static String getCurrentUsername() {
        return UserSessionManager.getCurrentUser();
    }
}