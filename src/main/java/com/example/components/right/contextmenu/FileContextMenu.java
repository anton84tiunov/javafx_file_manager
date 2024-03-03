package com.example.components.right.contextmenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

import com.example.components.right.FileTableView;

public class FileContextMenu {
    private FileTableView fileTableView;

    public FileContextMenu(FileTableView fileTableView) {
        this.fileTableView = fileTableView;
    }

    public ContextMenu createContextMenu(List<File> selectedFiles) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItemNewFolder = new MenuItem("New Folder");
        MenuItem menuItemNewFile = new MenuItem("New File");
        MenuItem menuItemOpen = new MenuItem("Open");
        MenuItem menuItemOpenWith = new MenuItem("Open With");

        MenuItem menuItemCopy = new MenuItem("Copy");
        MenuItem menuItemCut = new MenuItem("Cut");
        MenuItem menuItemPaste = new MenuItem("Paste");
        MenuItem menuItemDelete = new MenuItem("Delete");
        MenuItem menuItemRename = new MenuItem("Rename");

        menuItemNewFolder.setOnAction(event -> {
            // Ваш код для создания папки
            System.out.println("New Folder");
        });

        menuItemNewFile.setOnAction(event -> {
            // Ваш код для создания файлов
            System.out.println("New File");
        });

        menuItemRename.setOnAction(event -> {
            // Ваш код для переименования файлов
            System.out.println("Rename");
        });

        menuItemDelete.setOnAction(event -> {
            // Ваш код для удаления файлов
            System.out.println("Delete");
        });

        menuItemOpen.setOnAction(event -> {
            for (File selectedFile : selectedFiles) {
                try {
                    String command = "xdg-open";
                    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                        command = "cmd /c start";
                    }
                    Runtime.getRuntime().exec(command + " " + selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        menuItemOpenWith.setOnAction(event -> {
            if (!selectedFiles.isEmpty()) {
                List<String> applications = FileOpenWithUtils.getApplicationsForFile(selectedFiles.get(0).getAbsolutePath());
                if (!applications.isEmpty()) {
                    ChoiceDialog<String> dialog = new ChoiceDialog<>(applications.get(0), applications);
                    dialog.setTitle("Open With");
                    dialog.setHeaderText("Choose an application to open the file with");

                    TextField searchField = createSearchField(dialog, applications);
                    VBox content = new VBox(10, searchField, dialog.getDialogPane().getContent());
                    dialog.getDialogPane().setContent(content);

                    dialog.showAndWait().ifPresent(selectedApp -> {
                        for (File selectedFile : selectedFiles) {
                            try {
                                String command = selectedApp;
                                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                                    command += " \"" + selectedFile.getAbsolutePath() + "\"";
                                } else {
                                    command += " " + selectedFile.getAbsolutePath();
                                }
                                Process process = Runtime.getRuntime().exec(command);
                                int exitCode = process.waitFor();
                                if (exitCode == 0) {
                                    System.out.println("File opened successfully with: " + selectedApp);
                                } else {
                                    System.out.println("Failed to open file. Exit code: " + exitCode);
                                    printProcessOutput(process.getInputStream());
                                    printProcessOutput(process.getErrorStream());
                              
                                    // Создаем сообщение об ошибке с объяснением
                                    String errorMessage = "Failed to open file. Exit code: " + exitCode + "\n";
                                    errorMessage += "Standard Output:\n";
                                    errorMessage += readInputStream(process.getInputStream()) + "\n";
                                    errorMessage += "Error Output:\n";
                                    errorMessage += readInputStream(process.getErrorStream());

                                    // Показываем диалоговое окно с сообщением об ошибке
                                    showAlert("Error", "Failed to open file", errorMessage);

                                }
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    showAlert("Error", "No applications found", "No applications found to open the file with.");
                }
            } else {
                showAlert("Error", "No file selected", "No file selected to open with an application.");
            }
        });

        menuItemCopy.setOnAction(event -> {
            for (File selectedFile : selectedFiles) {
                // Ваш код для копирования файла
                System.out.println("Copy");
            }
        });

        menuItemCut.setOnAction(event -> {
            for (File selectedFile : selectedFiles) {
                // Ваш код для вырезания файла
                System.out.println("Cut");
            }
        });

        menuItemPaste.setOnAction(event -> {
            for (File selectedFile : selectedFiles) {
                // Ваш код для вставки файла
                System.out.println("Paste");
            }
        });

        if (selectedFiles.size() == 1) {
            File selectedFile = selectedFiles.get(0);
            if (selectedFile.isDirectory()) {
                contextMenu.getItems().addAll(menuItemNewFolder, menuItemNewFile, menuItemOpen, menuItemCopy, menuItemCut, menuItemPaste, menuItemDelete, menuItemRename);
                System.out.println("Directory selection type");
            } else {
                contextMenu.getItems().addAll(menuItemOpen, menuItemOpenWith, menuItemCopy, menuItemCut, menuItemPaste, menuItemDelete, menuItemRename);
                System.out.println("File selection type");
            }
        } else if (selectedFiles.size() > 1) {
            contextMenu.getItems().addAll(menuItemNewFolder, menuItemNewFile, menuItemOpen, menuItemCopy, menuItemCut, menuItemPaste, menuItemDelete);
            System.out.println("Multiple selection type");
        } else {
            contextMenu.getItems().addAll(menuItemNewFolder, menuItemNewFile);
            System.out.println("Unknown selection type");
        }

        return contextMenu;
    }

    private TextField createSearchField(ChoiceDialog<String> dialog, List<String> items) {
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            Predicate<String> filter = s -> s.toLowerCase().contains(newValue.toLowerCase());
            List<String> filteredItems = items.stream().filter(filter).collect(Collectors.toList());
            dialog.getItems().setAll(filteredItems);
        });
        
        return searchField;
    }
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void printProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

// Метод для чтения содержимого InputStream в строку
private String readInputStream(InputStream inputStream) {
    StringBuilder result = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
    } catch (IOException e) {
        // Обработка ошибок ввода-вывода
        e.printStackTrace();
        return "Ошибка ввода-вывода: " + e.getMessage();
    } catch (Exception e) {
        // Обработка других исключений
        e.printStackTrace();
        return "Произошла ошибка: " + e.getMessage();
    }
    return result.toString();
}

}