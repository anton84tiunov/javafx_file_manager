package com.example.components.left;

import java.io.File;
import java.text.SimpleDateFormat;

import com.example.components.right.FileTabsPane;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.converter.LongStringConverter;

public class FileTreeTableView extends TreeTableView<File> {

    private ContextMenu contextMenu = new ContextMenu();;
    FileTabsPane tabPane;

    public FileTreeTableView(File rootDirectory, FileTabsPane tabPane) {

        this.tabPane = tabPane;
        // Устанавливаем предпочтительные размеры для FileTreeTableView
        this.setPrefWidth(2000); // Ширина в пикселях
        this.setPrefHeight(2000); // Высота в пикселях

        TreeItem<File> rootItem = createTreeTableView(rootDirectory);
        setRoot(rootItem);
        
        // Создаем столбец для отображения имен файлов
        TreeTableColumn<File, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getName()));
   
        getColumns().add(nameColumn);
        
        // Создаем столбец для отображения путей файлов
        TreeTableColumn<File, String> pathColumn = new TreeTableColumn<>("Path");
        pathColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getPath()));
        getColumns().add(pathColumn);
        
        // Создаем столбец для отображения размера файлов
        TreeTableColumn<File, Long> sizeColumn = new TreeTableColumn<>("Size");
        sizeColumn.setCellValueFactory(cellData -> {
            File file = cellData.getValue().getValue();
            long size = file.isDirectory() ? 0 : file.length();
            return new SimpleLongProperty(size).asObject();
        });
        getColumns().add(sizeColumn);

             // Создаем столбец для отображения количества файлов и папок
             TreeTableColumn<File, String> countColumn = new TreeTableColumn<>("Count");
             countColumn.setCellValueFactory(cellData -> {
                 File file = cellData.getValue().getValue();
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
             getColumns().add(countColumn);
        
        // Создаем столбец для отображения даты последнего изменения файлов
        TreeTableColumn<File, String> dateColumn = new TreeTableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            File file = cellData.getValue().getValue();
            long lastModified = file.lastModified();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(dateFormat.format(lastModified));
        });
        getColumns().add(dateColumn);
        
        // Устанавливаем ячейки редактируемыми
        nameColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        // Установка начального размера столбца "Name"
        nameColumn.setPrefWidth(150);
        pathColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        sizeColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new LongStringConverter()));
        countColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        dateColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());


          // Добавляем обработчик событий мыши к TreeView
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                // Получаем элемент, по которому произведен щелчок
                TreeItem<File> selectedItem = this.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem instanceof FileTreeItem) {
                    // Обработка события щелчка правой кнопкой мыши на элементе FileTreeItem
                    showContextMenuFileTreeItem((FileTreeItem) selectedItem, event);
                }
            }
        });

    }

    private void showContextMenuFileTreeItem(FileTreeItem item, MouseEvent event ) {
        // Обработка действий при клике правой клавишей мыши на элементе FileTreeItem
        File file = item.getValue();
        // contextMenu = new ContextMenu();
       // Скрыть предыдущее контекстное меню, если оно было открыто
       if (contextMenu.isShowing()) {
            contextMenu.hide();
           
        }
        contextMenu.getItems().clear();

        // создание пунктов меню
        MenuItem menuItem1 = new MenuItem("открыть в новой вкладке");
        MenuItem menuItem2 = new MenuItem("открыть как корневую дирректорию");

        // добавление пунктов меню в контекстное меню
        contextMenu.getItems().addAll(menuItem1, menuItem2);

        // обработчики событий для пунктов меню
        menuItem1.setOnAction(e -> {
            tabPane.addTab(file);
        });

        menuItem2.setOnAction(e -> {
            System.out.println("Выбран пункт меню 2");
        });

        if (file != null) {
     
  
            // Показываем контекстное меню для элемента, на котором был выполнен щелчок правой кнопкой мыши
            contextMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }

    private TreeItem<File> createTreeTableView(File rootDirectory) {
        FileTreeItem rootItem = new FileTreeItem(rootDirectory);
        File[] children = rootDirectory.listFiles();
        if (children != null) {
            for (File child : children) {
                rootItem.getChildren().add(new FileTreeItem(child)); // Заменяем addChild на добавление нового FileTreeItem
            }
            // Если папка не пустая, добавляем какой-нибудь файл
            if (children.length > 0) {
                rootItem.getChildren().add(new FileTreeItem(new File(rootDirectory, "example.txt"))); // Добавляем примерный файл в папку
            }
        }
        return rootItem;
    }



}
