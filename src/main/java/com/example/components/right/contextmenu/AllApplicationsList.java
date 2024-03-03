package com.example.components.right.contextmenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllApplicationsList {

    public static List<String> getAllApplications() {
        List<String> applications = new ArrayList<>();
        try {
            File directory = new File("/usr/bin"); // Путь к каталогу с исполняемыми файлами
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        applications.add(file.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applications;
    }

    public static void main(String[] args) {
        List<String> allApplications = getAllApplications();
        for (String app : allApplications) {
            System.out.println(app);
        }
    }
}
