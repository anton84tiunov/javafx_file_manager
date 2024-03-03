package com.example.components.right.contextmenu;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ApplicationUtils {

    public static List<String> getAllApplications() {
        List<String> allApplications = new ArrayList<>();

        // Получаем корневой путь файловой системы
        Path rootPath = FileSystems.getDefault().getRootDirectories().iterator().next();

        try {
            // Проходимся по всем файлам и каталогам, начиная с корневого пути
            Files.walk(rootPath)
                    .filter(Files::isRegularFile)  // Фильтруем только обычные файлы
                    .filter(Files::isExecutable)   // Фильтруем только исполняемые файлы
                    .forEach(file -> allApplications.add(file.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allApplications;
    }
}