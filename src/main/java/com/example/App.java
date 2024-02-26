package com.example;


import com.example.components.Content;
import com.example.components.Menubar;              
import com.example.components.Toolbar;

import javafx.application.Application;
import javafx.scene.Scene;
// import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage ) {
        BorderPane root = new BorderPane();

        Menubar menuBar = new Menubar();
        root.setTop(menuBar);
        Toolbar toolBar = new Toolbar();
        root.setBottom(toolBar);
        Content content = new Content();
        root.setCenter(content);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("File Manager");
        primaryStage.show();
    }

    
}

 
