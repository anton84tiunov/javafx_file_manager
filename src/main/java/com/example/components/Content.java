package com.example.components;

import java.io.File;

import com.example.components.left.FileTreeTableView;
import com.example.components.right.FileTabsPane;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Content extends SplitPane {
    
    public Content(){

        // Создаем кисть для фона
        BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(0, 255, 255), null, null);

        // Создаем объект Background с установленным фоном
        Background background = new Background(backgroundFill);

        FileTabsPane tabPane = new FileTabsPane();

        File rootDirectory = new File("/");
        FileTreeTableView fileTreeTableView = new FileTreeTableView(rootDirectory, tabPane);

        // Создаем CheckBox для скрытия/показа скрытых файлов и папок
        CheckBox hideShowCheckBox = new CheckBox("скрытые");
        // Обработчик изменения состояния CheckBox
        hideShowCheckBox.setOnAction(event -> {
            boolean showHidden = hideShowCheckBox.isSelected();
            // Передаем информацию о том, нужно ли показывать скрытые файлы и папки в FileTreeTableView
            // fileTreeTableView.setShowHiddenFiles(showHidden);
        });

        // Создаем пункт меню для CheckBox
        MenuItem checkBoxMenuItem = new MenuItem();
        checkBoxMenuItem.setGraphic(hideShowCheckBox);

        // Создаем меню и добавляем в него пункт с CheckBox
        MenuButton hideShowMenuButton = new MenuButton("...", null, checkBoxMenuItem);

        ToolBar toolbar = new ToolBar(new Button("<<"), new Button(">>"), hideShowMenuButton);
        toolbar.setBackground(background);
        VBox lVBox = new VBox(toolbar, fileTreeTableView);

        VBox.setVgrow(fileTreeTableView, Priority.ALWAYS);
        getItems().addAll(lVBox, tabPane);
    }
}
