package headliner;

public class Article {
    private String title;
    private String description;
    private String category;
    private String publishedAt;
    private String urlToImage;
    private String url;

    public Article(String title, String description, String category, String publishedAt, String urlToImage, String url) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
        this.url = url;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getPublishedAt() { return publishedAt; }
    public String getUrlToImage() { return urlToImage; }
    public String getUrl() { return url; }
}