package com.example.components.right.contextmenu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileOpenWithUtils {

    public static List<String> getAssociatedApplications(String filePath) {
        List<String> associatedApplications = new ArrayList<>();

        // Добавьте здесь логику для получения ассоциированных приложений
        // Например, вы можете использовать Desktop API для открытия файла с помощью ассоциированного приложения
        // https://docs.oracle.com/javase/7/docs/api/java/awt/Desktop.html#open(java.io.File)
        // или какие-либо другие способы, подходящие для вашей операционной системы и среды выполнения

        return associatedApplications;
    }

    public static List<String> getAllApplications() {
        List<String> allApplications = new ArrayList<>();
        try {
            // Получаем каталог с исполняемыми файлами
            Path directory = Paths.get("/usr/bin");
            // Проходим по всем файлам в каталоге
            Files.list(directory)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .forEach(allApplications::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allApplications;
    }

    public static List<String> getApplicationsForFile(String filePath) {
        List<String> associatedApplications = getAssociatedApplications(filePath);
        List<String> allApplications = getAllApplications();

        // Добавляем все остальные приложения, которые не ассоциированы с файлом
        for (String app : allApplications) {
            if (!associatedApplications.contains(app)) {
                associatedApplications.add(app);
            }
        }

        return associatedApplications;
    }
}