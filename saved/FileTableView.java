package com.example.components.right;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class FileTableView extends TableView<File>{


    public FileTableView(){
        VBox.setVgrow(this, Priority.ALWAYS);
        // tableView.setPrefWidth(2000); // Ширина в пикселях
        // tableView.setPrefHeight(2000); // Высота в пикселях
    
        TableColumn<File, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<File, String> pathColumn = new TableColumn<>("Path");
        pathColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPath()));
        
        TableColumn<File, Long> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(cellData -> {
            File file = cellData.getValue();
            long size = file.isDirectory() ? 0 : file.length();
            return new SimpleLongProperty(size).asObject();
        });
        
        TableColumn<File, String> countColumn = new TableColumn<>("Count");
        countColumn.setCellValueFactory(cellData -> {
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
            return new SimpleStringProperty("F: " + numFiles + ", D " + numDirectories);
        });
        
        TableColumn<File, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            File file = cellData.getValue();
            long lastModified = file.lastModified();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(dateFormat.format(new Date(lastModified)));
        });
        
        getColumns().add(nameColumn);
        getColumns().add(pathColumn);
        getColumns().add(sizeColumn);
        getColumns().add(countColumn);
        getColumns().add(dateColumn);
    
        
    }



}
