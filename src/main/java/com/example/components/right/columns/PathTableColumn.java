package com.example.components.right.columns;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.io.File;

public class PathTableColumn extends TableColumn<File, String> {

    public PathTableColumn(String text) {
        super(text);
        setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPath()));
    }
}