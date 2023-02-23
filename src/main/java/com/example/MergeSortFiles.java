package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;


public class MergeSortFiles {
    public static void main(String[] args) {
        boolean ascending = true; // Флаг сортировки по возрастанию
        boolean isInteger = true; // Флаг типа данных (числа или строки)
        String outputFileName = ""; // Имя выходного файла
        List<String> inputFiles = new ArrayList<>(); // Список входных файлов

        // Чтение аргументов командной строки
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-a".equals(arg)) {
                ascending = true;
            } else if ("-d".equals(arg)) {
                ascending = false;
            } else if ("-i".equals(arg)) {
                isInteger = true;
            } else if ("-s".equals(arg)) {
                isInteger = false;
            } else if (i == args.length - 1) {
                outputFileName = arg;
            } else {
                inputFiles.add(arg);
            }
        }

        if (outputFileName.isEmpty()) {
            System.out.println("Выходной файл не указан");
            return;
        }
        if (inputFiles.isEmpty()) {
            System.out.println("Входные файлы не указаны");
            return;
        }

        // Чтение исходных данных и сортировка
        List<String> sortedData = inputFiles.stream()
                .flatMap(file -> readFileLines(file))
                .sorted(getComparator(isInteger, ascending))
                .collect(Collectors.toList());

        // Запись результата
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
            for (String line : sortedData) {
                writer.println(line);
            }
            System.out.println("Сортировка успешно завершена");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл " + outputFileName);
        }
    }
    public static Stream<String> readFileLines(String fileName) {
        try {
            return new BufferedReader(new FileReader(fileName)).lines();
        } catch (IOException e) {
            System.out.println("Ошибка: файл " + fileName + " не найден");
            return Stream.empty();
        }
    }

    // Метод, возвращающий компаратор для типа данных
    public static Comparator<String> getComparator(boolean isInteger, boolean ascending) {
        Comparator<String> comparator;
        if (isInteger) {
            comparator = (s1, s2) -> {
                try {
                    Integer i1 = Integer.parseInt(s1);
                    Integer i2 = Integer.parseInt(s2);
                    return i1.compareTo(i2);
                } catch (NumberFormatException e) {
                    // Если одна из строк не является числом, то она считается меньше чисел
                    return -1;
                }
            };
        } else {
            comparator = Comparator.naturalOrder();
        }
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}