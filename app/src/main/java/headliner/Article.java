package headliner;

public class Article {
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String source;
    private String publishedAt;
    private String category;
    private String language;

    public Article(String title, String description, String url,
                   String imageUrl, String source, String publishedAt,
                   String category, String language) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.source = source;
        this.publishedAt = publishedAt;
        this.category = category;
        this.language = language;
    }

    public Article(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = "";
        this.source = "";
        this.publishedAt = "";
        this.category = "";
        this.language = "";
    }

    public String getUrlToImage() {
        return imageUrl;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
    public String getImageUrl() { return imageUrl; }
    public String getSource() { return source; }
    public String getPublishedAt() { return publishedAt; }
    public String getCategory() { return category; }
    public String getLanguage() { return language; }
}