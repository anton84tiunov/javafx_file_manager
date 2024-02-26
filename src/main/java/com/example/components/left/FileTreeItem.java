package com.example.components.left;

import java.io.File;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class FileTreeItem extends TreeItem<File> {

    public FileTreeItem(File file) {
        super(file);

        // Проверяем, что файл не null и не пустой
        if (file != null && file.exists()) {
            // Если файл является папкой, добавляем дочерние элементы
            if (file.isDirectory()) {
                addDummyChild(); // Добавляем фиктивный элемент
            }
        }


        
        // Переопределяем свойство раскрытия узла
        expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && !isLeaf()) {
                getChildren().clear(); // Очищаем список дочерних элементов
                loadChildren(); // Загружаем дочерние элементы при раскрытии узла
            }
        });

      

    }

     public void setOnRightMouseClicked(EventHandler<MouseEvent> handler) {
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                handler.handle(event);
                event.consume();
            }
        });
    }

    private void addDummyChild() {
        // Добавляем фиктивный элемент для папки
        getChildren().add(new TreeItem<>(null));
    }

    private void loadChildren() {
        // Получаем файл, соответствующий данному узлу
        File file = getValue();
        if (file != null && file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                // Добавляем каждого ребенка как дочерний элемент
                for (File child : children) {
                    FileTreeItem childItem = new FileTreeItem(child);
                    getChildren().add(childItem);
                }
            }
        }
    }

}