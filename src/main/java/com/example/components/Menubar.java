package com.example.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class Menubar extends MenuBar{
    public Menubar(){
         Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> System.exit(0));
        fileMenu.getItems().add(exitMenuItem);
        getMenus().add(fileMenu);
    }
}
