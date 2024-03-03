package com.example.components.right.columns;

import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.TableColumn;

import java.io.File;

public class SizeTableColumn extends TableColumn<File, Long> {

    public SizeTableColumn(String text) {
        super(text);
        setCellValueFactory(cellData -> {
            File file = cellData.getValue();
            long size = file.isDirectory() ? 0 : file.length();
            return new SimpleLongProperty(size).asObject();
        });
    }
}