package com.infoshareacademy.zajavka.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {

    public static ArrayList listFilesForFolder(final Path path) {
        ArrayList<String> fileList = new ArrayList<>();

        try (Stream<Path> filePathStream = Files.walk(path)) {
            filePathStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    fileList.add(filePath.getFileName().toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    public static List readFile(final Path path) {
        List<String> file = null;
        try {
            file = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
