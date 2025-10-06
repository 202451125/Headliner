package headliner;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
<<<<<<< HEAD
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
=======
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612

public class ArticleCell extends ListCell<Article> {
    private final HBox root = new HBox(20);
    private final ImageView thumb = new ImageView();
    private final VBox contentBox = new VBox(8);
    private final Label title = new Label();
    private final Label desc = new Label();
    private final Label meta = new Label();
    private final Rectangle imagePlaceholder = new Rectangle(160, 90);

    public ArticleCell() {
        // Setup thumbnail with rounded corners
        thumb.setFitWidth(160);
        thumb.setFitHeight(90);
        thumb.setPreserveRatio(true);
        thumb.setSmooth(true);
        
        // Create rounded corners for image
        Rectangle clip = new Rectangle(160, 90);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        thumb.setClip(clip);
        
        // Image placeholder
        imagePlaceholder.setArcWidth(12);
        imagePlaceholder.setArcHeight(12);
        imagePlaceholder.setFill(Color.web("#f1f5f9"));
        imagePlaceholder.setStroke(Color.web("#e2e8f0"));
        imagePlaceholder.setStrokeWidth(1);

        // Title styling
        title.setWrapText(true);
        title.setStyle("-fx-font-weight: 800; -fx-font-size: 16px; -fx-text-fill: #1e293b;");
        title.setMaxWidth(500);

        // Description styling
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        desc.setMaxWidth(500);
        desc.setMaxHeight(40);

        // Meta info styling
        meta.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8; -fx-font-weight: 600;");

        contentBox.getChildren().addAll(title, desc, meta);
        contentBox.setStyle("-fx-padding: 15 0;");
        
        root.getChildren().addAll(thumb, contentBox);
<<<<<<< HEAD
        root.setStyle("-fx-padding: 20; -fx-background-radius: 16;");
=======
>>>>>>> fedec1065c6b9237717e0e8de2583da1bbcc5612
    }

    @Override
    protected void updateItem(Article item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            title.setText(item.getTitle() == null ? "" : item.getTitle());
            desc.setText(item.getDescription() == null ? "" : item.getDescription());
            
            // Create meta info
            String metaText = String.format("%s • %s • %s", 
                item.getSource(), 
                formatDate(item.getPublishedAt()),
                item.getCategory().toUpperCase()
            );
            meta.setText(metaText);

            // Load image with better error handling
            if (item.getUrlToImage() != null && !item.getUrlToImage().isBlank()) {
                try {
                    Image img = new Image(item.getUrlToImage(), 160, 90, true, true, true);
                    thumb.setImage(img);
                    // Hide placeholder when image loads
                    if (root.getChildren().contains(imagePlaceholder)) {
                        root.getChildren().remove(imagePlaceholder);
                    }
                    if (!root.getChildren().contains(thumb)) {
                        root.getChildren().add(0, thumb);
                    }
                } catch (Exception e) {
                    // Show placeholder on error
                    thumb.setImage(null);
                    if (!root.getChildren().contains(imagePlaceholder)) {
                        root.getChildren().add(0, imagePlaceholder);
                    }
                }
            } else {
                // Show placeholder when no image
                thumb.setImage(null);
                if (!root.getChildren().contains(imagePlaceholder)) {
                    root.getChildren().add(0, imagePlaceholder);
                }
            }

            setGraphic(root);
        }
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "Recent";
        }
        try {
            return dateString.substring(0, 10);
        } catch (Exception e) {
            return "Recent";
        }
    }
}