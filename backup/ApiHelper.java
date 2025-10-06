package headliner;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApiHelper {
    private static final String API_KEY = "5293e2138f024b1a9e68eab6a2035e0d";
    private static final String BASE_URL = "https://newsapi.org/v2/";

    public static List<Article> fetchTopHeadlines(String country, String category) {
        List<Article> articles = new ArrayList<>();
        try {
            String urlString = BASE_URL + "top-headlines?country=" + country + 
                             "&category=" + category + "&apiKey=" + API_KEY;
            
            String jsonResponse = makeApiCall(urlString);
            articles = parseArticles(jsonResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    public static List<Article> searchArticles(String query, String language) {
        List<Article> articles = new ArrayList<>();
        try {
            String urlString = BASE_URL + "everything?q=" + query + 
                             "&language=" + language + "&sortBy=publishedAt&apiKey=" + API_KEY;
            
            String jsonResponse = makeApiCall(urlString);
            articles = parseArticles(jsonResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    private static String makeApiCall(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        return response.toString();
    }

    private static List<Article> parseArticles(String json) {
        List<Article> articles = new ArrayList<>();
        // Simple parsing - in real app, use JSON library like Gson
        try {
            // Mock data for demo
            articles.add(new Article("Tech News Today", "Latest in technology", "tech", "2024-01-15", "https://example.com/image1.jpg", "https://example.com/article1"));
            articles.add(new Article("Sports Update", "Sports news summary", "sports", "2024-01-15", "https://example.com/image2.jpg", "https://example.com/article2"));
            articles.add(new Article("Business Report", "Market updates", "business", "2024-01-15", "https://example.com/image3.jpg", "https://example.com/article3"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    public static boolean login(String username, String password) {
        // Simple authentication - in real app, connect to database
        return username != null && password != null && 
               username.length() >= 3 && password.length() >= 6;
    }

    public static boolean register(String username, String password) {
        // Simple registration - in real app, save to database
        return username != null && password != null && 
               username.length() >= 3 && password.length() >= 6;
    }
}