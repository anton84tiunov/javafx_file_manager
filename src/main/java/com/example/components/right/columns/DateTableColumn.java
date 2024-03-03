package com.example.components.right.columns;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTableColumn extends TableColumn<File, String> {

    public DateTableColumn(String text) {
        super(text);
        setCellValueFactory(cellData -> {
            File file = cellData.getValue();
            long lastModified = file.lastModified();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(dateFormat.format(new Date(lastModified)));
        });
    }
}