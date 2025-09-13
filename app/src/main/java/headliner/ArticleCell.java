package headliner;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ArticleCell extends ListCell<Article> {
    private final HBox root = new HBox(12);
    private final ImageView thumb = new ImageView();
    private final VBox contentBox = new VBox(6);
    private final Label title = new Label();
    private final Label desc = new Label();

    public ArticleCell() {
        thumb.setFitWidth(160);
        thumb.setFitHeight(90);
        thumb.setPreserveRatio(true);

        title.setWrapText(true);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        desc.setWrapText(true);
        desc.setMaxWidth(420);

        contentBox.getChildren().addAll(title, desc);
        root.getChildren().addAll(thumb, contentBox);
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

            if (item.getUrlToImage() != null && !item.getUrlToImage().isBlank()) {
                try {
                    Image img = new Image(item.getUrlToImage(), 160, 90, true, true, true);
                    thumb.setImage(img);
                } catch (Exception e) {
                    thumb.setImage(null);
                }
            } else {
                thumb.setImage(null);
            }

            setGraphic(root);
        }
    }
}