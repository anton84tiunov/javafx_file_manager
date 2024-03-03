package com.example.components.right.columns;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.io.File;

public class CountTableColumn extends TableColumn<File, String> {

    public CountTableColumn(String text) {
        super(text);
        setCellValueFactory(cellData -> {
            File file = cellData.getValue();
            int numFiles = 0;
            int numDirectories = 0;
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isDirectory()) {
                            numDirectories++;
                        } else {
                            numFiles++;
                        }
                    }
                }
            }
            return new SimpleStringProperty("F: " + numFiles + ", D: " + numDirectories);
        });
    }
}