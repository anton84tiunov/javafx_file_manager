package com.example.components;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class Toolbar extends ToolBar {

  Button btn1 = new Button("Button 1");
  Button btn2 = new Button("Button 2");

  public Toolbar() {

    getItems().add(btn1);
    getItems().add(btn2);
  }
}
