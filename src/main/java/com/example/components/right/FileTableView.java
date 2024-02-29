package com.example.components.right;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FileTableView extends TableView<File> {

    Integer currentDirIndex = -1;
    List<File> historyDir = new ArrayList<>();

    Button back = new Button("<<");
    Button forward = new Button(">>");

    // Создаем CheckBox для скрытия/показа скрытых файлов и папок
    CheckBox hideShowCheckBox = new CheckBox("скрытые");
    // Создаем пункт меню для CheckBox
    MenuItem checkBoxMenuItem = new MenuItem();
    // Создаем меню и добавляем в него пункт с CheckBox
    MenuButton hideShowMenuButton = new MenuButton("", null, checkBoxMenuItem);

    // Добавляем поле поиска
    TextField searchField = new TextField();

    public FileTableView() {
        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        VBox.setVgrow(this, Priority.ALWAYS);
        // tableView.setPrefWidth(2000); // Ширина в пикселях
        // tableView.setPrefHeight(2000); // Высота в пикселях
        setColumns();

        setupToolBar();
        setupSearchField();
    }

    private void setupToolBar() {
        back.setOnAction(event -> navigateBack());
        forward.setOnAction(event -> navigateForward());
        // Обработчик изменения состояния CheckBox
        hideShowCheckBox.setOnAction(event -> {
            // boolean showHidden = hideShowCheckBox.isSelected();
            // Передаем информацию о том, нужно ли показывать скрытые файлы и папки в
            // FileTreeTableView
            // fileTreeTableView.setShowHiddenFiles(showHidden);
        });
        checkBoxMenuItem.setGraphic(hideShowCheckBox);

    }

    public void setItems(File file) {
        // Если мы двигались назад по истории, удаляем все элементы после текущей
        // позиции
        if (currentDirIndex < historyDir.size() - 1) {
            historyDir.subList(currentDirIndex + 1, historyDir.size()).clear();
        }

        // Добавляем в историю только если текущая директория не совпадает с последней
        // добавленной
        if (historyDir.isEmpty() || !Objects.equals(historyDir.get(currentDirIndex), file)) {
            historyDir.add(file);
            currentDirIndex++;
        }

        refreshTableView();
    }

    public void setColumns() {

        // Создаем столбец для отображения изображения
        TableColumn<File, ImageView> imageColumn = new TableColumn<>("Image");
        imageColumn.setCellValueFactory(cellData -> {
            // Получаем объект File из ячейки
            File file = cellData.getValue();

            // Создаем изображение на основе информации о файле
            Image image;
            if (file.isDirectory()) {
                image = new Image(getClass().getResourceAsStream("/com/example/icons/folder_icon.png"));
            } else {
                // image = new
                // Image(getClass().getResourceAsStream("/com/example/icons/file_icon.png"));
                String fileName = file.getName();
                String extension = "";

                // Находим расширение файла
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                    extension = fileName.substring(dotIndex + 1).toLowerCase();
                }

                switch (extension) {
                    case "txt":
                        image = new Image(getClass().getResourceAsStream("/com/example/icons/txt_icon.png"));
                        break;
                    case "jpg":
                    case "jpeg":
                    case "png":
                    case "gif":
                        image = new Image(getClass().getResourceAsStream("/com/example/icons/image_icon.png"));
                        break;
                    case "pdf":
                        image = new Image(getClass().getResourceAsStream("/com/example/icons/PDF.jpg"));
                        break;
                    // Другие кейсы для других расширений
                    default:
                        image = new Image(getClass().getResourceAsStream("/com/example/icons/file_icon.png"));
                        break;
                }

            }

            // Создаем ImageView для отображения изображения в ячейке
            ImageView imageView = new ImageView(image);

            // Устанавливаем размеры изображения
            imageView.setFitWidth(24); // Устанавливаем ширину
            imageView.setFitHeight(24); // Устанавливаем высоту

            // Возвращаем ImageView в качестве значения столбца
            return new SimpleObjectProperty<>(imageView);
        });

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
        addColumn(imageColumn);
        addColumn(nameColumn);
        addColumn(pathColumn);
        addColumn(sizeColumn);
        addColumn(countColumn);
        addColumn(dateColumn);

        setRowFactory(tv -> {
            TableRow<File> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    File selectedFile = row.getItem();
                    // Действия при двойном клике на файле
                    System.out.println("Double-clicked on file: " + selectedFile.getName());
                    if (selectedFile.isDirectory()) {
                        if (!Objects.equals(historyDir.get(currentDirIndex), selectedFile)) {
                            getItems().clear();
                            setItems(selectedFile);
                        }
                    }
                }
            });

            row.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    File selectedFile = row.getItem();

                    // Действия при клике правой кнопкой мыши на файле
                    System.out.println("Right-clicked on file: " + selectedFile.getName());

                    // Определение типа выбора
                    int selectedIndex = getSelectionModel().getSelectedIndex();
                    if (selectedIndex != -1) {
                        List<File> selectedFiles = new ArrayList<>(getSelectionModel().getSelectedItems());
                        if (getSelectionModel().getSelectedIndices().size() == 1) {
                            // Одиночный файл или папка выбраны
                            if (selectedFile.isDirectory()) {
                                addContextMenu("dir", selectedFiles);
                            } else {
                                addContextMenu("file", selectedFiles);
                            }
                        } else if (getSelectionModel().getSelectedIndices().size() > 1) {
                            // Множественный выбор файлов
                            addContextMenu("multi", selectedFiles);
                        } else {
                            // Нет выбранных элементов или неизвестный тип
                            System.out.println("Unknown selection type");
                        }
                    }
                }
            });

            return row;
        });
    }

    private void navigateBack() {
        System.out.println("navigateBack " + currentDirIndex + historyDir.size());
        System.out.println("historyDir" + historyDir);
        if (currentDirIndex > 0) {
            currentDirIndex--;
            refreshTableView();
        }
    }

    private void navigateForward() {
        System.out.println("navigateForward " + currentDirIndex + historyDir.size());
        System.out.println("historyDir" + historyDir);
        if (currentDirIndex < historyDir.size() - 1) {
            currentDirIndex++;
            refreshTableView();
        }
    }

    private void refreshTableView() {
        getItems().clear();
        if (currentDirIndex >= 0 && currentDirIndex < historyDir.size()) {
            File currentDir = historyDir.get(currentDirIndex);
            File[] children = currentDir.listFiles();
            if (children != null) {
                for (File child : children) {
                    getItems().add(child);
                }
            }
        }
    }

    private void refreshTableView(List<File> files) {
        getItems().clear();
        getItems().addAll(files);
    }

    private void setupSearchField() {
        // Добавляем слушатель событий для текстового поля поиска
        searchField.setOnKeyReleased(event -> {
            String searchText = searchField.getText().toLowerCase();
            List<File> filteredFiles = new ArrayList<>();
            if (currentDirIndex >= 0 && currentDirIndex < historyDir.size()) {
                File currentDir = historyDir.get(currentDirIndex);
                File[] children = currentDir.listFiles();
                if (children != null) {
                    for (File child : children) {
                        if (child.getName().toLowerCase().contains(searchText)) {
                            filteredFiles.add(child);
                        }
                    }
                }
            }
            refreshTableView(filteredFiles);
        });
    }

    // Метод-обёртка для добавления столбца
    public void addColumn(TableColumn<File, ?> column) {
        getColumns().add(column); // Вызов метода суперкласса
    }

    // Метод-обёртка для добавления элемента
    public void addItem(File item) {
        getItems().add(item); // Вызов метода суперкласса
    }

    public void addContextMenu(String type, List<File> selectedFile) {

        // Создайте контекстное меню
        ContextMenu contextMenu = new ContextMenu();

        // Создайте пункты меню для контекстного меню
        MenuItem menuItem1 = new MenuItem("Open");
        MenuItem menuItem2 = new MenuItem("Delete");

        // Добавьте действия для пунктов меню
        menuItem1.setOnAction(event1 -> {
            File selectedF = getSelectionModel().getSelectedItem();
            if (selectedF != null) {
                System.out.println("menuItem1: " + type + selectedFile);
                // Ваше действие при выборе "Open"
            }
        });

        menuItem2.setOnAction(event2 -> {
            File selectedF = getSelectionModel().getSelectedItem();
            if (selectedF != null) {
                System.out.println("menuItem2: " + type + selectedFile);
                // Ваше действие при выборе "Delete"
            }
        });

        // Добавьте пункты меню в контекстное меню
        contextMenu.getItems().addAll(menuItem1, menuItem2);

        // Установите контекстное меню для таблицы
        setContextMenu(contextMenu);
    }

}
