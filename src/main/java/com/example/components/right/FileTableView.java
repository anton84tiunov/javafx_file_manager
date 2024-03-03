package com.example.components.right;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.components.right.columns.CountTableColumn;
import com.example.components.right.columns.DateTableColumn;
import com.example.components.right.columns.ImageTableColumn;
import com.example.components.right.columns.NameTableColumn;
import com.example.components.right.columns.PathTableColumn;
import com.example.components.right.columns.SizeTableColumn;
import com.example.components.right.contextmenu.FileContextMenu;

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

    public boolean showHidden = false;

    public FileTableView() {
        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        VBox.setVgrow(this, Priority.ALWAYS);
        // tableView.setPrefWidth(2000); // Ширина в пикселях
        // tableView.setPrefHeight(2000); // Высота в пикселях
        setupColumns();

        setupToolBar();
        setupSearchField();
        showHidden = hideShowCheckBox.isSelected();
    }

    private void setupToolBar() {
        back.setOnAction(event -> navigateBack());
        forward.setOnAction(event -> navigateForward());
        // Обработчик изменения состояния CheckBox
        hideShowCheckBox.setOnAction(event -> {
            showHidden = hideShowCheckBox.isSelected();
            refreshTableView(showHidden);

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

        refreshTableView(showHidden);
    }

    public void setupColumns() {

        addColumn(new ImageTableColumn("Image"));
        addColumn(new NameTableColumn("Name"));
        addColumn(new PathTableColumn("Path"));
        addColumn(new SizeTableColumn("Size"));
        addColumn(new CountTableColumn("Count"));
        addColumn(new DateTableColumn("Date"));

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
                        FileContextMenu fileContextMenu = new FileContextMenu(this);
                        ContextMenu contextMenu = fileContextMenu.createContextMenu(selectedFiles);
                        setContextMenu(contextMenu);
                       
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
            refreshTableView(showHidden);
        }
    }

    private void navigateForward() {
        System.out.println("navigateForward " + currentDirIndex + historyDir.size());
        System.out.println("historyDir" + historyDir);
        if (currentDirIndex < historyDir.size() - 1) {
            currentDirIndex++;
            refreshTableView(showHidden);
        }
    }

    public void refreshTableView(boolean showHidden) {
        getItems().clear();
        if (currentDirIndex >= 0 && currentDirIndex < historyDir.size()) {
            File currentDir = historyDir.get(currentDirIndex);
            File[] children = currentDir.listFiles();
            if (children != null) {
                for (File child : children) {
                    // Проверка на скрытые файлы и папки
                    if (!child.isHidden() || showHidden) {
                        getItems().add(child);
                    }
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

  

}
