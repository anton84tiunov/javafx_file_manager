package com.example.components.right.columns;


import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ImageTableColumn extends TableColumn<File, ImageView> {

    public ImageTableColumn(String text) {
        super(text);
        setCellValueFactory(cellData -> {
            File file = cellData.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitWidth(24);
            imageView.setFitHeight(24);
            imageView.setImage(getImageForFile(file));
            return new SimpleObjectProperty<>(imageView);
        });
    }

    private Image getImageForFile(File file) {
        if (file.isDirectory()) {
            return new Image(ImageTableColumn.class.getResourceAsStream("/com/example/icons/folder_icon.png"));
        } else {
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

            switch (extension) {
                case "txt":
                    return new Image(ImageTableColumn.class.getResourceAsStream("/com/example/icons/txt_icon.png"));
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                    return new Image(ImageTableColumn.class.getResourceAsStream("/com/example/icons/image_icon.png"));
                case "pdf":
                    return new Image(ImageTableColumn.class.getResourceAsStream("/com/example/icons/PDF.jpg"));
                default:
                    return new Image(ImageTableColumn.class.getResourceAsStream("/com/example/icons/file_icon.png"));
            }
        }
    }
}