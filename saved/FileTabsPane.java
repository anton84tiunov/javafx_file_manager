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
                FileTableView tableView = new FileTableView();
                
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

  

}