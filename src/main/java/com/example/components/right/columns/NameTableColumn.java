package com.example.components.right.columns;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;

public class NameTableColumn extends TableColumn<File, String> {

    public NameTableColumn(String text) {
        super(text);
        setCellValueFactory(new PropertyValueFactory<>("name"));
    }
}