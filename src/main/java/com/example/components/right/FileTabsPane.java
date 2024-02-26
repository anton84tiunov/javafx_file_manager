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

public class FileTabsPane extends TabPane {

    // Создаем кисть для фона
    BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(90, 170, 255), null, null);

    // Создаем объект Background с установленным фоном
    Background background = new Background(backgroundFill);

    public FileTabsPane() {

    }

    public void addTab(File file) {
       
        Tab newTab = new Tab(file.getName());
        VBox pane = new VBox();
    
        pane.setBackground(background);
        newTab.setContent(pane);
        getTabs().add(newTab);
        
        // Если файл является папкой, добавляем содержимое первой вложенной папки в pane
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null && children.length > 0) {
                // Создаем TableView или другой контейнер для отображения содержимого папки
                TableView<File> tableView = createTableView();
                
                // Добавляем содержимое папки в TableView
                for (File child : children) {
                    // Добавляем файлы в TableView
                    tableView.getItems().add(child);
                }
                
                // Добавляем TableView в pane
                pane.getChildren().add(tableView);
            }
        }
    }

    // Пример создания TableView (вам нужно адаптировать это под свои нужды)
   private TableView<File> createTableView() {
    TableView<File> tableView = new TableView<>();
    VBox.setVgrow(tableView, Priority.ALWAYS);
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
    
    tableView.getColumns().add(nameColumn);
    tableView.getColumns().add(pathColumn);
    tableView.getColumns().add(sizeColumn);
    tableView.getColumns().add(countColumn);
    tableView.getColumns().add(dateColumn);

    
    return tableView;
}

}