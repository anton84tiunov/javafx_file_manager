package com.example.components.right;

import java.io.File;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FileTabsPane extends TabPane {

    // Создаем кисть для фона
    BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(90, 170, 255), null, null);

    // Создаем объект Background с установленным фоном
    Background background = new Background(backgroundFill);

    // FileTableView tableView = new FileTableView();

    public FileTabsPane() {

    }

    public void addTab(File file) {
        // VBox pane = new VBox();

        FileTableView tableView = new FileTableView();
        Tab newTab = new Tab(file.getName());

        // pane.setBackground(background);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        
        getTabs().add(newTab);

        // Создаем TableView или другой контейнер для отображения содержимого папки
        ToolBar toolbar = new ToolBar(tableView.back, tableView.forward, tableView.hideShowMenuButton,
                tableView.searchField);
        // toolbar.setBackground(background);
        VBox vBox = new VBox(toolbar, tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.setItems(file);
        // Добавляем TableView в pane
        newTab.setContent(vBox);
        // pane.getChildren().add(vBox);

    }

}